package com.gemasr.surgeonwizard.data.local.database

import androidx.room.TypeConverter
import com.gemasr.surgeonwizard.data.local.model.Phase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromPhaseList(value: List<Phase>): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun toPhaseList(value: String): List<Phase> {
        val listType = object : TypeToken<List<Phase>>() {}.type
        return Gson().fromJson(value, listType)
    }
}