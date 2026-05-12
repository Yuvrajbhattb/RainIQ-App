package com.rainiq.ui.onboarding.pages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.rainiq.databinding.FragmentOnboardingPage3Binding

/**
 * OnboardingPage3Fragment — "Your AI water wisdom companion"
 * Gemini AI badge, full-width headline + subtitle, polished AI chat preview card,
 * and three horizontal feature rows in a unified glass card.
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
        // Apply real status bar inset so content clears it on every device
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { _, insets ->
            val statusBarHeight = insets.getInsets(WindowInsetsCompat.Type.statusBars()).top
            val contentPadding = statusBarHeight + resources.getDimensionPixelSize(com.rainiq.R.dimen.onboarding_content_top_padding)
            // The scrollable content is the ScrollView's child LinearLayout
            val contentLayout = binding.root.getChildAt(0) as? android.view.ViewGroup
            contentLayout?.setPadding(contentLayout.paddingLeft, contentPadding, contentLayout.paddingRight, contentLayout.paddingBottom)
            insets
        }
        resetState()
    }

    private fun resetState() {
        binding.tvLabel.alpha = 0f
        binding.tvHeadline.alpha = 0f
        binding.tvSubtitle.alpha = 0f
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

        // Badge fades + slides up
        binding.tvLabel.animate().alpha(1f).translationYBy(-20f).setDuration(400)
            .setInterpolator(interp).setStartDelay(60).start()

        // Headline fades + slides up
        binding.tvHeadline.animate().alpha(1f).translationYBy(-24f).setDuration(500)
            .setInterpolator(interp).setStartDelay(160).start()

        // Subtitle fades in shortly after
        binding.tvSubtitle.animate().alpha(1f).translationYBy(-16f).setDuration(460)
            .setInterpolator(interp).setStartDelay(280).start()

        // Chat card reveals with a slight upward drift
        binding.chatContainer.animate().alpha(1f).translationYBy(-18f).setDuration(520)
            .setInterpolator(interp).setStartDelay(440).start()

        // Feature rows card fades in last
        binding.featureCards.animate().alpha(1f).translationYBy(-14f).setDuration(480)
            .setInterpolator(interp).setStartDelay(680).start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
