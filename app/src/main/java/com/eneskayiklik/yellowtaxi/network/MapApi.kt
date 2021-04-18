package com.eneskayiklik.yellowtaxi.network

import com.eneskayiklik.yellowtaxi.R
import com.eneskayiklik.yellowtaxi.network.model.direction.DirectionResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MapApi {
    @GET("/maps/api/directions/json")
    suspend fun getDirection(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String
    ): DirectionResponse
}