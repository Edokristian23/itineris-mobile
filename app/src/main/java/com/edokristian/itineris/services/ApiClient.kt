package com.edokristian.itineris.services

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {
        companion object {
            fun getRetroClientInstance(): Retrofit{

                val gson = GsonBuilder().setLenient().create()

                return Retrofit.Builder()
                    .baseUrl("https://itineris-production.up.railway.app/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
        }
}

