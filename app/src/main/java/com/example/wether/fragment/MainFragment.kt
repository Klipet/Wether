package com.example.wether.fragment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.activityViewModels
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wether.Const.Companion.API_KEY
import com.example.wether.DialogMenager
import com.example.wether.MainViewModel
import com.example.wether.adapters.VpAdapter
import com.example.wether.adapters.WeatherMode
import com.example.wether.databinding.FragmentMainBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.json.JSONObject


class MainFragment : Fragment() {
    private lateinit var fLocationclient: FusedLocationProviderClient
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
        updateCurrentCard()

    }

    override fun onResume() {
        super.onResume()
        checkLocation()
    }
    private fun init() = with(binding){
        fLocationclient = LocationServices.getFusedLocationProviderClient(requireContext())
        val adapter = VpAdapter(activity as FragmentActivity, fList)
        vp.adapter = adapter
        TabLayoutMediator(tabLayout, vp){
            tab, pos -> tab.text = tList[pos]
        }.attach()
        ibSinc.setOnClickListener{
            tabLayout.selectTab(tabLayout.getTabAt(0))
            checkLocation()
        }
        ibSearch.setOnClickListener {
            DialogMenager.serchByNameDialog(requireContext(), object : DialogMenager.Listener{
                override fun onClick(name: String?) {
                    name?.let { it1 -> reqestWeatherData(it1) }
                }

            })
        }
    }
    private fun checkLocation(){
        if(isLocationEnable()){
            getLocation()
        }else{
            DialogMenager.locationSettingsDialog(requireContext(), object : DialogMenager.Listener{
                override fun onClick(name: String?) {
                    startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
                }

            })


        }
    }

    private fun isLocationEnable(): Boolean{
        val lm = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return lm.isProviderEnabled(LocationManager.GPS_PROVIDER)

    }
    private fun getLocation(){
        val ct = CancellationTokenSource()
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fLocationclient
            .getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, ct.token)
            .addOnCompleteListener{
                reqestWeatherData("${it.result.latitude},${it.result.longitude}")
            }
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
                maxTemp = day.getJSONObject("day").getString("maxtemp_c").toFloat().toInt().toString(),
                minTemp = day.getJSONObject("day").getString("mintemp_c").toFloat().toInt().toString(),
                imageUrl = day.getJSONObject("day").getJSONObject("condition").getString("icon"),
                hours = day.getJSONArray("hour").toString()
            )
            list.add(item)
        }
        model.liveDataList.value = list
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
        model.liveDataCurrent.value = item
        Log.d("MyLog","city: ${item.hours}")
        Log.d("MyLog","Lastupdate: ${item.maxTemp}")
        Log.d("MyLog","Lastupdate: ${item.minTemp}")

    }
    private fun updateCurrentCard() = with(binding){
        model.liveDataCurrent.observe(viewLifecycleOwner){
            val maxMinTemp = "${it.maxTemp}°C/${it.minTemp}°C"
            tvData.text = it.time
            tvCity.text = it.city
            tvCurrentTemp.text = it.currentTemp.ifEmpty { maxMinTemp }
            tvCondition.text = it.condition
            tvMaxMin.text = if (it.currentTemp.isEmpty()) "" else maxMinTemp
            Picasso.get().load("https:" + it.imageUrl).into(imVeather)


        }
    }

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()


    }
}