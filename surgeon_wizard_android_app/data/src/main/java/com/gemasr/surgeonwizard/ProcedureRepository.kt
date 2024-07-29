package com.gemasr.surgeonwizard

import com.gemasr.surgeonwizard.data.ErrorMappers.toDomainError
import com.gemasr.surgeonwizard.data.ProcedureMappers.toProcedureDetail
import com.gemasr.surgeonwizard.data.ProcedureMappers.toProcedureDetailEntity
import com.gemasr.surgeonwizard.data.ProcedureMappers.toProcedureEntity
import com.gemasr.surgeonwizard.data.ProcedureMappers.toProcedureItems
import com.gemasr.surgeonwizard.data.local.ProcedureLocalDataSource
import com.gemasr.surgeonwizard.data.remote.ProcedureRemoteDataSource
import com.gemasr.surgeonwizard.domain.procedure.IProcedureRepository
import com.gemasr.surgeonwizard.domain.procedure.error.ProcedureError
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureDetail
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureItem
import com.google.gson.Gson
import javax.inject.Inject

class ProcedureRepository
@Inject
constructor(
    private val localDataSource: ProcedureLocalDataSource,
    private val remoteDataSource: ProcedureRemoteDataSource,
    private val gson: Gson,
) : IProcedureRepository {
    override suspend fun getAllProcedures(): Result<List<ProcedureItem>> {
        return try {
            val localProcedures = localDataSource.getAllProcedures()
            if (localProcedures.isNotEmpty()) {
                Result.success(localProcedures.toProcedureItems())
            } else {
                updateProcedures()
            }
        } catch (e: Exception) {
            Result.failure(e.toDomainError())
        }
    }

    override suspend fun getFavoriteProcedures(): Result<List<ProcedureItem>> {
        return try {
            val (shouldUpdate, localProcedures) = localDataSource.getFavoriteProcedures()
            when {
                localProcedures.isNotEmpty() -> Result.success(localProcedures.toProcedureItems())
                shouldUpdate -> updateFavoriteProcedures()
                else -> Result.success(emptyList())
            }
        } catch (e: Exception) {
            Result.failure(e.toDomainError())
        }
    }

    override suspend fun getProcedureDetail(id: String): Result<ProcedureDetail> {
        return try {
            val localProcedureDetail = localDataSource.getProcedureDetail(id)
            if (localProcedureDetail != null) {
                Result.success(localProcedureDetail.toProcedureDetail(gson))
            } else {
                updateProcedureDetail(id)
            }
        } catch (e: Exception) {
            Result.failure(e.toDomainError())
        }
    }

    override suspend fun setFavorite(id: String, isFavorite: Boolean): Result<Unit> {
        return try {
            localDataSource.setFavorite(id, isFavorite)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e.toDomainError())
        }
    }

    private suspend fun updateProcedures(): Result<List<ProcedureItem>> {
        val remoteProcedures = remoteDataSource.getProceduresList()
        val entities = remoteProcedures.map { it.toProcedureEntity() }
        localDataSource.insertProcedures(entities)

        val updatedProcedures = localDataSource.getAllProcedures()
        return if (updatedProcedures.isEmpty()) {
            Result.failure(ProcedureError.EmptyList)
        } else {
            Result.success(updatedProcedures.toProcedureItems())
        }
    }

    private suspend fun updateFavoriteProcedures(): Result<List<ProcedureItem>> {
        val remoteProcedures = remoteDataSource.getProceduresList()
        val entities = remoteProcedures.map { it.toProcedureEntity() }
        localDataSource.insertProcedures(entities)

        val (_, updatedProcedures) = localDataSource.getFavoriteProcedures()
        return if (updatedProcedures.isEmpty()) {
            Result.failure(ProcedureError.EmptyList)
        } else {
            Result.success(updatedProcedures.toProcedureItems())
        }
    }

    private suspend fun updateProcedureDetail(id: String): Result<ProcedureDetail> {
        val remoteProcedure = remoteDataSource.getProcedureDetail(id)
        val entity = remoteProcedure.toProcedureDetailEntity(gson)
        localDataSource.insertProcedureDetail(entity)

        val updatedProcedure = localDataSource.getProcedureDetail(id)
        return if (updatedProcedure == null) {
            Result.failure(ProcedureError.NotFound)
        } else {
            Result.success(updatedProcedure.toProcedureDetail(gson))
        }
    }
}