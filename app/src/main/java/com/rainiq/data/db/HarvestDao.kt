package com.rainiq.data.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/**
 * HarvestDao — Data Access Object for harvest_entries table.
 *
 * Designed and Developed by Rahul Mirji
 */
@Dao
interface HarvestDao {

    @Insert
    suspend fun insert(entry: HarvestEntry)

    @Delete
    suspend fun delete(entry: HarvestEntry)

    /** All entries newest-first — emits on every DB change. */
    @Query("SELECT * FROM harvest_entries ORDER BY dateMillis DESC")
    fun getAll(): Flow<List<HarvestEntry>>

    /** Sum of all harvested liters ever. Returns null if no entries. */
    @Query("SELECT SUM(harvestedLiters) FROM harvest_entries")
    suspend fun getTotalLiters(): Float?

    /** Sum of harvested liters since a given Unix timestamp (ms). */
    @Query("SELECT SUM(harvestedLiters) FROM harvest_entries WHERE dateMillis >= :sinceMillis")
    suspend fun getLitersSince(sinceMillis: Long): Float?

    /** Distinct calendar days (YYYY-MM-DD strings) that have at least one entry. */
    @Query("SELECT DISTINCT strftime('%Y-%m-%d', dateMillis / 1000, 'unixepoch', 'localtime') FROM harvest_entries ORDER BY 1 DESC LIMIT 30")
    suspend fun getDistinctActiveDays(): List<String>
}
