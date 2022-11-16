package com.group.daily_forecast.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.group.daily_forecast.databinding.ItemResultBinding
import com.group.daily_forecast.pojo.response.DailyForecastData
import com.group.daily_forecast.utils.Utils.convertFromKelvinToCelsius
import com.group.daily_forecast.utils.Utils.format2Digits
import javax.inject.Inject


class ResultAdapter @Inject constructor() :
    ListAdapter<DailyForecastData, ResultAdapter.ViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, Type: Int): ViewHolder =
        ViewHolder(
            ItemResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(val binding: ItemResultBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(data: DailyForecastData) {

            binding.apply {

                tvTemp.text = data.main?.temp?.convertFromKelvinToCelsius().format2Digits() + " ℃"
                tvHumidity.text = "${data.main?.humidity} %"
                tvWindSpeed.text = "${data.wind?.speed} k/hr"

                if (!data.weather.isNullOrEmpty())
                    tvDescription.text = data.weather.first().description

            }

            binding.root.setOnClickListener {
                onItemClickListener?.let { click ->
                    click(data)
                }
            }

            binding.executePendingBindings()
        }

    }

    class DiffCallback : DiffUtil.ItemCallback<DailyForecastData>() {
        override fun areItemsTheSame(
            oldItem: DailyForecastData, newItem: DailyForecastData
        ): Boolean = newItem == oldItem

        override fun areContentsTheSame(
            oldItem: DailyForecastData, newItem: DailyForecastData
        ): Boolean = newItem == oldItem
    }

    private var onItemClickListener: ((DailyForecastData) -> Unit)? = null

    fun setOnItemClickListener(listener: (DailyForecastData) -> Unit) {
        onItemClickListener = listener
    }

    fun clear() {
        submitList(emptyList())
    }
}