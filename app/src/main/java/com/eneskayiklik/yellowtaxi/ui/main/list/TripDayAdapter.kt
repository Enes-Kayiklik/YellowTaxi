package com.eneskayiklik.yellowtaxi.ui.main.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.eneskayiklik.yellowtaxi.databinding.OneRowTripDayDataBinding
import com.eneskayiklik.yellowtaxi.network.model.TripDayData

class TripDayAdapter(
    private val onClick: (Int, View) -> Unit
): ListAdapter<TripDayData, TripDayAdapter.CustomViewHolder>(CustomDiff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        return CustomViewHolder(OneRowTripDayDataBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    inner class CustomViewHolder(val binding: OneRowTripDayDataBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: TripDayData) {
            binding.apply {
                tvPassengerCount.text = data.passengerCount.toString()
                tvTotalDistance.text = "%.2f km".format(data.totalDistance)
                tvTripCount.text = data.tripCount.toString()
                tvTripDate.text = "${data.day}/12/2020"
                tvAmount.text = "%.2f".format(data.avgAmount)
                root.setOnClickListener {
                    onClick(data.day - 1, root)
                }
                root.transitionName = data.day.toString()
            }
        }
    }

    class CustomDiff: DiffUtil.ItemCallback<TripDayData>() {
        override fun areItemsTheSame(oldItem: TripDayData, newItem: TripDayData): Boolean {
            return oldItem.day == newItem.day
        }

        override fun areContentsTheSame(oldItem: TripDayData, newItem: TripDayData): Boolean {
            return oldItem.day == newItem.day
        }
    }
}