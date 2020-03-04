package com.jacquessmuts.overengineered.db

import android.content.Context
import com.jacquessmuts.overengineered.Database
import com.jacquessmuts.overengineered.Generators.generateDeck
import com.jacquessmuts.overengineered.test
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver.Companion.IN_MEMORY
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@ExperimentalCoroutinesApi
internal class DeckDbTest {

    val randomDeck = generateDeck()
    val randomDeck2 = generateDeck()

    private lateinit var deckDb: DeckDb

    @BeforeEach
    fun setup() {

        val context = mockk<Context>()

        val queries = Database(
            JdbcSqliteDriver(IN_MEMORY).apply { Database.Schema.create(this) }
        ).deckQueries

        deckDb = DeckDb(context, queries)
    }

    @Test
    fun `get same deck after insert`() {

        deckDb.insertNewDeck(randomDeck)
        assertEquals(randomDeck, deckDb.topDeck)

        deckDb.insertNewDeck(randomDeck2)
        assertEquals(randomDeck2, deckDb.topDeck)
    }

    @Test
    fun `get same deck with flow after insert`() = runBlocking {

        deckDb.insertNewDeck(randomDeck)
        deckDb.insertNewDeck(randomDeck2)

        deckDb.latestDeck.test {
            assertEquals(randomDeck2, expectItem())
            cancel()
            expectNoMoreEvents()
        }

        assert(true)
    }
}
