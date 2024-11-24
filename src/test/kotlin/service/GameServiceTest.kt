package service

import entity.*
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.*
import org.junit.jupiter.api.assertThrows

/*** This service class is responsible for modifying the game entities.
 *
 * @property rootService The root service, which provides access to all other services*/
class GameServiceTest {
    private var rootService = RootService()
    private var testRefreshable = TestRefreshable(rootService)

    /**setting up for game service tests*/
    @BeforeTest
    fun setUp() {
        val game = TauchenGame(mutableListOf(Player("Alice", 0, true), Player("Bob", 0, true)))
        rootService = RootService()
        testRefreshable = TestRefreshable(rootService)
        rootService.addRefreshable(testRefreshable)
        rootService.currentGame = game
    }

    /** Tests the startGame method to ensure it initializes the game correctly. */
    @Test
    fun testStartGame() {
        rootService.gameService.startGame(listOf("Alice", "Bob"))
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        assertEquals(2, game.players.size)
        assertEquals("Alice", game.players[0].name)
        assertEquals("Bob", game.players[1].name)
        assertNotNull(game.drawStack)
        assertNotNull(game.players[0].hand)
        assertNotNull(game.players[1].hand)
        assertTrue(game.isPlayerOneActive)
    }

    /** Tests startTurn and endTurn methods to validate the turn logic. */
    @Test
    fun testStartAndEndTurn() {
        rootService.gameService.startGame(listOf("Alice", "Bob"))
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }

        assertDoesNotThrow { rootService.gameService.startTurn() }
        assertTrue(testRefreshable.refreshAfterStartTurn)

        assertDoesNotThrow { rootService.gameService.endTurn() }
        assertTrue(testRefreshable.refreshAfterEndTurn)
        assertFalse(game.isPlayerOneActive)
    }

    /** This is a test method for if there is no current game situation*/
    @Test
    fun testEndTurnNoGame() {
        rootService.currentGame = null
        assertThrows<IllegalStateException> { rootService.gameService.endTurn() }
    }

    /** Tests endGame to ensure the correct game-ending behavior. */
    @Test
    fun testEndGame() {
        rootService.gameService.startGame(listOf("Alice", "Bob"))
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        game.drawStack.clear()

        assertDoesNotThrow { rootService.gameService.endGame() }
        assertTrue(testRefreshable.refreshAfterEndGame)
    }

    /**Tests no game situation*/
    @Test
    fun testStartTurnNoGame() {
        rootService.currentGame = null
        assertThrows<IllegalStateException> { rootService.gameService.startTurn() }
    }

    /**Tests endTurn with an empty drawStack to trigger game end.*/
    @Test
    fun testEndTurnWithEmptyDrawStack() {
        rootService.gameService.startGame(listOf("Alice", "Bob"))
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        game.drawStack.clear()

        assertDoesNotThrow { rootService.gameService.endTurn() }
        // Game should end
        assertTrue(testRefreshable.refreshAfterEndGame)
    }

    /** Tests startGame to ensure the draw stack is populated.*/
    @Test
    fun testDrawStackAfterStartGame() {
        rootService.gameService.startGame(listOf("Eve", "Frank"))
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        assertTrue(game.drawStack.isNotEmpty())
    }

    /** Tests if the collection stacks are created properly for each player. */
    @Test
    fun testCollectionStackInitialization() {
        rootService.gameService.startGame(listOf("Alice", "Bob"))
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }

        val card1 = Card(CardSuit.HEARTS, CardValue.FIVE)
        val card2 = Card(CardSuit.HEARTS, CardValue.FOUR)
        val card3 = Card(CardSuit.HEARTS, CardValue.THREE)
        game.playStack.add(card1)
        game.playStack.add(card2)
        game.playStack.add(card3)
        game.isPlayerOneActive = true

        assertFalse(game.players[0].collectionStack.isNotEmpty())
        assertFalse(game.players[1].collectionStack.isNotEmpty())
        game.players[0].collectionStack.addAll(game.playStack)


        assertTrue(game.players[0].collectionStack.isNotEmpty())
        assertFalse(game.players[1].collectionStack.isNotEmpty())
    }

    /** Tests if hasPlayed property switches correctly at the end of each turn. */
    @Test
    fun testHasPlayedSwitch() {
        rootService.gameService.startGame(listOf("Alice", "Bob"))
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }

        rootService.gameService.startTurn()
        val card1 = Card(CardSuit.HEARTS, CardValue.FIVE)
        val currentPlayer =
            if (game.isPlayerOneActive) {
                game.players[0]
            } else {
                game.players[1]
            }
        assertDoesNotThrow { rootService.playerActionService.playCard(card1) }
        assertTrue(game.players[0].hasPlayed || game.players[1].hasPlayed)
        assertDoesNotThrow { rootService.gameService.endTurn() }
    }
}