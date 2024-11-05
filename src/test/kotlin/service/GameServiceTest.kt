package service

import entity.Player
import entity.TauchenGame
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.*
import service.*
import gui.Refreshable
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

    /**test for startGame*/
    @Test
    fun testStartGame(){

        val game = rootService.currentGame
        assertEquals(2, game!!.players.size)
        assertNotNull(game)
        assertEquals("Alice", game.players[0].name)
        assertEquals("Bob", game.players[1].name)
    }


    /**test for start and end turn. (for end turn, the turn should be started)*/
    @Test
    fun testStartAndEndTurn(){
        val game = rootService.currentGame

        checkNotNull(game) { "No game is currently active" }
        assertDoesNotThrow { rootService.gameService.endTurn()}
        assertFalse(testRefreshable.refreshAfterStartTurn)
        assertEquals(true, game.isPlayerOneActive)
        assertDoesNotThrow { rootService.gameService.startTurn()}
    }
    /** This is a test method for if there is no current game situation*/
    @Test
    fun testEndTurnNoGame() {
        /** Set the current game of the root service to null*/
        rootService.currentGame = null

        /**Test: No game is currently active*/
        assertThrows<IllegalStateException> { rootService.gameService.endTurn() }
    }

}