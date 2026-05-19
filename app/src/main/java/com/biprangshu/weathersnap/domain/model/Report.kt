package com.biprangshu.weathersnap.domain.model

data class Report(
    val id: Long = 0,
    val weather: Weather,
    val notes: String,
    val imagePath: String,
    val originalImageSizeBytes: Long,
    val compressedImageSizeBytes: Long,
    val createdAt: Long,
)
