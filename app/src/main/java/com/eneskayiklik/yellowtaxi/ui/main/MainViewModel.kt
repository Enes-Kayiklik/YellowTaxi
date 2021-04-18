package com.eneskayiklik.yellowtaxi.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.eneskayiklik.yellowtaxi.network.TaxiApi
import com.eneskayiklik.yellowtaxi.network.model.TripDataModel
import com.eneskayiklik.yellowtaxi.network.model.TripDayData
import com.eneskayiklik.yellowtaxi.network.model.ZoneDataModel
import com.eneskayiklik.yellowtaxi.util.enums.FilterType
import com.eneskayiklik.yellowtaxi.util.extensions.getDayFromDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val taxiApi: TaxiApi
) : ViewModel() {
    private val nonFilterData = MutableList(31) { TripDayData(it + 1) }

    private val _tripDataFlow = MutableStateFlow(emptyList<TripDayData>())
    val tripDataFlow: StateFlow<List<TripDayData>> = _tripDataFlow

    private val _zoneDataFlow = MutableStateFlow(emptyList<ZoneDataModel>())
    val zoneDataFlow: StateFlow<List<ZoneDataModel>> = _zoneDataFlow

    private val _dayDataFlow = MutableStateFlow(emptyList<TripDataModel>())
    val dayDataFlow: StateFlow<List<TripDataModel>> = _dayDataFlow

    private val _selectedFilter = MutableStateFlow(FilterType.PASSENGER_SIZE)
    val selectedFilter: StateFlow<FilterType> = _selectedFilter

    private val _selectedDay = MutableStateFlow(emptyList<TripDataModel>())
    val selectedDay: StateFlow<List<TripDataModel>> = _selectedDay

    private val _showDatePicker = MutableStateFlow(false)
    val showDatePicker: StateFlow<Boolean> = _showDatePicker

    private val _minMaxDistance = MutableStateFlow<List<Double>>(listOf())
    val minMaxDistance: StateFlow<List<Double>> = _minMaxDistance

    private var _isFiltered: Boolean = false

    init {
        if (nonFilterData.any { it.tripCount == 0 }) {
            getTripData()
            getZoneData()
        }
    }

    private fun getZoneData() {
        viewModelScope.launch {
            _zoneDataFlow.value = taxiApi.getZoneData()
        }
    }

    private fun getTripData() {
        viewModelScope.launch {
            separateTripData(taxiApi.getTripData())
        }
    }

    private fun separateTripData(data: List<TripDataModel>) {
        data.forEach {
            val day = it.pickupDate.getDayFromDate()
            nonFilterData[day].tripList.add(it)
        }
        _tripDataFlow.value = nonFilterData
        getMinAndMaxDistance()
    }

    fun sortData(key: FilterType, distance: Double? = null) {
        _isFiltered = true
        _selectedFilter.value = key
        when (key) {
            FilterType.PASSENGER_SIZE -> {
                _tripDataFlow.value = nonFilterData.sortedByDescending { it.passengerCount }.subList(0, 5)
                _dayDataFlow.value = listOf()
            }
            FilterType.AVG_AMOUNT -> {
                var (index1, index2) = listOf(0, 0)
                var data1 = nonFilterData.first().avgAmount
                nonFilterData.forEachIndexed { i, it ->
                    if (it.avgAmount < data1)
                        data1 = it.avgAmount.also { index2 = index1; index1 = i }
                }
                _tripDataFlow.value = nonFilterData.subList(minOf(index1, index2), maxOf(index1, index2) + 1)
                _dayDataFlow.value = listOf()
            }
            FilterType.MAX_BY_DISTANCE -> {
                val allList = mutableListOf<TripDataModel>()
                nonFilterData.forEach { allList.addAll(it.tripList) }
                _dayDataFlow.value = allList.sortedByDescending { it.tripDistance }.subList(0, 5)
            }
            FilterType.MAX_BY_SELECTED_DISTANCE -> _tripDataFlow.value = nonFilterData.filter { it.totalDistance < distance ?: 0.0 }
        }
    }

    fun clearFilter() {
        _dayDataFlow.value = listOf()
        if (_isFiltered)
            _tripDataFlow.value = nonFilterData.also { _isFiltered = false; _selectedFilter.value = FilterType.PASSENGER_SIZE }
    }

    fun selectDay(index: Int) {
        _selectedDay.value = nonFilterData[index].tripList
    }

    fun showDatePicker(b: Boolean) {
        _showDatePicker.value = b
    }

    fun selectMinDistance(start: Int, end: Int) {
        val allList = mutableListOf<TripDataModel>()
        if (end < 30)
            nonFilterData.subList(start - 1, end).forEach { allList.addAll(it.tripList) }
        else
            nonFilterData.subList(start - 1, end - 1).forEach { allList.addAll(it.tripList) }
        _dayDataFlow.value = allList.filter { it.tripDistance != 0.0 }.sortedBy { it.tripDistance }.subList(0, 5)
    }

    private fun getMinAndMaxDistance() {
        _minMaxDistance.value = listOf(
            nonFilterData.minByOrNull { it.totalDistance }?.totalDistance ?: 0.0,
            nonFilterData.maxByOrNull { it.totalDistance }?.totalDistance ?: 0.0
        )
    }
}
