package service

import entity.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach
import kotlin.test.*

/*** This service class is responsible for modifying the creating stacks and dealing cards for players hand.
 *
 * @property rootService The root service, which provides access to all other services
 * @property testRefreshable An instance representing a refreshable component within the service */
class CardServiceTest {

    private var rootService= RootService()
    private var testRefreshable= TestRefreshable(rootService)

    /**
     * Sets up a new game before each test with [TestRefreshable]s attached to a newly created [RootService].
     * Starts a new game before each test with two players (Alice, Bob) and 5 cards each.
     */
    @BeforeTest
    fun setUp() {
        rootService = RootService()
        testRefreshable = TestRefreshable(rootService)
        rootService.addRefreshable(testRefreshable)
        val game =TauchenGame(mutableListOf(Player("Alice",0,true), Player("Bob",0,true)))
        val players :List<String> =listOf("Alice","Bob")

        rootService.currentGame = game
        rootService.gameService.startGame(players)
    }

    /**This function testCreateDeck() creates a mix deck before dealing cards and creating draw Stack */
    @Test
    fun testCreateDeck() {
        val deck = rootService.cardService.createDeck()

        /**Assert deck is not empty and contains 52 cards*/
        assertFalse(deck.isEmpty(), "Deck should not be empty")
        assertEquals(52, deck.size, "Deck should contain 52 cards")

        /** Check that deck has no duplicate cards*/
        val uniqueCards = deck.toSet()
        assertEquals(52, uniqueCards.size, "Deck should contain unique cards")
    }

    /**testDealCards() is a test function to control dealing cards*/
    @Test
    fun testDealCards() {
        val game = rootService.currentGame
        assertNotNull(game, "Game should be initialized")

        val player1 = game.players[0]
        val player2 = game.players[1]

        /** Test that players were dealt 5 cards*/
        assertEquals(5, player1.hand.size, "Player 1 should have 5 cards in hand")
        assertEquals(5, player2.hand.size, "Player 2 should have 5 cards in hand")
    }
}




