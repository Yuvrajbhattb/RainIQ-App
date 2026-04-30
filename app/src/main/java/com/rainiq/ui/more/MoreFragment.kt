package com.rainiq.ui.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rainiq.databinding.FragmentMoreBinding

/**
 * MoreFragment — Settings and Profile screen (Screen 6)
 *
 * Phase 4B: Profile card and settings list using glassmorphism.
 */
class MoreFragment : Fragment() {

    private var _binding: FragmentMoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.cardProfile.setOnClickListener { showToast("Edit Profile clicked") }
        binding.rowUnits.setOnClickListener { showToast("Change Units clicked") }
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked -> 
            showToast("Dark Mode: $isChecked") 
        }
        binding.switchRainAlerts.setOnCheckedChangeListener { _, isChecked -> 
            showToast("Rain Alerts: $isChecked") 
        }
        binding.switchReminders.setOnCheckedChangeListener { _, isChecked -> 
            showToast("Daily Reminders: $isChecked") 
        }
        binding.rowExport.setOnClickListener { showToast("Exporting History...") }
        binding.rowClearData.setOnClickListener { showToast("Clear Data clicked") }
    }
    
    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
