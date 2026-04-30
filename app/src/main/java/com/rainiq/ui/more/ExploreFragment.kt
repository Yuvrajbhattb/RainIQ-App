package com.rainiq.ui.more

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rainiq.databinding.FragmentExploreBinding
import com.rainiq.ui.MainActivity
import com.rainiq.ui.features.AchievementsFragment
import com.rainiq.ui.features.AiTipsDetailFragment
import com.rainiq.ui.features.GardenFragment
import com.rainiq.ui.features.GoalsFragment
import com.rainiq.ui.features.HistoryDetailFragment
import com.rainiq.ui.features.ImpactReportFragment
import com.rainiq.ui.features.JalBotFragment
import com.rainiq.ui.features.ShareFragment
import com.rainiq.ui.features.TrackRainFragment
import com.rainiq.ui.features.WeatherFragment

/**
 * ExploreFragment — "More Features" bottom nav tab.
 * Each card navigates to its dedicated feature screen using
 * MainActivity.navigateTo() which pushes onto the back stack.
 */
class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListeners()
        animateCards()
    }

    private fun nav(fragment: Fragment, title: String) {
        (requireActivity() as? MainActivity)?.navigateTo(fragment, title)
    }

    private fun setupClickListeners() {
        binding.cardTrackRain.setOnClickListener { nav(TrackRainFragment(), "Track Rain") }
        binding.cardHistory.setOnClickListener { nav(HistoryDetailFragment(), "History") }
        binding.cardWeather.setOnClickListener { nav(WeatherFragment(), "Weather") }
        binding.cardJalBot.setOnClickListener { nav(JalBotFragment(), "Jal-Bot AI") }
        binding.cardAiTips.setOnClickListener { nav(AiTipsDetailFragment(), "AI Tips") }
        binding.cardGoals.setOnClickListener { nav(GoalsFragment(), "Goals") }
        binding.cardAchievements.setOnClickListener { nav(AchievementsFragment(), "Achievements") }
        binding.cardReport.setOnClickListener { nav(ImpactReportFragment(), "Impact Report") }
        binding.cardGarden.setOnClickListener { nav(GardenFragment(), "Garden Planner") }
        binding.cardShare.setOnClickListener { nav(ShareFragment(), "Share Impact") }
    }

    private fun animateCards() {
        val cards = listOf(
            binding.cardTrackRain, binding.cardHistory, binding.cardWeather,
            binding.cardJalBot, binding.cardAiTips,
            binding.cardGoals, binding.cardAchievements, binding.cardReport,
            binding.cardGarden, binding.cardShare
        )
        cards.forEachIndexed { index, card ->
            card.alpha = 0f
            card.translationY = 24f
            card.animate()
                .alpha(1f).translationY(0f)
                .setStartDelay(index * 50L).setDuration(300)
                .setInterpolator(android.view.animation.DecelerateInterpolator())
                .start()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
