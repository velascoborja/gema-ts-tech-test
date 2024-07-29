package com.gemasr.surgeonwizard.procedures.list

import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureItem

sealed interface ProcedureListViewState {
    data object Loading : ProcedureListViewState

    data class ProceduresLoaded(val procedures: List<ProcedureItem> = emptyList()) :
        ProcedureListViewState

    data class Error(val error: ProcedureListError) : ProcedureListViewState
}

sealed interface ProcedureListError {
    data object UnidentifiedError : ProcedureListError

    data object NoInternetError : ProcedureListError

    data object EmptyList : ProcedureListError
}

sealed interface ProcedureListEvent {
    data object None : ProcedureListEvent

    data class ShowDetail(val procedureId: String) : ProcedureListEvent

    data class ShowFavoriteMessage(val added: Boolean) : ProcedureListEvent

    data object ErrorAssigningFavorite : ProcedureListEvent
}