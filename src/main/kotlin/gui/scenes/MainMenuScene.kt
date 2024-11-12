package gui.scenes

import BUTTON_BG_FILE
import gui.Refreshable
import gui.*
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual

import java.awt.Color


/**
 * The main menu scene of the game.
 *
 * @param rootService The root service to which this scene belongs
 */


class MainMenuScene(val rootService: RootService,val tauchenApplication: TauchenApplication) :
    MenuScene(width = 400, height = 1080, background = ColorVisual(Color.WHITE)), Refreshable {

    private val headLineLabel = Label(
        width = 300,
        height = 50,
        posX = 50,
        posY = 50,
        text = "ENTER NAMES",
        font = Font(size = 30, color = Color.BLACK)
    )

    private val p1Label = Label(
        width =  100,
        height = 35,
        posX = 50,
        posY = 125,
        text = "Player 1:"
    )

    private val p1Input: TextField = TextField(
        width =  200,
        height = 35,
        posX = 150,
        posY = 125
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = this.text.isBlank() || p2Input.text.isBlank() || this.text == p2Input.text
        }
    }


    private val p2Label = Label(
        width =  100,
        height = 35,
        posX = 50,
        posY = 170,
        text = "Player 2:"
    )

    private val p2Input: TextField = TextField(
        width =  200,
        height = 35,
        posX = 150,
        posY = 170
    ).apply {
        onKeyTyped = {
            startButton.isDisabled = this.text.isBlank() || p1Input.text.isBlank() || this.text == p1Input.text
        }
    }

    val startButton = Button(
        width = 140,
        height = 35,
        posX = 50,
        posY = 240,
        text = "Start"
    ).apply {
        visual = ColorVisual(136,221,136)
        onMouseClicked = {
            rootService.gameService.startGame(
                listOf(p1Input.text.trim(), p2Input.text.trim()))
        }
    }

/*
    val startNewGame: Button = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 220,
        text = "New Game",
        font = Font(color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC),
        visual = ImageVisual("images/buttonBG.png")
    ).apply {
        onMouseClicked = {
            rootService.gameService.startGame(
                listOf(p1Input.text.trim(), p2Input.text.trim()))
        }
    }
    */
    val exitButton: Button = Button(
        height = 80,
        width = 200,
        posX = 50,
        posY = 330,
        text = "Exit",
        font = Font(color = Color.WHITE, fontStyle = Font.FontStyle.ITALIC),
        visual = ImageVisual("images/buttonBG.png")
    )

    val menuLabel: Label = Label(
        height = 100,
        width = 200,
        posX = 50,
        posY = 0,
        text = "Main menu",
        font = Font(fontWeight = Font.FontWeight.BOLD)
    )

    init {
        addComponents(
            headLineLabel,
           // menuLabel,
        //    startNewGame,
            p1Label,
            p1Input,
            p2Label,
            p2Input,
            menuLabel,
            startButton,
            exitButton
        )
    }

}