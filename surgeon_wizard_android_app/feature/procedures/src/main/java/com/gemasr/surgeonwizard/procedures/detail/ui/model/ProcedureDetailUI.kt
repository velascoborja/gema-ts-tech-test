package com.gemasr.surgeonwizard.procedures.detail.ui.model

import com.gemasr.surgeonwizard.domain.procedure.model.Phase
import com.gemasr.surgeonwizard.domain.procedure.model.ProcedureDetail
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ProcedureDetailUI(
    val id: String,
    val name: String,
    val phases: List<PhaseUI>,
    val cardImageUrl: String,
    val datePublished: String,
    val durationInMinutes: Int,
    val isFavorite: Boolean,
) {
    data class PhaseUI(
        val id: String,
        val name: String,
        val iconUrl: String,
    )
}

fun ProcedureDetail.toUI(): ProcedureDetailUI {
    return ProcedureDetailUI(
        id = id,
        name = name,
        phases = phases.toUI(),
        cardImageUrl = cardUrl,
        datePublished = datePublished.formatDatePublished(),
        durationInMinutes = durationInMinutes,
        isFavorite = isFavorite,
    )
}

fun List<Phase>.toUI(): List<ProcedureDetailUI.PhaseUI> {
    return map { it.toUI() }
}

fun Phase.toUI(): ProcedureDetailUI.PhaseUI {
    return ProcedureDetailUI.PhaseUI(
        id = id,
        name = name,
        iconUrl = iconUrl,
    )
}

fun LocalDate.formatDatePublished(): String {
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
    return formatter.format(this)
}