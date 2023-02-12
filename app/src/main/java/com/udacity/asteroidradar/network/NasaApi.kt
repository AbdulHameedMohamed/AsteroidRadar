package com.udacity.asteroidradar.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.udacity.asteroidradar.BuildConfig
import com.udacity.asteroidradar.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private fun apiKeyInterceptor(it: Interceptor.Chain): Response {
    val originalRequest = it.request()

    val newHttpUrl = originalRequest.url.newBuilder()
        .addQueryParameter("api_key", BuildConfig.API_KEY)
        .build()

    val newRequest = originalRequest.newBuilder()
        .url(newHttpUrl)
        .build()

    return it.proceed(newRequest)
}

val okHttpClient: OkHttpClient = OkHttpClient.Builder()
    .addInterceptor { chain-> apiKeyInterceptor(chain) }
    .build()

private val retrofit = Retrofit.Builder()
    .client(okHttpClient)
    .addConverterFactory(ScalarsConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(Constants.BASE_URL)
    .build()

object NasaServer {
    val nasaApi: NasaApi by lazy { retrofit.create(NasaApi::class.java) }
}

interface NasaApi {

    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(): String

    @GET("planetary/apod")
    suspend fun getPictureOfDay(): String

}