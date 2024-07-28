package com.gemasr.surgeonwizard

import com.gemasr.surgeonwizard.data.local.ProcedureLocalDataSource
import com.gemasr.surgeonwizard.data.remote.ProcedureRemoteDataSource
import com.gemasr.surgeonwizard.util.ProceduresFabricator
import com.google.gson.Gson
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class ProcedureRepositoryTest {

    private lateinit var repository: ProcedureRepository
    private lateinit var localDataSource: ProcedureLocalDataSource
    private lateinit var remoteDataSource: ProcedureRemoteDataSource
    private lateinit var gson: Gson

    @Before
    fun setUp() {
        localDataSource = mockk()
        remoteDataSource = mockk()
        gson = Gson()
        repository = ProcedureRepository(localDataSource, remoteDataSource, gson)
    }

    @Test
    fun `given local data available when getAllProcedures then return local data`() = runBlocking {
        // Given
        val localProcedures = listOf(ProceduresFabricator.createProcedureWithFavorite())
        val expectedProcedureItems = listOf(ProceduresFabricator.createProcedureItem())

        coEvery { localDataSource.getAllProcedures() } returns localProcedures

        // When
        val result = repository.getAllProcedures()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedProcedureItems.size, result.getOrNull()?.size)
        assertEquals(expectedProcedureItems.first().id, result.getOrNull()?.first()?.id)
        coVerify(exactly = 1) { localDataSource.getAllProcedures() }
        coVerify(exactly = 0) { remoteDataSource.getProceduresList() }
    }

    @Test
    fun `given empty local data when getAllProcedures then it should fetch remote data and store it before returning it`() = runBlocking {
        // Given
        val remoteProcedures = listOf(ProceduresFabricator.createProcedureListItemApiModel())
        val localProcedures = listOf(ProceduresFabricator.createProcedureWithFavorite())
        val expectedProcedureItems = listOf(ProceduresFabricator.createProcedureItem())

        coEvery { localDataSource.getAllProcedures() } returnsMany listOf(emptyList(), localProcedures)
        coEvery { remoteDataSource.getProceduresList() } returns remoteProcedures
        coEvery { localDataSource.insertProcedures(any()) } just Runs

        // When
        val result = repository.getAllProcedures()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedProcedureItems.size, result.getOrNull()?.size)
        assertEquals(expectedProcedureItems.first().id, result.getOrNull()?.first()?.id)
        coVerify(exactly = 2) { localDataSource.getAllProcedures() }
        coVerify(exactly = 1) { remoteDataSource.getProceduresList() }
        coVerify(exactly = 1) { localDataSource.insertProcedures(any()) }
    }

    @Test
    fun `given local detail available when getProcedureDetail then it should return local detail`() = runBlocking {
        // Given
        val id = "123"
        val localProcedureDetail = ProceduresFabricator.createProcedureDetailWithFavorite()
        val expectedProcedureDetail = ProceduresFabricator.createProcedureDetail()

        coEvery { localDataSource.getProcedureDetail(id) } returns localProcedureDetail

        // When
        val result = repository.getProcedureDetail(id)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedProcedureDetail.id, result.getOrNull()?.id)
        coVerify(exactly = 1) { localDataSource.getProcedureDetail(id) }
        coVerify(exactly = 0) { remoteDataSource.getProcedureDetail(id) }
    }

    @Test
    fun `given no local detail when getProcedureDetail then fetch remote detail`() = runBlocking {
        // Given
        val id = "123"
        val remoteProcedureDetail = ProceduresFabricator.createProcedureDetailApiModel()
        val localProcedureDetail = ProceduresFabricator.createProcedureDetailWithFavorite()
        val expectedProcedureDetail = ProceduresFabricator.createProcedureDetail()

        coEvery { localDataSource.getProcedureDetail(id) } returnsMany listOf(null, localProcedureDetail)
        coEvery { remoteDataSource.getProcedureDetail(id) } returns remoteProcedureDetail
        coEvery { localDataSource.insertProcedureDetail(any()) } just Runs

        // When
        val result = repository.getProcedureDetail(id)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(expectedProcedureDetail.id, result.getOrNull()?.id)
        coVerify(exactly = 2) { localDataSource.getProcedureDetail(id) }
        coVerify(exactly = 1) { remoteDataSource.getProcedureDetail(id) }
        coVerify(exactly = 1) { localDataSource.insertProcedureDetail(any()) }
    }

    @Test
    fun `given procedure id and favorite status when setFavorite then update local data source`() = runBlocking {
        // Given
        val id = "123"
        val isFavorite = true
        coEvery { localDataSource.setFavorite(id, isFavorite) } just Runs

        // When
        repository.setFavorite(id, isFavorite)

        // Then
        coVerify(exactly = 1) { localDataSource.setFavorite(id, isFavorite) }
    }
}