package com.eneskayiklik.yellowtaxi.ui.main.map

import android.graphics.Color
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.eneskayiklik.yellowtaxi.R
import com.eneskayiklik.yellowtaxi.databinding.FragmentMapBinding
import com.eneskayiklik.yellowtaxi.ui.base.BaseFragment
import com.eneskayiklik.yellowtaxi.util.drawMarker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MapFragment : BaseFragment<FragmentMapBinding>(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private val viewModel by viewModels<MapViewModel>()
    private val navArgs by navArgs<MapFragmentArgs>()
    override val layoutId: Int
        get() = R.layout.fragment_map

    override fun initialize() {
        setupMap()
        startCollect()
    }

    private fun startCollect() {
        lifecycleScope.launchWhenCreated {
            viewModel.directionData.collectLatest {
                it?.let { data ->
                    val options = PolylineOptions().width(5F).color(Color.BLUE).geodesic(true)
                    val pointList = PolyUtil.decode(data.routes.first().overview_polyline.points)
                    pointList.forEach { poly ->
                        options.add(poly)
                    }
                    mMap.addPolyline(options)
                }
            }
        }
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val start = navArgs.start
        val end = navArgs.end
        if (start != null && end != null) {
            val startLocation = LatLng(start.longitude.toDouble(), start.latitude.toDouble())
            val endLocation = LatLng(end.longitude.toDouble(), end.latitude.toDouble())
            // Add Marker and move camera to that position
            mMap.addMarker(
                MarkerOptions().position(startLocation).title(start.zoneName)
                    .icon(BitmapDescriptorFactory.fromBitmap(drawMarker(requireContext(), "S")))
            )
            mMap.addMarker(
                MarkerOptions().position(endLocation).title(end.zoneName)
                    .icon(BitmapDescriptorFactory.fromBitmap(drawMarker(requireContext(), "E")))
            )
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(startLocation, 15F))

            // Draw Line
            viewModel.getMapData(
                "${startLocation.latitude},${startLocation.longitude}",
                "${endLocation.latitude},${endLocation.longitude}",
                requireContext()
            )
        }
    }
}