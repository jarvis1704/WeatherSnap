package com.biprangshu.weathersnap.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.biprangshu.weathersnap.data.local.entity.ReportEntity

@Database(entities = [ReportEntity::class], version = 1, exportSchema = false)
abstract class WeatherSnapDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
}
