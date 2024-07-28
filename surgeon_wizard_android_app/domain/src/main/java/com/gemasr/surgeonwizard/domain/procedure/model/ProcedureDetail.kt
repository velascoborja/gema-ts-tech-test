package com.gemasr.surgeonwizard.domain.procedure.model

import java.time.ZonedDateTime

data class ProcedureDetail(
    val id: String,
    val name: String,
    val phases: List<Phase>,
    val cardUrl: String,
    val datePublished: ZonedDateTime,
    val durationInMinutes: Int,
    val isFavorite: Boolean
)

data class Phase(
    val id: String,
    val name: String,
    val iconUrl: String
)
