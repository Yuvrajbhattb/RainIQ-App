package com.rainiq.ui.onboarding.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import com.rainiq.databinding.FragmentOnboardingPage3Binding

/**
 * OnboardingPage3Fragment — "Your AI water wisdom companion"
 * Gemini AI badge, headline + AI avatar, static chat mockup, feature tiles.
 */
class OnboardingPage3Fragment : Fragment() {

    private var _binding: FragmentOnboardingPage3Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingPage3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        resetState()
    }

    private fun resetState() {
        binding.tvLabel.alpha = 0f
        binding.tvHeadline.alpha = 0f
        binding.tvSubtitle.alpha = 0f
        binding.aiAvatar.alpha = 0f
        binding.chatContainer.alpha = 0f
        binding.featureCards.alpha = 0f
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) startEntryAnimation()
    }

    fun startEntryAnimation() {
        if (_binding == null) return
        resetState()
        val interp = DecelerateInterpolator(2f)

        binding.tvLabel.animate().alpha(1f).translationYBy(-20f).setDuration(400)
            .setInterpolator(interp).setStartDelay(60).start()

        binding.tvHeadline.animate().alpha(1f).translationYBy(-24f).setDuration(500)
            .setInterpolator(interp).setStartDelay(160).start()

        binding.tvSubtitle.animate().alpha(1f).translationYBy(-16f).setDuration(460)
            .setInterpolator(interp).setStartDelay(260).start()

        binding.aiAvatar.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(500)
            .setInterpolator(interp).setStartDelay(200).start()

        binding.chatContainer.animate().alpha(1f).translationYBy(-16f).setDuration(500)
            .setInterpolator(interp).setStartDelay(500).start()

        binding.featureCards.animate().alpha(1f).translationYBy(-12f).setDuration(480)
            .setInterpolator(interp).setStartDelay(750).start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
