package com.eneskayiklik.yellowtaxi.network.model.direction

data class DirectionResponse(
    val geocoded_waypoints: List<GeocodedWaypoint> ,
    val routes: List<Route>,
    val status: String
)