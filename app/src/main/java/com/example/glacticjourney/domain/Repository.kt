package com.example.glacticjourney.domain

import android.util.Log
import com.example.glacticjourney.data.dto.PilotDto
import com.example.glacticjourney.data.dto.Result
import com.example.glacticjourney.data.remote.GlacticApi
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repository {
    companion object {
        val api = Retrofit
            .Builder()
            .baseUrl(GlacticApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GlacticApi::class.java)

    }

    fun returnSpaceships(resultCallback: (list: List<Result>) -> Unit) {
        try {
//        val result: MutableList<Result> = glacticList.results as MutableList<Result>
            CoroutineScope(Dispatchers.IO).launch {
                val glacticList = api.getStarshipDetails()
                val result = glacticList.results
                glacticList.results.forEachIndexed { index, glaticSpaceshipsDto ->
                    val pilotMutableList: MutableList<String> = mutableListOf()
                    glaticSpaceshipsDto.pilots.forEachIndexed { i, url ->
                        if (url.isNotBlank()) {
                            val pilotEndpoint = url.replace(GlacticApi.BASE_URL, "")
                            var pilotDto: PilotDto? = null
                            val pilotResponse = async {
                                pilotDto = api.getPilotName(pilotEndpoint)
                                pilotDto?.let {
                                    glaticSpaceshipsDto.pilots[i] = it.name
                                    Log.e("Repo", "Result fetched for index$i, name received ${it.name}")
                                }
                            }
                            Log.e("Repo", "Fetching result for index$i")
                            pilotResponse.await()
                        }
                    }
                }
                Log.e("Repo", "Results collected, sending it back")
                resultCallback(result)
            }
        } catch (e: HttpException) {
            resultCallback(emptyList())
            Log.e("Repository", " Http Error ${e.message}")
        } catch (e: Exception) {
            resultCallback(emptyList())
            Log.e("Repository", " Exception ${e.message}")

        }
    }

    /*suspend fun getPilots(glacticList: GlaticSpaceshipsDto, result: (list: List<Result>) -> Unit) = flow<List<Result>> {
        val result: MutableList<Result> = glacticList.results as MutableList<Result>
        CoroutineScope(Dispatchers.IO).launch {
            result.forEachIndexed { index, glaticSpaceshipsDto ->
                val pilotMutableList: MutableList<String> = mutableListOf()
                glaticSpaceshipsDto.pilots.forEachIndexed { i, url ->
                    if (url.isNotBlank()) {
                        val pilotEndpoint = url.replace(GlacticApi.BASE_URL, "")
                        var pilotName: PilotDto? = null
                        val pilotResponse = async {
                            pilotName = api.getPilotName(pilotEndpoint)
                            pilotName?.let {
                                glaticSpaceshipsDto.pilots[i] = it.name
                                result(glacticList.results)
                            }
                            Log.e("Inside async ", "pilotName ${pilotName!!.name}")
                        }
                        Log.e("Outside Async", "pilotName ${pilotName}")
                        pilotResponse.await()
                    }
                }
            }
        }
    }*/

}