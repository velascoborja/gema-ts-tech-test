package com.gemasr.surgeonwizard.procedures.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gemasr.surgeonwizard.core.di.IODispatcher
import com.gemasr.surgeonwizard.domain.procedure.IProcedureRepository
import com.gemasr.surgeonwizard.domain.procedure.error.ProcedureError
import com.gemasr.surgeonwizard.procedures.detail.ui.model.ProcedureDetailUI
import com.gemasr.surgeonwizard.procedures.detail.ui.model.toUI
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProcedureDetailViewModel
@Inject
constructor(
    private val procedureRepository: IProcedureRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _viewState = MutableStateFlow<ProcedureDetailViewState>(ProcedureDetailViewState.Loading)
    val viewState: StateFlow<ProcedureDetailViewState> = _viewState.asStateFlow()

    private val _viewEvent: MutableSharedFlow<ProcedureDetailEvent> = MutableSharedFlow()
    val viewEvent: Flow<ProcedureDetailEvent>
        get() = _viewEvent

    private val currentState
        get() = _viewState.value

    fun loadProcedureDetail(procedureId: String) {
        viewModelScope.launch(ioDispatcher) {
            procedureRepository.getProcedureDetail(procedureId)
                .onSuccess { procedureDetail ->
                    updateState {
                        ProcedureDetailViewState.ProcedureDetailLoaded(procedureDetail.toUI())
                    }
                }
                .onFailure { error ->
                    updateState {
                        ProcedureDetailViewState.Error(error.mapToUIError())
                    }
                }
        }
    }

    fun toggleFavorite() {
        viewModelScope.launch(ioDispatcher) {
            val procedure = (currentState as? ProcedureDetailViewState.ProcedureDetailLoaded)?.procedure
            procedure?.let { currentProcedure ->
                procedureRepository.setFavorite(currentProcedure.id, !currentProcedure.isFavorite)
                    .onSuccess { updateFavoriteInUI(currentProcedure) }
                    .onFailure { _viewEvent.emit(ProcedureDetailEvent.ErrorAssigningFavorite) }
            }
        }
    }

    private fun updateFavoriteInUI(currentProcedure: ProcedureDetailUI) {
        updateState {
            ProcedureDetailViewState.ProcedureDetailLoaded(
                currentProcedure.copy(isFavorite = !currentProcedure.isFavorite),
            )
        }
    }

    private fun updateState(update: (ProcedureDetailViewState) -> ProcedureDetailViewState): ProcedureDetailViewState {
        _viewState.update(update)
        return currentState
    }

    private fun Throwable.mapToUIError(): ProcedureDetailError {
        return when (this) {
            ProcedureError.NotFound -> ProcedureDetailError.NotFound
            else -> ProcedureDetailError.UnidentifiedError
        }
    }
}