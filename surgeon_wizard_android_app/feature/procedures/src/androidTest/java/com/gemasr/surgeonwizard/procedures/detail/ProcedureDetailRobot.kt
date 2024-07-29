package com.gemasr.surgeonwizard.procedures.detail

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.test.assertContentDescriptionEquals
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.gemasr.surgeonwizard.procedures.R
import com.gemasr.surgeonwizard.procedures.detail.ui.ProcedureDetailBottomSheet
import com.gemasr.surgeonwizard.procedures.detail.ui.ProcedureDetailTestTags
import com.gemasr.surgeonwizard.procedures.detail.ui.model.ProcedureDetailUI
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow

class ProcedureDetailRobot(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<ComponentActivity>, ComponentActivity>,
) {
    private lateinit var viewModel: ProcedureDetailViewModel
    private lateinit var snackbarHostState: SnackbarHostState

    fun initializeViewModel() {
        viewModel = mockk<ProcedureDetailViewModel>(relaxed = true)
        snackbarHostState = SnackbarHostState()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    fun launch() = apply {
        composeTestRule.setContent {
            val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

            ProcedureDetailBottomSheet(
                procedureId = "123",
                onDismiss = {},
                snackbarHostState = snackbarHostState,
                viewModel = viewModel,
                sheetState = sheetState,
            )
            SnackbarHost(
                hostState = snackbarHostState,
                modifier =
                Modifier
                    .fillMaxWidth()
                    .testTag("snackbar"),
            )
        }
    }

    fun givenLoadState() = apply {
        every { viewModel.viewState } returns MutableStateFlow(ProcedureDetailViewState.Loading)
    }

    fun givenProcedureDetailState() = apply {
        val procedureDetail = createDummyProcedureDetailUI()
        every { viewModel.viewState } returns
            MutableStateFlow(
                ProcedureDetailViewState.ProcedureDetailLoaded(
                    procedureDetail,
                ),
            )
    }

    fun givenNotFoundErrorState() = apply {
        every { viewModel.viewState } returns
            MutableStateFlow(
                ProcedureDetailViewState.Error(
                    ProcedureDetailError.NotFound,
                ),
            )
    }

    fun givenUnidentifiedErrorState() = apply {
        every { viewModel.viewState } returns
            MutableStateFlow(
                ProcedureDetailViewState.Error(
                    ProcedureDetailError.UnidentifiedError,
                ),
            )
    }

    fun givenFavoriteEvent() = apply {
        every { viewModel.viewEvent } returns
            MutableStateFlow(
                ProcedureDetailEvent.ShowFavoriteMessage(
                    added = true,
                ),
            )
    }

    fun thenAssertLoadingIsShown() {
        composeTestRule.onNodeWithTag(ProcedureDetailTestTags.LOADING_VIEW).assertIsDisplayed()
    }

    fun thenAssertDetailIsShown() = apply {
        val procedureDetail = createDummyProcedureDetailUI()
        with(composeTestRule) {
            onNodeWithTag(ProcedureDetailTestTags.CONTENT_VIEW).assertIsDisplayed()
            onNodeWithText(procedureDetail.name).assertIsDisplayed()
            onAllNodesWithTag(ProcedureDetailTestTags.PHASE_ITEM).assertCountEquals(2)
            onNodeWithTag(ProcedureDetailTestTags.FAVORITE_ICON, useUnmergedTree = true)
                .assertExists()
                .assertContentDescriptionEquals(
                    composeTestRule.activity.getString(R.string.favorite_button_content_description),
                )
        }
    }

    fun thenAssertUnidentifiedErrorIsShown() = apply {
        composeTestRule.onNodeWithTag(ProcedureDetailTestTags.ERROR_VIEW).assertIsDisplayed()
    }

    fun thenAssertNotFoundErrorIsShown() = apply {
        composeTestRule.onNodeWithTag(ProcedureDetailTestTags.ERROR_VIEW).assertIsDisplayed()
    }

    fun thenAssertFavoriteSnackbarIsShown() {
        composeTestRule.onNodeWithText(composeTestRule.activity.getString(R.string.favorite_added))
            .assertIsDisplayed()
    }

    private fun createDummyProcedureDetailUI(
        id: String = "dummy_id",
        name: String = "Dummy Procedure",
        phases: List<ProcedureDetailUI.PhaseUI> =
            listOf(
                createDummyPhaseUI(),
                createDummyPhaseUI(),
            ),
        cardImageUrl: String = "",
        datePublished: String = "29/07/2024",
        durationInMinutes: Int = 60,
        isFavorite: Boolean = false,
    ): ProcedureDetailUI {
        return ProcedureDetailUI(
            id = id,
            name = name,
            phases = phases,
            cardImageUrl = cardImageUrl,
            datePublished = datePublished,
            durationInMinutes = durationInMinutes,
            isFavorite = isFavorite,
        )
    }

    private fun createDummyPhaseUI(
        id: String = "dummy_phase_id",
        name: String = "Dummy Phase",
        iconUrl: String = "",
    ): ProcedureDetailUI.PhaseUI {
        return ProcedureDetailUI.PhaseUI(
            id = id,
            name = name,
            iconUrl = iconUrl,
        )
    }
}