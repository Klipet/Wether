package com.example.wether.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.wether.R
import com.example.wether.databinding.ListItemBinding
import com.squareup.picasso.Picasso

class WeatherAdapter(val listener: Listener?) : ListAdapter<WeatherMode, WeatherAdapter.Holder>(Comporator()) {
    class Holder(view: View, val listener: Listener?): RecyclerView.ViewHolder(view){
        val binding = ListItemBinding.bind(view)
        var itemTemp: WeatherMode? = null
        init {
            itemView.setOnClickListener{
                itemTemp?.let { it1 -> listener?.onclick(it1)}
            }
        }

        fun bind(item: WeatherMode) = with(binding){
            itemTemp = item
            tvDate.text = item.time
            tvCondtion.text= item.condition
            tvTemp.text= item.currentTemp.ifEmpty { "${item.maxTemp}°C /${item.minTemp}°C"}
            Picasso.get().load("https:" + item.imageUrl).into(im)

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
        return Holder(view, listener)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
       holder.bind(getItem(position))
    }

    interface Listener{
        fun onclick(item: WeatherMode)
    }
}