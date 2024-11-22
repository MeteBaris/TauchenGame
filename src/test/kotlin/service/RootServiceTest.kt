package service

import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.Test


/**
 * Class for testing multiple different methods of the [RootService].
 */
class RootServiceTest {

    /** Test adding a single Refreshable to the RootService. */
    @Test
    fun testAddRefreshable(){
        val rootService = RootService()
        val testRefreshable = TestRefreshable(rootService)
        assertDoesNotThrow{ rootService.addRefreshable(testRefreshable) }
    }

    /** Test adding multiple Refreshables to the RootService. */
    @Test
    fun testAddMultipleRefreshables() {
        val rootService = RootService()
        val refreshAfterStartGame = TestRefreshable(rootService)
        val refreshAfterStartTurn = TestRefreshable(rootService)
        val refreshAfterDrawCard = TestRefreshable(rootService)

        /** Test: The Refreshables are added without an error*/
        assertDoesNotThrow { rootService.addRefreshables(refreshAfterStartGame, refreshAfterStartTurn,refreshAfterDrawCard) }
    }

}