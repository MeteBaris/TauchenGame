package service

import entity.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class PlayerActionServiceTest {

    private var rootService= RootService()
    private var playerActionService= PlayerActionService(rootService)

    @BeforeEach
    fun setUp() {
        // Initialize the root service and current game
        rootService = RootService()
        val players = mutableListOf(Player("bob",0,true), Player("tom",0,true))
        val playStack = mutableListOf<Card>()
        var drawStack = mutableListOf(Card(CardSuit.HEARTS, CardValue.TWO),Card(CardSuit.SPADES, CardValue.THREE)) // Add a card to drawStack
        val discardStack = mutableListOf<Card>()

        // Set up the game with initialized stacks and players
        rootService.currentGame = TauchenGame( players)
        val game =rootService.currentGame
        checkNotNull(game) { "No game currently running." }
        game.drawStack = drawStack
        game.playStack = playStack
        game.discardStack =discardStack
        game.isPlayerOneActive =true
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

    @Test
            /**testplay card with  non-matching card (throws Exception)*/
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

    /**Test for swapping cards*/
    @Test
    fun testSwapCard() {
        val game = rootService.currentGame!!
        game.isPlayerOneActive = true
        val cardInHand = Card(CardSuit.HEARTS, CardValue.SEVEN)
        val cardInPlay1 = Card(CardSuit.DIAMONDS, CardValue.SEVEN)
        val cardInPlay2 = Card(CardSuit.SPADES, CardValue.SEVEN)

        game.playStack.add(cardInPlay1)
        game.playStack.add(cardInPlay2)

        game.players[0].hand.add(cardInHand)

        playerActionService.swapCard(cardInPlay1, cardInHand)

        assertTrue(game.players[0].hand.contains(cardInPlay1)) // Card from stack should now be in hand
        assertTrue(game.playStack.contains(cardInHand)) // Card from hand should now be in stack
        assertFalse(game.players[0].hand.contains(cardInHand)) // Original card should no longer be in hand
        assertFalse(game.playStack.contains(cardInPlay1)) // Original card should no longer be in stack
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

        assertDoesNotThrow{ playerActionService.discardCard(card8) }
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

    /**Test playCard to create a trio with matching values*/
    @Test
    fun testTrioWithMatchingValue() {
        val game = rootService.currentGame!!
        game.isPlayerOneActive = true

        val card1 = Card(CardSuit.HEARTS, CardValue.FIVE)
        val card2 = Card(CardSuit.SPADES, CardValue.FIVE)
        val cardToPlay = Card(CardSuit.DIAMONDS, CardValue.FIVE)
        game.playStack.addAll(listOf(card1, card2))
        game.players[0].hand.add(cardToPlay)

        playerActionService.playCard(cardToPlay)

        assertTrue(game.players[0].collectionStack.containsAll(listOf(card1, card2, cardToPlay)))
        assertEquals(20, game.players[0].score)
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

        assertDoesNotThrow{ playerActionService.drawCard() }
        assertFalse(game.players[0].hand.contains(drawnCard))
    }
}
