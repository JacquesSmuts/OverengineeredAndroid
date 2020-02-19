package com.jacquessmuts.overengineered.model

import com.squareup.moshi.Json
import retrofit2.http.Field

data class Deck (
    val success: Boolean,
    @Json(name = "deck_id") val id: String,
    val shuffled: Boolean,
    val remaining: Int
)