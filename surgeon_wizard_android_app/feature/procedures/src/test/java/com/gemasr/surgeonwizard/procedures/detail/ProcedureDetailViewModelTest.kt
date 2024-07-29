package com.gemasr.surgeonwizard.procedures.detail

import com.gemasr.surgeonwizard.domain.procedure.IProcedureRepository
import com.gemasr.surgeonwizard.domain.procedure.error.ProcedureError
import com.gemasr.surgeonwizard.domain.procedure.model.Phase
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureDetail
import io.mockk.coEvery
import io.mockk.mockk
import java.time.LocalDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ProcedureDetailViewModelTest {
    private lateinit var viewModel: ProcedureDetailViewModel
    private lateinit var procedureRepository: IProcedureRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        procedureRepository = mockk()
        viewModel = ProcedureDetailViewModel(procedureRepository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given a valid and existing procedure id, when loadProcedureDetail is called, then ProcedureDetailLoaded state is emitted`() =
        runTest {
            // Given
            val procedureId = "1"
            val procedureDetail = createDummyProcedureDetail(procedureId)
            coEvery { procedureRepository.getProcedureDetail(procedureId) } returns
                Result.success(
                    procedureDetail,
                )

            // When
            viewModel.loadProcedureDetail(procedureId)
            testScheduler.advanceUntilIdle()

            // Then
            val state = viewModel.viewState.value
            assertTrue(state is ProcedureDetailViewState.ProcedureDetailLoaded)
            assertEquals(
                procedureDetail.id,
                (state as ProcedureDetailViewState.ProcedureDetailLoaded).procedure.id,
            )
        }

    @Test
    fun `given an invalid procedure id, when loadProcedureDetail is called, then Error state is emitted`() = runTest {
        // Given
        val procedureId = "invalid"
        coEvery { procedureRepository.getProcedureDetail(procedureId) } returns
            Result.failure(
                ProcedureError.NotFound,
            )

        // When
        viewModel.loadProcedureDetail(procedureId)
        testScheduler.advanceUntilIdle()

        // Then
        val state = viewModel.viewState.value
        assertTrue(state is ProcedureDetailViewState.Error)
        assertTrue((state as ProcedureDetailViewState.Error).error is ProcedureDetailError.NotFound)
    }

    @Test
    fun `given a loaded procedure, when toggleFavorite is called, then procedure favorite status is updated`() = runTest {
        // Given
        val procedureId = "1"
        val procedureDetail = createDummyProcedureDetail(procedureId, isFavorite = false)
        coEvery { procedureRepository.getProcedureDetail(procedureId) } returns
            Result.success(
                procedureDetail,
            )
        coEvery { procedureRepository.setFavorite(procedureId, true) } returns
            Result.success(
                Unit,
            )

        viewModel.loadProcedureDetail(procedureId)
        testScheduler.advanceUntilIdle()

        // When
        viewModel.toggleFavorite()
        testScheduler.advanceUntilIdle()

        // Then
        val state = viewModel.viewState.value
        assertTrue(state is ProcedureDetailViewState.ProcedureDetailLoaded)
        assertEquals(
            true,
            (state as ProcedureDetailViewState.ProcedureDetailLoaded).procedure.isFavorite,
        )
    }

    @Test
    fun `given a failure in setting favorite, when toggleFavorite is called, then ErrorAssigningFavorite event is emitted`() = runTest {
        // Given
        val procedureId = "1"
        val procedureDetail = createDummyProcedureDetail(procedureId, isFavorite = false)
        coEvery { procedureRepository.getProcedureDetail(procedureId) } returns
            Result.success(
                procedureDetail,
            )
        coEvery { procedureRepository.setFavorite(procedureId, true) } returns
            Result.failure(
                Exception(),
            )

        viewModel.loadProcedureDetail(procedureId)
        testScheduler.advanceUntilIdle()

        // When
        viewModel.toggleFavorite()

        // Then
        val event = viewModel.viewEvent.first()
        assertTrue(event is ProcedureDetailEvent.ErrorAssigningFavorite)
    }

    private fun createDummyProcedureDetail(id: String, isFavorite: Boolean = false) = ProcedureDetail(
        id = id,
        name = "Procedure $id",
        phases =
        listOf(
            Phase("1", "Phase 1", "icon_url_1"),
            Phase("2", "Phase 2", "icon_url_2"),
        ),
        cardUrl = "https://example.com/card.png",
        datePublished = LocalDate.now(),
        durationInMinutes = 30,
        isFavorite = isFavorite,
    )
}