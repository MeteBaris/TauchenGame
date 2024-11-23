package service

import entity.TauchenGame
import gui.Refreshable

/**
 * This base service class provides access to all other services.
 *
 * @property gameService The connected [GameService]
 * @property playerActionService The connected [PlayerActionService]
 * @property cardService The connected [CardService]
 * @property currentGame The currently active [entity.TauchenGame]. Can be `null`, if no game has started yet.
 */

class RootService {
    val gameService = GameService(this)
    val playerActionService = PlayerActionService(this)
    val cardService = CardService(this)

    /**
     * The currently active game. Can be `null`, if no game has started yet.
     */
    lateinit var currentGame : TauchenGame

    /**
     * Adds the provided [newRefreshable] to all services connected to this root service
     *
     * @param newRefreshable The [Refreshable] to be added
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        gameService.addRefreshable(newRefreshable)
        playerActionService.addRefreshable(newRefreshable)
        cardService.addRefreshable(newRefreshable)
    }

    /**
     * Adds each of the provided [newRefreshables] to all services
     * connected to this root service
     *
     * @param newRefreshables The [Refreshable]s to be added
     */

    fun addRefreshables(vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }

}