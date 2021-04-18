package com.eneskayiklik.yellowtaxi.ui.main.list.filter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.transition.TransitionManager
import android.util.Log
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResult
import androidx.lifecycle.lifecycleScope
import com.eneskayiklik.yellowtaxi.R
import com.eneskayiklik.yellowtaxi.databinding.FilterDialogBinding
import com.eneskayiklik.yellowtaxi.ui.base.BaseDialogFragment
import com.eneskayiklik.yellowtaxi.ui.main.MainViewModel
import com.eneskayiklik.yellowtaxi.util.enums.FilterType
import com.eneskayiklik.yellowtaxi.util.extensions.hide
import com.eneskayiklik.yellowtaxi.util.extensions.show
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.withContext

class FilterDialog: BaseDialogFragment<FilterDialogBinding>() {
    private val mainViewModel by activityViewModels<MainViewModel>()
    override val layoutId: Int
        get() = R.layout.filter_dialog

    override fun initialize() {
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setupWidget()
        setupButtonsOnClick()
        setupSeekBar()
    }

    private fun setupSeekBar() {
        lifecycleScope.launchWhenCreated {
            mainViewModel.minMaxDistance.collectLatest {
                withContext(Dispatchers.Main) {
                    if (it.size == 2) {
                        binder.distanceSeekBar.max = it[1].toInt()
                    }
                }
            }
        }
    }

    private fun setupButtonsOnClick() {
        binder.apply {
            btnDismiss.setOnClickListener { dismiss().also { mainViewModel.clearFilter() } }
            btnSave.setOnClickListener {
                when (radioGroupTip1.checkedRadioButtonId) {
                    R.id.sortByDistance -> mainViewModel.sortData(FilterType.PASSENGER_SIZE).also { dismiss() }
                    R.id.sortByAmount -> mainViewModel.sortData(FilterType.AVG_AMOUNT).also { dismiss() }
                    R.id.sortByMaxDistance -> mainViewModel.sortData(FilterType.MAX_BY_DISTANCE).also { dismiss() }
                    R.id.sortBySelectedMaxDistance -> mainViewModel.sortData(FilterType.MAX_BY_SELECTED_DISTANCE, distanceSeekBar.progress.toDouble()).also { dismiss() }
                }
            }
            btnType1.setOnClickListener { setupOptionsList(1) }
            btnType2.setOnClickListener { setupOptionsList(2) }
        }
    }

    private fun setupOptionsList(type: Int) {
        TransitionManager.beginDelayedTransition(binder.root as ViewGroup)
        when (type) {
            1 -> {
                binder.filterButtonList.hide()
                binder.filterOptionList.show()
                binder.sortByAmount.hide()
                binder.sortByAmountDesc.hide()
                binder.sortByMinDistance.hide()
                binder.sortByMinDistanceDesc.hide()
                binder.sortByLocation.hide()
                binder.sortByLocationDesc.hide()
            }
            2 -> {
                binder.filterButtonList.hide()
                binder.filterOptionList.show()
                binder.sortByDistance.hide()
                binder.sortByMaxDistance.hide()
                binder.sortByMaxDistanceDesc.hide()
                binder.sortByDistanceDesc.hide()
                binder.sortBySelectedMaxDistance.hide()
                binder.sortBySelectedMaxDistanceDesc.hide()
            }
        }
    }

    private fun setupWidget() {
        lifecycleScope.launchWhenResumed {
            mainViewModel.selectedFilter.collectLatest {
                when (it) {
                    FilterType.PASSENGER_SIZE -> binder.sortByDistance.isChecked = true
                    FilterType.AVG_AMOUNT -> binder.sortByAmount.isChecked = true
                    FilterType.MAX_BY_DISTANCE -> binder.sortByMaxDistance.isChecked = true
                    FilterType.MAX_BY_SELECTED_DISTANCE -> binder.sortBySelectedMaxDistance.isChecked = true
                }
            }
        }
        binder.apply {
            radioGroupTip1.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.sortByMinDistance -> mainViewModel.showDatePicker(true).also { dismiss() }
                    R.id.sortBySelectedMaxDistance -> {
                        TransitionManager.beginDelayedTransition(root as ViewGroup)
                        seekBarContainer.show()
                    }
                    else -> {
                        TransitionManager.beginDelayedTransition(root as ViewGroup)
                        seekBarContainer.hide()
                    }
                }
            }
            distanceSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    seekBarCurrentValue.text = progress.toString().plus(" km")
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }
}