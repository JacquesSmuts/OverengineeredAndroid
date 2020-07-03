package com.jacquessmuts.overengineered.api

import com.jacquessmuts.overengineered.model.Deck
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.features.logging.LogLevel
import io.ktor.client.features.logging.Logger
import io.ktor.client.features.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.utils.buildHeaders
import kotlinx.serialization.UnstableDefault
import kotlinx.serialization.json.Json
import timber.log.Timber

@UnstableDefault
class DeckApi(private val client: HttpClient = buildHttpClient()) {

    suspend fun getDeck(): ApiResult<Deck> {
        return doApiCall(buildUrl("api/deck/new/shuffle/?deck_count=1"))
    }

    suspend fun drawCard(deckId: String, numberOfCards: Int = 1): ApiResult<Deck> {
        return doApiCall(buildUrl("api/deck/$deckId/draw/?count=$numberOfCards"))
    }

    private suspend inline fun <reified T : Any> doApiCall(url: String): ApiResult<T> {
        return try {
            Success(client.get(url))
        } catch (exception: Exception) {
            Failure(exception)
        }
    }

    companion object {

        fun buildUrl(suffix: String): String {
            return "https://deckofcardsapi.com/$suffix"
        }

        @UnstableDefault
        fun buildHttpClient(): HttpClient = HttpClient {
            buildHeaders {
                append("Content-Type", "application/json")
                append("User-Agent", System.getProperty("http.agent") ?: "Android")
            }
            install(JsonFeature) {
                serializer = defaultSerializer
            }
            install(Logging) {
                logger = TimberHttpLogger()
                level = LogLevel.BODY
            }
        }

        val defaultSerializer: KotlinxSerializer = KotlinxSerializer(Json {
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
}

class TimberHttpLogger() : Logger {
    override fun log(message: String) {
        Timber.d(message)
    }
}
