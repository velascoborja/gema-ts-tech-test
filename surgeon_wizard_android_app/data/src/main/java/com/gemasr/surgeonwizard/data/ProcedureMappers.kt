package com.gemasr.surgeonwizard.data

import com.gemasr.surgeonwizard.data.local.entity.ProcedureDetailEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureDetailWithFavorite
import com.gemasr.surgeonwizard.data.local.entity.ProcedureEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureWithFavorite
import com.gemasr.surgeonwizard.data.local.model.Phase as PhaseEntity
import com.gemasr.surgeonwizard.data.remote.model.PhaseApiModel
import com.gemasr.surgeonwizard.data.remote.model.ProcedureDetailApiModel
import com.gemasr.surgeonwizard.data.remote.model.ProcedureListItemApiModel
import com.gemasr.surgeonwizard.domain.procedure.model.Phase
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureDetail
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.ZonedDateTime

object ProcedureMappers {

    fun ProcedureListItemApiModel.toProcedureEntity() = ProcedureEntity(
        uuid = uuid,
        name = name,
        iconUrl = icon.url,
        duration = duration,
        phaseCount = phases.size,
        lastUpdated = System.currentTimeMillis()
    )

    fun ProcedureDetailApiModel.toProcedureDetailEntity(gson: Gson) = ProcedureDetailEntity(
        uuid = uuid,
        name = name,
        cardUrl = card.url,
        phases = gson.toJson(phases),
        datePublished = datePublished,
        duration = duration
    )

    fun PhaseApiModel.toPhaseEntity() = PhaseEntity(
        uuid = uuid,
        name = name,
        iconUrl = icon.url
    )

    fun List<ProcedureWithFavorite>.toProcedureItems(): List<ProcedureItem> =
        map { it.toProcedureItem() }

    fun ProcedureWithFavorite.toProcedureItem() = with(procedure) {
        ProcedureItem(
            id = uuid,
            name = name,
            iconUrl = iconUrl,
            duration = duration,
            isFavorite = isFavorite
        )
    }

    fun ProcedureDetailWithFavorite.toProcedureDetail(gson: Gson): ProcedureDetail = with(procedureDetail) {
        val phasesList: List<Phase> = gson.fromJson(phases, object : TypeToken<List<Phase>>() {}.type)
        return ProcedureDetail(
            id = uuid,
            name = name,
            cardUrl = cardUrl,
            phases = phasesList,
            datePublished = ZonedDateTime.parse(datePublished),
            durationInMinutes = duration,
            isFavorite = isFavorite
        )
    }

    fun ProcedureDetailEntity.toProcedureDetailWithDefaultFavorite(gson: Gson): ProcedureDetail {
        val phasesList: List<PhaseApiModel> = gson.fromJson(phases, object : TypeToken<List<PhaseApiModel>>() {}.type)
        return ProcedureDetail(
            id = uuid,
            name = name,
            cardUrl = cardUrl,
            phases = phasesList.map { it.toPhase() },
            datePublished = ZonedDateTime.parse(datePublished),
            durationInMinutes = duration,
            isFavorite = false
        )
    }

    fun PhaseApiModel.toPhase() = Phase(
        id = uuid,
        name = name,
        iconUrl = icon.url
    )
}