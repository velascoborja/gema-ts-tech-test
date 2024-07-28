package com.gemasr.surgeonwizard.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gemasr.surgeonwizard.data.local.entity.ProcedureDetailEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureFavoriteEntity

@Database(
    entities = [
        ProcedureEntity::class,
        ProcedureDetailEntity::class,
        ProcedureFavoriteEntity::class
    ],
    version = 1
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun procedureDao(): ProcedureDao
}