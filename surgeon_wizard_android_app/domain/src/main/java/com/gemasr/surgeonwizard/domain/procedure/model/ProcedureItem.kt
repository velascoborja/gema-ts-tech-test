package com.gemasr.surgeonwizard.domain.procedure.model

import java.util.Date

class ProcedureItem(
    val id: String,
    val iconUrl: String,
    val name: String,
    val duration: Int,
    val isFavorite: Boolean
)