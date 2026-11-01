package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.InspectionReport
import kotlinx.coroutines.flow.Flow

@Dao
interface InspectionDao {
    @Query("SELECT * FROM inspections ORDER BY timestamp DESC")
    fun getAllInspections(): Flow<List<InspectionReport>>

    @Query("SELECT * FROM inspections WHERE id = :id LIMIT 1")
    suspend fun getInspectionById(id: Long): InspectionReport?

    @Query("SELECT * FROM inspections WHERE id IN (:id1, :id2)")
    suspend fun getTwoInspections(id1: Long, id2: Long): List<InspectionReport>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInspection(report: InspectionReport): Long

    @Update
    suspend fun updateInspection(report: InspectionReport)

    @Delete
    suspend fun deleteInspection(report: InspectionReport)

    @Query("DELETE FROM inspections WHERE id = :id")
    suspend fun deleteInspectionById(id: Long)

    @Query("DELETE FROM inspections")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM inspections")
    fun getInspectionsCount(): Flow<Int>
}
