package com.jacquessmuts.overengineered.api

import com.jacquessmuts.overengineered.model.Deck
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface DeckOfCardsService {

    @GET("api/deck/new/shuffle/?deck_count=1")
    suspend fun getNewDeck(): Deck?

    @GET("api/deck/{deck_id}/draw/?count=2")
    suspend fun drawCards(
        @Path("deck_id") deckId: String,
        @Query("count") numberOfCards: Int = 1
    ): Deck?


}