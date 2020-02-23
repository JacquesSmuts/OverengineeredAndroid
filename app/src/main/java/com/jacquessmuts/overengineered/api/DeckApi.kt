package com.jacquessmuts.overengineered.api

import com.jacquessmuts.overengineered.BuildConfig
import com.jacquessmuts.overengineered.model.Deck
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import java.net.UnknownHostException
import java.util.concurrent.TimeUnit
import kotlin.reflect.KSuspendFunction0
import kotlin.reflect.KSuspendFunction2
import kotlin.reflect.full.callSuspend
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import timber.log.Timber

class DeckApi(okHttpClient: OkHttpClient = buildOkHttpClient()) {

    private val deckService by lazy {
        buildRetroFit(okHttpClient).create(DeckOfCardsService::class.java)
    }

    suspend fun getDeck(): Deck? = doApiCall(deckService::getNewDeck)

    suspend fun drawCard(deckId: String, numberOfCards: Int = 1): Deck? =
            doApiCall(deckService::drawCards, deckId, numberOfCards)

    // TODO: turn into response class
    private suspend fun <T> doApiCall(apiCall: KSuspendFunction0<T>): T? {
        return try {
            apiCall.callSuspend()
        } catch (exception: HttpException) {
            Timber.e(exception)
            null
        } catch (exception: UnknownHostException) {
            Timber.e(exception)
            null
        }
    }

    // TODO: turn into response class
    private suspend fun <Input1, Input2, Output> doApiCall(
        apiCall: KSuspendFunction2<Input1, Input2, Output>,
        input1: Input1,
        input2: Input2? = null
    ): Output? {

        return try {
            apiCall.callSuspend(input1, input2)
        } catch (exception: HttpException) {
            Timber.e(exception)
            null
        } catch (exception: UnknownHostException) {
            Timber.e(exception)
            null
        }
    }

    companion object {

        fun buildRetroFit(okHttpClient: OkHttpClient): Retrofit {
                return Retrofit.Builder()
                    .baseUrl("https://deckofcardsapi.com/")
                    .addConverterFactory(MoshiConverterFactory.create(Moshi
                        .Builder()
                        .add(KotlinJsonAdapterFactory())
                        .build()))
                    .client(okHttpClient)
                    .build()
        }

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
