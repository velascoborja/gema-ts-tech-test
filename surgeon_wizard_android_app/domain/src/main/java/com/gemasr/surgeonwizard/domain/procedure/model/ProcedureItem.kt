package com.gemasr.surgeonwizard.domain.procedure.model

data class ProcedureItem(
    val id: String,
    val iconUrl: String,
    val name: String,
    val duration: Int,
    val phaseCount: Int,
    val isFavorite: Boolean,
)