package gui.scenes

import gui.Refreshable
import gui.TauchenApplication
import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import service.*

class NextPlayerScene(private val rootService: RootService,val tauchenApplication: TauchenApplication): MenuScene(400, 1080),
    Refreshable {
        private val headlineLabel =Label(
            width = 300, height = 50,
            posX = 50, posY = 50,
            text = "Tauchen Game",
            font = Font(size = 24)
        )
    private val NextPlayerLabel = Label(
        width = 300, height = 50,
        posX = 50, posY = 125,
        text = "Next player is:",
        font = Font(size = 22)
    )
    private val NextPlayerLabelName = Label(
        width = 300, height = 50,
        posX = 180, posY = 125,
        text = "x",
        font = Font(size = 22)
    )

    val startButton = Button(
        width = 140, height = 50,
        posX = 140, posY = 400,
        text = "Start Next Turn"
    ).apply {
        visual = ColorVisual(136, 221, 136)
        onMouseClicked = {
            rootService.gameService.startTurn()
        }
    }
    init {
        opacity = .5
        addComponents(
            startButton,
            NextPlayerLabel,
            headlineLabel,
            NextPlayerLabelName
        )
    }

    override fun refreshAfterEndTurn() {
        val game = rootService.currentGame
        checkNotNull(game)
        if (game.isPlayerOneActive) {
            NextPlayerLabelName.text = game.players[0].name
        } else{
            NextPlayerLabelName.text = game.players[1].name
        }
    }
}