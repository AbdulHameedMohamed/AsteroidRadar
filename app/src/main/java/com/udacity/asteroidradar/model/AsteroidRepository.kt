package com.udacity.asteroidradar.model

import android.util.Log
import androidx.lifecycle.LiveData
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.network.NasaServer
import com.udacity.asteroidradar.utils.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidRepository(private val database: AsteroidDatabase) {

    val asteroidList: LiveData<List<Asteroid>>
        get() = database.asteroidDao().getAsteroids()

    val todayAsteroidList: LiveData<List<Asteroid>>
        get() = database.asteroidDao().getAsteroidToday(Date.getCurrentDate())

    val pictureOfDay: LiveData<PictureOfDay>
        get() = database.pictureDao().get()

    suspend fun getAsteroidList() {
        withContext(Dispatchers.IO) {
            try {
                val asteroid = NasaServer.nasaApi.getAsteroids()
                val data = parseAsteroidsJsonResult(JSONObject(asteroid))
                database.asteroidDao().updateData(data)
                Log.e("AsteroidRepository", "Success and Size: " + data.size)
            } catch (e: Exception) {
                Log.e("AsteroidRepository", e.message ?: "Error Occurred In Network")
            }
        }
    }

    suspend fun getNasaPictureOfDay() {
        withContext(Dispatchers.IO) {
            try {
                val response = NasaServer.nasaApi.getPictureOfDay()
                val picture = Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
                    .adapter(PictureOfDay::class.java)
                    .fromJson(response) ?: PictureOfDay(-1, "image", "", "")
                Log.e("AsteroidRepository", picture.title ?: "No title")
                database.pictureDao().updateData(picture)
            } catch (e: Exception) {
                Log.e("AsteroidRepository", e.message ?: "Image Not Found")
            }
        }
    }
}