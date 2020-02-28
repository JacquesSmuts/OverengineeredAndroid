package com.jacquessmuts.overengineered

import com.jacquessmuts.overengineered.Generators.generateDeck
import com.jacquessmuts.overengineered.api.DeckApi
import com.jacquessmuts.overengineered.db.DeckDb
import com.jacquessmuts.overengineered.model.Card
import com.jacquessmuts.overengineered.model.Deck
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import java.util.UUID
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@FlowPreview
@ExperimentalCoroutinesApi
internal class CardsRepositoryTest {

    lateinit var repository: CardsRepository

    val firstDeck = generateDeck()

    @BeforeEach
    fun setup() {

        val deckApi = mockk<DeckApi>()
        val deckDb = mockk<DeckDb>()

        coEvery { deckApi.getDeck() } returns firstDeck
        coEvery { deckDb.insertNewDeck(any()) } returns Unit
        every { deckDb.latestDeck } returns flow {
            emit(firstDeck)
        }

        repository = CardsRepository(deckApi, deckDb)
    }

    @Test
    fun `repo exposes deck from db`() = runBlockingTest {

        repository.deck.collect {
            assertEquals(firstDeck, it)
        }
    }
}

object Generators {

    val randomString
        get() = UUID.randomUUID().toString().substring(0, (0..20).random())

    val randomBoolean
        get() = Random.nextBoolean()

    val randomInt
        get() = (Int.MIN_VALUE..Int.MAX_VALUE).random()

    fun generateDeck(withCards: Boolean = true): Deck {

        // Generates 0-52 random cards
        val cards = (0..(0..52).random()).map { generateCard() }

        return Deck(
            success = randomBoolean,
            id = randomString,
            cards = if (withCards) cards else listOf(),
            shuffled = randomBoolean,
            remaining = randomInt
        )
    }

    fun generateCard(): Card {
        return Card(
            image = randomString,
            value = randomString,
            suit = randomString,
            code = randomString
        )
    }
}
