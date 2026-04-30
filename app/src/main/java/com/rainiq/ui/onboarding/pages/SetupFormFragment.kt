package com.rainiq.ui.onboarding.pages

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.rainiq.R
import com.rainiq.data.preferences.UserPreferences
import com.rainiq.databinding.FragmentOnboardingSetupBinding
import com.rainiq.ui.MainActivity

/**
 * SetupFormFragment — Onboarding Screen 5
 * Collects roof area, tank capacity, roof material, and city.
 * Validates all fields, shows live harvest preview, then saves and launches MainActivity.
 */
class SetupFormFragment : Fragment() {

    private var _binding: FragmentOnboardingSetupBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefs: UserPreferences

    private val roofMaterials = listOf(
        "Concrete/RCC" to 0.85f,
        "Clay Tiles" to 0.75f,
        "Metal/Tin" to 0.90f,
        "Asphalt" to 0.70f,
        "Other" to 0.65f
    )
    private var selectedRunoff = 0.85f

    // Average monthly rainfall for estimate (mm) — rough India average
    private val avgMonthlyRainfall = 150f

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingSetupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = UserPreferences(requireContext())

        setupDropdown()
        setupTextWatchers()
        setupSaveButton()

        // Initial states
        binding.tvSetupLabel.alpha = 0f
        binding.tvSetupHeadline.alpha = 0f
        binding.tvSetupSubtitle.alpha = 0f
        listOf(
            binding.llRoofArea, binding.llTankCapacity,
            binding.llRoofMaterial, binding.llCity, binding.btnSave
        ).forEach { it.alpha = 0f }
    }

    private fun setupDropdown() {
        binding.tvRoofMaterial.text = "Concrete/RCC  (×0.85)"
        binding.tvRoofMaterial.setOnClickListener {
            showMaterialPicker()
        }
    }

    private fun showMaterialPicker() {
        val bottomSheet = BottomSheetDialog(requireContext(), com.google.android.material.R.style.Theme_Design_BottomSheetDialog)
        val view = layoutInflater.inflate(R.layout.layout_material_picker, null)
        val container = view.findViewById<android.widget.LinearLayout>(R.id.pickerContainer)

        roofMaterials.forEach { (name, coeff) ->
            val item = layoutInflater.inflate(R.layout.item_material_picker, container, false) as TextView
            item.text = "$name  (×$coeff)"
            item.setOnClickListener {
                selectedRunoff = coeff
                binding.tvRoofMaterial.text = item.text
                updatePreview()
                bottomSheet.dismiss()
            }
            container.addView(item)
        }

        bottomSheet.setContentView(view)
        bottomSheet.show()
    }

    private fun setupTextWatchers() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) = updatePreview()
        }
        binding.etRoofArea.addTextChangedListener(watcher)
        binding.etTankCapacity.addTextChangedListener(watcher)
        binding.etCity.addTextChangedListener(watcher)
    }

    private fun updatePreview() {
        val areaText = binding.etRoofArea.text?.toString()
        val area = areaText?.toFloatOrNull() ?: 0f

        if (area > 0) {
            // Formula: Area (sq ft) × 0.0929 (sq ft → sq m) × rainfall (mm) × 0.001 (mm→m) × runoff × 1000 (m³→L)
            // Simplified: Area × rainfall × 0.0929 × runoff
            val estimated = (area * avgMonthlyRainfall * 0.0929f * selectedRunoff).toInt()
            binding.tvPreviewValue.text = "~$estimated L"

            if (binding.previewCard.visibility != View.VISIBLE || binding.previewCard.alpha < 0.5f) {
                binding.previewCard.visibility = View.VISIBLE
                binding.previewCard.alpha = 0f
                binding.previewCard.animate().alpha(1f).translationYBy(-12f)
                    .setDuration(400).setInterpolator(DecelerateInterpolator(2f)).start()
            }
        } else {
            binding.previewCard.animate().alpha(0f).setDuration(250)
                .withEndAction { binding.previewCard.visibility = View.GONE }
                .start()
        }
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            if (!validateForm()) return@setOnClickListener

            // Spring press animation
            it.animate().scaleX(0.94f).scaleY(0.94f).setDuration(80).withEndAction {
                it.animate().scaleX(1.04f).scaleY(1.04f).setDuration(120).withEndAction {
                    it.animate().scaleX(1f).scaleY(1f).setDuration(80).withEndAction {
                        saveAndLaunch()
                    }.start()
                }.start()
            }.start()
        }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val area = binding.etRoofArea.text?.toString()?.toFloatOrNull()
        if (area == null || area <= 0) {
            binding.tvRoofAreaError.text = "Please enter a valid roof area"
            binding.tvRoofAreaError.visibility = View.VISIBLE
            valid = false
        } else {
            binding.tvRoofAreaError.visibility = View.GONE
        }

        val tank = binding.etTankCapacity.text?.toString()?.toFloatOrNull()
        if (tank == null || tank <= 0) {
            binding.tvTankCapacityError.text = "Please enter a valid tank capacity"
            binding.tvTankCapacityError.visibility = View.VISIBLE
            valid = false
        } else {
            binding.tvTankCapacityError.visibility = View.GONE
        }

        val city = binding.etCity.text?.toString()?.trim()
        if (city.isNullOrEmpty()) {
            binding.tvCityError.text = "Please enter your city"
            binding.tvCityError.visibility = View.VISIBLE
            valid = false
        } else {
            binding.tvCityError.visibility = View.GONE
        }

        return valid
    }

    private fun saveAndLaunch() {
        val area = binding.etRoofArea.text.toString().toFloat()
        val tank = binding.etTankCapacity.text.toString().toFloat()
        val city = binding.etCity.text.toString().trim()

        // Extract material name without the coefficient part
        val materialFull = binding.tvRoofMaterial.text.toString()
        val material = roofMaterials.firstOrNull {
            materialFull.startsWith(it.first)
        }?.first ?: "Concrete/RCC"

        prefs.saveRoofSetup(area, tank, material, selectedRunoff, city)

        startActivity(Intent(requireContext(), MainActivity::class.java))
        requireActivity().finish()
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) {
            startEntryAnimation()
        }
    }

    fun startEntryAnimation() {
        if (_binding == null) return
        val interp = DecelerateInterpolator(2f)
        val overInterp = OvershootInterpolator(1.2f)
        val views = listOf<View>(
            binding.tvSetupLabel,
            binding.tvSetupHeadline,
            binding.tvSetupSubtitle,
            binding.llRoofArea,
            binding.llTankCapacity,
            binding.llRoofMaterial,
            binding.llCity,
            binding.btnSave
        )

        views.forEachIndexed { i, v ->
            v.translationY = 30f
            v.animate().alpha(1f).translationY(0f).setDuration(400)
                .setInterpolator(if (i < 3) interp else overInterp)
                .setStartDelay(60L + i * 80L).start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
