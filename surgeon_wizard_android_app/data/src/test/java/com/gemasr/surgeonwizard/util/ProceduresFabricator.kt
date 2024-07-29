package com.gemasr.surgeonwizard.util

import com.gemasr.surgeonwizard.data.local.entity.ProcedureDetailEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureDetailWithFavorite
import com.gemasr.surgeonwizard.data.local.entity.ProcedureEntity
import com.gemasr.surgeonwizard.data.local.entity.ProcedureWithFavorite
import com.gemasr.surgeonwizard.data.remote.model.CardApiModel
import com.gemasr.surgeonwizard.data.remote.model.IconApiModel
import com.gemasr.surgeonwizard.data.remote.model.PhaseApiModel
import com.gemasr.surgeonwizard.data.remote.model.ProcedureDetailApiModel
import com.gemasr.surgeonwizard.data.remote.model.ProcedureListItemApiModel
import com.gemasr.surgeonwizard.domain.procedure.model.Phase
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureDetail
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureItem
import com.google.gson.Gson
import java.time.LocalDate

class ProceduresFabricator {
    companion object {
        private const val FIXED_DATE_STRING = "2015-04-14T10:00:51.978180"
        private val FIXED_DATE = LocalDate.of(2015, 4, 14)
        private const val FIXED_TIMESTAMP = 1710498600000L
        private val gson = Gson()

        fun createProcedureListItemApiModel(
            uuid: String = "proc-001",
            name: String = "Annual Health Checkup",
            iconUrl: String = "https://example.com/icons/health-checkup.png",
            duration: Int = 90,
            phasesCount: Int = 4,
            datePublished: String = FIXED_DATE_STRING,
        ) = ProcedureListItemApiModel(
            uuid = uuid,
            name = name,
            icon = IconApiModel(url = iconUrl),
            duration = duration,
            phases = List(phasesCount) { "phase-$it" },
            datePublished = datePublished,
        )

        fun createProcedureEntity(
            uuid: String = "proc-001",
            name: String = "Annual Health Checkup",
            iconUrl: String = "https://example.com/icons/health-checkup.png",
            duration: Int = 90,
            phaseCount: Int = 4,
            lastUpdated: Long = FIXED_TIMESTAMP,
        ) = ProcedureEntity(uuid, name, iconUrl, phaseCount, duration, lastUpdated)

        fun createProcedureWithFavorite(procedure: ProcedureEntity = createProcedureEntity(), isFavorite: Boolean = true) =
            ProcedureWithFavorite(procedure, isFavorite)

        fun createProcedureDetailApiModel(
            uuid: String = "proc-001",
            name: String = "Annual Health Checkup",
            cardUrl: String = "https://example.com/cards/health-checkup.png",
            duration: Int = 90,
            phasesCount: Int = 4,
            datePublished: String = FIXED_DATE_STRING,
        ) = ProcedureDetailApiModel(
            uuid = uuid,
            name = name,
            card = CardApiModel(url = cardUrl),
            phases = List(phasesCount) { createPhaseApiModel(uuid = "phase-$it") },
            datePublished = datePublished,
            duration = duration,
        )

        fun createProcedureDetailEntity(
            uuid: String = "proc-001",
            name: String = "Annual Health Checkup",
            cardUrl: String = "https://example.com/cards/health-checkup.png",
            duration: Int = 90,
            phasesCount: Int = 4,
            datePublished: String = FIXED_DATE_STRING,
        ): ProcedureDetailEntity {
            val phases = List(phasesCount) { createPhaseApiModel(uuid = "phase-$it") }
            return ProcedureDetailEntity(
                uuid = uuid,
                name = name,
                cardUrl = cardUrl,
                phases = gson.toJson(phases),
                datePublished = datePublished,
                duration = duration,
            )
        }

        fun createPhaseApiModel(
            uuid: String = "phase-001",
            name: String = "Initial Assessment",
            iconUrl: String = "https://example.com/icons/assessment.png",
        ) = PhaseApiModel(
            uuid = uuid,
            name = name,
            icon = IconApiModel(url = iconUrl),
        )

        fun createPhaseEntity(
            uuid: String = "phase-001",
            name: String = "Initial Assessment",
            iconUrl: String = "https://example.com/icons/assessment.png",
        ) = Phase(uuid, name, iconUrl)

        fun createProcedureItem(
            id: String = "proc-001",
            name: String = "Annual Health Checkup",
            iconUrl: String = "https://example.com/icons/health-checkup.png",
            duration: Int = 90,
            phaseCount: Int = 4,
            isFavorite: Boolean = true,
        ) = ProcedureItem(id, name, iconUrl, duration, phaseCount, isFavorite)

        fun createProcedureDetail(
            id: String = "proc-001",
            name: String = "Annual Health Checkup",
            cardUrl: String = "https://example.com/cards/health-checkup.png",
            phasesCount: Int = 4,
            datePublished: LocalDate = FIXED_DATE,
            durationInMinutes: Int = 90,
            isFavorite: Boolean = true,
        ) = ProcedureDetail(
            id = id,
            name = name,
            cardUrl = cardUrl,
            phases = List(phasesCount) { createPhase(id = "phase-$it") },
            datePublished = datePublished,
            durationInMinutes = durationInMinutes,
            isFavorite = isFavorite,
        )

        fun createPhase(
            id: String = "phase-001",
            name: String = "Initial Assessment",
            iconUrl: String = "https://example.com/icons/assessment.png",
        ) = Phase(id, name, iconUrl)

        fun createProcedureDetailWithFavorite(
            procedureDetail: ProcedureDetailEntity = createProcedureDetailEntity(),
            isFavorite: Boolean = true,
        ) = ProcedureDetailWithFavorite(procedureDetail, isFavorite)

        fun createIconApiModel(url: String = "https://example.com/icons/default.png") = IconApiModel(url)

        fun createCardApiModel(url: String = "https://example.com/cards/default.png") = CardApiModel(url)
    }
}