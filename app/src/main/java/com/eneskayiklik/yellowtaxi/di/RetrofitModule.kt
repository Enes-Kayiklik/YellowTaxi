package com.eneskayiklik.yellowtaxi.di

import com.eneskayiklik.yellowtaxi.network.MapApi
import com.eneskayiklik.yellowtaxi.network.TaxiApi
import com.eneskayiklik.yellowtaxi.util.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {
    @Provides
    @Singleton
    fun provideRetrofit(): TaxiApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(BASE_URL)
        .build()
        .create(TaxiApi::class.java)

    @Provides
    @Singleton
    fun provideMapApi(): MapApi = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://maps.googleapis.com/")
        .build()
        .create(MapApi::class.java)
}