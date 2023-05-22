package com.example.wether.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wether.R
import com.example.wether.adapters.WeatherAdapter
import com.example.wether.adapters.WeatherMode
import com.example.wether.databinding.FragmentHoursBinding

class HoursFragment : Fragment() {
    private lateinit var binding: FragmentHoursBinding

    private lateinit var adapter: WeatherAdapter
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

    }

    private fun initRcView() = with(binding){
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter()
        rcView.adapter = adapter
        val list = listOf(
            WeatherMode(
                city = "",
                time = "12:00",
                condition = "Sany",
                currentTemp = "25",
                maxTemp = "",
                "","",""),
            WeatherMode(
                city = "",
                time = "13:00",
                condition = "Sany",
                currentTemp = "26",
                maxTemp = "",
            "","",""),
            WeatherMode(
                city = "",
                time = "16:00",
                condition = "Sany",
                currentTemp = "27",
                maxTemp = "",
                "","","")


        )

        adapter.submitList(list)
    }

    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}