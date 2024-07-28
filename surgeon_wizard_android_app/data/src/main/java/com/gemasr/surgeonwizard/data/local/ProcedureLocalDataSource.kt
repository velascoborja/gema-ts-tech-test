package com.gemasr.surgeonwizard.data.local

import com.gemasr.surgeonwizard.data.local.database.ProcedureDao
import com.gemasr.surgeonwizard.data.local.entity.ProcedureDetailEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureDetailWithFavorite
import com.gemasr.surgeonwizard.data.local.entity.ProcedureEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureFavoriteEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureWithFavorite
import javax.inject.Inject

class ProcedureLocalDataSource @Inject constructor(
    private val procedureDao: ProcedureDao,
) {

    companion object {
        private const val CACHE_TIMEOUT_MS = 24 * 60 * 60 * 1000
    }

    suspend fun getAllProcedures(): List<ProcedureWithFavorite> = if (shouldUpdateProcedures()) {
        emptyList()
    } else {
        procedureDao.getAllProceduresWithFavorites()
    }

    suspend fun getProcedureDetail(id: String): ProcedureDetailWithFavorite? = if (shouldUpdateProcedures()) {
        null
    } else {
        procedureDao.getProcedureDetailWithFavorite(id)
    }

    suspend fun setFavorite(id: String, isFavorite: Boolean) {
        procedureDao.insertOrUpdateFavorite(ProcedureFavoriteEntity(id, isFavorite))
    }

    suspend fun insertProcedures(procedures: List<ProcedureEntity>) {
        procedureDao.insertProcedures(procedures)
    }

    suspend fun insertProcedureDetail(procedureDetail: ProcedureDetailEntity) {
        procedureDao.insertProcedureDetail(procedureDetail)
    }

    private suspend fun shouldUpdateProcedures(): Boolean {
        val lastUpdated = procedureDao.getProceduresLastUpdated() ?: return true
        return System.currentTimeMillis() - lastUpdated > CACHE_TIMEOUT_MS
    }
}