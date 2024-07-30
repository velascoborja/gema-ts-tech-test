package com.gemasr.surgeonwizard.data.remote.client

import javax.inject.Inject
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitClientProvider
@Inject
constructor(
    private val okHttpClient: OkHttpClient,
    private val baseUrl: String,
) {
    fun provideRetrofitClient(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .baseUrl(baseUrl)
            .build()
    }
}