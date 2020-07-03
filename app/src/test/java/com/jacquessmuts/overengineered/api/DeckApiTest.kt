package com.jacquessmuts.overengineered.api

import com.jacquessmuts.overengineered.api.ClasspathResources.resource
import com.jacquessmuts.overengineered.api.DeckApi.Companion.defaultSerializer
import com.jacquessmuts.overengineered.model.Card
import com.jacquessmuts.overengineered.model.Deck
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.JsonFeature
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.core.ExperimentalIoApi
import io.ktor.utils.io.jvm.javaio.toByteReadChannel
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.UnstableDefault
import org.junit.jupiter.api.Assertions.assertEquals
import java.io.InputStream

@UnstableDefault
@ExperimentalIoApi
internal class DeckApiTest {

    var deckApi: DeckApi? = null

    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        deckApi = null
    }

    @org.junit.jupiter.api.Test
    fun `deck successfully parsed`() = runBlocking {
        deckApi = DeckApi(mockHttpClient("api/deck.json"))

        assertEquals(Success(Deck(
            true,
            "3p40paa87x90",
            listOf(),
            true,
            52
        )), deckApi!!.getDeck())
    }

    @org.junit.jupiter.api.Test
    fun `draw card successfully parsed`() = runBlocking {
        deckApi = DeckApi(mockHttpClient("api/deck_with_cards.json"))

        assertEquals(Success(Deck(
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
        )), deckApi!!.drawCard("3p40paa87x90"))
    }

    companion object {
        /**
         * Builds a fake http client that responds with the given file's contents:
         *
         * @see <a href="https://ktor.io/clients/http-client/testing.html">Official Testing doc for Ktor</a>
         */
        @ExperimentalIoApi
        fun mockHttpClient(responseFileName: String? = null): HttpClient = HttpClient(MockEngine) {
            engine {
                addHandler { request ->
                    responseFileName?.let {
                        respond(
                            content =resource(it).toByteReadChannel(),
                            headers = headersOf("Content-Type", "application/json"),
                            status = HttpStatusCode.OK
                        )
                    } ?: error("404 or something")
                }
            }
            install(JsonFeature) {
                serializer = defaultSerializer
            }
        }
    }
}

/**
 * A helper class to provide responses from classpath sources
 */
object ClasspathResources {
    /**
     * Loads the content from the given classpath resource
     *
     * @param name the name of the resource
     * @return the content as an [InputStream]
     */
    fun resource(name: String): InputStream {
        return resource(
            Thread.currentThread().contextClassLoader!!,
            name
        )
    }

    /**
     * Loads the content from the given classpath resource
     *
     * @param classLoader the base classloader
     * @param name        the name of the resource
     * @return the content as an [InputStream]
     */
    fun resource(classLoader: ClassLoader, name: String): InputStream {
        return classLoader.getResourceAsStream(name)
    }
}