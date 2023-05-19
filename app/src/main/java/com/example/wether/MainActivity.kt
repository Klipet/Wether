package com.example.wether

import android.app.DownloadManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.wether.databinding.ActivityMainBinding
import com.example.wether.fragment.MainFragment
import org.json.JSONObject

const val API_KEY = "dcc638b9622d4a668c4133433231805"
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        setContentView(binding.root)
           supportFragmentManager
               .beginTransaction().replace(R.id.placeholder, MainFragment.newInstance()).commit()


    }

}