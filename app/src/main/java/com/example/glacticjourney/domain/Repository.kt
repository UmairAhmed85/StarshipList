package com.example.glacticjourney.domain

import android.util.Log
import com.example.glacticjourney.data.dto.PilotDto
import com.example.glacticjourney.data.dto.Result
import com.example.glacticjourney.data.remote.GlacticApi
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Repository {
    private val TAG = "Repository"
    companion object {

       val okHttpClient =  OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.MINUTES)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                .method(original.method, original.body)
//                .addHeader("Authorization", "Bearer " + Utilities.jwtToken)
            val request = requestBuilder.build()
            chain.proceed(request)
        }.build()

        val api = Retrofit
            .Builder()
            .baseUrl(GlacticApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(GlacticApi::class.java)

    }

    suspend fun returnSpaceships(resultCallback: (list: List<Result>) -> Unit) {
        try {
            CoroutineScope(Dispatchers.IO).launch {
                Log.d(TAG, "Fetching data ...")
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
                                }
                            }
                            pilotResponse.await()
                        }
                    }
                }
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

    suspend fun getDataInBackgroundUsingWorkRequest() {
        val randomNumber = (Math.random() * 10).toInt()
        val result = returnSpaceships { _ ->
            // Save in DB or whatever needed here
            // Show notification
        }
       }

}