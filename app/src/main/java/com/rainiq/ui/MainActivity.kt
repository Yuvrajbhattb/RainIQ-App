package com.rainiq.ui

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import com.rainiq.R
import com.rainiq.databinding.ActivityMainBinding
import com.rainiq.ui.ai.AiTipsFragment
import com.rainiq.ui.dashboard.DashboardFragment
import com.rainiq.ui.features.AchievementsFragment
import com.rainiq.ui.features.AiTipsDetailFragment
import com.rainiq.ui.features.GardenFragment
import com.rainiq.ui.features.GoalsFragment
import com.rainiq.ui.features.HistoryDetailFragment
import com.rainiq.ui.features.ImpactReportFragment
import com.rainiq.ui.features.JalBotFragment
import com.rainiq.ui.features.ShareFragment
import com.rainiq.ui.features.TrackRainFragment
import com.rainiq.ui.features.WeatherFragment
import com.rainiq.ui.history.HistoryFragment
import com.rainiq.ui.more.ExploreFragment
import com.rainiq.ui.settings.ProfileFragment

/**
 * MainActivity — Root activity for RainIQ Tracker.
 *
 * Navigation model:
 * - Bottom nav tabs = root-level destinations (no back stack)
 * - Feature screens pushed via navigateTo() with addToBackStack
 * - Back button: pops back stack or goes to Dashboard if at root
 * - Screen header ← button = same as system back
 * - Screen header ••• button = context-aware options
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Current active bottom nav tab index (0 = Dashboard)
    private var activeNavIndex = 0

    // Root tab fragment instances (kept alive, not in back stack)
    private val dashboardFragment by lazy { DashboardFragment() }
    private val historyFragment by lazy { HistoryFragment() }
    private val aiTipsFragment by lazy { AiTipsFragment() }
    private val exploreFragment by lazy { ExploreFragment() }
    private val profileFragment by lazy { ProfileFragment() }

    // Background drawables per nav tab
    private val backgroundResources = mapOf(
        0 to R.drawable.gradient_bg_dark_green,
        1 to R.drawable.gradient_bg_dark_green,
        2 to R.drawable.gradient_bg_dark_green,
        3 to R.drawable.gradient_bg_dark_green
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val insetsController = WindowInsetsControllerCompat(window, window.decorView)
        insetsController.isAppearanceLightStatusBars = false
        insetsController.isAppearanceLightNavigationBars = false

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupWindowInsets()

        if (savedInstanceState == null) {
            showRootFragment(dashboardFragment)
        }

        applyDesaturation(binding.imgBackground)
        setupBottomNav()
        setupHeaderButtons()
        setupFab()

        // Listen to back stack to update header visibility
        supportFragmentManager.addOnBackStackChangedListener {
            onBackStackChanged()
        }
    }

    // ─────────────────────────────────────────────────────────
    // Window Insets
    // ─────────────────────────────────────────────────────────

    private fun setupWindowInsets() {
        val applyStatusBarMargin = { view: View, insets: WindowInsetsCompat ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                topMargin = statusBarInsets.top
            }
            insets
        }
        // Apply status bar top margin to screen header (used for all non-dashboard tabs / feature screens)
        ViewCompat.setOnApplyWindowInsetsListener(binding.layoutScreenHeader.root, applyStatusBarMargin)
        // Apply status bar top padding to the scroll view root on Dashboard (inline header)
        ViewCompat.setOnApplyWindowInsetsListener(binding.fragmentContainer) { view, insets ->
            val statusBarInsets = insets.getInsets(WindowInsetsCompat.Type.statusBars())
            // Pass status bar height to dashboard so inline header pads correctly
            view.tag = statusBarInsets.top
            insets
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.navContainer) { view, insets ->
            val navBarInsets = insets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = navBarInsets.bottom + resources.getDimensionPixelSize(R.dimen.nav_bar_margin_bottom)
            }
            insets
        }
    }

    // ─────────────────────────────────────────────────────────
    // Header Button Setup
    // ─────────────────────────────────────────────────────────

    private fun setupHeaderButtons() {
        // Home overlay header is now deprecated (inline in dashboard) — keep wiring for safety
        binding.layoutHeader.root.visibility = View.GONE

        // Screen header: ← back arrow
        binding.layoutScreenHeader.btnScreenBack.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        // Screen header: ••• options
        binding.layoutScreenHeader.btnScreenOptions.setOnClickListener {
            showOptionsForCurrentScreen()
        }
    }

    private fun showOptionsForCurrentScreen() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer)
        val options = when (currentFragment) {
            is TrackRainFragment -> "Clear form"
            is HistoryDetailFragment -> "Export as PDF"
            is JalBotFragment -> "Clear chat"
            is ProfileFragment -> "Edit profile"
            is GoalsFragment -> "Reset goals"
            is AchievementsFragment -> "Share achievements"
            is ImpactReportFragment -> "Share report"
            else -> "Options"
        }
        Toast.makeText(this, options, Toast.LENGTH_SHORT).show()
    }

    // ─────────────────────────────────────────────────────────
    // Background
    // ─────────────────────────────────────────────────────────

    private fun applyDesaturation(imageView: ImageView) {
        val matrix = ColorMatrix()
        matrix.setSaturation(0.85f)
        imageView.colorFilter = ColorMatrixColorFilter(matrix)
    }

    private fun switchBackground(navIndex: Int) {
        val newRes = backgroundResources[navIndex] ?: R.drawable.gradient_bg_dark_green
        binding.imgBackground.animate()
            .alpha(0f).setDuration(200)
            .withEndAction {
                binding.imgBackground.setImageResource(newRes)
                binding.imgBackground.animate().alpha(1f).setDuration(200).start()
            }.start()
    }

    // ─────────────────────────────────────────────────────────
    // Bottom Navigation
    // ─────────────────────────────────────────────────────────

    private fun setupBottomNav() {
        with(binding) {
            navIconContainerDashboard.setOnClickListener { selectNav(0) }
            navIconContainerLogEntry.setOnClickListener { selectNav(1) }
            navIconContainerAiTips.setOnClickListener { selectNav(2) }
            navIconContainerMore.setOnClickListener { selectNav(3) }
        }
        updateNavState(0)
        switchHeaderForTab(0)
    }

    private fun selectNav(index: Int) {
        // Pop any feature screens pushed on top
        clearFeatureBackStack()

        if (index == activeNavIndex) return
        activeNavIndex = index
        updateNavState(index)
        switchBackground(index)
        switchHeaderForTab(index)

        val fragment = when (index) {
            0 -> dashboardFragment
            1 -> historyFragment
            2 -> aiTipsFragment
            3 -> exploreFragment
            else -> dashboardFragment
        }
        showRootFragment(fragment)
    }

    private fun switchHeaderForTab(index: Int) {
        val screenTitles = mapOf(
            1 to "History",
            2 to "AI Insights",
            3 to "Explore"
        )
        // Overlay home header is always hidden — it's now inline inside DashboardFragment's ScrollView
        binding.layoutHeader.root.visibility = View.GONE
        val screenHeader: View = binding.layoutScreenHeader.root

        if (index == 0) {
            // Dashboard: no screen header needed (header is inside scroll view)
            screenHeader.visibility = View.GONE
        } else {
            screenHeader.visibility = View.VISIBLE
            screenHeader.findViewById<android.widget.TextView>(R.id.tvScreenTitle)
                .text = screenTitles[index] ?: ""
        }
    }

    private fun updateNavState(activeIndex: Int) {
        val icons = listOf(
            binding.navIconDashboard,
            binding.navIconLogEntry,
            binding.navIconAiTips,
            binding.navIconMore
        )
        icons.forEachIndexed { i, icon ->
            icon.alpha = if (i == activeIndex) 1.0f else 0.45f
        }
    }

    // ─────────────────────────────────────────────────────────
    // Feature Screen Navigation (back stack)
    // ─────────────────────────────────────────────────────────

    /**
     * Navigate to a feature screen, pushing it onto the back stack.
     * Hides ALL currently visible fragments to prevent overlap on return.
     */
    fun navigateTo(fragment: Fragment, title: String) {
        val tag = fragment::class.java.simpleName
        val fm = supportFragmentManager

        // Guard: don't add duplicate if already on back stack
        if (fm.findFragmentByTag(tag) != null && fm.backStackEntryCount > 0) return

        fm.beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_right,
                R.anim.slide_out_left,
                R.anim.slide_in_left,
                R.anim.slide_out_right
            )
            .apply {
                // Hide EVERY fragment currently in the container (not just the last one)
                fm.fragments.forEach { frag ->
                    if (!frag.isHidden) hide(frag)
                }
            }
            .add(R.id.fragmentContainer, fragment, tag)
            .addToBackStack(tag)
            .commit()

        // Show screen header with title
        binding.layoutHeader.root.visibility = View.GONE
        binding.layoutScreenHeader.root.visibility = View.VISIBLE
        binding.layoutScreenHeader.root
            .findViewById<android.widget.TextView>(R.id.tvScreenTitle)
            .text = title
    }

    private fun onBackStackChanged() {
        val backStackCount = supportFragmentManager.backStackEntryCount
        if (backStackCount == 0) {
            // Back at root — hide all fragments, then explicitly show the active root fragment
            val activeRoot = when (activeNavIndex) {
                0 -> dashboardFragment
                1 -> historyFragment
                2 -> aiTipsFragment
                3 -> exploreFragment
                else -> dashboardFragment
            }
            val fm = supportFragmentManager
            fm.beginTransaction().apply {
                fm.fragments.forEach { frag ->
                    if (frag !== activeRoot && !frag.isHidden) hide(frag)
                }
                if (activeRoot.isAdded && activeRoot.isHidden) show(activeRoot)
            }.commit()
            switchHeaderForTab(activeNavIndex)
        }
    }

    /** Clear all feature fragments from the back stack (when switching tabs). */
    private fun clearFeatureBackStack() {
        val count = supportFragmentManager.backStackEntryCount
        if (count > 0) {
            supportFragmentManager.popBackStackImmediate(
                supportFragmentManager.getBackStackEntryAt(0).name,
                androidx.fragment.app.FragmentManager.POP_BACK_STACK_INCLUSIVE
            )
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else if (activeNavIndex != 0) {
            selectNav(0)
        } else {
            super.onBackPressed()
        }
    }

    // ─────────────────────────────────────────────────────────
    // Root Fragment Management (tab switching, no back stack)
    // ─────────────────────────────────────────────────────────

    private fun showRootFragment(fragment: Fragment) {
        val tag = fragment::class.java.simpleName
        val fm = supportFragmentManager
        val existing = fm.findFragmentByTag(tag)

        fm.beginTransaction().apply {
            // Hide ALL fragments in the container (including non-visible ones)
            fm.fragments.forEach { frag -> if (!frag.isHidden) hide(frag) }
            if (existing != null) show(existing)
            else add(R.id.fragmentContainer, fragment, tag)
        }.commitNow()
    }

    // ─────────────────────────────────────────────────────────
    // FAB / Add Entry
    // ─────────────────────────────────────────────────────────

    private fun setupFab() {
        binding.navIconContainerAdd.setOnClickListener {
            com.rainiq.ui.entry.AddEntryBottomSheet().show(
                supportFragmentManager,
                com.rainiq.ui.entry.AddEntryBottomSheet.TAG
            )
        }
    }
}
