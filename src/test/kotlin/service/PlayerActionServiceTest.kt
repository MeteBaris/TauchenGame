package service

import entity.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


/**
 * Unit tests for the PlayerActionService class.
 *
 * This class contains various unit tests to ensure the functionality of the PlayerActionService
 * methods, which include player actions like playing, drawing, swapping, and discarding cards.
 * The tests cover different scenarios, such as valid and invalid plays, drawing from an empty stack,
 * performing special actions, and validating card matches.
 */
class PlayerActionServiceTest {

    /** Instance of RootService to initialize the game and manage game state */
    private var rootService = RootService()

    /** Instance of PlayerActionService to handle player actions during the game */
    private var playerActionService = PlayerActionService(rootService)

    @BeforeEach
    fun setUp() {
        // Initialize the root service and current game
        rootService = RootService()
        val players = mutableListOf(Player("bob", 0, true), Player("tom", 0, true))
        val playStack = mutableListOf<Card>()
        val drawStack = mutableListOf(
            Card(CardSuit.HEARTS, CardValue.TWO),
            Card(CardSuit.SPADES, CardValue.THREE)
        ) // Add a card to drawStack
        val discardStack = mutableListOf<Card>()

        // Set up the game with initialized stacks and players
        rootService.currentGame = TauchenGame(players)
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running." }
        game.drawStack = drawStack
        game.playStack = playStack
        game.discardStack = discardStack
        game.isPlayerOneActive = true
        playerActionService = PlayerActionService(rootService)
    }

    /**test PlayCard() with empty playStack for first player
     * (it can be  also a test for second player)*/
    @Test
    fun testPlayCardWithEmptyPlayStack() {
        val game = rootService.currentGame!!
        game.isPlayerOneActive = true
        val card = Card(CardSuit.HEARTS, CardValue.FIVE)
        game.players[0].hand.add(card)
        playerActionService.playCard(card)

        assertTrue(game.playStack.contains(card))
        assertFalse(game.players[0].hand.contains(card))
    }

    @Test
            /**test playCard with matching suits*/
    fun testPlayCardMatchingSuit() {
        val game = rootService.currentGame!!
        game.isPlayerOneActive = true
        val stackCard = Card(CardSuit.HEARTS, CardValue.FIVE)
        val cardToPlay = Card(CardSuit.HEARTS, CardValue.SEVEN)
        game.playStack.add(stackCard)
        game.players[0].hand.add(cardToPlay)
        playerActionService.playCard(cardToPlay)

        assertTrue(game.playStack.contains(cardToPlay))
        assertFalse(game.players[0].hand.contains(cardToPlay))
    }

    /**testplay card with  non-matching card (throws Exception)*/
    @Test
    fun testplayCardNonMatchingCard() {
        val game = rootService.currentGame!!
        game.isPlayerOneActive = true
        val stackCard = Card(CardSuit.HEARTS, CardValue.FIVE)
        val cardToPlay = Card(CardSuit.CLUBS, CardValue.SEVEN)
        game.playStack.add(stackCard)
        game.players[0].hand.add(cardToPlay)

        assertThrows<IllegalStateException> { playerActionService.playCard(cardToPlay) }
    }

    /**test for trio form with matching suits*/
    @Test
    fun testTrioWithMatchingSuit() {
        val game = rootService.currentGame!!
        game.isPlayerOneActive = true

        val card1 = Card(CardSuit.HEARTS, CardValue.FIVE)
        val card2 = Card(CardSuit.HEARTS, CardValue.SIX)
        val cardToPlay = Card(CardSuit.HEARTS, CardValue.SEVEN)
        game.playStack.addAll(listOf(card1, card2))
        game.players[0].hand.add(cardToPlay)

        playerActionService.playCard(cardToPlay)

        assertTrue(game.players[0].collectionStack.containsAll(listOf(card1, card2, cardToPlay)))
        assertEquals(5, game.players[0].score)
    }

    /**test drawCard with non-empty drawStack.*/
    @Test
    fun testDrawCardNonEmptyDrawStack() {
        val game = rootService.currentGame
        checkNotNull(game)
        game.drawStack.isEmpty()
        val card1 = Card(CardSuit.HEARTS, CardValue.FIVE)
        val card2 = Card(CardSuit.CLUBS, CardValue.FIVE)

        game.drawStack.add(card1)
        game.drawStack.add(card2)


        val currentPlayer = if (game.isPlayerOneActive) game.players[0] else game.players[1]
        currentPlayer.hand.clear()
        rootService.playerActionService.drawCard()

        assertTrue(currentPlayer.hand.contains(card2))
        assertFalse(game.drawStack.contains(card2))
    }

    /**test drawCard with empty drawStack. (end game situation)*/
    @Test
    fun testDrawCardEmptyDrawStack() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running." }


        game.drawStack.clear()

        assertDoesNotThrow { rootService.playerActionService.drawCard() }

