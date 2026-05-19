package com.biprangshu.weathersnap.domain.repository

import com.biprangshu.weathersnap.domain.model.City
import com.biprangshu.weathersnap.domain.model.Report
import com.biprangshu.weathersnap.domain.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun searchCities(query: String): Result<List<City>>
    suspend fun getWeather(city: City): Result<Weather>
    suspend fun saveReport(report: Report): Long
    fun getAllReports(): Flow<List<Report>>
}
