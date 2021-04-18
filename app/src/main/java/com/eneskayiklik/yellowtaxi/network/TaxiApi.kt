package com.eneskayiklik.yellowtaxi.network

import com.eneskayiklik.yellowtaxi.network.model.TripDataModel
import com.eneskayiklik.yellowtaxi.network.model.ZoneDataModel
import retrofit2.http.GET

interface TaxiApi {
    @GET("/Enes-Kayiklik/KocaeliUniversitesiProgramlamaLab-1/master/zonedata.json")
    suspend fun getZoneData(): List<ZoneDataModel>

    @GET("Enes-Kayiklik/KocaeliUniversitesiProgramlamaLab-1/master/yellow-tripData.json")
    suspend fun getTripData(): List<TripDataModel>
}