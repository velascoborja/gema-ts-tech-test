package com.gemasr.surgeonwizard.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "procedures")
data class ProcedureEntity(
    @PrimaryKey @ColumnInfo(name = "uuid") val uuid: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "iconUrl") val iconUrl: String,
    @ColumnInfo(name = "phaseCount") val phaseCount: Int,
    @ColumnInfo(name = "duration") val duration: Int,
    @ColumnInfo(name = "lastUpdated") val lastUpdated: Long,
)

data class ProcedureWithFavorite(
    @Embedded val procedure: ProcedureEntity,
    @ColumnInfo(name = "isFavorite") val isFavorite: Boolean,
)