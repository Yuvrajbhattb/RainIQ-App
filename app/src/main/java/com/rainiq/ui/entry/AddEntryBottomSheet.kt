package com.rainiq.ui.entry

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rainiq.R
import com.rainiq.data.db.AppDatabase
import com.rainiq.data.preferences.UserPreferences
import com.rainiq.data.repository.HarvestRepository
import com.rainiq.databinding.FragmentAddEntryBinding
import com.rainiq.utils.formatIndian
import kotlinx.coroutines.launch

class AddEntryBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentAddEntryBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefs: UserPreferences
    private lateinit var repo: HarvestRepository

    override fun getTheme(): Int = R.style.Theme_RainIQ_BottomSheet

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddEntryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        prefs = UserPreferences(requireContext())
        repo = HarvestRepository(AppDatabase.getInstance(requireContext()), prefs)
        
        setupInputs()
        setupSaveButton()
    }

    private fun setupInputs() {
        // Handle input focus to switch background drawable
        binding.etRainfall.setOnFocusChangeListener { _, hasFocus ->
            binding.etRainfall.setBackgroundResource(
                if (hasFocus) R.drawable.bg_glass_input_focused else R.drawable.bg_glass_input
            )
        }
        
        binding.etNotes.setOnFocusChangeListener { _, hasFocus ->
            binding.etNotes.setBackgroundResource(
                if (hasFocus) R.drawable.bg_glass_input_focused else R.drawable.bg_glass_input
            )
        }

        // Live preview calculation on text change
        binding.etRainfall.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                calculatePreview()
            }
        })

        // Chip selection changes
        binding.chipGroupRoof.setOnCheckedStateChangeListener { _, _ ->
            calculatePreview()
        }
    }

    private fun calculatePreview() {
        val rainfallText = binding.etRainfall.text.toString()
        val rainfallMm = rainfallText.toFloatOrNull() ?: 0f
        
        // Use user setup area or fallback to 100
        val area = if (prefs.roofArea > 0) prefs.roofArea else 100f
        
        val coefficient = when (binding.chipGroupRoof.checkedChipId) {
            R.id.chipConcrete -> 0.85f
            R.id.chipClay -> 0.70f
            R.id.chipMetal -> 0.90f
            else -> prefs.runoffCoefficient
        }

        // Formula: area (sq ft) * 0.0929 (sq m) * rainfall (mm) * 0.001 (m) * runoff * 1000 (L)
        // Simplified: area * rainfall * 0.0929 * runoff
        val estimatedLiters = (area * rainfallMm * 0.0929f * coefficient).toInt()
        binding.tvPreviewLiters.text = "${estimatedLiters.formatIndian()} L"
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val rainfallText = binding.etRainfall.text.toString()
            val rainfallMm = rainfallText.toFloatOrNull() ?: 0f
            
            if (rainfallMm <= 0f) {
                binding.etRainfall.setBackgroundResource(R.drawable.bg_glass_input_focused)
                Toast.makeText(requireContext(), "Please enter rainfall amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            viewLifecycleOwner.lifecycleScope.launch {
                repo.logRainfall(rainfallMm)
                val savedAmount = binding.tvPreviewLiters.text.toString()
                Toast.makeText(requireContext(), "Saved $savedAmount to your harvest history!", Toast.LENGTH_LONG).show()
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "AddEntryBottomSheet"
    }
}
