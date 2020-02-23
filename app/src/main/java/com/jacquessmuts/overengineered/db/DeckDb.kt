package com.jacquessmuts.overengineered.db

import android.content.Context
import com.jacquessmuts.overengineered.Database
import com.jacquessmuts.overengineered.coroutines.DefaultCoroutineScope
import com.jacquessmuts.overengineered.model.Card
import com.jacquessmuts.overengineered.model.Deck
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneNotNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@FlowPreview
@ExperimentalCoroutinesApi
class DeckDb(
    context: Context,
    private val queries: DeckQueries = Database(AndroidSqliteDriver(Database.Schema, context, "deck.db")).deckQueries
) : CoroutineScope by DefaultCoroutineScope() {

    /**
     * This returns the latest deck, and any changes. If there is no deck it hangs forever.
     */
    val topDeck: Flow<Deck> =
        queries.getTopDeck()
            .asFlow()
            .mapToOneNotNull()
            .map {
                it.toDeck()
                    .copy(cards = queries.getCards(it.id)
                    .executeAsList()
                    .map { it.toCard() })
            }

    val latestDeck: Deck
        get() = queries.getTopDeck().executeAsOne().toDeck().run {
            this.copy(cards = queries.getCards(this.id).executeAsList().map { it.toCard() })
        }

    fun insertNewDeck(deck: Deck) {

        queries.clearDeck()
        queries.clearCards()
        queries.insertDeck(
            success = deck.success,
            id = deck.id,
            shuffled = deck.shuffled,
            remaining = deck.remaining
        )
        deck.cards.forEach { card ->

            queries.insertCard(
                id = card.id,
                deck_id = deck.id,
                image = card.image,
                value = card.value,
                suit = card.suit,
                code = card.code
            )
        }
    }
}

private fun CardEntity.toCard(): Card {
    return Card(
        id = this.id,
        image = this.image ?: "",
        value = this.value ?: "",
        suit = this.suit ?: "",
        code = this.code ?: ""
    )
}

fun DeckEntity.toDeck(): Deck {

    return Deck(
        this.success ?: false,
        this.id,
        listOf(),
        this.shuffled ?: false,
        this.remaining ?: 0
    )
}
