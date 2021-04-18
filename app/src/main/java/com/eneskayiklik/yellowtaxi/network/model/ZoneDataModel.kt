package com.eneskayiklik.yellowtaxi.network.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ZoneDataModel(
    @SerializedName("LocationID")
    val locationId: Int = 0,
    @SerializedName("zone")
    val zoneName: String = "",
    val latitude: Float = 0F,
    val longitude: Float = 0F,
): Parcelable
