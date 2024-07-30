package com.gemasr.surgeonwizard.procedures.detail

import com.gemasr.surgeonwizard.procedures.detail.ui.model.ProcedureDetailUI

sealed interface ProcedureDetailViewState {
    data object Loading : ProcedureDetailViewState

    data class ProcedureDetailLoaded(val procedure: ProcedureDetailUI) : ProcedureDetailViewState

    data class Error(val error: ProcedureDetailError) : ProcedureDetailViewState
}

sealed interface ProcedureDetailError {
    data object UnidentifiedError : ProcedureDetailError

    data object NotFound : ProcedureDetailError
}

sealed interface ProcedureDetailEvent {
    data object None : ProcedureDetailEvent

    data class ShowFavoriteMessage(val added: Boolean) : ProcedureDetailEvent

    data object ErrorAssigningFavorite : ProcedureDetailEvent
}