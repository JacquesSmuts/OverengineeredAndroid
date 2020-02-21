package com.jacquessmuts.overengineered.db

import android.content.Context
import com.jacquessmuts.overengineered.Database
import com.jacquessmuts.overengineered.coroutines.DefaultCoroutineScope
import com.squareup.sqldelight.android.AndroidSqliteDriver
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneNotNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class DeckDb (
    context: Context
): CoroutineScope by DefaultCoroutineScope() {

    private val driver: SqlDriver = AndroidSqliteDriver(Database.Schema, context, "deck.db")
    private val queries = Database(driver).deckQueries

    /**
     * This returns the latest deck, and any changes. If there is no deck it hangs forever.
     */
    val topDeck: Flow<Deck> =
        queries.getTopDeck()
            .asFlow()
            .mapToOneNotNull()

    fun insertNewDeck(deck: com.jacquessmuts.overengineered.model.Deck) {
        launch {
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

}