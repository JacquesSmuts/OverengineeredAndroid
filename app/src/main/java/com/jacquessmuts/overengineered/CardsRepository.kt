package com.jacquessmuts.overengineered

import com.jacquessmuts.overengineered.api.DeckApi
import com.jacquessmuts.overengineered.db.DeckDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class CardsRepository(
    private val deckApi: DeckApi,
    private val deckDb: DeckDb
): CoroutineScope by MainScope() {

    init{
        launch {
            deckApi.getDeck()?.let { deckDb.insertNewDeck(it) }
        }
    }

    val deck = deckDb.topDeck
}