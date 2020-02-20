package com.jacquessmuts.overengineered.api

import com.jacquessmuts.overengineered.api.API.Companion.buildOkHttpClient
import com.jacquessmuts.overengineered.model.Deck
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineContext
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.withTestContext
import okhttp3.mock.*
import okhttp3.mock.ClasspathResources.resource
import okhttp3.mock.HttpCode.HTTP_400_BAD_REQUEST
import okhttp3.mock.HttpCode.HTTP_401_UNAUTHORIZED
import okhttp3.mock.MediaTypes.MEDIATYPE_JSON

@ExperimentalCoroutinesApi
internal class APITest {

    var api: API? = null


    @org.junit.jupiter.api.AfterEach
    fun tearDown() {
        api = null
    }

    @org.junit.jupiter.api.Test
    fun getDeckFail() = runBlocking {
        api = API(buildOkHttpClient(badInterceptor()))

        assertEquals(api!!.getDeck(), null)
    }

    @org.junit.jupiter.api.Test
    fun getDeckSuccess() = runBlocking {
        api = API(buildOkHttpClient(goodInterceptor("api/deck.json")))

        assertEquals(api!!.getDeck(), Deck(true, "3p40paa87x90", true, 52))
    }

    companion object {

        fun goodInterceptor(responseFileName: String): MockInterceptor {
            return MockInterceptor().apply {
                rule (get or post or put, url matches ".*".toRegex()) {
                    respond(resource(responseFileName))
                }
            }
        }

        fun badInterceptor(): MockInterceptor {
            return MockInterceptor().apply {
                rule (get or post or put, url matches ".*".toRegex()) {
                    respond(HTTP_400_BAD_REQUEST)
                }
            }
        }
    }
}