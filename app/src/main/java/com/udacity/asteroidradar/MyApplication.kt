package com.udacity.asteroidradar

import android.app.Application
import androidx.work.*
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.model.AsteroidRepository
import com.udacity.asteroidradar.utils.Constants
import com.udacity.asteroidradar.work.AsteroidWorkManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
/**
 * Override application to setup background work via WorkManager
 */
class MyApplication : Application() {

    private val applicationScope = CoroutineScope(Dispatchers.Default)
    private lateinit var database: AsteroidDatabase
    lateinit var myRepository: AsteroidRepository

    override fun onCreate() {
        super.onCreate()

        delayedInit()

        database = AsteroidDatabase.getInstance(this)
        myRepository = AsteroidRepository(database)
    }

    private fun delayedInit() {
        applicationScope.launch {
            setupRecurringWork()
        }
    }

    private fun setupRecurringWork() {
        val constraints = Constraints.Builder()
            .setRequiresBatteryNotLow(true)
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.UNMETERED)
            .setRequiresDeviceIdle(true)
            .build()

        val repeatingRequest = PeriodicWorkRequestBuilder<AsteroidWorkManager>(Constants.REPEAT_INTERVAL, TimeUnit.DAYS)
            .setConstraints(constraints)
            .build()

        WorkManager.getInstance()
            .enqueueUniquePeriodicWork(
            AsteroidWorkManager.WORK_MANAGER_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            repeatingRequest
        )
    }
}