package com.eneskayiklik.yellowtaxi.network.model

data class TripDayData(
    val day: Int = 0,
    val tripList: MutableList<TripDataModel> = mutableListOf()
) {
    val passengerCount: Int
        get() = tripList.sumBy { it.passengerCount }
    val totalDistance: Double
        get() = tripList.sumByDouble { it.tripDistance }
    val tripCount: Int
        get() = tripList.size
    val avgAmount: Double
        get() = tripList.sumByDouble { it.totalAmount } / tripCount
    val longestTrip: TripDataModel?
        get() = tripList.maxByOrNull { it.tripDistance }
    val shortestTrip: TripDataModel?
        get() = tripList.minByOrNull { it.tripDistance }
}