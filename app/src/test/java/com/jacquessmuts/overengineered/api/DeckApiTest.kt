package com.jacquessmuts.overengineered.api

import com.jacquessmuts.overengineered.api.DeckApi.Companion.buildOkHttpClient
import com.jacquessmuts.overengineered.model.Card
import com.jacquessmuts.overengineered.model.Deck
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import okhttp3.mock.ClasspathResources.resource
import okhttp3.mock.HttpCode.HTTP_400_BAD_REQUEST
import okhttp3.mock.MockInterceptor
import okhttp3.mock.get
import okhttp3.mock.matches
import okhttp3.mock.or
import okhttp3.mock.post
import okhttp3.mock.put
import okhttp3.mock.rule
import okhttp3.mock.url
import org.junit.jupiter.api.Assertions.assertEquals

@ExperimentalCoroutinesApi
internal class DeckApiTest {

    var deckApi: DeckApi? = null

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        deckApi = null
    }

    @org.junit.jupiter.api.Test
    fun `get deck failure handled`() = runBlocking {
        deckApi = DeckApi(buildOkHttpClient(badInterceptor()))

        assertEquals(deckApi!!.getDeck(), null)
    }

    @org.junit.jupiter.api.Test
    fun `deck successfully parsed`() = runBlocking {
        deckApi = DeckApi(buildOkHttpClient(goodInterceptor("api/deck.json")))

        assertEquals(deckApi!!.getDeck(), Deck(
            true,
            "3p40paa87x90",
            listOf(),
            true,
            52
        ))
    }

    @org.junit.jupiter.api.Test
    fun `draw card failure handled`() = runBlocking {
        deckApi = DeckApi(buildOkHttpClient(badInterceptor()))

        assertEquals(deckApi!!.drawCard(""), null)
    }

    @org.junit.jupiter.api.Test
    fun `draw card successfully parsed`() = runBlocking {
        deckApi = DeckApi(buildOkHttpClient(goodInterceptor("api/deck_with_cards.json")))

        assertEquals(deckApi!!.drawCard("3p40paa87x90"), Deck(
            true,
            "3p40paa87x90",
            listOf(Card(
                "aa",
                "https://deckofcardsapi.com/static/img/KH.png",
                "KING",
                "HEARTS",
                "KH"
            )),
            null,
            50
        ))
    }

    companion object {

        fun goodInterceptor(responseFileName: String): MockInterceptor {
            return MockInterceptor().apply {
                rule(get or post or put, url matches ".*".toRegex()) {
                    respond(resource(responseFileName))
                }
            }
        }

        fun badInterceptor(): MockInterceptor {
            return MockInterceptor().apply {
                rule(get or post or put, url matches ".*".toRegex()) {
                    respond(HTTP_400_BAD_REQUEST)
                }
            }
        }
    }
}
