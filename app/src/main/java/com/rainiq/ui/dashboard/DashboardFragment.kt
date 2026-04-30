package com.rainiq.ui.dashboard

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.rainiq.R
import com.rainiq.databinding.FragmentDashboardBinding
import com.rainiq.ui.MainActivity
import com.rainiq.ui.settings.ProfileFragment
import com.rainiq.utils.animateCardEntrance
import com.rainiq.utils.dpToPx
import com.rainiq.utils.formatIndian

/**
 * DashboardFragment — Home screen (Screen 2)
 *
 * Phase 3A: Full content — water tank animation, stats grid, insight card, streak dots.
 */
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupInlineHeaderInsets()
        setupInlineHeaderButtons()
        setupAnimations()
        populateStreakDays()
        animateNumbers()
    }

    /** Apply status bar top inset to the inline header so it clears the system status bar. */
    private fun setupInlineHeaderInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.inlineDashboardHeader) { v, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(
                v.paddingLeft,
                statusBar.top + resources.getDimensionPixelSize(com.rainiq.R.dimen.space_8),
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }
    }

    /** Wire inline header buttons to MainActivity navigation. */
    private fun setupInlineHeaderButtons() {
        val mainActivity = requireActivity() as? MainActivity ?: return
        binding.btnProfileInline.setOnClickListener {
            mainActivity.navigateTo(ProfileFragment(), "Profile")
        }
        // Notification button — placeholder for now
        binding.btnNotificationInline.setOnClickListener { /* TODO: open notifications */ }
    }

    private fun setupAnimations() {
        // Staggered card entrance
        val cards = listOf(
            binding.waterTankView, // Treat as a card for animation
            binding.cardInsight,
            binding.cardTotalSavings,
            binding.cardMonthlyGoal,
            binding.cardGoalProgress,
            binding.cardStreak
        )
        
        cards.forEachIndexed { index, view ->
            view.animateCardEntrance(index)
        }

        // Animate water tank fill
        // Mock data: 450L / 1000L = 45%
        binding.waterTankView.postDelayed({
            binding.waterTankView.setFillPercentage(45f, true)
        }, 500)
    }

    private fun populateStreakDays() {
        // Mock data: Last 5 days active, today active, tomorrow inactive
        val days = listOf("M", "T", "W", "T", "F", "S", "S")
        val activeDays = 6 // Let's say up to Saturday is active
        
        binding.streakDaysContainer.removeAllViews()
        val size = 32.dpToPx(requireContext())
        
        days.forEachIndexed { index, dayLabel ->
            val isActive = index < activeDays
            val bgRes = if (isActive) R.drawable.bg_streak_day_active else R.drawable.bg_streak_day_inactive
            val textColor = if (isActive) R.color.text_on_light_glass else R.color.text_secondary
            
            val tv = TextView(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(size, size).apply {
                    weight = 1f // For spacing, actually we should use fixed size and space-between in parent
                }
                text = dayLabel
                gravity = android.view.Gravity.CENTER
                setBackgroundResource(bgRes)
                setTextColor(ContextCompat.getColor(requireContext(), textColor))
                setTextAppearance(R.style.TextStyle_BodySecondary)
            }
            
            // To ensure perfect circles and spacing, use a FrameLayout container for each dot
            val container = android.widget.FrameLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }
            val dotParams = android.widget.FrameLayout.LayoutParams(size, size).apply {
                gravity = android.view.Gravity.CENTER
            }
            container.addView(tv, dotParams)
            
            binding.streakDaysContainer.addView(container)
        }
    }

    private fun animateNumbers() {
        // Total savings counter animation (0 to 1240)
        ValueAnimator.ofInt(0, 1240).apply {
            duration = 1500
            addUpdateListener { animator ->
                val value = animator.animatedValue as Int
                binding.tvTotalSavings.text = value.formatIndian()
            }
            start()
        }
        
        // Progress bar animation
        binding.pbGoalProgress.progress = 0
        binding.pbGoalProgress.postDelayed({
            val animator = android.animation.ObjectAnimator.ofInt(binding.pbGoalProgress, "progress", 0, 62)
            animator.duration = 1000
            animator.interpolator = android.view.animation.DecelerateInterpolator()
            animator.start()
        }, 500)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
