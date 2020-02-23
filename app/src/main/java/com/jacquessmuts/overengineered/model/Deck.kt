package com.jacquessmuts.overengineered.model

import com.squareup.moshi.Json
import java.util.UUID

data class Deck(
    val success: Boolean,
    @Json(name = "deck_id") val id: String,
    val cards: List<Card> = listOf(),
    val shuffled: Boolean?,
    val remaining: Int
)

data class Card(
    val id: String = UUID.randomUUID().toString(),
    val image: String,
    val value: String,
    val suit: String,
    val code: String
)
