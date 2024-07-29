package com.gemasr.surgeonwizard.procedures.detail

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProcedureDetailTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private val robot = ProcedureDetailRobot(composeTestRule)

    @Before
    fun setup() {
        robot.initializeViewModel()
    }

    @Test
    fun whenLoading_thenShowsLoadingIndicator() {
        robot
            .givenLoadState()
            .launch()
            .thenAssertLoadingIsShown()
    }

    @Test
    fun whenProcedureLoaded_thenShowsProcedureDetail() {
        robot
            .givenProcedureDetailState()
            .launch()
            .thenAssertDetailIsShown()
    }

    @Test
    fun whenUnidentifiedError_thenShowsErrorMessage() {
        robot
            .givenUnidentifiedErrorState()
            .launch()
            .thenAssertUnidentifiedErrorIsShown()
    }

    @Test
    fun whenNotFoundError_thenShowsErrorMessage() {
        robot
            .givenNotFoundErrorState()
            .launch()
            .thenAssertNotFoundErrorIsShown()
    }

    @Test
    fun whenFavoriteTap_thenToggleFavorite() {
        robot
            .givenProcedureDetailState()
            .givenFavoriteEvent()
            .launch()
            .thenAssertDetailIsShown()
            .thenAssertFavoriteSnackbarIsShown()
    }
}