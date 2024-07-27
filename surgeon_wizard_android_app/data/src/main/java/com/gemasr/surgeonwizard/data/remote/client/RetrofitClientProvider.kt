package com.gemasr.surgeonwizard.data.remote.client

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RetrofitClientProvider @Inject constructor(
    private val okHttpClient: OkHttpClient,
    private val baseUrl: String,
) {
    fun provideRetrofitClient() : Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .build()
    }
}