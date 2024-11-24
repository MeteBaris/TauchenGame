package gui.scenes


import entity.Player
import gui.Refreshable
import gui.TauchenApplication
import service.RootService
import tools.aqua.bgw.core.MenuScene

import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import java.awt.Color


class ResultMenuScene(private val rootService: RootService,val tauchenApplication: TauchenApplication) : MenuScene(400, 1080), Refreshable {
    // This pane is used to hold all components of the scene and easily center them on the screen
    val game = rootService.currentGame

    private val headlineLabel = Label(
        width = 300, height = 50, posX = 50, posY = 50,
        text = "Game Over",
        font = Font(size = 22)
    )

    private val p2Score = Label(width = 300, height = 35, posX = 50, posY = 125)

    private val p1Score = Label(width = 300, height = 35, posX = 50, posY = 160)

    private val gameResult = Label(width = 300, height = 35, posX = 50, posY = 195).apply {

    }

    val quitButton = Button(width = 140, height = 35, posX = 50, posY = 265, text = "Quit").apply {
        visual = ColorVisual(Color(221, 136, 136))
    }

    val newGameButton = Button(width = 140, height = 35, posX = 210, posY = 265, text = "New Game").apply {
        onMouseClicked = {
            rootService.gameService.startGame(mutableListOf(text,text))
        }
        visual = ColorVisual(Color(136, 221, 136))
    }

    init {
        opacity = .5
        addComponents(headlineLabel, p1Score, p2Score, gameResult, newGameButton, quitButton)
    }

    private fun Player.scoreString(): String = "${this.name} scored ${this.score} points."


    private fun TauchenApplication.gameResultString(): String {
        val game = rootService.currentGame
        checkNotNull(game) { "no game is active" }
        val p1Score = game.players[0].score
        val p2Score = game.players[1].score
        return when {
            p1Score - p2Score > 0 -> "${game.players[0].name} wins the game."
            p1Score - p2Score < 0 -> "${game.players[1].name} wins the game."
            else -> "Draw. No winner."
        }
    }


    override fun refreshAfterEndGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game running" }



        p1Score.text = game.players[0].scoreString()
        p2Score.text = game.players[1].scoreString()
        gameResult.text = tauchenApplication.gameResultString()

    }

}