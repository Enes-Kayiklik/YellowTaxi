package com.eneskayiklik.yellowtaxi.ui.main.list

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.eneskayiklik.yellowtaxi.R
import com.eneskayiklik.yellowtaxi.databinding.FragmentDayBinding
import com.eneskayiklik.yellowtaxi.ui.base.BaseFragment
import com.eneskayiklik.yellowtaxi.ui.main.MainViewModel
import com.eneskayiklik.yellowtaxi.util.extensions.maxByIndexOf
import com.google.android.material.transition.MaterialContainerTransform
import kotlinx.coroutines.flow.collectLatest

class DayFragment: BaseFragment<FragmentDayBinding>() {
    private val mainViewModel by activityViewModels<MainViewModel>()
    private var longestTripIndex: Int? = null
    private val tripAdapter by lazy {
        TripSingleAdapter { start, end ->
            if (start != null && end != null) {
                val action = DayFragmentDirections.actionDayFragmentToMapFragment(start, end)
                findNavController().navigate(action)
            }
        }
    }
    override val layoutId: Int
        get() = R.layout.fragment_day

    override fun initialize() {
        binder.root.transitionName = arguments?.getString("transition_name")
        collectTripList()
        setupButtonsOnClick()
    }

    private fun setupButtonsOnClick() {
        binder.btnBack.setOnClickListener { requireActivity().onBackPressed() }
        binder.btnOpenMap.setOnClickListener {
            longestTripIndex?.let { index ->
                Log.e("Longest", "$index")
                val trip = tripAdapter.currentList[index]
                val puLocationData = tripAdapter.zoneNameData.getOrNull(trip.puLocationId - 1)
                val doLocationData = tripAdapter.zoneNameData.getOrNull(trip.doLocationId - 1)
                val action = DayFragmentDirections.actionDayFragmentToMapFragment(puLocationData, doLocationData)
                findNavController().navigate(action)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.fragmentNavHost
            duration = 300L
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(Color.WHITE)
        }
    }

    private fun collectTripList() {
        lifecycleScope.launchWhenCreated {
            mainViewModel.zoneDataFlow.collectLatest {
                tripAdapter.zoneNameData = it
                tripAdapter.notifyDataSetChanged()
            }
        }
        lifecycleScope.launchWhenCreated {
            mainViewModel.selectedDay.collectLatest {
                binder.apply {
                    recyclerViewAllData.adapter = tripAdapter
                    tripAdapter.submitList(it)
                    tripAdapter.notifyDataSetChanged()
                    longestTripIndex = it.maxByIndexOf { d -> d.tripDistance }
                }
            }
        }
    }
}