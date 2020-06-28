package com.jacquessmuts.overengineered.model

import androidx.annotation.Keep
import java.util.UUID
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Deck(
    val success: Boolean,
    @SerialName("deck_id") val id: String,
    val cards: List<Card> = listOf(),
    val shuffled: Boolean? = null,
    val remaining: Int
)

@Keep
@Serializable
data class Card(
    val id: String = UUID.randomUUID().toString(),
    val image: String,
    val value: String,
    val suit: String,
    val code: String
)
