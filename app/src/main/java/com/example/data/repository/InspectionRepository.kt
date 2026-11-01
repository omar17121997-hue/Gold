package com.example.data.repository

import com.example.data.dao.InspectionDao
import com.example.data.model.InspectionReport
import kotlinx.coroutines.flow.Flow

class InspectionRepository(private val dao: InspectionDao) {
    val allInspections: Flow<List<InspectionReport>> = dao.getAllInspections()
    val count: Flow<Int> = dao.getInspectionsCount()

    suspend fun getById(id: Long): InspectionReport? = dao.getInspectionById(id)

    suspend fun getTwo(id1: Long, id2: Long): List<InspectionReport> = dao.getTwoInspections(id1, id2)

    suspend fun saveInspection(report: InspectionReport): Long = dao.insertInspection(report)

    suspend fun deleteById(id: Long) = dao.deleteInspectionById(id)

    suspend fun clearHistory() = dao.clearAll()
}
