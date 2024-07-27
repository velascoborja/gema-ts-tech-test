package com.gemasr.surgeonwizard.di

import com.gemasr.surgeonwizard.data.BuildConfig
import com.gemasr.surgeonwizard.data.remote.ProcedureApi
import com.gemasr.surgeonwizard.data.remote.client.OkHttpClientProvider
import com.gemasr.surgeonwizard.data.remote.client.RetrofitClientProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Provides
    fun providesBaseUrl(): String {
        return BuildConfig.BASE_URL
    }

    @Provides
    @Singleton
    fun providesOkHttpClient(
        okHttpClientProvider: OkHttpClientProvider
    ) = okHttpClientProvider.provideSharedOkHttpClient()

    @Provides
    fun providesRetrofitClientProvider(
        retrofitClientProvider: RetrofitClientProvider
    ) = retrofitClientProvider.provideRetrofitClient()

    @Provides
    fun providesProcedureApi(
        retrofit: Retrofit
    ) = retrofit.create(ProcedureApi::class.java)
}