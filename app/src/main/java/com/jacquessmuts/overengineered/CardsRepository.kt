package com.jacquessmuts.overengineered

import com.jacquessmuts.overengineered.api.DeckApi
import com.jacquessmuts.overengineered.coroutines.DefaultCoroutineScope
import com.jacquessmuts.overengineered.db.DeckDb
import com.jacquessmuts.overengineered.model.Card
import com.jacquessmuts.overengineered.model.Deck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@FlowPreview
class CardsRepository(
    private val deckApi: DeckApi,
    private val deckDb: DeckDb
) : CoroutineScope by DefaultCoroutineScope() {

    var latestDeck: Deck? = null

    init {
        updateDeck()
    }

    val deck = deckDb.topDeck.map {
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
                    val oldCards = latestDeck?.cards ?: listOf()
                    val nuCards: List<Card> = it.cards.plus(oldCards)
                    deckDb.insertNewDeck(it.copy(cards = nuCards))
                }
            }
        }
    }
}
