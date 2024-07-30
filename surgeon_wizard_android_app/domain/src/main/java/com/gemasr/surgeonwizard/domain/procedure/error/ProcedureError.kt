package com.gemasr.surgeonwizard.domain.procedure.error

sealed class ProcedureError : Throwable() {
    data object UnidentifiedError : ProcedureError()

    data object NoInternetError : ProcedureError()

    data object NotFound : ProcedureError()

    data object EmptyList : ProcedureError()
}