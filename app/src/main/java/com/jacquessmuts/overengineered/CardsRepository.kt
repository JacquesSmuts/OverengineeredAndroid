package com.jacquessmuts.overengineered

import com.jacquessmuts.overengineered.api.DeckApi
import com.jacquessmuts.overengineered.api.onFailure
import com.jacquessmuts.overengineered.api.onSuccess
import com.jacquessmuts.overengineered.coroutines.DefaultCoroutineScope
import com.jacquessmuts.overengineered.db.DeckDb
import com.jacquessmuts.overengineered.model.Deck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.UnstableDefault
import timber.log.Timber

@UnstableDefault
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
            deckApi.getDeck().onSuccess {
                Timber.i("Success. Inserting $it")
                deckDb.insertNewDeck(it)
            }.onFailure {
                Timber.e(it)
            }
        }
    }

    fun drawCard(number: Int = 1) {
        require(number in 1..52)

        launch {
            latestDeck?.let { oldDeck ->
                deckApi.drawCard(oldDeck.id)
                    .onSuccess {
                        deckDb.insertNewDeck(oldDeck.copy(cards = it.cards.reversed()))
                    }.onFailure {
                        Timber.e(it)
                    }
            } ?: TODO("Handle a 'no deck' error")
        }
    }
}
