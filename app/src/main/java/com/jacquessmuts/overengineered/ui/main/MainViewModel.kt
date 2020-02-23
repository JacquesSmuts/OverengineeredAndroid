package com.jacquessmuts.overengineered.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jacquessmuts.overengineered.CardsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@FlowPreview
@ExperimentalCoroutinesApi
class MainViewModel(val cardsRepo: CardsRepository) : ViewModel() {

    private val _state = BroadcastChannel<MainState>(1)
    val state = _state.asFlow()

    init {
        listenToRepo()
    }

    private fun listenToRepo() {
        viewModelScope.launch {
            cardsRepo.deck.collect { deck ->
                _state.send(MainState(deck.toString()))
            }
        }
    }

    fun buttonClicked() {
        cardsRepo.drawCard()
    }
}

data class MainState(val text: String)
