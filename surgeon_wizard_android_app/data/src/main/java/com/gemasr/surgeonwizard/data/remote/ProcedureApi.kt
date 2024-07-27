package com.gemasr.surgeonwizard.data.remote

import com.gemasr.surgeonwizard.data.remote.model.ProcedureDetailApiModel
import com.gemasr.surgeonwizard.data.remote.model.ProcedureListItemApiModel
import retrofit2.http.GET
import retrofit2.http.Path

interface ProcedureApi {

    @GET("procedures")
    suspend fun getProcedures(): List<ProcedureListItemApiModel>

    @GET("procedures/{uuid}")
    suspend fun getProcedureDetail(@Path("uuid") uuid: String): ProcedureDetailApiModel
}