package com.rainiq.data.repository

import com.rainiq.data.db.AppDatabase
import com.rainiq.data.db.HarvestEntry
import com.rainiq.data.preferences.UserPreferences
import com.rainiq.domain.calculator.HarvestCalculator
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * HarvestRepository -- Single source of truth for all harvest data.
 * Wraps the DAO and exposes clean, calculated values to the UI layer.
 *
 * Designed and Developed by Rahul Mirji
 */
class HarvestRepository(
    private val db: AppDatabase,
    private val prefs: UserPreferences
) {
    private val dao = db.harvestDao()

    // Live list of all entries, newest first. Collect in Fragment with repeatOnLifecycle.
    val allEntries: Flow<List<HarvestEntry>> = dao.getAll()

    // Total liters ever harvested. 0f if no entries.
    suspend fun totalLiters(): Float = dao.getTotalLiters() ?: 0f

    // Liters harvested this calendar month.
    suspend fun thisMonthLiters(): Float {
        val cal = Calendar.getInstance().apply {
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return dao.getLitersSince(cal.timeInMillis) ?: 0f
    }

    // Liters harvested today.
    suspend fun todayLiters(): Float {
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return dao.getLitersSince(cal.timeInMillis) ?: 0f
    }

    // Returns a 7-element boolean list [Mon..Sun] for the current week.
    // true = user logged at least one entry that day.
    suspend fun weekStreakBooleans(): List<Boolean> {
        val fmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val activeDays = dao.getDistinctActiveDays().toSet()

        val cal = Calendar.getInstance()
        val dow = cal.get(Calendar.DAY_OF_WEEK)
        val offset = if (dow == Calendar.SUNDAY) -6 else -(dow - Calendar.MONDAY)
        cal.add(Calendar.DAY_OF_YEAR, offset)

        return (0 until 7).map {
            val dayStr = fmt.format(cal.time)
            cal.add(Calendar.DAY_OF_YEAR, 1)
            activeDays.contains(dayStr)
        }
    }

    // Insert a new harvest session.
    // Automatically calculates liters from user's saved roof setup.
    suspend fun logRainfall(rainfallMm: Float) {
        val liters = HarvestCalculator.calculate(
            prefs.roofArea,
            rainfallMm,
            prefs.runoffCoefficient
        )
        dao.insert(
            HarvestEntry(
                dateMillis = System.currentTimeMillis(),
                rainfallMm = rainfallMm,
                harvestedLiters = liters,
                roofMaterial = prefs.roofMaterial
            )
        )
    }

    suspend fun deleteEntry(entry: HarvestEntry) = dao.delete(entry)
}
