package com.rainiq.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * HarvestEntry — A single rainfall logging event.
 * Stores the raw rainfall (mm) entered by the user and the calculated harvest in liters.
 *
 * Designed and Developed by Rahul Mirji
 */
@Entity(tableName = "harvest_entries")
data class HarvestEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    /** Unix timestamp (ms) of when the entry was logged. */
    val dateMillis: Long,

    /** Rainfall in mm as entered by the user. */
    val rainfallMm: Float,

    /** Liters harvested — calculated from HarvestCalculator at insert time. */
    val harvestedLiters: Float,

    /** Snapshot of the roof material used at the time of this entry. */
    val roofMaterial: String
)
