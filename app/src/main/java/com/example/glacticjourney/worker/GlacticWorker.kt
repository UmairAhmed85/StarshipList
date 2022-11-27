package com.example.glacticjourney.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.glacticjourney.GlacticApplication
import com.example.glacticjourney.domain.Repository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GlacticWorker(private val context: Context, private val parameters: WorkerParameters) : Worker(context, parameters) {
    private val TAG = "GlacticWorker"
    override fun doWork(): Result {
        Log.e(TAG, " Initializing worker request")
        val repository: Repository = (context as GlacticApplication).repository
        CoroutineScope(Dispatchers.IO).launch {
            repository.getDataInBackgroundUsingWorkRequest()
        }
        return Result.success() // Simply assigning success, TODO.. implement exception handling here when free
    }
}