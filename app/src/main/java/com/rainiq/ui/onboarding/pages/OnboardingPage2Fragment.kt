package com.rainiq.ui.onboarding.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.rainiq.databinding.FragmentOnboardingPage2Binding

/**
 * OnboardingPage2Fragment — "Your roof harvests water every monsoon"
 * DID YOU KNOW layout with illustration, floating chip, and 3 info rows.
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
        // Apply real status bar inset so content clears it on every device
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val contentPadding = statusBarHeight + resources.getDimensionPixelSize(com.rainiq.R.dimen.onboarding_content_top_padding)
            v.setPadding(v.paddingLeft, contentPadding, v.paddingRight, v.paddingBottom)
            insets
        }
        binding.tvLabel.alpha = 0f
        binding.tvHeadline.alpha = 0f
        binding.illustrationFrame.alpha = 0f
        binding.infoCard.alpha = 0f
    }

    override fun onResume() {
        super.onResume()
        if (_binding != null) startEntryAnimation()
    }

    fun startEntryAnimation() {
        if (_binding == null) return
        val interp = DecelerateInterpolator(2f)

        binding.tvLabel
            .animate().alpha(1f).translationYBy(-20f).setDuration(400)
            .setInterpolator(interp).setStartDelay(80).start()

        binding.tvHeadline
            .animate().alpha(1f).translationYBy(-24f).setDuration(500)
            .setInterpolator(interp).setStartDelay(180).start()

        binding.illustrationFrame
            .animate().alpha(1f).setDuration(600)
            .setInterpolator(interp).setStartDelay(320).start()

        binding.infoCard
            .animate().alpha(1f).translationYBy(-16f).setDuration(500)
            .setInterpolator(interp).setStartDelay(520).start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
