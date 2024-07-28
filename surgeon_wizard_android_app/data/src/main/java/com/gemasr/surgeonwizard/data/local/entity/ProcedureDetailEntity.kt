package com.gemasr.surgeonwizard.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "procedure_details")
data class ProcedureDetailEntity(
    @PrimaryKey
    @ColumnInfo(name = "uuid") val uuid: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "cardUrl") val cardUrl: String,
    @ColumnInfo(name = "phases") val phases: String,
    @ColumnInfo(name = "datePublished") val datePublished: String,
    @ColumnInfo(name = "duration") val duration: Int
)

data class ProcedureDetailWithFavorite(
    @Embedded val procedureDetail: ProcedureDetailEntity,
    @ColumnInfo(name = "isFavorite") val isFavorite: Boolean
)
