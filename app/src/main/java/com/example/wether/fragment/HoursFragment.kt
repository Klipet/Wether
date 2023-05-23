package com.example.wether.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wether.MainViewModel
import com.example.wether.R
import com.example.wether.adapters.WeatherAdapter
import com.example.wether.adapters.WeatherMode
import com.example.wether.databinding.FragmentHoursBinding
import org.json.JSONArray
import org.json.JSONObject

class HoursFragment : Fragment() {
    private lateinit var binding: FragmentHoursBinding
    private lateinit var adapter: WeatherAdapter
    private val model: MainViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        model.liveDataCurrent.observe(viewLifecycleOwner){
            adapter.submitList(getHouersList(it))

        }
    }

    private fun initRcView() = with(binding){
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter(null)
        rcView.adapter = adapter

    }

    private fun getHouersList(wItem: WeatherMode): List<WeatherMode>{
        val houersArray = JSONArray(wItem.hours)
        val list = ArrayList<WeatherMode>()
        for (i in 0 until houersArray.length()){
            val item = WeatherMode(
                city = wItem.city,
                time = (houersArray[i] as JSONObject).getString("time"),
                condition = (houersArray[i] as JSONObject).getJSONObject("condition").getString("text"),
                currentTemp = (houersArray[i] as JSONObject).getString("temp_c"),
                maxTemp = "",
                minTemp = "",
                imageUrl = (houersArray[i] as JSONObject).getJSONObject("condition").getString("icon"),
                hours = ""
            )
            list.add(item)
        }
        return list
    }

    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}