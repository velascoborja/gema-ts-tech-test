package com.gemasr.surgeonwizard.procedures.list.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureItem
import com.gemasr.surgeonwizard.procedures.R
import com.gemasr.surgeonwizard.procedures.detail.ui.ProcedureDetailBottomSheet
import com.gemasr.surgeonwizard.procedures.list.ProcedureListEvent
import com.gemasr.surgeonwizard.procedures.list.ProcedureListViewModel
import com.gemasr.surgeonwizard.procedures.list.ProcedureListViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProcedureListScreen(
    viewModel: ProcedureListViewModel = hiltViewModel(),
    onlyFavorites: Boolean = false,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier,
) {
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val viewEvents by viewModel.viewEvent.collectAsStateWithLifecycle(ProcedureListEvent.None)
    var showDetail by remember { mutableStateOf<String?>(null) }
    val favoriteAddedString = stringResource(id = R.string.favorite_added)
    val favoriteRemovedString = stringResource(id = R.string.favorite_removed)
    val favoriteErrorString = stringResource(id = R.string.favorite_error)

    LaunchedEffect(key1 = Unit) {
        viewModel.initProceduresList(onlyFavorites)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (viewState) {
            is ProcedureListViewState.ProceduresLoaded ->
                ProceduresList(
                    procedures = (viewState as ProcedureListViewState.ProceduresLoaded).procedures,
                    onlyFavorites = onlyFavorites,
                    onItemClick = { viewModel.showDetail(it) },
                    onFavoriteToggle = { viewModel.toggleFavorite(it) },
                )
            is ProcedureListViewState.Error ->
                ProcedureListErrorView(
                    error = (viewState as ProcedureListViewState.Error).error,
                    isShowingOnlyFavorites = onlyFavorites,
                ) {
                    viewModel.initProceduresList(onlyFavorites)
                }

            ProcedureListViewState.Loading ->
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )
        }
    }

    showDetail?.let {
        ProcedureDetailBottomSheet(
            procedureId = (viewEvents as ProcedureListEvent.ShowDetail).procedureId,
            onDismiss = {
                showDetail = null
                viewModel.procedureDetailClosed()
            },
            snackbarHostState = snackbarHostState,
        )
    }

    LaunchedEffect(viewEvents) {
        when (viewEvents) {
            is ProcedureListEvent.ShowDetail -> {
                showDetail = (viewEvents as ProcedureListEvent.ShowDetail).procedureId
            }

            is ProcedureListEvent.ShowFavoriteMessage -> {
                val added = (viewEvents as ProcedureListEvent.ShowFavoriteMessage).added
                val message = if (added) favoriteAddedString else favoriteRemovedString

                snackbarHostState.showSnackbar(
                    message = message,
                    duration = SnackbarDuration.Short,
                )
            }

            is ProcedureListEvent.ErrorAssigningFavorite -> {
                snackbarHostState.showSnackbar(
                    message = favoriteErrorString,
                    duration = SnackbarDuration.Short,
                )
            }
            ProcedureListEvent.None -> {}
        }
    }
}

@Composable
private fun ProceduresList(
    procedures: List<ProcedureItem>,
    onlyFavorites: Boolean,
    onItemClick: (String) -> Unit,
    onFavoriteToggle: (String) -> Unit,
) {
    LazyColumn {
        items(procedures) { procedure ->
            ProcedureItem(
                procedure = procedure,
                showFavoriteButton = onlyFavorites,
                onItemClick = { onItemClick(procedure.id) },
                onFavoriteToggle = { onFavoriteToggle(procedure.id) },
            )
        }
    }
}

// region Previews

@Preview(showBackground = true)
@Composable
fun ProceduresListPreview() {
    Column {
        Text("All procedures", modifier = Modifier.padding(16.dp))
        ProceduresList(
            procedures =
            listOf(
                ProcedureItem(
                    id = "1",
                    iconUrl = "https://example.com/icon1.png",
                    name = "Procedure 1",
                    duration = 30,
                    phaseCount = 3,
                    isFavorite = true,
                ),
                ProcedureItem(
                    id = "2",
                    iconUrl = "https://example.com/icon2.png",
                    name = "Procedure 2",
                    duration = 45,
                    phaseCount = 4,
                    isFavorite = false,
                ),
                ProcedureItem(
                    id = "3",
                    iconUrl = "https://example.com/icon3.png",
                    name = "Procedure 3",
                    duration = 60,
                    phaseCount = 5,
                    isFavorite = true,
                ),
            ),
            onlyFavorites = false,
            onItemClick = {},
            onFavoriteToggle = {},
        )

        Text("Just favorites", modifier = Modifier.padding(16.dp))
        ProceduresList(
            procedures =
            listOf(
                ProcedureItem(
                    id = "1",
                    iconUrl = "https://example.com/icon1.png",
                    name = "Procedure 1",
                    duration = 30,
                    phaseCount = 3,
                    isFavorite = true,
                ),
                ProcedureItem(
                    id = "3",
                    iconUrl = "https://example.com/icon3.png",
                    name = "Procedure 3",
                    duration = 60,
                    phaseCount = 5,
                    isFavorite = true,
                ),
            ),
            onlyFavorites = true,
            onItemClick = {},
            onFavoriteToggle = {},
        )
    }
}

@Preview(showBackground = true, widthDp = 320, heightDp = 480)
@Composable
fun LoadingPreview() {
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
            )
        }
    }
}

// endregion Previews