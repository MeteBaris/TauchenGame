package entity

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

//Test class for Tauchen
class TauchenTest {
    @Test
    fun testDealMoreThreeCards(){
        val game = TauchenGame(isPlayerOneActive = true)

        //adding cards for test
        game.playStack.add(Card(CardSuit.CLUBS , CardValue.TWO))
        assertThrows<IllegalArgumentException> { require(game.playStack.size < 3) }

        game.playStack.add(Card(CardSuit.HEARTS , CardValue.ACE))
        assertThrows<IllegalArgumentException> { require(game.playStack.size < 3) }

        game.playStack.add(Card(CardSuit.CLUBS , CardValue.TWO))
        assertThrows<IllegalArgumentException> { require(game.playStack.size < 3) }


}}