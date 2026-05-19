package com.biprangshu.weathersnap.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.biprangshu.weathersnap.data.local.entity.ReportEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Insert
    suspend fun insert(report: ReportEntity): Long

    @Query("SELECT * FROM reports ORDER BY createdAt DESC")
    fun getAllReports(): Flow<List<ReportEntity>>
}
