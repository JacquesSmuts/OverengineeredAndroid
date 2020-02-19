package com.jacquessmuts.overengineered.api

import com.jacquessmuts.overengineered.model.Deck
import retrofit2.http.GET

interface DeckOfCardsService {

    @GET("api/deck/new/shuffle/?deck_count=1")
    suspend fun getNewDeck(): Deck?

}