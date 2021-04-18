package com.eneskayiklik.yellowtaxi.network.model.direction

data class GeocodedWaypoint(
    val geocoder_status: String,
    val place_id: String,
    val types: List<String>
)