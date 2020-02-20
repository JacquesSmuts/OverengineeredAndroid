package com.jacquessmuts.overengineered.model

import com.squareup.moshi.Json

data class Deck (
    val success: Boolean,
    @Json(name = "deck_id") val id: String,
    val cards: List<Card> = listOf(),
    val shuffled: Boolean?,
    val remaining: Int
)

data class Card (
    val image: String,
    val value: String,
    val suit: String,
    val code: String
)