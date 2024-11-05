package entity

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

//Test class for Tauchen
class TauchenTest {
    @Test
    fun testDealMoreThreeCards(){
        val game = TauchenGame()

        //adding cards for test
        game.playStack.add(Card(CardSuit.CLUBS , CardValue.TWO))
        game.playStack.add(Card(CardSuit.HEARTS , CardValue.ACE))
        game.playStack.add(Card(CardSuit.SPADES , CardValue.TWO))
        game.playStack.add(Card(CardSuit.CLUBS , CardValue.TWO))
        assertThrows<IllegalArgumentException> { require(game.playStack.size <= 3) }

}}