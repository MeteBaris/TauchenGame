package service

import entity.Player
import entity.TauchenGame
import gui.*
/**
 * Service layer class that provides the logic for actions not directly
 * related to a single player.
 */

class GameService(
    private val rootService: RootService
) : AbstractRefreshingService() {

    val playerNames: List<String> = listOf("Alice", "Bob")

    /**The startGame(playerName: List<String>) method starts a new game with the specified player names.
     * It deals the cards and prepares the stacks and center of the table according to the game rules.
     * (It will be suitable after GUI implementation)*/
    fun startGame(playerNames: List<String>) {

        val game = TauchenGame(mutableListOf(Player(playerNames[0], 0, true), Player(playerNames[1], 0, true)))

        //name will be  written after GUI service
        //for random first player
        game.players.shuffle()


        game.players[0].hand = rootService.cardService.dealCards()
        game.players[1].hand = rootService.cardService.dealCards()

        game.players[0].collectionStack = rootService.cardService.createCollectionStack().toMutableList()
        game.players[1].collectionStack = rootService.cardService.createCollectionStack().toMutableList()
        game.drawStack = rootService.cardService.createDrawStack().toMutableList()

        rootService.currentGame = game
        game.isPlayerOneActive = true
        onAllRefreshables {
            refreshAfterStartGame()
        }
    }

    /**The endGame() method ends the current game and calculates the players final scores*/
    fun endGame() {
        val game = rootService.currentGame
        if (game!!.players[0].score > game.players[1].score) {
            println(
                "Winner is " + game.players[0].name + " with " +
                        game.players[0].score + " score."
            )
            println(
                "Second winner is " + game.players[1].name + " with " +
                        game.players[1].score + " score."
            )
        } else if (game.players[0].score == game.players[1].score) {
            println("The game is a draw ")
            println(
                game.players[0].name + " with " +
                        game.players[0].score + " score."
            )
            println(
                println(
                    game.players[1].name + " with " +
                            game.players[1].score + " score."
                )
            )
        } else {
            println(
                "Winner is " + game.players[1].name + " with " +
                        game.players[1].score + " score."
            )
            println(
                "Second winner is " + game.players[0].name + " with " +
                        game.players[0].score + " score."
            )
        }
        onAllRefreshables { }
        rootService.currentGame = null
    }

    /**The startTurn() starts automatically after startGame. It will be better after GUI implementation */
    fun startTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running." }

        onAllRefreshables {
            refreshAfterStartTurn()
        }

    }

    /**After startTurn(), endTurn() starts automatically. It will be better after GUI implementation */
    fun endTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        var currentPlayer: Player =
            if (game.isPlayerOneActive) {
                game.players[0]
            } else
                game.players[1]
        if (game.drawStack.isNotEmpty()) {

         /*   if (game.playStack.size == 3) {
              //  currentPlayer.collectionStack= game.playStack
                //game.playStack.clear()
            }*/
            game.isPlayerOneActive = !game.isPlayerOneActive
        } else {
            endGame()
        }
        onAllRefreshables {
            refreshAfterEndTurn()
        }
    }

    /**Helper for selecting first player */
    private fun selectStartingPlayer(players: MutableList<Player>): Player {
        return players[0]
    }
}