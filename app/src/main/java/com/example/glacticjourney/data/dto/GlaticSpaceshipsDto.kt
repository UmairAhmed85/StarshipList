package com.example.glacticjourney.data.dto

data class GlaticSpaceshipsDto(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)