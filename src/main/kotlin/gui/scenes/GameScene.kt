package gui.scenes


import entity.Card
import entity.Player
import entity.TauchenGame
import gui.LabeledStackView
import gui.Refreshable
import gui.TauchenApplication
import service.CardImageLoader
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

class GameScene(private val rootService: RootService, val tauchenApplication: TauchenApplication) :
    BoardGameScene(1920, 1080, background = ImageVisual("images/bg.jpg")), Refreshable {

    private val allCards: BidirectionalMap<Card, CardView> = BidirectionalMap()
    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()
    /**it generates current hand card for ...*/
    private var currentHandCard: Card? = null

    private val player1Label =
        Label(width = 300, height = 50, posX = 350, posY = 200, text = "Player 1", font = Font(size = 14))
    private val player1CollectedStack = LabeledStackView(posX = 1650, posY = 100, "collected")
    private val player2Label =
        Label(width = 300, height = 50, posX = 350, posY = 900, text = "Player 2", font = Font(size = 14))
    private val player2CollectedStack = LabeledStackView(posX = 1650, posY = 500, "collected")

    private val player1Hand: LinearLayout<CardView> =
        LinearLayout<CardView>(
            height = 220,
            width = 800,
            posX = 560,
            posY = 750,
            spacing = -70,
            alignment = Alignment.CENTER,
            visual = ColorVisual(255, 255, 255, 50)
        )
    var player2Hand: LinearLayout<CardView> =
        LinearLayout<CardView>(
            height = 220,
            width = 800,
            posX = 560,
            posY = 50,
            spacing = -70,
            alignment = Alignment.CENTER,
            visual = ColorVisual(255, 255, 255, 50)
        ).apply {
            rotation = 180.0
        }


    //do we need this? I think it is an extra func. I ll change it later!!!!
    private fun currentPlayerFinder(): Player {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        return if (game.isPlayerOneActive) {
            game.players[0]
        } else
            game.players[1]
    }

    private fun otherPlayerFinder(): Player {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        return if (!game.isPlayerOneActive) {
            game.players[0]
        } else
            game.players[1]
    }

    //????????????????????????
    private val discardStack = CardStack<CardView>(
        250, 300,
        width = 162,
        height = 250,
        alignment = Alignment.CENTER
    ).apply {
        onMouseClicked = {
            rootService.currentGame.let { game ->
                if (game != null) {
                    currentHandCard?.let { it1 -> rootService.playerActionService.discardCard(it1) }
                }
            }
        }
    }

    private val currentPlayerLabel = Label(
        width = 300,
        height = 50,
        posX = 1500,
        posY = 100,
        font = Font(size = 24, color = Color.BLACK)
    )


    val draw_Stack = LabeledStackView(
        posX = 400, posY = 360,
        "Draw Stack"
    ).apply {
        onMouseClicked = {
            rootService.currentGame?.let { game -> rootService.playerActionService.drawCard() }
        }
    }

    /** place in the table where the game will be played */
    var play_Stack: LinearLayout<CardView> =
        LinearLayout<CardView>(
            height = 220,
            width = 600,
            posX = 560,
            posY = 420,
            spacing = -50,
            alignment = Alignment.CENTER,
            visual = ColorVisual(255, 255, 255, 50)
        )


    /*   val play_Stack: CardStack<CardView> = CardStack(
           height = 200,
           width = 130,
           posX = 1040,
           posY = 360,
           visual = ColorVisual(255, 255, 255, 50)
       )
     */

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

    //it should be end turn button :(
    private val startTurnButton = Button(
        width = 150,
        height = 50,
        posX = 1400,
        posY = 825,
        text = "Start Turn"
    ).apply {
        onMouseClicked = {
            rootService.gameService.startTurn()
        }
    }

    //add a func with current player, after selecting a card it can be applied
   /* private val playCardButton = Button(
        width = 150,
        height = 50,
        posX = 620,
        posY = 680,
        text = "PlayCard"
    ).apply {//????????????
        onMouseClicked = {
            if (rootService.currentGame?.isPlayerOneActive == true) {
                var currentCard = currentHandCard
                checkNotNull(currentCard)
                rootService.playerActionService.playCard(currentCard)
                currentHandCard = null
            }
        }
    }
*/

    private val playCardButton = Button(
        width = 150,
        height = 50,
        posX = 620,
        posY = 680,
        text = "PlayCard"
    ).apply {//????????????
        onMouseClicked = {
            if (rootService.currentGame?.isPlayerOneActive == true) {
                var currentCard = currentHandCard
                checkNotNull(currentCard)
                rootService.playerActionService.playCard(currentCard)
                currentHandCard = null
            }
        }
    }


    /**visualize all the components*/
    init {
        addComponents(
            player1Label,
            player1Hand,
            player1CollectedStack,
            player2Label,
            player2Hand,
            player2CollectedStack,
            draw_Stack,
            play_Stack,
            discardStack,
            currentPlayerHand,
            otherPlayerHand,
            startTurnButton,
            playCardButton
        )
    }

    override fun refreshAfterStartGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }
        cardMap.clear()
        val cardImageLoader = CardImageLoader()

        initializeStackView(game.drawStack, draw_Stack, cardImageLoader)
        initializePlayersHandView(cardImageLoader)
        updatePlayerLabels(game)

        player1CollectedStack.clear()
        player2CollectedStack.clear()
    }

    private fun updatePlayerLabels(game: TauchenGame) {
        player1Label.text = "${if (game.isPlayerOneActive) "(starting) " else ""}${game.players[0].name}"
        player2Label.text = "${if (game.isPlayerOneActive) "(starting) " else ""}${game.players[1].name}"

    }


    /** refreshAfterPlayCard */
    override fun refreshAfterPlayCard() {
        val game = rootService.currentGame
        val currentCard = currentHandCard

        /** getting current player */
        val currentPlayer = currentPlayerFinder()

        checkNotNull(game) { "No game found." }
        checkNotNull(currentCard)
        /** checking if the cards actually is in the playStack*/
        //it can work, maybe not
        when (currentPlayer) {
            game.players[0] -> moveCardView(cardMap.forward(currentCard), play_Stack )
            game.players[1] -> moveCardView(cardMap.forward(currentCard), play_Stack )
        }
        checkAllStackViews(game)
    }

    /*
        override fun refreshAfterDrawCard() {
            val game = rootService.currentGame
            checkNotNull(game) { "No game found." }

            val currentPlayer = currentPlayerFinder()


            val drawnCard = game.drawStack.lastOrNull()
            checkNotNull(drawnCard) { "No card was drawn." }

            currentHandCard = drawnCard

            // Remove the card from the draw stack in the cardMap
            val lastComponent = draw_Stack.components.lastOrNull()
            if (lastComponent is CardView) {
                cardMap.backward(lastComponent).let { cardMap.remove(drawnCard to CardView(height = 200,
                    width = 130,
                    front = CardImageLoader().frontImageFor(drawnCard.suit, drawnCard.value),
                    back = CardImageLoader().backImage)) }
            }
            // Move the card to the player's hand or update the view
            when (currentPlayer) {
                game.players[0] -> {
                    cardMap.add(drawnCard to CardView( // Update cardMap with the new card
                        height = 200,
                        width = 130,
                        front = CardImageLoader().frontImageFor(drawnCard.suit, drawnCard.value),
                        back = CardImageLoader().backImage
                    ).also { cardView ->
                        moveCardView(cardView, player1Hand)
                    })
                }
                game.players[1] -> {
                    cardMap.add(drawnCard to CardView( // Update cardMap with the new card
                        height = 200,
                        width = 130,
                        front = CardImageLoader().frontImageFor(drawnCard.suit, drawnCard.value),
                        back = CardImageLoader().backImage
                    ).also { cardView ->
                        moveCardView(cardView, player2Hand)
                    })
                }
            }

            // Update the draw stack visually
            draw_Stack.clear()
            initializeStackView(game.drawStack, draw_Stack, CardImageLoader())

            checkAllStackViews(game)
        }
  */
        override fun refreshAfterDrawCard() {
            val game = rootService.currentGame
            checkNotNull(game) { "No game found." }

            val currentPlayer = currentPlayerFinder()

            // Get the card that was just drawn
            val drawnCard = game.drawStack.lastOrNull() // Adjust based on your game's draw logic
            checkNotNull(drawnCard) { "No card was drawn." }
        //?????????

            currentHandCard = drawnCard

            // Move the card to the player's hand or update the view
            when (currentPlayer) {
                game.players[0] -> moveCardView(cardMap.forward(drawnCard), player1Hand )
                game.players[1] -> moveCardView(cardMap.forward(drawnCard), player2Hand )
            }
            checkAllStackViews(game)
        }




  /*  private fun moveCardView(cardView: CardView, toStack: LabeledStackView, flip: Boolean = false) {
        if (flip) {
            when (cardView.currentSide) {
                CardView.CardSide.BACK -> cardView.showFront()
                CardView.CardSide.FRONT -> cardView.showBack()
            }
        }
        cardView.removeFromParent()
        toStack.add(cardView)
    }

   */

