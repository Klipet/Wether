package com.example.wether.adapters

import android.os.Parcel
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wether.R

import com.example.wether.databinding.ListItemBinding

class WeatherAdapter() : ListAdapter<WeatherMode, WeatherAdapter.Holder>(Comporator()) {
    class Holder(view: View): RecyclerView.ViewHolder(view){
        val binding = ListItemBinding.bind(view)

        fun bind(item: WeatherMode) = with(binding){
            tvDate.text = item.time
            tvCondtion.text= item.condition
            tvTemp.text= item.currentTemp

        }
    }
// проверка элементов
    class Comporator: DiffUtil.ItemCallback<WeatherMode>(){
        override fun areItemsTheSame(oldItem: WeatherMode, newItem: WeatherMode): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherMode, newItem: WeatherMode): Boolean {
            return oldItem == newItem
        }

    }
// ресуим  элименты забирая все из списка элиментов bind
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
       holder.bind(getItem(position))
    }
}