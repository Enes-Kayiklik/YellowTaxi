package com.eneskayiklik.yellowtaxi.network.model

import com.google.gson.annotations.SerializedName

data class TripDataModel(
    @SerializedName("tpep_pickup_datetime")
    val pickupDate: String = "",
    @SerializedName("tpep_dropoff_datetime")
    val dropOffDate: String = "",
    @SerializedName("passenger_count")
    val passengerCount: Int = 0,
    @SerializedName("trip_distance")
    val tripDistance: Double = 0.0,
    @SerializedName("PULocationID")
    val puLocationId: Int = 0,
    @SerializedName("DOLocationID")
    val doLocationId: Int = 0,
    @SerializedName("total_amount")
    val totalAmount: Double = 0.0
)
