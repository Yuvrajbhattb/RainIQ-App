package com.rainiq.ui.ai

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rainiq.databinding.FragmentAiTipsBinding

/**
 * AiTipsFragment — AI Tips screen (Screen 5)
 *
 * Phase 4A: List of eco-tips and a floating "Ask AI" input bar.
 */
class AiTipsFragment : Fragment() {

    private var _binding: FragmentAiTipsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiTipsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        setupAskAi()
    }

    private fun setupRecyclerView() {
        val mockTips = listOf(
            AiTip(
                category = "Gardening",
                title = "Optimize your sprinklers",
                body = "Water your plants early in the morning or late in the evening to reduce evaporation. Adjust sprinkler heads to avoid watering the pavement.",
                savings = "Saves ~40L per week"
            ),
            AiTip(
                category = "Home",
                title = "Fix that leaky faucet",
                body = "A faucet dripping at a rate of one drop per second can waste up to 3,000 gallons per year. Tighten the washer today!",
                savings = "Saves ~200L per month"
            ),
            AiTip(
                category = "Habits",
                title = "Shorter showers",
                body = "Cutting your shower time by just 2 minutes can save up to 10 gallons of water per shower. Put on a 5-minute song to keep time.",
                savings = "Saves ~150L per week"
            ),
            AiTip(
                category = "Appliance",
                title = "Full loads only",
                body = "Run your washing machine and dishwasher only when they are completely full. This maximizes the water efficiency of the cycle.",
                savings = "Saves ~50L per cycle"
            )
        )

        binding.rvAiTips.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAiTips.adapter = AiTipsAdapter(mockTips)
    }

    private fun setupAskAi() {
        binding.btnSend.setOnClickListener {
            val query = binding.etAskAi.text.toString()
            if (query.isNotBlank()) {
                Toast.makeText(requireContext(), "Asking AI: $query", Toast.LENGTH_SHORT).show()
                binding.etAskAi.text?.clear()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
