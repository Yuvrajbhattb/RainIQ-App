package com.rainiq.ui.onboarding

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.rainiq.ui.onboarding.pages.OnboardingPage1Fragment
import com.rainiq.ui.onboarding.pages.OnboardingPage2Fragment
import com.rainiq.ui.onboarding.pages.OnboardingPage3Fragment
import com.rainiq.ui.onboarding.pages.OnboardingPage4Fragment
import com.rainiq.ui.onboarding.pages.SetupFormFragment

/**
 * OnboardingPagerAdapter — ViewPager2 adapter for the 5 onboarding screens.
 *
 * Pages:
 * 0 — Every Drop, Counted (rain illustration)
 * 1 — Roof Conversion (cloud → tank animation)
 * 2 — Powered by AI (chat typewriter)
 * 3 — Track. Achieve. Impact. (counters + badges)
 * 4 — Setup Form (roof data collection)
 */
class OnboardingPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    private val fragments: List<Fragment> = listOf(
        OnboardingPage1Fragment(),
        OnboardingPage2Fragment(),
        OnboardingPage3Fragment(),
        OnboardingPage4Fragment(),
        SetupFormFragment()
    )

    override fun getItemCount(): Int = fragments.size

    override fun createFragment(position: Int): Fragment = fragments[position]

    fun getFragment(position: Int): Fragment? = fragments.getOrNull(position)
}
