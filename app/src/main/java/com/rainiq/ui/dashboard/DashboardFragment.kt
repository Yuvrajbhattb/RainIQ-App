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
import androidx.lifecycle.lifecycleScope
import com.rainiq.R
import com.rainiq.data.db.AppDatabase
import com.rainiq.data.preferences.UserPreferences
import com.rainiq.data.repository.HarvestRepository
import com.rainiq.databinding.FragmentDashboardBinding
import com.rainiq.domain.calculator.HarvestCalculator
import com.rainiq.ui.MainActivity
import com.rainiq.ui.settings.ProfileFragment
import com.rainiq.utils.animateCardEntrance
import com.rainiq.utils.dpToPx
import com.rainiq.utils.formatIndian
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * DashboardFragment — Home screen
 *
 * All values are now sourced from UserPreferences (setup data) and
 * the Room HarvestRepository (logged rainfall entries).
 * No mock data remains.
 *
 * Designed and Developed by Rahul Mirji
 */
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var prefs: UserPreferences
    private lateinit var repo: HarvestRepository

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

        prefs = UserPreferences(requireContext())
        repo = HarvestRepository(AppDatabase.getInstance(requireContext()), prefs)

        setupInlineHeaderInsets()
        setupInlineHeaderButtons()
        setupAnimations()
        loadRealData()
    }

    // ─── Insets & Header ──────────────────────────────────────────────────────

    private fun setupInlineHeaderInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.inlineDashboardHeader) { v, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            v.setPadding(
                v.paddingLeft,
                statusBar.top + resources.getDimensionPixelSize(R.dimen.space_8),
                v.paddingRight,
                v.paddingBottom
            )
            insets
        }
    }

    private fun setupInlineHeaderButtons() {
        val mainActivity = requireActivity() as? MainActivity ?: return
        binding.btnProfileInline.setOnClickListener {
            mainActivity.navigateTo(ProfileFragment(), "Profile")
        }
        binding.btnNotificationInline.setOnClickListener { /* TODO: notifications */ }
    }

    // ─── Animations ───────────────────────────────────────────────────────────

    private fun setupAnimations() {
        listOf(
            binding.waterTankView,
            binding.cardInsight,
            binding.cardTotalSavings,
            binding.cardMonthlyGoal,
            binding.cardGoalProgress,
            binding.cardStreak
        ).forEachIndexed { index, view -> view.animateCardEntrance(index) }
    }

    // ─── Real Data ────────────────────────────────────────────────────────────

    private fun loadRealData() {
        // 1. Greeting — time-based
        binding.tvGreeting.text = buildGreeting()

        // 2. Avatar initials from city name
        val city = prefs.city
        val initials = if (city.isNotBlank()) city.take(2).uppercase() else "RQ"
        binding.tvAvatarInitialsInline.text = initials

        // 3. Static prefs-driven values
        val tankCap = prefs.tankCapacity
        val monthlyGoal = prefs.monthlyGoalLiters
        binding.tvTankCapacity.text = "of ${tankCap.toInt().formatIndian()} L capacity"
        binding.tvMonthlyGoalValue.text = monthlyGoal.toInt().formatIndian()

        // 4. All DB-driven values (coroutine)
        viewLifecycleOwner.lifecycleScope.launch {
            val totalAll = repo.totalLiters()
            val thisMonth = repo.thisMonthLiters()
            val today = repo.todayLiters()
            val streak = repo.weekStreakBooleans()

            // Tank level = all-time harvest % of tank capacity
            val fillPct = if (tankCap > 0f) (totalAll / tankCap * 100f).coerceIn(0f, 100f) else 0f

            updateTankSection(totalAll, fillPct)
            updateInsight(today, thisMonth)
            animateTotalSavings(totalAll)
            updateGoalProgress(thisMonth, monthlyGoal)
            populateStreakDays(streak)
        }
    }

    private fun buildGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when {
            hour < 12 -> "Good morning! ☀️"
            hour < 17 -> "Good afternoon! 🌤"
            else      -> "Good evening! 🌙"
        }
    }

    private fun updateTankSection(totalLiters: Float, fillPct: Float) {
        binding.tvCurrentLevel.text = "${totalLiters.toInt().formatIndian()} L"

        // Animate tank fill
        binding.waterTankView.postDelayed({
            binding.waterTankView.setFillPercentage(fillPct, true)
        }, 500)

        // Status chip
        val status = HarvestCalculator.tankStatus(fillPct)
        binding.tvTankStatus.text = status
        val chipColor = when {
            fillPct < 25f -> R.color.accent_orange
            fillPct < 75f -> R.color.accent_teal
            else          -> R.color.accent_lime
        }
        binding.tvTankStatus.setTextColor(ContextCompat.getColor(requireContext(), chipColor))
        binding.dotTankStatus.backgroundTintList =
            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), chipColor))
    }

    private fun updateInsight(todayLiters: Float, thisMonthLiters: Float) {
        val insight = when {
            todayLiters > 0f -> {
                val cycles = HarvestCalculator.toWashingMachineCycles(todayLiters)
                if (cycles > 0)
                    "You saved ${todayLiters.toInt()} L today — enough for $cycles washing machine cycle${if (cycles > 1) "s" else ""}! 🌊"
                else
                    "You saved ${todayLiters.toInt()} L today. Keep it up! 💧"
            }
            thisMonthLiters > 0f ->
                "You've harvested ${thisMonthLiters.toInt().formatIndian()} L this month. Log today's rain to keep the streak!"
            else ->
                "No rainfall logged yet. Tap ＋ to log your first harvest and start saving water! 💧"
        }
        binding.tvInsightText.text = insight
    }

    private fun animateTotalSavings(totalLiters: Float) {
        val target = totalLiters.toInt()
        ValueAnimator.ofInt(0, target).apply {
            duration = if (target > 0) 1500L else 0L
            addUpdateListener { binding.tvTotalSavings.text = (it.animatedValue as Int).formatIndian() }
            start()
        }
    }

    private fun updateGoalProgress(thisMonth: Float, goal: Float) {
        if (goal <= 0f) {
            binding.pbGoalProgress.progress = 0
            binding.tvGoalPercentage.text = "0%"
            return
        }
        val pct = (thisMonth / goal * 100f).toInt().coerceIn(0, 100)
        binding.tvGoalPercentage.text = "$pct%"

        binding.pbGoalProgress.progress = 0
        binding.pbGoalProgress.postDelayed({
            android.animation.ObjectAnimator.ofInt(binding.pbGoalProgress, "progress", 0, pct).apply {
                duration = 1000
                interpolator = android.view.animation.DecelerateInterpolator()
                start()
            }
        }, 500)
    }

    private fun populateStreakDays(streak: List<Boolean>) {
        val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
        binding.streakDaysContainer.removeAllViews()
        val size = 32.dpToPx(requireContext())

        dayLabels.forEachIndexed { index, dayLabel ->
            val isActive = streak.getOrElse(index) { false }
            val bgRes = if (isActive) R.drawable.bg_streak_day_active else R.drawable.bg_streak_day_inactive
            val textColor = if (isActive) R.color.text_on_light_glass else R.color.text_secondary

            val tv = TextView(requireContext()).apply {
                text = dayLabel
                gravity = android.view.Gravity.CENTER
                setBackgroundResource(bgRes)
                setTextColor(ContextCompat.getColor(requireContext(), textColor))
                setTextAppearance(R.style.TextStyle_BodySecondary)
            }

            val container = android.widget.FrameLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f)
            }
            container.addView(tv, android.widget.FrameLayout.LayoutParams(size, size).apply {
                gravity = android.view.Gravity.CENTER
            })
            binding.streakDaysContainer.addView(container)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
