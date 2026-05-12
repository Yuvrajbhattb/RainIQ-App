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
 * Full-bleed illustration with hero text fade+slide-up entrance.
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
        binding.tvHeadlinePart1.alpha = 0f
        binding.tvHeadline.alpha = 0f
        binding.tvSubtitle.alpha = 0f
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) startEntryAnimation()
    }

    fun startEntryAnimation() {
        if (_binding == null) return
        val interp = DecelerateInterpolator(2f)

        binding.tvHeadlinePart1
            .animate().alpha(1f).translationYBy(-24f).setDuration(500)
            .setInterpolator(interp).setStartDelay(80).start()

        binding.tvHeadline
            .animate().alpha(1f).translationYBy(-24f).setDuration(500)
            .setInterpolator(interp).setStartDelay(180).start()

        binding.tvSubtitle
            .animate().alpha(1f).translationYBy(-16f).setDuration(480)
            .setInterpolator(interp).setStartDelay(320).start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
