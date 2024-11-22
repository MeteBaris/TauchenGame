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

    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    /**it generates current hand card for ...*/
    private var currentHandCard: Card? = null


    override fun refreshAfterStartGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }
        cardMap.clear()

        val cardImageLoader = CardImageLoader()
        initializeDrawStackView(game.drawStack, draw_Stack, cardImageLoader)
        initializePlayersHandView(cardImageLoader)
        updatePlayerLabels(game)

        println("${currentPlayerFinder().name} is current player")

        player1CollectedStack.clear()
        player2CollectedStack.clear()
    }


    private val player1Label =
        Label(
            width = 300,
            height = 50,
            posX = 350, posY = 200,
            text = "Player 1",
            font = Font(size = 14)
        )

    private val player1CollectedStack = CardStack<CardView>(
        posX = 1650,
        posY = 100,
        visual = ColorVisual(255, 255, 255, 50)
    )

    private val player2Label =
        Label(
            width = 300,
            height = 50,
            posX = 350,
            posY = 900,
            text = "Player 2",
            font = Font(size = 14)
        )


    private val player2CollectedStack = CardStack<CardView>(
        posX = 1650,
        posY = 750,
        visual = ColorVisual(255, 255, 255, 50)
    )

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
        )

    private fun currentPlayerFinder(): Player {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        return if (game.isPlayerOneActive) {
            game.players[0]
        } else
            game.players[1]
    }
