package com.biprangshu.weathersnap.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponseDto(
    @SerializedName("latitude") val latitude: Double,
    @SerializedName("longitude") val longitude: Double,
    @SerializedName("current") val current: CurrentWeatherDto,
)

data class CurrentWeatherDto(
    @SerializedName("temperature_2m") val temperature2m: Double,
    @SerializedName("relative_humidity_2m") val relativeHumidity2m: Int,
    @SerializedName("apparent_temperature") val apparentTemperature: Double,
    @SerializedName("weather_code") val weatherCode: Int,
    @SerializedName("surface_pressure") val surfacePressure: Double,
    @SerializedName("wind_speed_10m") val windSpeed10m: Double,
)
