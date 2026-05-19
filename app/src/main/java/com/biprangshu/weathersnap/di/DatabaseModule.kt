package com.biprangshu.weathersnap.di

import android.content.Context
import androidx.room.Room
import com.biprangshu.weathersnap.data.local.db.ReportDao
import com.biprangshu.weathersnap.data.local.db.WeatherSnapDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): WeatherSnapDatabase =
        Room.databaseBuilder(
            context,
            WeatherSnapDatabase::class.java,
            "weathersnap.db",
        ).build()

    @Provides
    @Singleton
    fun provideReportDao(database: WeatherSnapDatabase): ReportDao =
        database.reportDao()
}