/*
    private fun otherPlayerFinder(): Player {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        return if (!game.isPlayerOneActive) {
            game.players[0]
        } else
            game.players[1]
    }

 */

    private val discardStack = CardStack<CardView>(
        250, 300,
        width = 100,
        height = 100,
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

    val draw_Stack = LabeledStackView(
        posX = 400, posY = 360,
        "Draw Stack"
    ).apply {
        onMouseClicked = {
            val game = rootService.currentGame
            checkNotNull(game) { "No game found." }

            // play the card using the logic in PlayerActionService
            rootService.playerActionService.drawCard()

        }
    }

    /** place in the table where the game will be played */
    private var play_Stack: LinearLayout<CardView> =
        LinearLayout<CardView>(
            height = 220,
            width = 600,
            posX = 560,
            posY = 420,
            spacing = -50,
            alignment = Alignment.CENTER,
            visual = ColorVisual(255, 255, 255, 50)
        )


    private var currentPlayerHand: LinearLayout<CardView> = LinearLayout(
        height = 220,
        width = 800,
        posX = 560,
        posY = 750,
        spacing = -50,
        alignment = Alignment.CENTER,
        visual = ColorVisual(255, 255, 255, 50)
    )
    private var otherPlayerHand: LinearLayout<CardView> = LinearLayout<CardView>(
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
  /*
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


   */
    //it should be end turn button :(
    private val startTurnButton = Button(
        width = 150,
        height = 50,
        posX = 1400,
        posY = 700,
        text = "Start Turn"
    ).apply {
        onMouseClicked = {

            rootService.gameService.startTurn()
        }
    }

    private val endTurnButton = Button(
        width = 150,
        height = 50,
        posX = 1400,
        posY = 825,
        text = "End Turn"
    ).apply {
        onMouseClicked = {
            rootService.gameService.endTurn()
        }
    }


    private val playCardButton = Button(
        width = 150,
        height = 50,
        posX = 620,
        posY = 680,
        text = "PlayCard"
    ).apply {
        visual = ColorVisual(221, 136, 136)
        onMouseClicked = {
            val game = rootService.currentGame
            checkNotNull(game) { "No game found." }



            val currentCard = currentHandCard
            checkNotNull(currentCard) { "No card selected." }

            // play the card using the logic in PlayerActionService
            rootService.playerActionService.playCard(currentCard)

            println(currentPlayerFinder().name)

            currentHandCard = null

        }
    }

    private val swapCardButton = Button(
        width = 150,
        height = 50,
        posX = 820,
        posY = 680,
        text = "Swap Card"
    )

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
            swapCardButton,
            discardStack,
            startTurnButton,
            endTurnButton,
            playCardButton,
        )
    }

    private fun updatePlayerLabels(game: TauchenGame) {
        player1Label.text = "${if (game.isPlayerOneActive) "(starting) " else ""}${game.players[0].name}"
        player2Label.text = "${if (!game.isPlayerOneActive) "(starting) " else ""}${game.players[1].name}"

    }


    /** refreshAfterPlayCard */
    override fun refreshAfterPlayCard() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }

        /** getting current player */
        val currentPlayer = currentPlayerFinder()

        val currentCard = currentHandCard
        checkNotNull(currentCard) { "No card selected." }

        checkNotNull(currentCard)
        /** checking if the cards actually is in the playStack*/
        //it can work, maybe not
        when (currentPlayer) {
            game.players[0] -> moveCardView(cardMap.forward(currentCard), play_Stack)
            game.players[1] -> moveCardView(cardMap.forward(currentCard), play_Stack)
        }
        println("playStack size  = " + game.playStack.size)
        println("playStack view Size = " + play_Stack.components.size)

        swapCardButton.isDisabled = true
        playCardButton.isDisabled = true
       // drawCardButton.isDisabled = true
        endTurnButton.isDisabled = false

        /*if (rootService.playerActionService.isTrio){
            moveCardViewToCardStack(play_Stack,player1CollectedStack)
            println(player1CollectedStack.components.size)
            rootService.playerActionService.isTrio = false
        }
         */
        //    checkAllStackViews(game)

    }

    private fun moveCardViewToCardStack(currentPlayStack : LinearLayout<CardView>, toCollectStack:CardStack<CardView>){
        currentPlayStack.components.forEach{view ->
            view.showFront()
            view.removeFromParent()
            toCollectStack.add(view)
        }
    }

 /*   private fun moveCardFromDrawStack(draw_stack : CardStack<CardView>, playerHandStack: LinearLayout<CardView>){
        val game= rootService.currentGame



        val currentDrawCardView =
        draw_stack.removeFromParent()
        playerHandStack.add(view)

    }


  */
    override fun refreshAfterStartTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }

       // swapCardButton.isDisabled = true
        playCardButton.isDisabled = false
        // drawCardButton.isDisabled = true
        endTurnButton.isDisabled = false


        game.isPlayerOneActive = !game.isPlayerOneActive

        // update player labels to show whose turn it is
        player1Label.text = "${if (game.isPlayerOneActive) "(Your Turn) " else ""}${game.players[0].name}"
        player2Label.text = "${if (!game.isPlayerOneActive) "(Your Turn) " else ""}${game.players[1].name}"

        // set the current hand to the active player's hand
        currentPlayerHand = if (game.isPlayerOneActive) player1Hand else player2Hand
        otherPlayerHand = if (!game.isPlayerOneActive) player2Hand else player1Hand

        println("Start turn for: ${if (game.isPlayerOneActive) "Player 1" else "Player 2"}")
    }

    private fun initializePlayersHandView(cardImageLoader: CardImageLoader) {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }
        cardMap.clear() // Clear the mapping

        val player1 = game.players[0]
        val player2 = game.players[1]

        // player 1's hand
        player1Hand.clear()
        player1.hand.forEach { card ->
            val cardView = createCardView(card, cardImageLoader)
            cardView.showFront()
            player1Hand.add(cardView)
            cardMap[card] = cardView
        }
        // player 2's hand
        player2Hand.clear()
        player2.hand.forEach { card ->
            val cardView = createCardView(card, cardImageLoader)
            cardView.showFront()
            player2Hand.add(cardView)
            cardMap[card] = cardView
        }


    }


    /*
     game.playStack.forEach{card ->
        player1CollectedStack.add((cardMap[card] as CardView).apply {
            showFront()
        })
     */

    /**Creating card views*/
    private fun createCardView(card: Card, cardImageLoader: CardImageLoader): CardView {
        return CardView(
            height = 200,
            width = 130,
            front = cardImageLoader.frontImageFor(card.suit, card.value),
            back = cardImageLoader.backImage
        ).apply {
            onMouseClicked = {
                currentHandCard = card
                println("${card.suit}${card.value}")
                this.frontVisual = frontVisual
            }
        }
    }

    /*
        //?????????????????????????????????????????
        override fun refreshAfterTakeTrio() {
            val game = rootService.currentGame
            checkNotNull(game) { "No started game found." }
            cardMap.clear() // Clear the mapping
            game.playStack.forEach {card ->
                player1CollectedStack.add(cardMap[card] as CardView)
                    /*.apply {
                    showFront()
                })
            }
        } */ */


    override fun refreshAfterDrawCard(lastCard: Card, hasToDiscard: Boolean) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }

        val currentPlayer = currentPlayerFinder()

        // get the card that was just drawn
        val drawnCard = currentPlayer.lastDrawnCard
        checkNotNull(drawnCard) { "No card was drawn." }
      //  val card = cardMap.forward(drawnCard)

       // currentHandCard = drawnCard

        // move the card to the player's hand or update the view
        when (currentPlayer) {
            game.players[0] -> moveCardView(cardMap.forward(drawnCard), player1Hand)
            game.players[1] -> moveCardView(cardMap.forward(drawnCard), player2Hand)
        }

        checkAllStackViews(game)
    }


    /**override function of refreshAfterEndTurn. */
    override fun refreshAfterEndTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }

        val currentPlayer = currentPlayerFinder()
        val currentCollectionStack =
            if (currentPlayer == game.players[0]) player1CollectedStack else player2CollectedStack

        if (game.playStack.size == 3 && play_Stack.components.size == 3) {
            println("Trio formed! Moving cards to ${currentPlayer.name}'s collection stack.")


            if (game.isPlayerOneActive) {
                moveCardViewToCardStack(play_Stack,player1CollectedStack)
                println(player1CollectedStack.components.size)
                rootService.playerActionService.isTrio = false
            } else {
                moveCardViewToCardStack(play_Stack,player2CollectedStack)
                println(player2CollectedStack.components.size)
                rootService.playerActionService.isTrio = false
            }
                // clear the logical play stack in the game
                play_Stack.clear()
                game.playStack.clear()

            println("***********")
            println(game.players[0].name)
            println("***********")
                println("${currentPlayer.name}'s collected stack now contains ${currentCollectionStack.components.size} cards.")


            endTurnButton.isDisabled = true


      /*      startTurnButton.isDisabled=false
            swapCardButton.isDisabled = true
            playCardButton.isDisabled = true
            // drawCardButton.isDisabled = true
            endTurnButton.isDisabled = false

       */
        }

    }

    /***/
    private fun moveCardView(cardView: CardView, playerHandStack: LinearLayout<CardView>) {
        cardView.showFront()
        cardView.removeFromParent()
        playerHandStack.add(cardView)
    }



    /***/
    private fun initializeDrawStackView(
        stack: MutableList<Card>,
        stackView: CardStack<CardView>,
        cardImageLoader: CardImageLoader
    ) {
        stackView.clear()
        stack.forEach { card ->
            val cardView = createCardView(card, cardImageLoader)
            stackView.add(cardView)
            cardMap[card] = cardView
            println("Mapped Card: $card -> CardView: $cardView")
        }
    }

    /**check the stack view  (MutableList - LabeledStackView)*/
    private fun checkStackView(stack: MutableList<Card>, stackView: LabeledStackView) {
        check(stack.size == stackView.components.size) {
            "Stack size (${stack.size}) is not equal to view size (${stackView.components.size})"
        }
        /**stack.forEachIndexed  pairs the cardViews and cards */
        stack.forEachIndexed { index, cardOnStack ->
            val cardInView = cardMap[cardOnStack]
            val cardViewInStack = stackView.components[index]
            check(cardViewInStack == cardInView) {
                "Card on stack ($cardOnStack) is not equal to card in view ($cardInView)"
            }
        }
    }

    /**this function check all Stack views*/
  private fun checkAllStackViews(game: TauchenGame) {
        checkLinearLayout(game.playStack, play_Stack)
        checkStackView(game.drawStack, draw_Stack)
        checkLinearLayout(game.players[0].hand, player1Hand)
        //   moveCardViewPlayStackToCollectionStack(play_Stack, player1CollectedStack)
        checkLinearLayout(game.players[1].hand, player2Hand)
        // moveCardViewPlayStackToCollectionStack(play_Stack, player2CollectedStack)
    }




    private fun checkLinearLayout(stack: MutableList<Card>, view: LinearLayout<CardView>) {
        check(stack.size == view.components.size) {
            "Stack size (${stack.size}) is not equal to view size (${view.components.size})"
        }
        /*  stack.forEachIndexed { index, cardOnStack ->
              val cardInView = cardMap[cardOnStack]
              val cardViewInStack = view.components[index]
              check(cardViewInStack == cardInView) {
                  "Card on stack ($cardOnStack) is not equal to card in view ($cardInView)"
              }
          }
         */
    }
}
