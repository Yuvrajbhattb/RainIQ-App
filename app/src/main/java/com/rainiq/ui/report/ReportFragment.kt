package com.rainiq.ui.report

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rainiq.databinding.FragmentReportBinding

/**
 * ReportFragment — Monthly Report (Screen 5)
 *
 * Phase 2: Empty shell.
 * Phase 4A: Month navigation + stats grid + bar/line charts + insight card.
 */
class ReportFragment : Fragment() {

    private var _binding: FragmentReportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Phase 4A: Monthly Report content setup goes here
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
