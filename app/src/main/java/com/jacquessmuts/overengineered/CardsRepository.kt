package com.jacquessmuts.overengineered

import android.util.Log
import com.jacquessmuts.overengineered.api.DeckOfCardsService
import com.jacquessmuts.overengineered.model.Deck
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.flow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class CardsRepository(): CoroutineScope by MainScope() {

    val okHttpClient: OkHttpClient by lazy {
        val interceptor = HttpLoggingInterceptor()

        if (BuildConfig.DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.NONE
        }

        OkHttpClient.Builder()
            .addInterceptor { chain: Interceptor.Chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Content-Type", "application/json")
                    .addHeader("User-Agent", System.getProperty("http.agent") ?: "Android")
                    .build()

                Timber.d("Headers ${request.headers()}")
                chain.proceed(request)
            }
            .addInterceptor(interceptor)
            .readTimeout(
                30,
                TimeUnit.SECONDS
            )
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }

    val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    val retrofit =
        Retrofit.Builder()
            .baseUrl("https://deckofcardsapi.com/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()


    val deckService by lazy {
        retrofit.create<DeckOfCardsService>(DeckOfCardsService::class.java)
    }

    val deck = flow {
        val deck = deckService.getNewDeck()
        deck?.let { emit(it) }
    }
}