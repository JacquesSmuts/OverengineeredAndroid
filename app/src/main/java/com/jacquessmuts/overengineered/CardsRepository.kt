package com.jacquessmuts.overengineered

import android.util.Log
import com.jacquessmuts.overengineered.api.API
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

class CardsRepository(private val api: API): CoroutineScope by MainScope() {


    val deck = flow {
        val deck = api.getDeck()
        deck?.let { emit(it) }
    }
}