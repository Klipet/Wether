package com.example.wether

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.wether.adapters.WeatherMode

class MainViewModel: ViewModel() {
    val liveDataCurrent = MutableLiveData<WeatherMode>()
    val liveDataList = MutableLiveData<List<WeatherMode>>()

}