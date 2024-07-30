package com.gemasr.surgeonwizard.data

import com.gemasr.surgeonwizard.domain.procedure.error.ProcedureError
import java.net.UnknownHostException

object ErrorMappers {
    fun Exception.toDomainError(): ProcedureError {
        return when (this) {
            is UnknownHostException -> ProcedureError.NoInternetError
            else -> ProcedureError.UnidentifiedError
        }
    }
}