package com.example.weatherapp.view.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.R
import com.example.weatherapp.repo.ForecastOneDayInfo
import com.facebook.drawee.view.SimpleDraweeView

class ForecastAdapter(private val forecastList: List<ForecastItem>) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.forecast_item, parent, false)
        return ForecastViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val currentItem = forecastList[position]
        holder.dayTemp.text = currentItem.day
        holder.nightTemp.text = currentItem.night
        holder.weatherDescription.text = currentItem.desc
        holder.weatherIcon.setImageURI(currentItem.icon)
        holder.weatherTitle.text = currentItem.title
    }

    override fun getItemCount() = forecastList.size

    inner class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayTemp: TextView = itemView.findViewById(R.id.forecast_day)
        val nightTemp: TextView = itemView.findViewById(R.id.forecast_night)
        val weatherIcon: SimpleDraweeView = itemView.findViewById(R.id.forecast_icon)
        val weatherDescription: TextView = itemView.findViewById(R.id.forecast_desc)
        val weatherTitle: TextView = itemView.findViewById(R.id.forecast_title)
    }

}
