package ru.khalil.ibeacon.data.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.POST
import retrofit2.http.Query


interface BeaconApiService {

    @POST("ibeacon")
    fun storeBeaconsAsync (
        @Query("beacons") beacons: String
    ): Deferred<Int>


    companion object Factory{
        private const val BASE_URL = "http://tryourself.ru/"

        operator fun invoke(): BeaconApiService {

            val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory((GsonConverterFactory.create()))
                .baseUrl(BASE_URL)
                .build()

            return retrofit.create(BeaconApiService::class.java)
        }
    }
}