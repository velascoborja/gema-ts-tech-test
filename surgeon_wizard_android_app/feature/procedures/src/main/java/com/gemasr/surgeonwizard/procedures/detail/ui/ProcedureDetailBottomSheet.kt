package com.gemasr.surgeonwizard.procedures.detail.ui

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gemasr.surgeonwizard.procedures.R
import com.gemasr.surgeonwizard.procedures.detail.ProcedureDetailEvent
import com.gemasr.surgeonwizard.procedures.detail.ProcedureDetailViewModel
import com.gemasr.surgeonwizard.procedures.detail.ProcedureDetailViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcedureDetailBottomSheet(
    procedureId: String,
    onDismiss: () -> Unit,
    snackbarHostState: SnackbarHostState,
    sheetState: SheetState = rememberModalBottomSheetState(),
    viewModel: ProcedureDetailViewModel = hiltViewModel(),
) {
    val viewState by viewModel.viewState.collectAsState()
    val viewEvents by viewModel.viewEvent.collectAsStateWithLifecycle(ProcedureDetailEvent.None)
    val favoriteAddedString = stringResource(id = R.string.favorite_added)
    val favoriteRemovedString = stringResource(id = R.string.favorite_removed)
    val favoriteErrorString = stringResource(id = R.string.favorite_error)

    LaunchedEffect(procedureId) {
        viewModel.loadProcedureDetail(procedureId)
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        when (viewState) {
            is ProcedureDetailViewState.Loading ->
                CircularProgressIndicator(
                    modifier =
                    Modifier
                        .align(Alignment.CenterHorizontally)
                        .testTag(ProcedureDetailTestTags.LOADING_VIEW),
                )
            is ProcedureDetailViewState.ProcedureDetailLoaded ->
                ProcedureDetailContent(
                    procedureDetail = (viewState as ProcedureDetailViewState.ProcedureDetailLoaded).procedure,
                    onFavoriteToggle = { viewModel.toggleFavorite() },
                )
            is ProcedureDetailViewState.Error ->
                ProcedureDetailError((viewState as ProcedureDetailViewState.Error).error)
        }
    }

    LaunchedEffect(viewEvents) {
        when (viewEvents) {
            is ProcedureDetailEvent.ShowFavoriteMessage -> {
                val added = (viewEvents as ProcedureDetailEvent.ShowFavoriteMessage).added
                val message = if (added) favoriteAddedString else favoriteRemovedString

                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short,
                )
            }

            is ProcedureDetailEvent.ErrorAssigningFavorite -> {
                snackbarHostState.showSnackbar(
                    message = favoriteErrorString,
                    duration = SnackbarDuration.Short,
                )
            }
            ProcedureDetailEvent.None -> {}
        }
    }
}