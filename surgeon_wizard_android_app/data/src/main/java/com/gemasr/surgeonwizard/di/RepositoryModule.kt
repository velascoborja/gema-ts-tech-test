package com.gemasr.surgeonwizard.di

import com.gemasr.surgeonwizard.ProcedureRepository
import com.gemasr.surgeonwizard.domain.procedure.IProcedureRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun providesRepository(procedureRepository: ProcedureRepository): IProcedureRepository
}