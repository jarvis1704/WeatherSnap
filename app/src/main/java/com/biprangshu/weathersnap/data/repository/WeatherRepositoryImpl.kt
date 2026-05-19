package com.biprangshu.weathersnap.data.repository

import com.biprangshu.weathersnap.data.local.db.ReportDao
import com.biprangshu.weathersnap.data.local.entity.ReportEntity
import com.biprangshu.weathersnap.data.remote.api.GeocodingApiService
import com.biprangshu.weathersnap.data.remote.api.WeatherApiService
import com.biprangshu.weathersnap.data.remote.dto.CityDto
import com.biprangshu.weathersnap.data.remote.dto.WeatherResponseDto
import com.biprangshu.weathersnap.domain.model.City
import com.biprangshu.weathersnap.domain.model.Report
import com.biprangshu.weathersnap.domain.model.Weather
import com.biprangshu.weathersnap.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherRepositoryImpl @Inject constructor(
    private val geocodingApi: GeocodingApiService,
    private val weatherApi: WeatherApiService,
    private val reportDao: ReportDao,
) : WeatherRepository {

    private val suggestionCache = mutableMapOf<String, List<City>>()

    override suspend fun searchCities(query: String): Result<List<City>> {
        suggestionCache[query]?.let { return Result.success(it) }
        return runCatching {
            val response = geocodingApi.searchCities(name = query)
            val cities = response.results?.map { it.toDomain() } ?: emptyList()
            suggestionCache[query] = cities
            cities
        }
    }

    override suspend fun getWeather(city: City): Result<Weather> = runCatching {
        val response = weatherApi.getCurrentWeather(
            latitude = city.latitude,
            longitude = city.longitude,
        )
        response.toDomain(cityName = city.name, lat = city.latitude, lon = city.longitude)
    }

    override suspend fun saveReport(report: Report): Long =
        reportDao.insert(report.toEntity())

    override fun getAllReports(): Flow<List<Report>> =
        reportDao.getAllReports().map { entities -> entities.map { it.toDomain() } }

    private fun CityDto.toDomain() = City(
        id = id,
        name = name,
        latitude = latitude,
        longitude = longitude,
        country = country ?: "",
        region = admin1 ?: "",
    )

    private fun WeatherResponseDto.toDomain(cityName: String, lat: Double, lon: Double) = Weather(
        cityName = cityName,
        latitude = lat,
        longitude = lon,
        temperatureCelsius = current.temperature2m,
        apparentTemperatureCelsius = current.apparentTemperature,
        weatherCode = current.weatherCode,
        condition = Weather.weatherCodeToCondition(current.weatherCode),
        humidityPercent = current.relativeHumidity2m,
        windSpeedKmh = current.windSpeed10m,
        pressureHpa = current.surfacePressure,
    )

    private fun Report.toEntity() = ReportEntity(
        id = id,
        cityName = weather.cityName,
        latitude = weather.latitude,
        longitude = weather.longitude,
        temperatureCelsius = weather.temperatureCelsius,
        apparentTemperatureCelsius = weather.apparentTemperatureCelsius,
        weatherCode = weather.weatherCode,
        condition = weather.condition,
        humidityPercent = weather.humidityPercent,
        windSpeedKmh = weather.windSpeedKmh,
        pressureHpa = weather.pressureHpa,
        notes = notes,
        imagePath = imagePath,
        originalImageSizeBytes = originalImageSizeBytes,
        compressedImageSizeBytes = compressedImageSizeBytes,
        createdAt = createdAt,
    )

    private fun ReportEntity.toDomain() = Report(
        id = id,
        weather = Weather(
            cityName = cityName,
            latitude = latitude,
            longitude = longitude,
            temperatureCelsius = temperatureCelsius,
            apparentTemperatureCelsius = apparentTemperatureCelsius,
            weatherCode = weatherCode,
            condition = condition,
            humidityPercent = humidityPercent,
            windSpeedKmh = windSpeedKmh,
            pressureHpa = pressureHpa,
        ),
        notes = notes,
        imagePath = imagePath,
        originalImageSizeBytes = originalImageSizeBytes,
        compressedImageSizeBytes = compressedImageSizeBytes,
        createdAt = createdAt,
    )
}
