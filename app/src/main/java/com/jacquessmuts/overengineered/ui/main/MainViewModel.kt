package com.jacquessmuts.overengineered.ui.main

import androidx.lifecycle.viewModelScope
import com.jacquessmuts.overengineered.CardsRepository
import com.jacquessmuts.overengineered.model.Card
import com.jacquessmuts.overengineered.ui.BaseState
import com.jacquessmuts.overengineered.ui.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModel(val cardsRepo: CardsRepository) : BaseViewModel<MainState>() {

    init {
        listenToRepo()
    }

    private fun listenToRepo() {
        cardsRepo.deck.onEach { deck ->
            updateState(MainState(text = deck.toString(),
                cardsRemaining = deck.remaining,
                shouldShowCards = deck.cards.isNotEmpty(),
                cards = deck.cards))
        }.launchIn(viewModelScope)
    }

    fun buttonClicked() {
        cardsRepo.drawCard()
    }

    fun deckClicked() {
        cardsRepo.updateDeck()
    }
}

data class MainState(
    val text: String,
    val cardsRemaining: Int,
    val shouldShowCards: Boolean,
    val cards: List<Card>
) : BaseState()
