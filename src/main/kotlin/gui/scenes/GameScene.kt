package gui.scenes


import entity.Card
import entity.TauchenGame
import gui.LabeledStackView
import gui.Refreshable
import gui.TauchenApplication
import service.RootService
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.Alignment
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

class GameScene(private val rootService: RootService,val tauchenApplication: TauchenApplication) :
    BoardGameScene(1920, 1080,background = ImageVisual("images/bg.jpg") ), Refreshable {

    private val allCards: BidirectionalMap<Card, CardView> = BidirectionalMap()
    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    private val discardStack = CardStack<CardView>(250, 300,
        width = 162,
        height = 250,
        alignment = Alignment.CENTER ).apply {
        onMouseClicked = {
            rootService.currentGame.let { game ->
                if (game != null) {
                    //rootService.playerActionService.discardCard(card)
                }
            }
        }
    }

    private val player1Label = Label(posX = 400, posY = 920, text = "Player 1")


    private val player1Hand = LinearLayout<CardView>(
        width = 300,
        height = 50,
        posX = 895,
        posY = 800
    )
    private val player2Hand = LinearLayout<CardView>(
        width = 300,
        height = 50,
        posX = 895,
        posY = 50
    )

    private val player1CollectedStack = LabeledStackView(posX = 895, posY = 830, "collected")

    private val drawStack = LabeledStackView(posX = 250, posY = 600, "draw Stack").apply {
        onMouseClicked = {
            rootService.currentGame?.let { game -> rootService.cardService.createDrawStack() }
        }
    }
    private val player2Label = Label(posX = 400, posY = 1020, text = "Player 2")
    private val player2CollectedStack = LabeledStackView(posX = 895, posY = 870, "collected")


    private val currentPlayerLabel = Label(
        width = 300,
        height = 50,
        posX = 1500,
        posY = 100,
        font = Font(size = 24, color = Color.BLACK)
    )
    val draw_Stack: CardStack<CardView> = CardStack(
        height = 200,
        width = 130,
        posX = 750,
        posY = 360,
        visual = ColorVisual(255, 255, 255, 50)
    )
    val play_Stack: CardStack<CardView> = CardStack(
        height = 200,
        width = 130,
        posX = 1040,
        posY = 360,
        visual = ColorVisual(255, 255, 255, 50)
    )
    var currentPlayerHand: LinearLayout<CardView> = LinearLayout(
        height = 220,
        width = 800,
        posX = 560,
        posY = 750,
        spacing = -50,
        alignment = Alignment.CENTER,
        visual = ColorVisual(255, 255, 255, 50)
    )
    var otherPlayerHand: LinearLayout<CardView> = LinearLayout<CardView>(
        height = 220,
        width = 800,
        posX = 560,
        posY = 50,
        spacing = -50,
        alignment = Alignment.CENTER,
        visual = ColorVisual(255, 255, 255, 50)
    ).apply {
        rotation = 180.0
    }
    private val player1ScoreLabel = Label(
        width = 300,
        height = 50,
        posX = 1500,
        posY = 200,
        font = Font(size = 22, color = Color.RED)
    )
    private val player2ScoreLabel = Label(
        width = 300,
        height = 50,
        posX = 1500,
        posY = 300,
        font = Font(size = 22, color = Color.RED)
    )
    private val startTurnButton = Button(
        width = 150,
        height = 50,
        posX = 1500,
        posY = 850,
        text = "Start Turn"
    ).apply {
        onMouseClicked = {
            rootService.gameService.startTurn()
        }
    }

    private fun updatePlayerLabels(game: TauchenGame) {
        player1Label.text = "${if (game.isPlayerOneActive) "(starting) " else ""}${game.players[0].name}"
        player2Label.text = "${if (!game.isPlayerOneActive) "(starting) " else ""}${game.players[1].name}"

    }

    init {
        addComponents(
            draw_Stack,
            play_Stack,
            player1Hand,
            currentPlayerHand,
            otherPlayerHand,
        )
    }

    override fun refreshAfterStartGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }
        updatePlayerLabels(game)

    }


}