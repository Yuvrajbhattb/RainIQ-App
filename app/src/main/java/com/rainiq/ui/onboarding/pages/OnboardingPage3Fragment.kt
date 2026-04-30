package com.rainiq.ui.onboarding.pages

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import com.rainiq.databinding.FragmentOnboardingPage3Binding

/**
 * OnboardingPage3Fragment — "Powered by AI"
 * Shows an animated Jal-Bot chat sequence (typewriter) + feature cards stagger.
 */
class OnboardingPage3Fragment : Fragment() {

    private var _binding: FragmentOnboardingPage3Binding? = null
    private val binding get() = _binding!!

    private val handler = Handler(Looper.getMainLooper())
    private val aiResponse = "With 500L you can grow tomatoes 🍅, coriander 🌿, and mint. I'll build your watering schedule!"
    private var typewriterRunnable: Runnable? = null
    private var dotPulseAnimator: ValueAnimator? = null

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
        binding.bubbleUser.alpha = 0f
        binding.thinkingDots.visibility = View.GONE
        binding.bubbleAi.alpha = 0f
        binding.tvAiResponse.text = ""
        binding.card1.alpha = 0f
        binding.card2.alpha = 0f
        binding.card3.alpha = 0f
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
        resetState()
        val interp = DecelerateInterpolator(2f)

        binding.tvLabel.animate().alpha(1f).translationYBy(-20f).setDuration(400)
            .setInterpolator(interp).setStartDelay(60).start()

        binding.tvHeadline.animate().alpha(1f).translationYBy(-24f).setDuration(500)
            .setInterpolator(interp).setStartDelay(160).start()

        // Step 1: User bubble appears at 500ms
        handler.postDelayed({
            if (_binding == null) return@postDelayed
            binding.bubbleUser.animate().alpha(1f).translationYBy(-16f).setDuration(400)
                .setInterpolator(interp).start()
        }, 500)

        // Step 2: Thinking dots at 1100ms
        handler.postDelayed({
            if (_binding == null) return@postDelayed
            binding.thinkingDots.visibility = View.VISIBLE
            startDotPulse()
        }, 1100)

        // Step 3: Hide dots, show AI bubble, start typewriter at 2000ms
        handler.postDelayed({
            if (_binding == null) return@postDelayed
            dotPulseAnimator?.cancel()
            binding.thinkingDots.visibility = View.GONE
            binding.bubbleAi.animate().alpha(1f).translationYBy(-12f).setDuration(350)
                .setInterpolator(interp).start()
            startTypewriter()
        }, 2000)

        // Step 4: Feature cards stagger in at 3400ms
        handler.postDelayed({
            if (_binding == null) return@postDelayed
            listOf(binding.card1, binding.card2, binding.card3).forEachIndexed { i, card ->
                card.animate().alpha(1f).translationYBy(-16f).setDuration(400)
                    .setInterpolator(interp).setStartDelay(i * 120L).start()
            }
        }, 3400)
    }

    private fun startDotPulse() {
        val dots = listOf(binding.dot1, binding.dot2, binding.dot3)
        dots.forEachIndexed { i, dot ->
            val anim = ValueAnimator.ofFloat(0.3f, 1f, 0.3f).apply {
                duration = 900L
                startDelay = i * 200L
                repeatCount = ValueAnimator.INFINITE
                addUpdateListener { dot.alpha = it.animatedValue as Float }
            }
            anim.start()
            dotPulseAnimator = anim // just keep last one to cancel
        }
    }

    private fun startTypewriter() {
        val sb = StringBuilder()
        var charIndex = 0
        typewriterRunnable = object : Runnable {
            override fun run() {
                if (_binding == null) return
                if (charIndex < aiResponse.length) {
                    sb.append(aiResponse[charIndex])
                    binding.tvAiResponse.text = sb.toString()
                    charIndex++
                    handler.postDelayed(this, 28)
                }
            }
        }
        handler.post(typewriterRunnable!!)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacksAndMessages(null)
        typewriterRunnable = null
        dotPulseAnimator?.cancel()
        _binding = null
    }
}
