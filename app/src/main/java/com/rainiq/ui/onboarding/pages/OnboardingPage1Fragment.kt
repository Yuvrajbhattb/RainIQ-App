package com.rainiq.ui.onboarding.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import com.rainiq.databinding.FragmentOnboardingPage1Binding

/**
 * OnboardingPage1Fragment — "Every Drop, Counted"
 * Shows animated rain illustration and hero text with fade+slide-up entrance.
 */
class OnboardingPage1Fragment : Fragment() {

    private var _binding: FragmentOnboardingPage1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingPage1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Reset to invisible state for re-animation when revisited
        binding.tvHeadline.alpha = 0f
        binding.tvSubtitle.alpha = 0f
        binding.chipTagline.alpha = 0f
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) {
            startEntryAnimation()
        }
    }

    /** Called by OnboardingActivity when this page becomes visible */
    fun startEntryAnimation() {
        if (_binding == null) return
        val interp = DecelerateInterpolator(2f)

        // Headline slides up + fades in
        binding.tvHeadline
            .animate().alpha(1f).translationYBy(-24f).setDuration(500)
            .setInterpolator(interp).setStartDelay(100).start()

        // Subtitle
        binding.tvSubtitle
            .animate().alpha(1f).translationYBy(-20f).setDuration(500)
            .setInterpolator(interp).setStartDelay(250).start()

        // Chip
        binding.chipTagline
            .animate().alpha(1f).translationYBy(-16f).setDuration(450)
            .setInterpolator(interp).setStartDelay(400).start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
