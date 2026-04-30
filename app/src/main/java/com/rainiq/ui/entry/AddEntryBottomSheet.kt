package com.rainiq.ui.entry

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rainiq.R
import com.rainiq.databinding.FragmentAddEntryBinding
import com.rainiq.utils.formatIndian

class AddEntryBottomSheet : BottomSheetDialogFragment() {

    private var _binding: FragmentAddEntryBinding? = null
    private val binding get() = _binding!!

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
        
        // Mock calculation: Area (100) * Rainfall * Coefficient (0.85 for concrete)
        val area = 100f
        
        val coefficient = when (binding.chipGroupRoof.checkedChipId) {
            R.id.chipConcrete -> 0.85f
            R.id.chipClay -> 0.70f
            R.id.chipMetal -> 0.90f
            else -> 0.85f
        }

        val estimatedLiters = (area * rainfallMm * coefficient).toInt()
        binding.tvPreviewLiters.text = "${estimatedLiters.formatIndian()} L"
    }

    private fun setupSaveButton() {
        binding.btnSave.setOnClickListener {
            val rainfallText = binding.etRainfall.text.toString()
            if (rainfallText.isBlank()) {
                binding.etRainfall.setBackgroundResource(R.drawable.bg_glass_input_focused)
                // In a real app we'd change border to error color
                Toast.makeText(requireContext(), "Please enter rainfall amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            
            // Mock success and close
            val savedAmount = binding.tvPreviewLiters.text.toString().replace(" L", "")
            Toast.makeText(requireContext(), getString(R.string.entry_saved, savedAmount), Toast.LENGTH_LONG).show()
            dismiss()
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
