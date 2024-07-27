package com.gemasr.surgeonwizard.data.remote

import com.gemasr.surgeonwizard.data.remote.model.ProcedureDetailApiModel
import com.gemasr.surgeonwizard.data.remote.model.ProcedureListItemApiModel
import javax.inject.Inject

class ProcedureRemoteDataSource @Inject constructor(
    private val procedureApi: ProcedureApi
) {

    suspend fun getProceduresList(): List<ProcedureListItemApiModel> {
        return procedureApi.getProcedures()
    }

    suspend fun getProcedureDetail(id: String): ProcedureDetailApiModel {
        return procedureApi.getProcedureDetail(uuid = id)
    }
}