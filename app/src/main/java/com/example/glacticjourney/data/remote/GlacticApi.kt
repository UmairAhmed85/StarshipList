package com.example.glacticjourney.data.remote

import com.example.glacticjourney.data.dto.GlaticSpaceshipsDto
import com.example.glacticjourney.data.dto.PilotDto
import retrofit2.http.GET
import retrofit2.http.Path

interface GlacticApi {

    @GET("starships")
    suspend fun getStarshipDetails(): GlaticSpaceshipsDto

    @GET("{endpoint}")
    suspend fun getPilotName(
        @Path("endpoint") endpoint: String
    ): PilotDto

    companion object {
        const val BASE_URL = "https://swapi.dev/api/"
    }
}