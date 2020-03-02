package com.jacquessmuts.overengineered

import com.jacquessmuts.overengineered.api.DeckApi
import com.jacquessmuts.overengineered.coroutines.DefaultCoroutineScope
import com.jacquessmuts.overengineered.db.DeckDb
import com.jacquessmuts.overengineered.model.Card
import com.jacquessmuts.overengineered.model.Deck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CardsRepository(
    private val deckApi: DeckApi,
    private val deckDb: DeckDb
) : CoroutineScope by DefaultCoroutineScope() {

    var latestDeck: Deck? = null

    init {
        updateDeck()
    }

    val deck = deckDb.latestDeck.map {
        latestDeck = it
        it
    }

    fun updateDeck() {
        launch {
            deckApi.getDeck()?.let {
                deckDb.insertNewDeck(it)
            }
        }
    }

    fun drawCard(number: Int = 1) {
        require(number in 1..52)

        launch {
            latestDeck?.let {
                deckApi.drawCard(it.id)?.let {
                    deckDb.insertNewDeck(it.copy(cards = it.cards.reversed()))
                }
            }
        }
    }
}
