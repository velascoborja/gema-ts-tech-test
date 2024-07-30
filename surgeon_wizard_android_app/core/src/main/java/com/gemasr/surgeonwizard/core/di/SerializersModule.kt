package com.gemasr.surgeonwizard.core.di

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SerializersModule {
    @Provides
    @Singleton
    fun providesGson(): Gson {
        return Gson()
    }
}