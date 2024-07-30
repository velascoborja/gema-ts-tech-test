package com.gemasr.surgeonwizard.procedures.list

import com.gemasr.surgeonwizard.domain.procedure.IProcedureRepository
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureItem
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
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
class ProcedureListViewModelTest {
    private lateinit var viewModel: ProcedureListViewModel
    private lateinit var procedureRepository: IProcedureRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        procedureRepository = mockk()
        viewModel = ProcedureListViewModel(procedureRepository, testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `given onlyFavorites is false, when initProceduresList is called, then all procedures are loaded`() = runTest {
        // Given
        val procedures = listOf(createDummyProcedure("1"), createDummyProcedure("2"))
        coEvery { procedureRepository.getAllProcedures() } returns Result.success(procedures)

        // When
        viewModel.initProceduresList(onlyFavorites = false)
        testScheduler.advanceUntilIdle()

        // Then
        val state = viewModel.viewState.value
        assertTrue(state is ProcedureListViewState.ProceduresLoaded)
        assertEquals(procedures, (state as ProcedureListViewState.ProceduresLoaded).procedures)
    }

    @Test
    fun `given onlyFavorites is true, when initProceduresList is called, then only favorite procedures are loaded`() = runTest {
        // Given
        val favoriteProcedures = listOf(createDummyProcedure("1", isFavorite = true))
        coEvery { procedureRepository.getFavoriteProcedures() } returns
            Result.success(
                favoriteProcedures,
            )

        // When
        viewModel.initProceduresList(onlyFavorites = true)
        testScheduler.advanceUntilIdle()

        // Then
        val state = viewModel.viewState.value
        assertTrue(state is ProcedureListViewState.ProceduresLoaded)
        assertEquals(
            favoriteProcedures,
            (state as ProcedureListViewState.ProceduresLoaded).procedures,
        )
    }

    @Test
    fun `given repository returns an error, when initProceduresList is called, then error state is shown`() = runTest {
        // Given
        coEvery { procedureRepository.getAllProcedures() } returns Result.failure(Exception())

        // When
        viewModel.initProceduresList(onlyFavorites = false)
        testScheduler.advanceUntilIdle()

        // Then
        val state = viewModel.viewState.value
        assertTrue(state is ProcedureListViewState.Error)
        assertTrue((state as ProcedureListViewState.Error).error is ProcedureListError.UnidentifiedError)
    }

    @Test
    fun `given a procedure exists, when toggleFavorite is called, then procedure favorite status is updated`() = runTest {
        // Given
        val procedures = listOf(createDummyProcedure("1", isFavorite = false))
        coEvery { procedureRepository.getAllProcedures() } returns Result.success(procedures)
        coEvery { procedureRepository.setFavorite(any(), any()) } returns Result.success(Unit)
        viewModel.initProceduresList(onlyFavorites = false)
        testScheduler.advanceUntilIdle()
        val events = mutableListOf<ProcedureListEvent>()
        val job =
            launch {
                viewModel.viewEvent.toList(events)
            }

        // When
        viewModel.toggleFavorite("1")
        testScheduler.advanceUntilIdle()
        job.cancelAndJoin()

        // Then
        val state = viewModel.viewState.value
        assertTrue(state is ProcedureListViewState.ProceduresLoaded)
        assertEquals(
            true,
            (state as ProcedureListViewState.ProceduresLoaded).procedures.first().isFavorite,
        )

        val event = events.first()
        assertTrue(event is ProcedureListEvent.ShowFavoriteMessage)
        assertEquals(true, (event as ProcedureListEvent.ShowFavoriteMessage).added)
    }

    @Test
    fun `given a procedure id, when showDetail is called, then ShowDetail event is emitted`() = runTest {
        // Given
        val procedureId = "1"
        val procedures = listOf(createDummyProcedure("1", isFavorite = false))
        coEvery { procedureRepository.getAllProcedures() } returns Result.success(procedures)
        viewModel.initProceduresList(false)
        testScheduler.advanceUntilIdle()
        val events = mutableListOf<ProcedureListEvent>()
        val job =
            launch {
                viewModel.viewEvent.toList(events)
            }

        // When
        viewModel.showDetail(procedureId)
        testScheduler.advanceUntilIdle()
        job.cancelAndJoin()

        // Then
        val event = events.first()
        assertTrue(event is ProcedureListEvent.ShowDetail)
        assertEquals(procedureId, (event as ProcedureListEvent.ShowDetail).procedureId)
    }

    @Test
    fun `given procedure detail was open, when procedureDetailClosed is called, then procedures are reloaded and None event is emitted`() =
        runTest {
            // Given
            val procedures = listOf(createDummyProcedure("1"))
            coEvery { procedureRepository.getAllProcedures() } returns Result.success(procedures)
            viewModel.initProceduresList(onlyFavorites = false)
            testScheduler.advanceUntilIdle()
            viewModel.showDetail("1")
            val events = mutableListOf<ProcedureListEvent>()
            val job =
                launch {
                    viewModel.viewEvent.toList(events)
                }

            // When
            viewModel.procedureDetailClosed()
            testScheduler.advanceUntilIdle()
            job.cancelAndJoin()

            // Then
            val state = viewModel.viewState.value
            assertTrue(state is ProcedureListViewState.ProceduresLoaded)
            assertEquals(procedures, (state as ProcedureListViewState.ProceduresLoaded).procedures)

            assertTrue(events.first() is ProcedureListEvent.None)
        }

    private fun createDummyProcedure(id: String, isFavorite: Boolean = false) = ProcedureItem(
        id = id,
        iconUrl = "https://example.com/icon.png",
        name = "Procedure $id",
        duration = 30,
        phaseCount = 3,
        isFavorite = isFavorite,
    )
}