package com.rainiq.data.preferences

import android.content.Context
import android.content.SharedPreferences

/**
 * UserPreferences — SharedPreferences wrapper for all user setup data.
 * Stores onboarding completion flag, roof setup data, and app preferences.
 */
class UserPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // ─────────────────────────────────────────────────────────
    // Onboarding
    // ─────────────────────────────────────────────────────────

    var isOnboardingDone: Boolean
        get() = prefs.getBoolean(KEY_ONBOARDING_DONE, false)
        set(value) = prefs.edit().putBoolean(KEY_ONBOARDING_DONE, value).apply()

    var userName: String
        get() = prefs.getString(KEY_USER_NAME, "") ?: ""
        set(value) = prefs.edit().putString(KEY_USER_NAME, value).apply()

    // ─────────────────────────────────────────────────────────
    // Roof Setup
    // ─────────────────────────────────────────────────────────

    var roofArea: Float
        get() = prefs.getFloat(KEY_ROOF_AREA, 0f)
        set(value) = prefs.edit().putFloat(KEY_ROOF_AREA, value).apply()

    var tankCapacity: Float
        get() = prefs.getFloat(KEY_TANK_CAPACITY, 0f)
        set(value) = prefs.edit().putFloat(KEY_TANK_CAPACITY, value).apply()

    var roofMaterial: String
        get() = prefs.getString(KEY_ROOF_MATERIAL, "Concrete/RCC") ?: "Concrete/RCC"
        set(value) = prefs.edit().putString(KEY_ROOF_MATERIAL, value).apply()

    var runoffCoefficient: Float
        get() = prefs.getFloat(KEY_RUNOFF_COEFFICIENT, 0.85f)
        set(value) = prefs.edit().putFloat(KEY_RUNOFF_COEFFICIENT, value).apply()

    var city: String
        get() = prefs.getString(KEY_CITY, "") ?: ""
        set(value) = prefs.edit().putString(KEY_CITY, value).apply()

    /** Auto-calculated once at end of onboarding. Used as the Home screen monthly goal. */
    var monthlyGoalLiters: Float
        get() = prefs.getFloat(KEY_MONTHLY_GOAL, 0f)
        set(value) = prefs.edit().putFloat(KEY_MONTHLY_GOAL, value).apply()

    /**
     * Saves all roof setup data atomically.
     */
    fun saveRoofSetup(
        roofArea: Float,
        tankCapacity: Float,
        roofMaterial: String,
        runoffCoefficient: Float,
        city: String
    ) {
        val monthlyGoal = com.rainiq.domain.calculator.HarvestCalculator
            .estimateMonthlyGoal(roofArea, runoffCoefficient)
        prefs.edit()
            .putFloat(KEY_ROOF_AREA, roofArea)
            .putFloat(KEY_TANK_CAPACITY, tankCapacity)
            .putString(KEY_ROOF_MATERIAL, roofMaterial)
            .putFloat(KEY_RUNOFF_COEFFICIENT, runoffCoefficient)
            .putString(KEY_CITY, city)
            .putFloat(KEY_MONTHLY_GOAL, monthlyGoal)
            .putBoolean(KEY_ONBOARDING_DONE, true)
            .apply()
    }

    fun clearAll() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val PREFS_NAME = "jal_sanchay_prefs"
        const val KEY_ONBOARDING_DONE = "onboarding_done"
        const val KEY_USER_NAME = "user_name"
        const val KEY_ROOF_AREA = "roof_area"
        const val KEY_TANK_CAPACITY = "tank_capacity"
        const val KEY_ROOF_MATERIAL = "roof_material"
        const val KEY_RUNOFF_COEFFICIENT = "runoff_coefficient"
        const val KEY_CITY = "city"
        const val KEY_MONTHLY_GOAL = "monthly_goal_liters"
    }
}
