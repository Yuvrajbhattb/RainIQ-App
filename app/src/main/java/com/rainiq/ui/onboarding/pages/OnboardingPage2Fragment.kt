package com.rainiq.ui.onboarding.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import com.rainiq.databinding.FragmentOnboardingPage2Binding

/**
 * OnboardingPage2Fragment — "Your Roof Is Worth More"
 * Shows cloud → water conversion animation + glass info card.
 */
class OnboardingPage2Fragment : Fragment() {

    private var _binding: FragmentOnboardingPage2Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingPage2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvLabel.alpha = 0f
        binding.tvHeadline.alpha = 0f
        binding.infoCard.alpha = 0f
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

        binding.tvLabel
            .animate().alpha(1f).translationYBy(-20f).setDuration(400)
            .setInterpolator(interp).setStartDelay(80).start()

        binding.tvHeadline
            .animate().alpha(1f).translationYBy(-24f).setDuration(500)
            .setInterpolator(interp).setStartDelay(200).start()

        binding.infoCard
            .animate().alpha(1f).translationYBy(-20f).setDuration(500)
            .setInterpolator(interp).setStartDelay(500).start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
