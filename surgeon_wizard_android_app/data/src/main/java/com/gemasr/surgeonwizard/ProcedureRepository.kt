package com.gemasr.surgeonwizard

import com.gemasr.surgeonwizard.data.ProcedureMappers.toProcedureDetail
import com.gemasr.surgeonwizard.data.ProcedureMappers.toProcedureDetailEntity
import com.gemasr.surgeonwizard.data.ProcedureMappers.toProcedureDetailWithDefaultFavorite
import com.gemasr.surgeonwizard.data.ProcedureMappers.toProcedureEntity
import com.gemasr.surgeonwizard.data.ProcedureMappers.toProcedureItems
import com.gemasr.surgeonwizard.data.local.ProcedureLocalDataSource
import com.gemasr.surgeonwizard.data.remote.ProcedureRemoteDataSource
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureDetail
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureItem
import com.google.gson.Gson
import javax.inject.Inject

class ProcedureRepository @Inject constructor(
    private val localDataSource: ProcedureLocalDataSource,
    private val remoteDataSource: ProcedureRemoteDataSource,
    private val gson: Gson
) {

    suspend fun getAllProcedures(): Result<List<ProcedureItem>> {
        return try {
            val localProcedures = localDataSource.getAllProcedures()
            if (localProcedures.isNotEmpty()) {
                Result.success(localProcedures.toProcedureItems())
            } else {
                updateProcedures()
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun updateProcedures(): Result<List<ProcedureItem>> {
        val remoteProcedures = remoteDataSource.getProceduresList()
        val entities = remoteProcedures.map { it.toProcedureEntity() }
        localDataSource.insertProcedures(entities)

        val updatedProcedures = localDataSource.getAllProcedures()
        return Result.success(updatedProcedures.toProcedureItems())
    }

    suspend fun getProcedureDetail(id: String): Result<ProcedureDetail?> {
        return try {
            val localProcedureDetail = localDataSource.getProcedureDetail(id)
            if (localProcedureDetail != null) {
                Result.success(localProcedureDetail.toProcedureDetail(gson))
            } else {
                updateProcedureDetail(id)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private suspend fun updateProcedureDetail(id: String): Result<ProcedureDetail> {
        val remoteProcedure = remoteDataSource.getProcedureDetail(id)
        val entity = remoteProcedure.toProcedureDetailEntity(gson)
        localDataSource.insertProcedureDetail(entity)

        val updatedProcedure = localDataSource.getProcedureDetail(id)
        return Result.success(updatedProcedure?.toProcedureDetail(gson) ?:
            entity.toProcedureDetailWithDefaultFavorite(gson))
    }

    suspend fun setFavorite(id: String, isFavorite: Boolean) {
        localDataSource.setFavorite(id, isFavorite)
    }
}