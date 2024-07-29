package com.gemasr.surgeonwizard.data

import com.gemasr.surgeonwizard.data.local.entity.ProcedureDetailEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureDetailWithFavorite
import com.gemasr.surgeonwizard.data.local.entity.ProcedureEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureWithFavorite
import com.gemasr.surgeonwizard.data.local.model.Phase as PhaseEntity
import com.gemasr.surgeonwizard.data.remote.model.ProcedureDetailApiModel
import com.gemasr.surgeonwizard.data.remote.model.ProcedureListItemApiModel
import com.gemasr.surgeonwizard.domain.procedure.model.Phase as PhaseDomain
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureDetail
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object ProcedureMappers {
    fun ProcedureListItemApiModel.toProcedureEntity() = ProcedureEntity(
        uuid = uuid,
        name = name,
        iconUrl = icon.url,
        duration = duration,
        phaseCount = phases.size,
        lastUpdated = System.currentTimeMillis(),
    )

    fun ProcedureDetailApiModel.toProcedureDetailEntity(gson: Gson) = ProcedureDetailEntity(
        uuid = uuid,
        name = name,
        cardUrl = card.url,
        phases = gson.toJson(phases),
        datePublished = datePublished,
        duration = duration,
    )

    fun List<ProcedureWithFavorite>.toProcedureItems(): List<ProcedureItem> = map { it.toProcedureItem() }

    fun ProcedureWithFavorite.toProcedureItem() = with(procedure) {
        ProcedureItem(
            id = uuid,
            name = name,
            iconUrl = iconUrl,
            duration = duration,
            isFavorite = isFavorite,
            phaseCount = phaseCount,
        )
    }

    fun ProcedureDetailWithFavorite.toProcedureDetail(gson: Gson): ProcedureDetail = with(procedureDetail) {
        val phasesList: List<PhaseEntity> = gson.fromJson(phases, object : TypeToken<List<PhaseEntity>>() {}.type)
        return ProcedureDetail(
            id = uuid,
            name = name,
            cardUrl = cardUrl,
            phases = phasesList.map { it.toPhase() },
            datePublished = datePublished.toLocalDate(),
            durationInMinutes = duration.toDurationInMinutes(),
            isFavorite = isFavorite,
        )
    }

    fun ProcedureDetailEntity.toProcedureDetailWithDefaultFavorite(gson: Gson): ProcedureDetail {
        val phasesList: List<PhaseEntity> = gson.fromJson(phases, object : TypeToken<List<PhaseEntity>>() {}.type)
        return ProcedureDetail(
            id = uuid,
            name = name,
            cardUrl = cardUrl,
            phases = phasesList.map { it.toPhase() },
            datePublished = datePublished.toLocalDate(),
            durationInMinutes = duration.toDurationInMinutes(),
            isFavorite = false,
        )
    }

    fun PhaseEntity.toPhase() = PhaseDomain(
        id = uuid,
        name = name,
        iconUrl = icon.url,
    )

    private fun String.toLocalDate(): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
        return LocalDate.parse(this, formatter)
    }

    private fun Int.toDurationInMinutes(): Int {
        return if (this > 0) {
            this / 60
        } else {
            0
        }
    }
}