package com.gemasr.surgeonwizard.procedures.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gemasr.surgeonwizard.core.di.IODispatcher
import com.gemasr.surgeonwizard.domain.procedure.IProcedureRepository
import com.gemasr.surgeonwizard.domain.procedure.error.ProcedureError
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureItem
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ProcedureListViewModel
@Inject
constructor(
    private val procedureRepository: IProcedureRepository,
    @IODispatcher private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : ViewModel() {

    private val _viewState = MutableStateFlow<ProcedureListViewState>(ProcedureListViewState.Loading)
    val viewState: StateFlow<ProcedureListViewState> = _viewState.asStateFlow()

    private val _viewEvent: MutableSharedFlow<ProcedureListEvent> = MutableSharedFlow()
    val viewEvent: Flow<ProcedureListEvent>
        get() = _viewEvent

    private val _currentState
        get() = _viewState.value

    private var onlyFavorites: Boolean = false

    fun initProceduresList(onlyFavorites: Boolean) {
        if (onlyFavorites) {
            this.onlyFavorites = onlyFavorites
            loadFavoriteProcedures()
        } else {
            loadAllProcedures()
        }
    }

    private fun loadAllProcedures() {
        viewModelScope.launch(ioDispatcher) {
            procedureRepository.getAllProcedures()
                .onSuccess { procedures ->
                    updateState { ProcedureListViewState.ProceduresLoaded(procedures) }
                }
                .onFailure { error ->
                    updateState {
                        ProcedureListViewState.Error(error.mapToUIError())
                    }
                }
        }
    }

    private fun loadFavoriteProcedures() {
        viewModelScope.launch(ioDispatcher) {
            procedureRepository.getFavoriteProcedures()
                .onSuccess { procedures ->
                    updateState {
                        if (procedures.isEmpty()) {
                            ProcedureListViewState.Error(ProcedureListError.EmptyList)
                        } else {
                            ProcedureListViewState.ProceduresLoaded(procedures)
                        }
                    }
                }
                .onFailure { error ->
                    updateState {
                        ProcedureListViewState.Error(error.mapToUIError())
                    }
                }
        }
    }

    fun toggleFavorite(procedureId: String) {
        viewModelScope.launch(ioDispatcher) {
            val procedures = (_viewState.value as? ProcedureListViewState.ProceduresLoaded)?.procedures
            procedures?.let { procedures ->
                val procedure = procedures.find { it.id == procedureId }
                try {
                    val setAsFavorite = !(procedure!!.isFavorite)
                    procedureRepository.setFavorite(procedureId, setAsFavorite)
                    updateFavoriteInUI(procedures, procedureId)
                    _viewEvent.emitEvent(ProcedureListEvent.ShowFavoriteMessage(setAsFavorite))
                } catch (e: Exception) {
                    _viewEvent.emitEvent(ProcedureListEvent.ErrorAssigningFavorite)
                }
            }
        }
    }

    fun showDetail(procedureId: String) {
        _viewEvent.emitEvent(ProcedureListEvent.ShowDetail(procedureId))
    }

    fun procedureDetailClosed() {
        initProceduresList(onlyFavorites = this.onlyFavorites)
        _viewEvent.emitEvent(ProcedureListEvent.None)
    }

    private fun updateFavoriteInUI(procedures: List<ProcedureItem>, procedureId: String) {
        updateState {
            ProcedureListViewState.ProceduresLoaded(
                procedures.map { procedure ->
                    if (procedure.id == procedureId) {
                        procedure.copy(isFavorite = !procedure.isFavorite)
                    } else {
                        procedure
                    }
                },
            )
        }
    }

    private fun MutableSharedFlow<ProcedureListEvent>.emitEvent(event: ProcedureListEvent) {
        viewModelScope.launch {
            emit(event)
        }
    }

    private fun updateState(update: (ProcedureListViewState) -> ProcedureListViewState): ProcedureListViewState {
        _viewState.update(update)
        return _currentState
    }

    private fun Throwable.mapToUIError(): ProcedureListError {
        return when (this) {
            ProcedureError.NoInternetError -> ProcedureListError.NoInternetError
            ProcedureError.EmptyList -> ProcedureListError.EmptyList
            else -> ProcedureListError.UnidentifiedError
        }
    }
}