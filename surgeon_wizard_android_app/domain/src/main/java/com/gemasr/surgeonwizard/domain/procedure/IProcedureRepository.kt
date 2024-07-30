package com.gemasr.surgeonwizard.domain.procedure

import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureDetail
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureItem

interface IProcedureRepository {
    suspend fun getAllProcedures(): Result<List<ProcedureItem>>

    suspend fun getFavoriteProcedures(): Result<List<ProcedureItem>>

    suspend fun getProcedureDetail(id: String): Result<ProcedureDetail>

    suspend fun setFavorite(id: String, isFavorite: Boolean): Result<Unit>
}