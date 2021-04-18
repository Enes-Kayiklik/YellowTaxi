package com.eneskayiklik.yellowtaxi.ui.main.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eneskayiklik.yellowtaxi.databinding.OneRowTripAllDataBinding
import com.eneskayiklik.yellowtaxi.databinding.OneRowTripDayDataBinding
import com.eneskayiklik.yellowtaxi.network.model.TripDataModel
import com.eneskayiklik.yellowtaxi.network.model.TripDayData
import com.eneskayiklik.yellowtaxi.network.model.ZoneDataModel
import com.eneskayiklik.yellowtaxi.util.extensions.getDayFromDate

class TripSingleAdapter(
    private val onClick: (ZoneDataModel?, ZoneDataModel?) -> Unit
): ListAdapter<TripDataModel, TripSingleAdapter.CustomViewHolder>(CustomDiff()) {
    var zoneNameData: List<ZoneDataModel> = listOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(OneRowTripAllDataBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class CustomViewHolder(private val binding: OneRowTripAllDataBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TripDataModel) {
            binding.apply {
                tvPassengerCount.text = data.passengerCount.toString()
                tvTotalDistance.text = "%.2f km".format(data.tripDistance)
                tvTripDate.text = "${data.pickupDate.getDayFromDate() + 1}/12/2020"
                tvAmount.text = "%.2f".format(data.totalAmount)
                if (zoneNameData.isNotEmpty()) {
                    val puLocationData = zoneNameData.getOrNull(data.puLocationId - 1)
                    val doLocationData = zoneNameData.getOrNull(data.doLocationId - 1)
                    puLocation.text = puLocationData?.zoneName ?: "?????"
                    doLocation.text = doLocationData?.zoneName ?: "?????"
                    root.setOnClickListener {
                        onClick(puLocationData, doLocationData)
                    }
                }
            }
        }
    }

    class CustomDiff: DiffUtil.ItemCallback<TripDataModel>() {
        override fun areItemsTheSame(oldItem: TripDataModel, newItem: TripDataModel): Boolean {
            return oldItem.doLocationId == newItem.doLocationId
        }

        override fun areContentsTheSame(oldItem: TripDataModel, newItem: TripDataModel): Boolean {
            return oldItem.doLocationId == newItem.doLocationId
        }
    }
}