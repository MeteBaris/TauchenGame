package gui.scenes


import entity.Card
import entity.Player
import entity.TauchenGame
import gui.Refreshable
import gui.TauchenApplication
import service.CardImageLoader
import service.RootService
import tools.aqua.bgw.components.container.CardStack
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.*
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual




class GameScene(private val rootService: RootService, val tauchenApplication: TauchenApplication) :
    BoardGameScene(1920, 1080, background = ImageVisual("images/bg.jpg")), Refreshable {

    private val cardImageLoader = CardImageLoader()
    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    /**it generates current hand card for ...*/
    private var currentHandCard: Card? = null
    private var hasDrawn: Card? = null


    override fun refreshAfterStartGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }
        cardMap.clear()

        val cardImageLoader = CardImageLoader()

       /* game.drawStack.forEach { card ->
            cardMap[card] = CardView(
                posX = 400, posY = 360,
                width = DEFAULT_CARD_WIDTH,
                height = DEFAULT_CARD_HEIGHT,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage)}


        */
        initializeDrawStack(game.drawStack, draw_Stack, cardImageLoader)
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
            font = Font(size = 14),
            visual = ColorVisual(255, 255, 255, 50)
        )

    private val player1CollectedStack = CardStack<CardView>(
        posX = 1650,
        posY = 750,
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
        posY = 100,
        visual = ColorVisual(255, 255, 255, 50)
    )



    var player1Hand: LinearLayout<CardView> =
        LinearLayout<CardView>(
            height = 220,
            width = 800,
            posX = 560,
            posY = 50,
            spacing = -70,
            alignment = Alignment.CENTER,
            visual = ColorVisual(255, 255, 255, 50)
        )
    private val player2Hand: LinearLayout<CardView> =
        LinearLayout<CardView>(
            height = 220,
            width = 800,
            posX = 560,
            posY = 750,
            spacing = -70,
            alignment = Alignment.CENTER,
            visual = ColorVisual(255, 255, 255, 50)
        )

    private fun currentPlayerFinder(): Player {
        return if (rootService.currentGame.isPlayerOneActive) {
            rootService.currentGame.players[0]
        } else
            rootService.currentGame.players[1]
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
                currentHandCard?.let { it1 -> rootService.playerActionService.discardCard(it1) }
            }
        }
    }

  private val draw_Stack = CardStack<CardView>(
        posX = 400, posY = 360, DEFAULT_CARD_WIDTH, DEFAULT_CARD_HEIGHT,Alignment.CENTER, visual = cardImageLoader.backImage
    ).apply {
        onMouseClicked = {

            rootService.playerActionService.drawCard()
        }
    }

    private val draw_StackLabel = Label(
        posX = 350, posY = 360
    )

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
            endTurnButton.isDisabled = false
            swapCardButton.isDisabled = true
            this.isDisabled = true
            //     val currentCard = currentHandCard
            checkNotNull(currentHandCard) { "No card selected." }

            // play the card using the logic in PlayerActionService
            rootService.playerActionService.playCard(currentHandCard!!)

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
            player2Label,
            player1Hand,
            player2Hand,
            player1CollectedStack,
            player2CollectedStack,
            draw_Stack,
            draw_StackLabel,
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


        /** checking if the cards actually is in the playStack*/
        when (currentPlayer) {
            game.players[0] -> moveCardView(cardMap.forward(currentCard), play_Stack)
            game.players[1] -> moveCardView(cardMap.forward(currentCard), play_Stack)
        }
        println("playStack size  = " + game.playStack.size)
        println("playStack view Size = " + play_Stack.components.size)

        currentPlayer.hasPlayed = true

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

    private fun moveCardViewToCardStack(currentPlayStack: LinearLayout<CardView>, toCollectStack: CardStack<CardView>) {
        currentPlayStack.components.forEach { view ->
            view.showFront()
            view.removeFromParent()
            toCollectStack.add(view)
        }
    }

    override fun refreshAfterStartTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }


        endTurnButton.isDisabled = true
        swapCardButton.isDisabled = false
        playCardButton.isDisabled = false
        draw_Stack.isDisabled = false


        val currentPlayer = currentPlayerFinder()
        currentPlayer.hasPlayed = false


        // update player labels to show whose turn it is
        player1Label.text = "${if (game.isPlayerOneActive) "(Your Turn) " else ""}${game.players[0].name}"
        player2Label.text = "${if (!game.isPlayerOneActive) "(Your Turn) " else ""}${game.players[1].name}"

        // set the current hand to the active player's hand
        currentPlayerHand = if (game.isPlayerOneActive) player1Hand else player2Hand
        otherPlayerHand = if (!game.isPlayerOneActive) player2Hand else player1Hand

        println("Start turn for: ${if (game.isPlayerOneActive) "Player 1" else "Player 2"}")
    }



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


    override fun refreshAfterDrawCard(lastCard: Card, hasToDiscard: Boolean) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }

        println("${player1Hand.components.size}   player1 hand view")
        println("${player2Hand.components.size}   player2 hand view")

        val currentPlayer = currentPlayerFinder()
        currentHandCard = lastCard
        val cardView= cardMap[currentHandCard!!] as CardView

        when (currentPlayer) {
            game.players[0] -> moveCardView(cardView, player1Hand)
            game.players[1] -> moveCardView(cardView, player2Hand)
        }



        // move the card to the player's hand or update the view
        /*  when (currentPlayer) {
                game.players[0] -> moveCardView(cardView, player1Hand)
                game.players[1] -> moveCardView(cardView, player2Hand)
            //    game.players[1] -> moveCardView(cardMap.forward(lastCard), player2Hand)
            }


         */

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
                moveCardViewToCardStack(play_Stack, player1CollectedStack)
                println(player1CollectedStack.components.size)
                rootService.playerActionService.isTrio = false
            } else {
                moveCardViewToCardStack(play_Stack, player2CollectedStack)
                println(player2CollectedStack.components.size)
                rootService.playerActionService.isTrio = false
            }
            // clear the logical play stack in the game
            play_Stack.clear()
            game.playStack.clear()

            println("***********")
            println("${currentPlayer.name}'s collected stack now contains ${currentCollectionStack.components.size} cards.")
            println("***********")



        }
        startTurnButton.isDisabled = false
        swapCardButton.isDisabled = true
        playCardButton.isDisabled = true
    }

    /***/
    private fun moveCardView(cardView: CardView, playerHandStack: LinearLayout<CardView>) {
        cardView.showFront()
        cardView.removeFromParent()
        playerHandStack.add(cardView)
    }



    private fun initializePlayersHandView(cardImageLoader: CardImageLoader) {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }


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
 /*   private fun initializeDrawStack(
        stack: MutableList<Card>,
        stackView: CardView,
        cardImageLoader: CardImageLoader
    ) {

        stack.forEach { card ->

            val cardView = createCardView(card, cardImageLoader)
            cardView.showFront()
            stackView.add(cardView)
            cardMap[card] = cardView
        }
    }


  */
    /*game.drawStack.forEach { card ->
            cardMap[card] = CardView(
                posX = 400, posY = 360,
                width = DEFAULT_CARD_WIDTH,
                height = DEFAULT_CARD_HEIGHT,
                front = cardImageLoader.frontImageFor(card.suit, card.value),
                back = cardImageLoader.backImage*/


     private fun initializeDrawStack(
           stack: MutableList<Card>,
           stackView: CardStack<CardView>,
           cardImageLoader: CardImageLoader
       ) {
           stackView.clear()
           stack.forEach { card ->
               val cardView = createCardView(card, cardImageLoader)
               stackView.add(cardView)
               cardMap[card] = cardView
           }
       }







 /*
    private fun initializeDrawStack(
        stack: MutableList<Card>,
        stackView: CardStack<CardView>,
        cardImageLoader: CardImageLoader
    ) {
        stackView.clear()
        stack.forEach { card ->
            stackView.add(cardMap[card] as CardView)
            stackView.add(cardView)
            cardMap[card] = cardView
            println("Mapped Card: $card -> CardView: ${cardMap[card] as CardView}")
        }
    }

  */
/*
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



 */

    /**this function check all Stack views*/
    private fun checkAllStackViews(game: TauchenGame) {
        checkLinearLayout(game.playStack, play_Stack)
        // checkStackView(game.drawStack, draw_Stack)
        checkLinearLayout(game.players[0].hand, player1Hand)
        //   moveCardViewPlayStackToCollectionStack(play_Stack, player1CollectedStack)
        checkLinearLayout(game.players[1].hand, player2Hand)
        // moveCardViewPlayStackToCollectionStack(play_Stack, player2CollectedStack)
    }


    private fun checkLinearLayout(stack: MutableList<Card>, view: LinearLayout<CardView>) {

        stack.forEachIndexed { index, cardOnStack ->
            val cardInView = cardMap[cardOnStack]
            val cardViewInStack = view.components[index]
            check(cardViewInStack == cardInView) {
                "Card on stack ($cardOnStack) is not equal to card in view ($cardInView)"
            }
        }
        check(stack.size == view.components.size) {
            "Stack size (${stack.size}) is not equal to view size (${view.components.size})"
        }
    }
}