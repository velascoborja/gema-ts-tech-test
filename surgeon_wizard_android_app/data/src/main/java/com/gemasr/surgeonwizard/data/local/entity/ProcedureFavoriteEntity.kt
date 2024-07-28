package com.gemasr.surgeonwizard.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "procedure_favorites")
data class ProcedureFavoriteEntity(
    @PrimaryKey val procedureId: String,
    val isFavorite: Boolean
)