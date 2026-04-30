package com.rainiq.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.rainiq.databinding.FragmentProfileBinding

/**
 * ProfileFragment — User profile card + settings.
 *
 * Opened via the avatar button in the header (NOT the bottom nav).
 * Displays: user info, roof setup, preferences, data/privacy.
 */
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.cardProfileEdit.setOnClickListener { showToast("Edit Profile") }
        binding.rowRoofArea.setOnClickListener { showToast("Edit Roof Area") }
        binding.rowTankCapacity.setOnClickListener { showToast("Edit Tank Capacity") }
        binding.rowCity.setOnClickListener { showToast("Change City") }
        binding.rowUnits.setOnClickListener { showToast("Change Units") }
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            showToast("Dark Mode: $isChecked")
        }
        binding.switchRainAlerts.setOnCheckedChangeListener { _, isChecked ->
            showToast("Rain Alerts: $isChecked")
        }
        binding.switchReminders.setOnCheckedChangeListener { _, isChecked ->
            showToast("Daily Reminders: $isChecked")
        }
        binding.rowExport.setOnClickListener { showToast("Exporting history...") }
        binding.rowClearData.setOnClickListener { showToast("Clear Data — are you sure?") }
    }

    private fun showToast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
