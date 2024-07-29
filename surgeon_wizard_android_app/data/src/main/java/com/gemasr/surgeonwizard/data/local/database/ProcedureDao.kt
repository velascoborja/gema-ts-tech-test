package com.gemasr.surgeonwizard.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gemasr.surgeonwizard.data.local.entity.ProcedureDetailEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureDetailWithFavorite
import com.gemasr.surgeonwizard.data.local.entity.ProcedureEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureFavoriteEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureWithFavorite

@Dao
interface ProcedureDao {
    @Query(
        """
        SELECT p.uuid as uuid, p.name as name, p.iconUrl as iconUrl, p.phaseCount as phaseCount, p.duration as duration, p.lastUpdated as lastUpdated, 
               COALESCE(f.isFavorite, 0) as isFavorite
        FROM procedures p
        LEFT JOIN procedure_favorites f ON p.uuid = f.procedureId
    """,
    )
    fun getAllProceduresWithFavorites(): List<ProcedureWithFavorite>

    @Query(
        """
        SELECT p.uuid as uuid, p.name as name, p.iconUrl as iconUrl, p.phaseCount as phaseCount, p.duration as duration, p.lastUpdated as lastUpdated, 
               COALESCE(f.isFavorite, 0) as isFavorite
        FROM procedures p
        LEFT JOIN procedure_favorites f ON p.uuid = f.procedureId
        WHERE isFavorite = 1
    """,
    )
    fun getAllFavoriteProcedures(): List<ProcedureWithFavorite>

    @Query(
        """
        SELECT pd.*, COALESCE(f.isFavorite, 0) as isFavorite
        FROM procedure_details pd
        LEFT JOIN procedure_favorites f ON pd.uuid = f.procedureId
        WHERE pd.uuid = :id
    """,
    )
    fun getProcedureDetailWithFavorite(id: String): ProcedureDetailWithFavorite?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProcedures(procedures: List<ProcedureEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProcedureDetail(procedureDetail: ProcedureDetailEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdateFavorite(favorite: ProcedureFavoriteEntity)

    @Query("SELECT lastUpdated FROM procedures ORDER BY lastUpdated DESC LIMIT 1")
    fun getProceduresLastUpdated(): Long?
}