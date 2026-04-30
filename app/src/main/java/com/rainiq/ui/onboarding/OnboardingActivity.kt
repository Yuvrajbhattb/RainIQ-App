package com.rainiq.ui.onboarding

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import androidx.viewpager2.widget.ViewPager2
import com.rainiq.databinding.ActivityOnboardingBinding
import com.rainiq.ui.onboarding.pages.OnboardingPage1Fragment
import com.rainiq.ui.onboarding.pages.OnboardingPage2Fragment
import com.rainiq.ui.onboarding.pages.OnboardingPage3Fragment
import com.rainiq.ui.onboarding.pages.OnboardingPage4Fragment
import com.rainiq.ui.onboarding.pages.SetupFormFragment

/**
 * OnboardingActivity — Hosts the ViewPager2 with 5 onboarding screens.
 */
class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var adapter: OnboardingPagerAdapter

    private val TOTAL_PAGES = 5
    private val SETUP_PAGE = 4

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsCtrl = WindowInsetsControllerCompat(window, window.decorView)
        insetsCtrl.isAppearanceLightStatusBars = false
        insetsCtrl.isAppearanceLightNavigationBars = false

        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.bottomControls) { view, insets ->
            val navBar = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = navBar.bottom + resources.getDimensionPixelSize(
                    com.rainiq.R.dimen.nav_bar_margin_bottom
                )
            }
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.btnSkip) { view, insets ->
            val statusBar = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = statusBar.top + 12
            }
            insets
        }

        setupViewPager()
        setupDots()
        setupButtons()
    }

    private fun setupViewPager() {
        adapter = OnboardingPagerAdapter(this)
        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateDots(position)
                updateButtonLabel(position)
                toggleBottomControls(position)
            }
        })
    }

    private fun setupDots() {
        binding.dotsContainer.removeAllViews()
        val dp6 = (6 * resources.displayMetrics.density).toInt()
        val dp4 = (4 * resources.displayMetrics.density).toInt()

        repeat(TOTAL_PAGES - 1) { i ->
            val dot = View(this)
            val params = LinearLayout.LayoutParams(dp6, dp6).apply {
                marginEnd = dp4
                marginStart = dp4
            }
            dot.layoutParams = params
            dot.setBackgroundResource(com.rainiq.R.drawable.bg_notification_badge)
            dot.backgroundTintList = android.content.res.ColorStateList.valueOf(
                if (i == 0) Color.WHITE else Color.argb(128, 255, 255, 255)
            )
            binding.dotsContainer.addView(dot)
        }
    }

    private fun updateDots(position: Int) {
        val displayPos = position.coerceAtMost(TOTAL_PAGES - 2)
        repeat(binding.dotsContainer.childCount) { i ->
            val dot = binding.dotsContainer.getChildAt(i)
            val isSelected = i == displayPos
            
            val targetColor = if (isSelected) Color.WHITE else Color.argb(128, 255, 255, 255)
            dot.backgroundTintList = android.content.res.ColorStateList.valueOf(targetColor)
            
            val targetSize = if (isSelected) {
                (10 * resources.displayMetrics.density).toInt()
            } else {
                (6 * resources.displayMetrics.density).toInt()
            }
            
            val currentSize = dot.layoutParams.width
            if (currentSize != targetSize && currentSize > 0) {
                android.animation.ValueAnimator.ofInt(currentSize, targetSize).apply {
                    duration = 200
                    addUpdateListener { anim ->
                        val value = anim.animatedValue as Int
                        dot.layoutParams = (dot.layoutParams as LinearLayout.LayoutParams).apply {
                            width = value
                            height = value
                        }
                    }
                    start()
                }
            } else {
                dot.layoutParams = (dot.layoutParams as LinearLayout.LayoutParams).apply {
                    width = targetSize
                    height = targetSize
                }
            }
        }
    }

    private fun updateButtonLabel(position: Int) {
        binding.tvNextLabel.text = when (position) {
            TOTAL_PAGES - 2 -> "SET UP MY ROOF →"
            else -> "NEXT →"
        }
    }

    private fun toggleBottomControls(position: Int) {
        val isSetupPage = position == SETUP_PAGE
        binding.bottomControls.animate()
            .alpha(if (isSetupPage) 0f else 1f)
            .setDuration(250)
            .withEndAction {
                binding.bottomControls.visibility = if (isSetupPage) View.INVISIBLE else View.VISIBLE
            }.start()

        binding.btnSkip.animate()
            .alpha(if (isSetupPage) 0f else 1f)
            .setDuration(200).start()
    }

    private fun setupButtons() {
        binding.btnNext.setOnClickListener {
            val current = binding.viewPager.currentItem
            if (current < TOTAL_PAGES - 1) {
                it.animate().scaleX(0.95f).scaleY(0.95f).setDuration(80).withEndAction {
                    it.animate().scaleX(1.02f).scaleY(1.02f).setDuration(100).withEndAction {
                        it.animate().scaleX(1f).scaleY(1f).setDuration(80).start()
                        binding.viewPager.currentItem = current + 1
                    }.start()
                }.start()
            }
        }

        binding.btnSkip.setOnClickListener {
            binding.viewPager.currentItem = SETUP_PAGE
        }
    }
}
