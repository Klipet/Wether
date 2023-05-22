package com.example.wether.fragment

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wether.Const.Companion.API_KEY
import com.example.wether.MainViewModel
import com.example.wether.adapters.VpAdapter
import com.example.wether.adapters.WeatherMode
import com.example.wether.databinding.FragmentMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import org.json.JSONObject


class MainFragment : Fragment() {
    private lateinit var pLauncher: ActivityResultLauncher<String>
    private lateinit var binding: FragmentMainBinding
    private val model: MainViewModel by activityViewModels()
    private val fList = listOf(
        HoursFragment.newInstance(),
        DaysFragment.newInstance()
    )
    private val tList = listOf(
        "Hours",
        "Days"
    )


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater,container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermision()
        init()
        reqestWeatherData("London")
    }
    private fun init() = with(binding){
        val adapter = VpAdapter(activity as FragmentActivity, fList)
        vp.adapter = adapter
        TabLayoutMediator(tabLayout, vp){
            tab, pos -> tab.text = tList[pos]

        }.attach()

    }

    private fun permisionLostener(){
        pLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()){
            Toast.makeText(activity, "Permision is $it", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPermision(){
        if (!isPermissionGrande(Manifest.permission.ACCESS_FINE_LOCATION)){
            permisionLostener()
            pLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun reqestWeatherData(sity: String){
       // dcc638b9622d4a668c4133433231805
        val url = "https://api.weatherapi.com/v1/forecast.json?key="+
                API_KEY +
                "&q="+
                sity +
                "&days=5" +
                "&aqi=no" +
                "&alerts=no"
        val queue = Volley.newRequestQueue(context)
        val reqest = StringRequest(
            Request.Method.GET,
            url,
            {
                result -> parseWhiterData(result)
            },
            {
                error -> Log.d("MyLog", "error: $error")
            }
        )
        queue.add(reqest)
    }

    private fun parseWhiterData(result: String){
        val mainObject = JSONObject(result)
        val list = parsDays(mainObject)
        parsCurrentData(mainObject, list[0])

    }
    private fun parsDays(mainObject: JSONObject): List<WeatherMode>{
        val list = ArrayList<WeatherMode>()
        val daysArry = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
        val name =   mainObject.getJSONObject("location").getString("name")
        for (i in 0 until daysArry.length()){
            val day = daysArry[i] as JSONObject
            val item = WeatherMode(
                city =  name,
                time = day.getString("date"),
                condition = day.getJSONObject("day").getJSONObject("condition").getString("text"),
                currentTemp = "",
                maxTemp = day.getJSONObject("day").getString("maxtemp_c"),
                minTemp = day.getJSONObject("day").getString("mintemp_c"),
                imageUrl = day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                hours = day.getJSONArray("hour").toString()
            )
            list.add(item)
        }
        return list

    }
    private fun parsCurrentData(mainObject: JSONObject, weatherItem: WeatherMode){
        val item = WeatherMode(
            city =  mainObject.getJSONObject("location").getString("name"),
            time = mainObject.getJSONObject("current").getString("last_updated"),
            condition = mainObject.getJSONObject("current").getJSONObject("condition").getString("text"),
            currentTemp =  mainObject.getJSONObject("current").getString("temp_c"),
            maxTemp = weatherItem.maxTemp,
            minTemp = weatherItem.minTemp,
            imageUrl = mainObject.getJSONObject("current").getJSONObject("condition").getString("icon"),
            hours = weatherItem.hours
        )
        Log.d("MyLog","city: ${item.hours}")
        Log.d("MyLog","Lastupdate: ${item.maxTemp}")
        Log.d("MyLog","Lastupdate: ${item.minTemp}")

    }
    private fun updateCurrentCard(){
        model
    }


    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()


    }
}