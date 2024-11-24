package service

import entity.Player
import entity.TauchenGame
/**
 * Service layer class that provides the logic for game-level actions
 * that are not directly tied to a single player. This includes starting
 * the game, ending the game, and managing turns.
 *
 * @property rootService [RootService] provides access to the entity layer
 */

class GameService(
    private val rootService: RootService
) : AbstractRefreshingService() {


    /**
     * Starts a new game with the specified player names.
     *
     * - Initializes the players' hands, collection stacks, and the draw stack.
     * - Prepares the discard stack and other game components.
     * - Sets the first player to be active.
     * - Triggers a refresh to update all UI components after the game setup.
     *
     * @param playerNames A list of player names used to create player instances.
     * @throws IllegalArgumentException if fewer than two player names are provided.
     */
    fun startGame(playerNames: List<String>) {

        val game = TauchenGame(mutableListOf(Player(playerNames[0], 0, true), Player(playerNames[1], 0, true)))



        game.players[0].hand = rootService.cardService.dealCards()
        game.players[1].hand = rootService.cardService.dealCards()


        game.players[0].collectionStack = rootService.cardService.createCollectionStack().toMutableList()
        game.players[1].collectionStack = rootService.cardService.createCollectionStack().toMutableList()
        game.drawStack = rootService.cardService.createDrawStack()

        game.isPlayerOneActive = true

        game.discardStack = rootService.cardService.createDiscardStack().toMutableList()
        rootService.currentGame = game
        onAllRefreshables {
            refreshAfterStartGame()
        }
    }

    /**
     * Ends the current game and calculates the final scores of the players.
     *
     * - Checks if the draw stack is empty to determine the game-ending condition.
     * - Triggers a refresh to display the final game state and results.
     */
    fun endGame() {
        val game = rootService.currentGame
        checkNotNull(game)
        if (game.drawStack.size==0){
            onAllRefreshables { refreshAfterEndGame() }
        }

    }

    /**
     * Starts a new turn for the current active player.
     *
     * - Ensures that the game is currently active before proceeding.
     * - Notifies all refreshable components to update the state for the new turn.
     *
     * @throws IllegalStateException if no game is currently running.
     */
    fun startTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running." }

        onAllRefreshables {
            refreshAfterStartTurn()
        }
    }

    /**
     * Ends the current turn and switches the active player.
     *
     * - Verifies if the game is active and the draw stack is not empty.
     * - Updates the `hasPlayed` property of the current player.
     * - Alternates the active player for the next turn.
     * - If the draw stack is empty, the game ends automatically.
     * - Triggers a refresh to update the game state for the end of the turn.
     *
     * @throws IllegalStateException if no game is currently active.
     */
    fun endTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }

        if (game.drawStack.isNotEmpty()) {
        val currentPlayer : Player =
            if (game.isPlayerOneActive){
                game.players[0]
            }else{
                game.players[1]
            }

            currentPlayer.hasPlayed =true
            game.isPlayerOneActive = !game.isPlayerOneActive
            currentPlayer.hasPlayed = false
            onAllRefreshables {
                refreshAfterEndTurn()
            }
        } else {
            endGame()
        }

    }
    }

