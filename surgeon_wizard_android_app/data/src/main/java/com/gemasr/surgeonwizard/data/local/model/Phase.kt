package com.gemasr.surgeonwizard.data.local.model

data class Phase(
    val uuid: String,
    val name: String,
    val icon: Icon,
)

data class Icon(
    val url: String,
)