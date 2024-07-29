package com.gemasr.surgeonwizard.data.remote

import com.gemasr.surgeonwizard.data.remote.model.ProcedureDetailApiModel
import com.gemasr.surgeonwizard.data.remote.model.ProcedureListItemApiModel
import retrofit2.http.GET
import retrofit2.http.Path

interface ProcedureApi {
    @GET("api/v3/procedures")
    suspend fun getProcedures(): List<ProcedureListItemApiModel>

    @GET("api/v3/procedures/{uuid}")
    suspend fun getProcedureDetail(@Path("uuid") uuid: String): ProcedureDetailApiModel
}