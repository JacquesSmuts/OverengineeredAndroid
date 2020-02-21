package com.jacquessmuts.overengineered

import com.jacquessmuts.overengineered.api.DeckApi
import com.jacquessmuts.overengineered.db.DeckDb
import com.jacquessmuts.overengineered.model.Deck
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

@ExperimentalCoroutinesApi
internal class CardsRepositoryTest {

    lateinit var repository: CardsRepository

    val firstDeck = Deck(true, "aaa", listOf(), true, 52)


    @BeforeEach
    fun setup() {
        val deckApi = mockk<DeckApi>()
        val deckDb = mockk<DeckDb>()

        coEvery { deckApi.getDeck() } returns firstDeck

        repository = CardsRepository(deckApi, deckDb)

    }

    @Test
    fun `repo collects at least one deck from api`() = runBlockingTest {

        repository.deck.collect {
            assertEquals(firstDeck, it)
        }
    }
}