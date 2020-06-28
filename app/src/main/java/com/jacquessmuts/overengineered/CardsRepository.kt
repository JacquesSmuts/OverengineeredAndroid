package com.jacquessmuts.overengineered

import com.jacquessmuts.overengineered.api.ApiResult
import com.jacquessmuts.overengineered.api.DeckApi
import com.jacquessmuts.overengineered.coroutines.DefaultCoroutineScope
import com.jacquessmuts.overengineered.db.DeckDb
import com.jacquessmuts.overengineered.model.Deck
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber

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
            val result = deckApi.getDeck()

            if (result is ApiResult.Success) {
                Timber.i("Success. Inserting ${result.data}")
                deckDb.insertNewDeck(result.data)
            } else if (result is ApiResult.Error) {
                Timber.e(result.exception)
            }
        }
    }

    fun drawCard(number: Int = 1) {
        require(number in 1..52)

        launch {
            latestDeck?.let { oldDeck ->
                val result = deckApi.drawCard(oldDeck.id)

                if (result is ApiResult.Success) {
                    deckDb.insertNewDeck(oldDeck.copy(cards = result.data.cards.reversed()))
                } else if (result is ApiResult.Error) {
                    Timber.e(result.exception)
                }
            } ?: TODO("Handle a 'no deck' error")
        }
    }
}
