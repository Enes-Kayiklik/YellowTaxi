package com.eneskayiklik.yellowtaxi.ui.main.map

import android.content.Context
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.yellowtaxi.R
import com.eneskayiklik.yellowtaxi.network.MapApi
import com.eneskayiklik.yellowtaxi.network.model.direction.DirectionResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val mapApi: MapApi
) : ViewModel() {
    private val _directionData = MutableStateFlow<DirectionResponse?>(null)
    val directionData: StateFlow<DirectionResponse?> = _directionData

    fun getMapData(origin: String, destination: String, context: Context) {
        viewModelScope.launch {
            _directionData.value = mapApi.getDirection(origin, destination, context.getString(R.string.google_maps_key))
        }
    }
}