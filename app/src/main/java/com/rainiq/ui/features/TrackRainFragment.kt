package com.rainiq.ui.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.rainiq.data.db.AppDatabase
import com.rainiq.data.preferences.UserPreferences
import com.rainiq.data.repository.HarvestRepository
import com.rainiq.databinding.FragmentFeatureTrackRainBinding
import com.rainiq.domain.calculator.HarvestCalculator
import com.rainiq.utils.formatIndian
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * TrackRainFragment — Dedicated screen to log rainfall entries.
 */
class TrackRainFragment : Fragment() {

    private var _binding: FragmentFeatureTrackRainBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefs: UserPreferences
    private lateinit var repo: HarvestRepository

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeatureTrackRainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        prefs = UserPreferences(requireContext())
        repo = HarvestRepository(AppDatabase.getInstance(requireContext()), prefs)

        setupUI()
        // Initialize with default or current progress
        updateCalculations(binding.seekRainfall.progress)
    }

    private fun setupUI() {
        // Set dynamic date
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        binding.tvEntryDate.text = "Today, ${sdf.format(Date())}"
        
        // Rainfall Slider
        binding.seekRainfall.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                updateCalculations(progress)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Save Button logic
        binding.btnSaveEntry.setOnClickListener {
            val rainfallMm = binding.seekRainfall.progress.toFloat()
            if (rainfallMm <= 0) {
                Toast.makeText(requireContext(), "Please slide to select rainfall amount", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveEntry(rainfallMm)
        }
    }

    private fun updateCalculations(rainfallMm: Int) {
        binding.tvRainfallValue.text = rainfallMm.toString()
        
        // Use user setup area or fallback to 1000
        val area = if (prefs.roofArea > 0) prefs.roofArea else 1000f
        val runoff = if (prefs.runoffCoefficient > 0) prefs.runoffCoefficient else 0.85f
        
        val liters = HarvestCalculator.calculate(area, rainfallMm.toFloat(), runoff)
        val moneySaved = (liters * 0.025f).toInt() // Approx ₹0.025 savings per liter

        binding.tvCalculatedLiters.text = liters.toInt().formatIndian()
        binding.tvCalculatedMoney.text = "₹${moneySaved.formatIndian()}"
    }

    private fun saveEntry(rainfallMm: Float) {
        viewLifecycleOwner.lifecycleScope.launch {
            repo.logRainfall(rainfallMm)
            Toast.makeText(requireContext(), "Rainfall entry logged! Check your Dashboard 💧", Toast.LENGTH_SHORT).show()
            // Return to previous screen
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
