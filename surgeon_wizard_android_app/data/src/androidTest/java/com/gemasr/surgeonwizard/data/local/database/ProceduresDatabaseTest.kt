package com.gemasr.surgeonwizard.data.local.database

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.gemasr.surgeonwizard.data.local.entity.ProcedureDetailEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureFavoriteEntity
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProceduresDatabaseTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: ProcedureDao

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = database.procedureDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun givenProceduresAndFavorites_whenGetAllProceduresWithFavorites_thenReturnCorrectList() {
        // Given
        val procedure1 = ProcedureEntity("1", "Procedure 1", "icon1", 3, 60, 1000L)
        val procedure2 = ProcedureEntity("2", "Procedure 2", "icon2", 2, 30, 2000L)
        dao.insertProcedures(listOf(procedure1, procedure2))
        dao.insertOrUpdateFavorite(ProcedureFavoriteEntity("1", true))

        // When
        val result = dao.getAllProceduresWithFavorites()

        // Then
        assertEquals(2, result.size)
        val favProcedure = result.find { it.procedure.uuid == "1" }
        val nonFavProcedure = result.find { it.procedure.uuid == "2" }
        assertTrue(favProcedure?.isFavorite == true)
        assertTrue(nonFavProcedure?.isFavorite == false)
    }

    @Test
    fun givenProcedureDetailAndFavorite_whenGetProcedureDetailWithFavorite_thenReturnCorrectDetail() {
        // Given
        val detail = ProcedureDetailEntity("1", "Procedure 1", "Description", "phases", "date", 60)
        dao.insertProcedureDetail(detail)
        dao.insertOrUpdateFavorite(ProcedureFavoriteEntity("1", true))

        // When
        val result = dao.getProcedureDetailWithFavorite("1")

        // Then
        assertNotNull(result)
        assertEquals("1", result?.procedureDetail?.uuid)
        assertTrue(result?.isFavorite == true)
    }

    @Test
    fun givenNonExistentId_whenGetProcedureDetailWithFavorite_thenReturnNull() {
        // When
        val result = dao.getProcedureDetailWithFavorite("nonexistent")

        // Then
        assertNull(result)
    }

    @Test
    fun givenProceduresList_whenInsertProcedures_thenAllProceduresAreInserted() {
        // Given
        val procedures =
            listOf(
                ProcedureEntity("1", "Procedure 1", "icon1", 3, 60, 1000L),
                ProcedureEntity("2", "Procedure 2", "icon2", 2, 30, 2000L),
            )

        // When
        dao.insertProcedures(procedures)

        // Then
        val result = dao.getAllProceduresWithFavorites()
        assertEquals(2, result.size)
        assertTrue(result.any { it.procedure.uuid == "1" && it.procedure.name == "Procedure 1" })
        assertTrue(result.any { it.procedure.uuid == "2" && it.procedure.name == "Procedure 2" })
    }

    @Test
    fun givenProcedureDetail_whenInsertProcedureDetail_thenDetailIsInserted() {
        // Given
        val detail = ProcedureDetailEntity("1", "Procedure 1", "Description", "phases", "date", 60)

        // When
        dao.insertProcedureDetail(detail)

        // Then
        val result = dao.getProcedureDetailWithFavorite("1")
        assertNotNull(result)
        assertEquals("1", result?.procedureDetail?.uuid)
        assertEquals("Procedure 1", result?.procedureDetail?.name)
    }

    @Test
    fun givenNewFavorite_whenInsertOrUpdateFavorite_thenFavoriteIsInserted() {
        // Given
        val detail = ProcedureDetailEntity("1", "Procedure 1", "Description", "phases", "date", 60)
        dao.insertProcedureDetail(detail)
        val favorite = ProcedureFavoriteEntity("1", true)

        // When
        dao.insertOrUpdateFavorite(favorite)

        // Then
        val result = dao.getProcedureDetailWithFavorite("1")
        assertTrue(result?.isFavorite == true)
    }

    @Test
    fun givenExistingFavorite_whenInsertOrUpdateFavorite_thenFavoriteIsUpdated() {
        // Given
        val detail = ProcedureDetailEntity("1", "Procedure 1", "Description", "phases", "date", 60)
        dao.insertProcedureDetail(detail)
        dao.insertOrUpdateFavorite(ProcedureFavoriteEntity("1", true))

        // When
        dao.insertOrUpdateFavorite(ProcedureFavoriteEntity("1", false))

        // Then
        val result = dao.getProcedureDetailWithFavorite("1")
        assertTrue(result?.isFavorite == false)
    }

    @Test
    fun givenProceduresWithDifferentDates_whenGetProceduresLastUpdated_thenReturnMostRecentDate() {
        // Given
        val procedures =
            listOf(
                ProcedureEntity("1", "Procedure 1", "icon1", 3, 60, 1000L),
                ProcedureEntity("2", "Procedure 2", "icon2", 2, 30, 2000L),
            )
        dao.insertProcedures(procedures)

        // When
        val result = dao.getProceduresLastUpdated()

        // Then
        assertEquals(2000L, result)
    }

    @Test
    fun givenProceduresWithFavorites_whenGetAllFavoriteProcedures_thenReturnOnlyFavorites() {
        // Given
        val procedure1 = ProcedureEntity("1", "Procedure 1", "icon1", 3, 60, 1000L)
        val procedure2 = ProcedureEntity("2", "Procedure 2", "icon2", 2, 30, 2000L)
        val procedure3 = ProcedureEntity("3", "Procedure 3", "icon3", 4, 45, 3000L)
        dao.insertProcedures(listOf(procedure1, procedure2, procedure3))

        // Set procedures 1 and 3 as favorites
        dao.insertOrUpdateFavorite(ProcedureFavoriteEntity("1", true))
        dao.insertOrUpdateFavorite(ProcedureFavoriteEntity("3", true))

        // When
        val result = dao.getAllFavoriteProcedures()

        // Then
        assertEquals(2, result.size)
        assertTrue(result.all { it.isFavorite })
        assertTrue(result.any { it.procedure.uuid == "1" && it.procedure.name == "Procedure 1" })
        assertTrue(result.any { it.procedure.uuid == "3" && it.procedure.name == "Procedure 3" })
        assertFalse(result.any { it.procedure.uuid == "2" })
    }
}