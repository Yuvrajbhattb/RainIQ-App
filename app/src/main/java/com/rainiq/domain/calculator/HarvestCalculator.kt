package com.rainiq.domain.calculator

/**
 * HarvestCalculator — Pure calculation engine for rainwater harvesting.
 *
 * Formula:
 *   Liters = roofAreaSqFt × 0.0929 (→ m²) × rainfallMm × 0.001 (→ m) × runoff × 1000 (m³ → L)
 *   Simplified: area × rainfall × 0.0929 × runoff
 *
 * Designed and Developed by Rahul Mirji
 */
object HarvestCalculator {

    private const val SQFT_TO_SQM = 0.0929f
    private const val MM_TO_M = 0.001f
    private const val M3_TO_LITERS = 1000f

    /** Calculate liters harvested from one rain event. */
    fun calculate(roofAreaSqFt: Float, rainfallMm: Float, runoff: Float): Float {
        if (roofAreaSqFt <= 0f || rainfallMm <= 0f || runoff <= 0f) return 0f
        return roofAreaSqFt * SQFT_TO_SQM * rainfallMm * MM_TO_M * runoff * M3_TO_LITERS
    }

    /**
     * Estimate a sensible monthly goal — based on average India monsoon rainfall (150 mm/month).
     * This is stored once after onboarding and shown as the "Monthly Goal" on the home screen.
     */
    fun estimateMonthlyGoal(roofAreaSqFt: Float, runoff: Float): Float {
        return calculate(roofAreaSqFt, 150f, runoff).coerceAtLeast(100f)
    }

    /** Status string for the tank chip based on fill %. */
    fun tankStatus(fillPercent: Float): String = when {
        fillPercent < 25f  -> "Running Low"
        fillPercent < 75f  -> "Looking Good"
        else               -> "Almost Full!"
    }

    /** Equivalent real-world uses for a given volume of water. */
    fun toWashingMachineCycles(liters: Float): Int = (liters / 60f).toInt() // avg 60L per cycle
    fun toDrinkingDays(liters: Float): Int = (liters / 3f).toInt()         // avg 3L/person/day
}
