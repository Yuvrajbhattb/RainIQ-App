package com.rainiq.ui.onboarding.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import com.rainiq.databinding.FragmentOnboardingPage4Binding

/**
 * OnboardingPage4Fragment — "Track. Achieve. Impact."
 * Shows animated triple impact counters + badge pop-in via ImpactCounterView.
 */
class OnboardingPage4Fragment : Fragment() {

    private var _binding: FragmentOnboardingPage4Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingPage4Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvLabel.alpha = 0f
        binding.tvHeadline.alpha = 0f
        binding.tvSubtitle.alpha = 0f
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) {
            startEntryAnimation()
        }
    }

    fun startEntryAnimation() {
        if (_binding == null) return
        val interp = DecelerateInterpolator(2f)

        binding.tvLabel.animate().alpha(1f).translationYBy(-20f).setDuration(400)
            .setInterpolator(interp).setStartDelay(60).start()

        binding.tvHeadline.animate().alpha(1f).translationYBy(-24f).setDuration(500)
            .setInterpolator(interp).setStartDelay(160).start()

        binding.tvSubtitle.animate().alpha(1f).translationYBy(-16f).setDuration(450)
            .setInterpolator(interp).setStartDelay(300).start()

        // ImpactCounterView starts its own animations via init {}
        // Re-trigger by requesting invalidate after a short delay
        binding.impactCounterView.postDelayed({ binding.impactCounterView.invalidate() }, 200)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
