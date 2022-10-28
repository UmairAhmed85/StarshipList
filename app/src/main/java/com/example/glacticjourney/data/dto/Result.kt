package com.example.glacticjourney.data.dto

data class Result(
    val cargo_capacity: String,
    val consumables: String,
    val model: String,
    val name: String,
    var passengers: String,
    var pilots: MutableList<String> = mutableListOf(),
)