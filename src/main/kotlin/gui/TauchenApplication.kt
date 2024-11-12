package gui

import tools.aqua.bgw.core.BoardGameApplication
import service.*
import entity.*
import gui.scenes.GameScene
import gui.scenes.MainMenuScene
import gui.scenes.ResultMenuScene

class TauchenApplication : BoardGameApplication("TauchenGame"), Refreshable {

    private val rootService = RootService()
    private val mainMenuScene = MainMenuScene(rootService,this)
    private val gameScene = GameScene(rootService,this)
    private val resultMenuScene = ResultMenuScene(rootService)


    init {
        rootService.addRefreshables(
            this,
            mainMenuScene,
            gameScene,
            resultMenuScene
        )
        registerMenuEvents()
      //  rootService.gameService.startGame(listOf("Bob","Alice"))
        rootService.gameService.startGame(listOf("Bob","Alice"))
        showMenuScene(mainMenuScene,0)
        showGameScene(gameScene)
        show()
    }


    private fun registerMenuEvents(){
        mainMenuScene.startButton.onMouseClicked ={
            refreshAfterStartGame()
        }

        mainMenuScene.exitButton.onMouseClicked = {
            exit()
        }
    }

    override fun refreshAfterStartGame() {
        hideMenuScene(500)
    }


    /**
     * The refreshAfterGameEnd method is called by the service layer after a game has ended.
     * It shows the result menu scene.
     *
     * @param winner The player who won the game
     */
    override fun refreshAfterEndGame(winner: Player) {

        showMenuScene(resultMenuScene)
    }


}

