package com.eneskayiklik.yellowtaxi.ui.main.list

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.eneskayiklik.yellowtaxi.R
import com.eneskayiklik.yellowtaxi.databinding.FragmentListBinding
import com.eneskayiklik.yellowtaxi.ui.base.BaseFragment
import com.eneskayiklik.yellowtaxi.ui.main.MainViewModel
import com.eneskayiklik.yellowtaxi.ui.main.list.filter.FilterDialog
import com.eneskayiklik.yellowtaxi.util.extensions.hide
import com.eneskayiklik.yellowtaxi.util.extensions.show
import com.google.android.material.transition.MaterialElevationScale
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class ListFragment : BaseFragment<FragmentListBinding>() {
    private val viewModel by activityViewModels<MainViewModel>()

    private val dayAdapter by lazy {
        TripDayAdapter { it, cardView ->
            viewModel.selectDay(it)
            exitTransition = MaterialElevationScale(false).apply {
                duration = 300L
            }
            reenterTransition = MaterialElevationScale(true).apply {
                duration = 300L
            }
            val extras = FragmentNavigatorExtras(cardView to (it + 1).toString())
            findNavController().navigate(
                R.id.action_listFragment_to_dayFragment,
                bundleOf("transition_name" to (it + 1).toString()),
                null,
                extras
            )
        }
    }
    private val tripAdapter by lazy {
        TripSingleAdapter { start, end ->
            if (start != null && end != null) {
                val action = ListFragmentDirections.actionListFragmentToMapFragment(start, end)
                findNavController().navigate(action)
            }
        }
    }
    override val layoutId: Int
        get() = R.layout.fragment_list

    override fun initialize() {
        binder.recyclerViewDay.adapter = dayAdapter
        binder.recyclerViewAllData.adapter = tripAdapter
        collectData()
        setupButtonsOnClick()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
    }

    private fun setupButtonsOnClick() {
        binder.apply {
            btnFilter.setOnClickListener {
                FilterDialog().show(parentFragmentManager, "filter")
            }
        }
    }

    private fun collectData() {
        lifecycleScope.launchWhenCreated {
            viewModel.tripDataFlow.collectLatest {
                dayAdapter.apply {
                    /*it.forEach {
                        val data = mutableMapOf<String, Any>()
                        data["day"] = it.day
                        data["passengerCount"] = it.passengerCount
                        data["totalDistance"] = it.totalDistance
                        data["tripCount"] = it.tripCount
                        data["avgAmount"] = it.avgAmount
                        data["tripList"] = it.tripList
                        Firebase.firestore.collection("trip_data").document().set(data)
                    }*/
                    submitList(it)
                    notifyDataSetChanged()
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.dayDataFlow.collectLatest {
                tripAdapter.apply {
                    submitList(it)
                    notifyDataSetChanged()
                    binder.apply {
                        if (it.isEmpty()) {
                            recyclerViewAllData.hide()
                            recyclerViewDay.show()
                        } else {
                            recyclerViewAllData.show()
                            recyclerViewDay.hide()
                        }
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.zoneDataFlow.collectLatest { d ->
                /*d.forEach {
                    val data = mutableMapOf<String, Any>()
                    data["latitude"] = it.latitude
                    data["longitude"] = it.longitude
                    data["locationId"] = it.locationId
                    data["zoneName"] = it.zoneName
                    Firebase.firestore.collection("zone_data").document().set(data)
                }*/
                tripAdapter.zoneNameData = d
            }
        }
    }
}