package gui

import tools.aqua.bgw.core.BoardGameApplication
import service.*
import entity.*
import gui.scenes.GameScene
import gui.scenes.MainMenuScene
import gui.scenes.NextPlayerScene
import gui.scenes.ResultMenuScene

/**
 * The main application class for the Tauchen game.
 *
 * This class initializes the game application and manages the transitions between
 * different scenes such as the main menu, game scene, and result menu. It also
 * acts as a refreshable component to update the GUI based on events from the
 * service layer.
 * @property [rootService]  The root service that handles the core game logic and state.
 * @property [mainMenuScene] The main menu scene displayed at the start of the game.
 * @property [gameScene] The game scene where the main gameplay occurs.
 * @property [resultMenuScene] The result menu scene displayed after the game ends.
 * @property [nextPlayerScene] The next player scene displayed between turns to manage transitions.
 * @constructor Creates the TauchenApplication and initializes all required scenes
 * and the root service.
 */
class TauchenApplication : BoardGameApplication("TauchenGame"), Refreshable {

    private val rootService = RootService()
    private val mainMenuScene = MainMenuScene(rootService,this)
    private val gameScene = GameScene(rootService,this)
    private val resultMenuScene = ResultMenuScene(rootService,this)
    private val nextPlayerScene = NextPlayerScene(rootService,this)


    /**
     * Initializes the application:
     * - Adds all scenes to the list of refreshables for updates from the service layer.
     * - Registers event listeners for menu interactions.
     * - Displays the initial main menu scene.
     */
    init {
        rootService.addRefreshables(
            this,
            mainMenuScene,
            gameScene,
            resultMenuScene,
            nextPlayerScene
        )
        registerMenuEvents()
        showMenuScene(mainMenuScene,0)
        showGameScene(gameScene)
       // showMenuScene(resultMenuScene)
    }

    /**
     * Registers event listeners for menu interactions, such as exiting the game.
     *
     * - Sets the behavior for the main menu's exit button.
     */
    private fun registerMenuEvents(){

        mainMenuScene.exitButton.onMouseClicked = {
            exit()
        }
    }

    /**
     * Refreshes the GUI after the game starts.
     *
     * - Hides the current menu scene.
     */
    override fun refreshAfterStartGame() {
        hideMenuScene()
    }
    /**
     * Refreshes the GUI at the start of a turn.
     *
     * - Hides any active menu scenes.
     * - Displays the game scene for the current player's turn.
     */
    override fun refreshAfterStartTurn() {
        this.hideMenuScene()
        this.showGameScene(gameScene)
    }

    /**
     * Refreshes the GUI at the end of a turn.
     *
     * - Displays the next player scene to handle the transition between turns.
     */
    override fun refreshAfterEndTurn() {
        this.showMenuScene(nextPlayerScene)
    }

    /**
     * Refreshes the GUI after the game ends.
     *
     * - Displays the result menu scene to show the final results.
     */
    override fun refreshAfterEndGame() {

        showMenuScene(resultMenuScene)
    }


}

