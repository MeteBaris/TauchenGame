package service

import entity.Player
import entity.TauchenGame
/**
 * Service layer class that provides the logic for actions not directly
 * related to a single player.
 * @property rootService [RootService] provides access to the entity layer
 */

class GameService(
    private val rootService: RootService
) : AbstractRefreshingService() {


    /**The startGame(playerName: List<String>) method starts a new game with the specified player names.
     * It deals the cards and prepares the stacks and center of the table according to the game rules.
     * (It will be suitable after GUI Implementation)*/
    fun startGame(playerNames: List<String>) {

        val game = TauchenGame(mutableListOf(Player(playerNames[0], 0, true), Player(playerNames[1], 0, true)))

        rootService.currentGame = game

        game.players[0].hand = rootService.cardService.dealCards()
        game.players[1].hand = rootService.cardService.dealCards()


        game.players[0].collectionStack = rootService.cardService.createCollectionStack().toMutableList()
        game.players[1].collectionStack = rootService.cardService.createCollectionStack().toMutableList()
        game.drawStack = rootService.cardService.createDrawStack()




        game.isPlayerOneActive = true


        onAllRefreshables {
            refreshAfterStartGame()
        }
    }

    /**The endGame() method ends the current game and calculates the players final scores*/
    fun endGame() {
        val game = rootService.currentGame
        if (game.players[0].score > game.players[1].score) {
            println(
                "Winner is " + game.players[0].name + " with " +
                        game.players[0].score + " score."
            )
            println(
                "Second winner is " + game.players[1].name + " with " +
                        game.players[1].score + " score."
            )
            onAllRefreshables {
                refreshAfterEndGame(game.players[1])
            }
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
            onAllRefreshables {
                refreshAfterEndGame(game.players[1])
            }
        } else {
            println(
                "Winner is " + game.players[1].name + " with " +
                        game.players[1].score + " score."
            )

            println(
                "Second winner is " + game.players[0].name + " with " +
                        game.players[0].score + " score."
            )
            onAllRefreshables {
                refreshAfterEndGame(game.players[1])
            }
        }

     //   rootService.currentGame = null
    }

    /**The startTurn() starts automatically after startGame. */
    fun startTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running." }

        onAllRefreshables {
            refreshAfterStartTurn()
        }
    }

    /**After startTurn(), endTurn() starts automatically.*/
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

            //I wrote this code in gameScene refAfterEndTurn    game.isPlayerOneActive = !game.isPlayerOneActive

            //!!!!!!!!!!!!!!!!
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

