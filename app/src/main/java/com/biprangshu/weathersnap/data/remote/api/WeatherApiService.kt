package com.biprangshu.weathersnap.data.remote.api

import com.biprangshu.weathersnap.data.remote.dto.WeatherResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("v1/forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("current") current: String = "temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,surface_pressure,wind_speed_10m",
        @Query("wind_speed_unit") windSpeedUnit: String = "kmh",
    ): WeatherResponseDto
}
