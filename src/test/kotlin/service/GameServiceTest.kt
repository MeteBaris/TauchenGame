package service

import entity.Player
import entity.TauchenGame
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
        val game =TauchenGame(mutableListOf(Player("Alice",0,true), Player("Bob",0,true)))
        rootService = RootService()
        testRefreshable = TestRefreshable(rootService)
        rootService.addRefreshable(testRefreshable)
        rootService.currentGame= game

    }

    /** Tests the startGame method to ensure it initializes the game correctly. */
    @Test
    fun testStartGame(){

        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        assertEquals(2, game.players.size)
        assertNotNull(game)
        assertEquals("Alice", game.players[0].name)
        assertEquals("Bob", game.players[1].name)
        assertNotNull(game.drawStack)
        assertNotNull(game.players[0].hand)
        assertNotNull(game.players[1].hand)
        assertTrue(game.isPlayerOneActive)

    }


    /** Tests startTurn and `endTurn` methods to validate the turn logic. */
    @Test
    fun testStartAndEndTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }

        assertDoesNotThrow { rootService.gameService.startTurn() }
        assertTrue(testRefreshable.refreshAfterStartTurn)

        assertDoesNotThrow { rootService.gameService.endTurn() }
        assertTrue(testRefreshable.refreshAfterEndTurn)
        assertTrue(game.isPlayerOneActive)
    }
    /** This is a test method for if there is no current game situation*/
    @Test
    fun testEndTurnNoGame() {
        /** Set the current game of the root service to null*/
        rootService.currentGame = null

        /**Test: No game is currently active*/
        assertThrows<IllegalStateException> { rootService.gameService.endTurn() }
    }
    /** Tests endGame to ensure the correct winner is determined. */
    @Test
    fun testEndGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        game.players[0].score = 50
        game.players[1].score = 40

        assertDoesNotThrow { rootService.gameService.endGame() }
        assertNull(rootService.currentGame)
    }

    /** Tests endGame for a draw situation. */
    @Test
    fun testEndGameDraw() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        game.players[0].score = 30
        game.players[1].score = 30

        assertDoesNotThrow { rootService.gameService.endGame() }
        assertNull(rootService.currentGame)
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
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        game.drawStack.clear()

        assertDoesNotThrow { rootService.gameService.endTurn() }
        // Game should end
        assertNull(rootService.currentGame)
    }

    /** Tests startGame to ensure the draw stack is populated.*/
    @Test
    fun testDrawStackAfterStartGame() {
        rootService.gameService.startGame(listOf("Eve", "Frank"))
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        assertNotNull(game)
        assertTrue(game.drawStack.isNotEmpty())
    }
}
