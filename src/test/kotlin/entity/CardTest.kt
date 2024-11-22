package entity

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertSame

/**
 * Test cases for [Card]
 */

class CardTest {
    private val card1 = Card(CardSuit.SPADES, CardValue.THREE)
    private val card2 = Card(CardSuit.DIAMONDS, CardValue.THREE)
    private val card3 = Card(CardSuit.DIAMONDS, CardValue.JACK)
    private val card4 = Card(CardSuit.SPADES, CardValue.THREE)
    private val card5 = card2

    @Test
    fun testEquality(){
        //test for same cardValues but different cardSuit
        assertNotEquals(card1,card2)

        //test for same cardSuits but different cardSuit
        assertNotEquals(card2,card3)

        //test for equal cards
        assertEquals(card1,card4)

        //test for same cards
        assertSame(card2,card5)
    }

}