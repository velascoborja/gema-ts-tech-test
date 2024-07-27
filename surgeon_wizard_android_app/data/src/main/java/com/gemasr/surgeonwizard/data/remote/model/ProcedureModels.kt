package com.gemasr.surgeonwizard.data.remote.model

import com.google.gson.annotations.SerializedName

data class ProcedureListItemApiModel(

    @SerializedName("uuid")
    val uuid: String,

    @SerializedName("icon")
    val icon: IconApiModel,

    @SerializedName("name")
    val name: String,

    @SerializedName("phases")
    val phases: List<String>,

    @SerializedName("date_published")
    val datePublished: String,

    @SerializedName("duration")
    val duration: Int
)

data class ProcedureDetailApiModel(

    @SerializedName("uuid")
    val uuid: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("phases")
    val phases: List<Phase>,

    @SerializedName("icon")
    val icon: IconApiModel,

    @SerializedName("card")
    val card: CardApiModel,

    @SerializedName("date_published")
    val datePublished: String,

    @SerializedName("duration")
    val duration: Int
)

data class Phase(

    @SerializedName("date_published")
    val uuid: String,

    @SerializedName("date_published")
    val name: String,

    @SerializedName("date_published")
    val icon: IconApiModel
)

data class IconApiModel(
    @SerializedName("url")
    val url: String
)


data class CardApiModel(
    @SerializedName("url")
    val url: String
)

