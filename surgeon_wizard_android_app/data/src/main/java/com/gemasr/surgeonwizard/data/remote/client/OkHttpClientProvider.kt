package com.gemasr.surgeonwizard.data.remote.client

import android.content.Context
import com.gemasr.surgeonwizard.data.BuildConfig
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Inject

class OkHttpClientProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    fun provideSharedOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(provideHttpLoggingInterceptor())
                }
            }.build()
    }

    private fun provideHttpLoggingInterceptor()= HttpLoggingInterceptor().apply {
        setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}