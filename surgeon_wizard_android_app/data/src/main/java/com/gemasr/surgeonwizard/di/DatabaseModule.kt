package com.gemasr.surgeonwizard.di

import android.content.Context
import androidx.room.Room
import com.gemasr.surgeonwizard.data.local.database.ProcedureDao
import com.gemasr.surgeonwizard.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideProcedureDao(appDatabase: AppDatabase): ProcedureDao {
        return appDatabase.procedureDao()
    }
}