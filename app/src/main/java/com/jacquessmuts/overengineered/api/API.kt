package com.jacquessmuts.overengineered.api

import com.jacquessmuts.overengineered.BuildConfig
import com.jacquessmuts.overengineered.model.Deck
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber
import java.util.concurrent.TimeUnit

class API(okHttpClient: OkHttpClient = buildOkHttpClient()) {

    private val retrofit =
        Retrofit.Builder()
            .baseUrl("https://deckofcardsapi.com/")
            .addConverterFactory(MoshiConverterFactory.create(Moshi
                .Builder()
                .add(KotlinJsonAdapterFactory())
                .build()))
            .client(okHttpClient)
            .build()


    private val deckService by lazy {
        retrofit.create<DeckOfCardsService>(DeckOfCardsService::class.java)
    }

    suspend fun getDeck(): Deck? {
        return try {
            deckService.getNewDeck()
        } catch (exception: HttpException) {
            Timber.e(exception)
            null
        }
    }

    suspend fun drawCard(deckId: String): Deck? {
        return try {
            deckService.drawCards(deckId)
        } catch (exception: HttpException) {
            Timber.e(exception)
            null
        }
    }

    companion object {

        fun buildOkHttpClient(additionalInterceptor: Interceptor? = null): OkHttpClient {
            val interceptor = HttpLoggingInterceptor().apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }

            val builder = OkHttpClient.Builder()

            if (additionalInterceptor != null) {
                builder.addInterceptor(additionalInterceptor)
            }

            return builder
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
    }
}