//????????????
    private fun moveCardView(cardView: CardView, toStack: LinearLayout<CardView>, flip: Boolean = false) {
        if (flip) {
            when (cardView.currentSide) {
                CardView.CardSide.BACK -> cardView.showFront()
                CardView.CardSide.FRONT -> cardView.showBack()
            }
        }
        cardView.removeFromParent()
        toStack.add(cardView)
    }
    private fun moveAllCardViews(movedCards: List<Card>,  toStack: LinearLayout<CardView>) {
        movedCards.forEach { card ->
            val cardView = cardMap.forward(card)
            moveCardView(cardView, toStack)
        }
    }



    private fun initializePlayersHandView(cardImageLoader: CardImageLoader) {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }
        val player1 = game.players[0]
        val player2 = game.players[1]


        // adding player1 view
        player1Hand.clear()
        player1.hand.toList().reversed().forEach { card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage
            ).apply {
                onMouseClicked = {
                    println("${card.suit}${card.value}")
                    currentHandCard = card
                }
            }
            player1Hand.visual
            player1Hand.add(cardView)
            cardMap.add(card to cardView)

        }
        player2Hand.clear()
        player2.hand.toList().reversed().forEach { card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage
            ).apply {
                onMouseClicked = {
                    println("${card.suit}${card.value}")
                    currentHandCard = card
                }
            }
            player2Hand.add(cardView)
            cardMap.add(card to cardView)
        }
    }



    /**It initializes Stack view from game.GameStack to GameScene.DrawStack*/
    private fun initializeStackView(
        stack: MutableList<Card>,
        stackView: LabeledStackView,
        cardImageLoader: CardImageLoader
    ) {
        stackView.clear()
        stack.toList().forEach { card ->
            val cardView = CardView(
                height = 200,
                width = 130,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage
            )
            stackView.add(cardView)
            cardMap.add(card to cardView)
        }
    }





    /**
     * Checks if the given [stackView] contains (in the correct order)
     * [CardView]s for all [Card]s currently in [stack].
     *
     * @throws IllegalStateException if a mismatch is found
     */
    private fun checkStackView(stack: MutableList<Card>, stackView: LabeledStackView) {

        check(stack.size == stackView.components.size) {
            "Stack size (${stack.size}) is not equal to view size (${stackView.components.size})"
        }

        val stackContents = stack.toList().reversed()

        for (i in 0 until stack.size) {
            val cardInView = cardMap.backward(stackView.components[i])
            val cardOnStack = stackContents[i]
            check(cardOnStack == cardInView) {
                "Card on stack ($cardOnStack) is not equal to card in view ($cardInView)"
            }
        }
    }


    private fun checkAllStackViews(game: TauchenGame) {

        checkStackView(game.drawStack, draw_Stack)
        checkStackView(game.players[0].hand, player1Hand as LabeledStackView)
        checkStackView(game.players[0].collectionStack, player1CollectedStack)
        checkStackView(game.players[1].hand, player2Hand as LabeledStackView)
        checkStackView(game.players[1].collectionStack, player2CollectedStack)
    }


}


