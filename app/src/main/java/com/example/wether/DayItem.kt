package com.example.wether

data class DayItem(
    val city: String,
    val time: String,
    val condition: String,
    val imageUrl: String,
    val currentTime: String,
    val maxTemp: String,
    val minTemp: String,
    val hour: String
)
