package com.example.glacticjourney

import android.app.Application
import androidx.work.*
import com.example.glacticjourney.domain.Repository
import com.example.glacticjourney.worker.GlacticWorker
import java.util.concurrent.TimeUnit

class GlacticApplication : Application() {
    lateinit var repository: Repository
    override fun onCreate() {
        super.onCreate()
        initialize()
        setupWorker()
    }

    private fun setupWorker() {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.METERED).build()
        val periodicWorkerRequest = PeriodicWorkRequest.Builder(GlacticWorker::class.java, 2, TimeUnit.MINUTES)
            .setConstraints(constraints)
            .build()
        WorkManager.getInstance(this).enqueue(periodicWorkerRequest)
    }

    private fun initialize() {
        repository = Repository()
    }

}