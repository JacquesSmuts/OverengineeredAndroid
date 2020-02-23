package com.jacquessmuts.overengineered.db

import android.content.Context
import com.jacquessmuts.overengineered.Database
import com.jacquessmuts.overengineered.Generators.generateDeck
import com.squareup.sqldelight.Query
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToList
import com.squareup.sqldelight.runtime.coroutines.mapToOneNotNull
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

@InternalCoroutinesApi
@ExperimentalCoroutinesApi
@FlowPreview
internal class DeckDbTest {

    val randomDeck = generateDeck()

    private lateinit var deckDb: DeckDb

    @BeforeEach
    fun setup() {

        val context = mockk<Context>()

        val driver: SqlDriver = JdbcSqliteDriver(IN_MEMORY).apply {
            Database.Schema.create(this)
        }

        val queries = Database(driver).deckQueries

        deckDb = DeckDb(context, queries)
    }

    @Test
    fun `get same deck after insert`() = runBlockingTest {

        deckDb.insertNewDeck(randomDeck)

        assertEquals(randomDeck, deckDb.latestDeck)
    }
}