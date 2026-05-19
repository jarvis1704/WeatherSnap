package com.biprangshu.weathersnap.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val cityName: String,
    val latitude: Double,
    val longitude: Double,
    val temperatureCelsius: Double,
    val apparentTemperatureCelsius: Double,
    val weatherCode: Int,
    val condition: String,
    val humidityPercent: Int,
    val windSpeedKmh: Double,
    val pressureHpa: Double,
    val notes: String,
    val imagePath: String,
    val originalImageSizeBytes: Long,
    val compressedImageSizeBytes: Long,
    val createdAt: Long,
)