        assertTrue(game.drawStack.isEmpty())
    }

    /** Test for swapping cards */
    @Test
    fun testSwapCard() {
        val game = rootService.currentGame!!
        game.isPlayerOneActive = true
        val cardPlaced = Card(CardSuit.HEARTS, CardValue.SEVEN)
        val cardInPlay1 = Card(CardSuit.DIAMONDS, CardValue.SEVEN)
        val cardInPlay2 = Card(CardSuit.SPADES, CardValue.SEVEN)

        game.playStack.add(cardInPlay1)
        game.playStack.add(cardInPlay2)

        game.players[0].hand.add(cardPlaced)
        game.players[0].hasSpecialAction = true

        // Test successful swap
        assertDoesNotThrow { playerActionService.swapCard(cardInPlay1, cardPlaced) }

        assertTrue(game.players[0].hand.contains(cardInPlay1)) // Card from stack should now be in hand
        assertTrue(game.playStack.contains(cardPlaced)) // Card from hand should now be in stack
        assertFalse(game.players[0].hand.contains(cardPlaced)) // Original card should no longer be in hand
        assertFalse(game.playStack.contains(cardInPlay1)) // Original card should no longer be in stack
        assertFalse(game.players[0].hasSpecialAction) // Special action should now be false

        // Test swap with invalid card
        val invalidCard = Card(CardSuit.CLUBS, CardValue.KING)
        game.players[0].hand.add(invalidCard)
        assertDoesNotThrow { playerActionService.swapCard(invalidCard, cardPlaced) }
        assertFalse(game.playStack.contains(invalidCard))
    }

    /**Test swapping cards without having special action*/
    @Test
    fun swapCardInvalidSwapNoSpecialAction() {
        val game = rootService.currentGame
        checkNotNull(game)
        val currentPlayer =
            if (game.isPlayerOneActive) game.players[0]
            else game.players[1]
        currentPlayer.hasSpecialAction = false

        currentPlayer.hand.clear()
        game.playStack.clear()


        val cardToPlace = Card(CardSuit.CLUBS, CardValue.KING)
        currentPlayer.hand.add(cardToPlace)
        val cardToTake = Card(CardSuit.CLUBS, CardValue.FIVE)
        game.playStack.add(cardToTake)

        assertDoesNotThrow {
            rootService.playerActionService.swapCard(cardToTake, cardToPlace)
        }
        assertFalse(currentPlayer.hand.contains(cardToTake))
    }

    /**Test discarding a card when hand size is nine*/
    @Test
    fun testDiscardCard() {
        val game = rootService.currentGame!!
        val card1 = Card(CardSuit.HEARTS, CardValue.FIVE)
        val card2 = Card(CardSuit.SPADES, CardValue.FIVE)
        val card3 = Card(CardSuit.DIAMONDS, CardValue.FIVE)
        val card4 = Card(CardSuit.CLUBS, CardValue.FIVE)
        val card5 = Card(CardSuit.HEARTS, CardValue.SEVEN)
        val card6 = Card(CardSuit.HEARTS, CardValue.SIX)
        val card7 = Card(CardSuit.CLUBS, CardValue.ACE)
        val card8 = Card(CardSuit.HEARTS, CardValue.ACE)
        val card9 = Card(CardSuit.SPADES, CardValue.JACK)

        game.players[0].hand.add(card1)
        game.players[0].hand.add(card2)
        game.players[0].hand.add(card3)
        game.players[0].hand.add(card4)
        game.players[0].hand.add(card5)
        game.players[0].hand.add(card6)
        game.players[0].hand.add(card7)
        game.players[0].hand.add(card8)
        game.players[0].hand.add(card9)

        assertDoesNotThrow { playerActionService.discardCard(card8) }
        assertTrue(game.discardStack.isNotEmpty())

    }

    /**Test discarding a card when hand size is not nine*/
    @Test
    fun testDiscardCardNotNine() {
        val game = rootService.currentGame!!
        val card = Card(CardSuit.HEARTS, CardValue.FIVE)
        game.players[0].hand.add(card) // Hand size is not 9

        assertDoesNotThrow { playerActionService.discardCard(card) }
        assertFalse(game.discardStack.isNotEmpty())
    }


    /**Test end turn after player has played a card*/
    @Test
    fun testEndTurnAfterPlayCard() {
        val game = rootService.currentGame!!
        game.isPlayerOneActive = true
        val card = Card(CardSuit.HEARTS, CardValue.FIVE)
        game.players[0].hand.add(card)
        playerActionService.playCard(card)

        assertTrue(game.players[0].hasPlayed)
    }

    /**Test draw card when player has already played*/
    @Test
    fun testDrawCardAfterPlay() {
        val game = rootService.currentGame!!
        game.isPlayerOneActive = true

        val cardToDraw = Card(CardSuit.CLUBS, CardValue.FIVE)
        game.drawStack.add(cardToDraw)
        val cardToPlay = Card(CardSuit.HEARTS, CardValue.FIVE)
        game.players[0].hand.add(cardToPlay)


        playerActionService.playCard(cardToPlay)

        val drawnCard = game.drawStack.removeLast()

        assertDoesNotThrow { playerActionService.drawCard() }
        assertFalse(game.players[0].hand.contains(drawnCard))
    }

    /** Test if 'isCardValid' returns true for cards with matching suits */
    @Test
    fun isCardValidValidMatchingSuits() {
        val stackCard = Card(CardSuit.HEARTS, CardValue.SEVEN)
        val cardToPlay = Card(CardSuit.HEARTS, CardValue.TEN)

        assertTrue(playerActionService.isCardValid(stackCard, cardToPlay))
    }

    /** Test if 'isCardValid' returns true for cards with matching values */
    @Test
    fun isCardValidValidMatchingValue() {
        val stackCard = Card(CardSuit.SPADES, CardValue.NINE)
        val cardToPlay = Card(CardSuit.HEARTS, CardValue.NINE)

        assertTrue(playerActionService.isCardValid(stackCard, cardToPlay))
    }

    /** Test if 'isCardValid' returns false for cards with neither matching suits nor values */
    @Test
    fun isCardValidNoMatch() {
        val stackCard = Card(CardSuit.SPADES, CardValue.NINE)
        val cardToPlay = Card(CardSuit.HEARTS, CardValue.TEN)

        assertFalse(playerActionService.isCardValid(stackCard, cardToPlay))
    }
}
