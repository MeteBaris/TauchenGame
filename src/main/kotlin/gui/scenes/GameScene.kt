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


    override fun refreshAfterStartGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }
        cardMap.clear()

        val cardImageLoader = CardImageLoader()

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
        val game = rootService.currentGame
        checkNotNull(game)
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
        posX = 1400,
        posY = 425,
        DEFAULT_CARD_STACK_WIDTH,
        DEFAULT_CARD_STACK_HEIGHT,
        alignment = Alignment.CENTER,
        visual = ColorVisual(255, 255, 255, 50)


    )

    private val discardButton = Button(
        width = 150,
        height = 50,
        posX = 1020,
        posY = 680,
        text = "Discard"
    ).apply {
        onMouseClicked = {
            if (currentPlayerFinder().hand.size>8){
                rootService.playerActionService.discardCard(currentHandCard!!)
            }else{
                throw IllegalStateException("Cards in the hand must be more than 8 cards")
            }


        }
    }


    private val draw_Stack = CardStack<CardView>(
        posX = 400,
        posY = 410,
        DEFAULT_CARD_WIDTH,
        DEFAULT_CARD_HEIGHT,
        Alignment.CENTER,
        visual = cardImageLoader.backImage
    ).apply {
        onMouseClicked = {
            if (!currentPlayerFinder().hasPlayed){
                rootService.playerActionService.drawCard()
            }
            else {
                throw IllegalStateException("Please play a card from hand or end this turn")
            }
        }
    }

    private val draw_StackLabel = Label(
        posX = 400, posY = 360, text = "DRAW CARD"
    )

    /** place in the table where the game will be played */
    private var play_Stack: LinearLayout<CardView> =
        LinearLayout<CardView>(
            height = 220,
            width = 600,
            posX = 675,
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


    private val player1ScoreLabel = Label(
        width = 300,
        height = 50,
        posX = 1500,
        posY = 200,
        font = Font(size = 22),


        )
    private val player2ScoreLabel = Label(
        width = 300,
        height = 50,
        posX = 1500,
        posY = 300,
        font = Font(size = 22),

        )





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

            if (currentHandCard == null ) {
                throw IllegalStateException("No card selected.")
            } else if(!currentPlayerFinder().hasPlayed){

                endTurnButton.isDisabled = false
                swapCardButton.isDisabled = true
                // play the card using the logic in PlayerActionService
                rootService.playerActionService.playCard(currentHandCard!!)

                println(currentPlayerFinder().name)

                currentHandCard = null
            }


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

    private val swapCardButton = Button(
        width = 150,
        height = 50,
        posX = 820,
        posY = 680,
        text = "Swap Card"
    )

    private val endGameButton = Button(
        width = 150,
        height = 50,
        posX = 1800,
        posY = 50,
        text = "END GAME FOR TEST "
    ).apply{
        onMouseClicked = {
            rootService.currentGame!!.drawStack.clear()
        }
    }

    /**visualize all the components*/
    init {
        addComponents(
            player1Label,
            player2Label,
            player1Hand,
            player2Hand,
            player1CollectedStack,
            player2CollectedStack,
            player1ScoreLabel,
            player2ScoreLabel,
            draw_Stack,
            draw_StackLabel,
            play_Stack,
            swapCardButton,
            discardStack,
            endTurnButton,
            playCardButton,
            discardButton,
            endGameButton

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


        //checkAllStackViews(game)

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

        rootService.playerActionService.hasDrawn=false

        if (game.isPlayerOneActive){
            player1Hand.isDisabled = false
            player2Hand.isDisabled = true
        } else{
            player1Hand.isDisabled = true
            player2Hand.isDisabled = false
        }
        val currentPlayer = currentPlayerFinder()
        currentPlayer.hasPlayed = false

        endTurnButton.isDisabled = true
        swapCardButton.isDisabled = true
        playCardButton.isDisabled = false
        draw_Stack.isDisabled = false


        // update player labels to show whose turn it is
        player1Label.text = "${if (game.isPlayerOneActive) "(Your Turn) " else ""}${game.players[0].name}"
        player2Label.text = "${if (!game.isPlayerOneActive) "(Your Turn) " else ""}${game.players[1].name}"

        // set the current hand to the active player's hand
        currentPlayerHand = if (game.isPlayerOneActive) player1Hand else player2Hand
        otherPlayerHand = if (!game.isPlayerOneActive) player2Hand else player1Hand

        println("Start turn for: ${if (game.isPlayerOneActive) "Player 1" else "Player 2"}")
    }




    override fun refreshAfterDrawCard(lastCard: Card, hasToDiscard: Boolean) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }
        endTurnButton.isDisabled = false
        println("${player1Hand.components.size}   player1 hand view")
        println("${player2Hand.components.size}   player2 hand view")

        val currentPlayer = currentPlayerFinder()


        val cardView = cardMap[lastCard] as CardView

        when (currentPlayer) {
            game.players[0] -> moveCardView(cardView, player1Hand)
            game.players[1] -> moveCardView(cardView, player2Hand)
        }

        currentHandCard = lastCard
        /*
            if (game.playStack.size==2 ) {
                if (rootService.playerActionService.isCardValid(game.playStack[0], lastCard)
                    && rootService.playerActionService.isCardValid(game.playStack[1], lastCard)
                ) {

                    rootService.playerActionService.playCard(lastCard)

                } else {
                    println("you have to end turn")
                }
                currentPlayer.hasPlayed = true
                endTurnButton.isDisabled = false

                println("${game.players[0].hand.size}   player1 hand size")
                println("${game.players[1].hand.size}   player2 hand size")
            }
            */
        endTurnButton.isDisabled = false
        checkAllStackViews(game)
    }


    /**override function of refreshAfterEndTurn. */
    override fun refreshAfterEndTurn() {
        val game = rootService.currentGame
        checkNotNull(game) { "No game found." }

        val currentPlayer = currentPlayerFinder()

        val currentCollectionStack =
            if (currentPlayer == game.players[0]) player1CollectedStack else player2CollectedStack

        if (game.playStack.size == 3 && play_Stack.components.size == 3 && rootService.playerActionService.isTrio) {
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

            rootService.playerActionService.isTrio = false
            println("***********")
            println("${currentPlayer.name}'s collected stack now contains ${currentCollectionStack.components.size} cards.")
            println("***********")
        }

        updatePlayerHands()
        //   updatePlayerHands(player1Hand,player2Hand)

        swapCardButton.isDisabled = true
        playCardButton.isDisabled = true
    }

    override fun refreshAfterDiscardCard() {
        val game = rootService.currentGame
        checkNotNull(game)

        moveCardViewToDiscardStack(cardMap.forward(currentHandCard!!), discardStack)
        currentHandCard=null
        endTurnButton.isDisabled = false
    }

     override fun refreshAfterSwapCard() {

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
                //?????????
                this.frontVisual = frontVisual
            }
        }
    }
    private fun moveCardView(cardView: CardView, playerHandStack: LinearLayout<CardView>) {
        cardView.showFront()
        cardView.removeFromParent()
        playerHandStack.add(cardView)
    }

    private fun moveCardViewToDiscardStack(cardView: CardView, toDiscard: CardStack<CardView>) {
        cardView.showFront()
        cardView.removeFromParent()
        toDiscard.add(cardView)
    }
    private fun updatePlayerHands() {

        for (cardView in player1Hand) {
            cardView.flip()
        }
        for (cardView in player2Hand) {
            cardView.flip()
        }
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
            cardView.showBack()

            player2Hand.add(cardView)

            cardMap[card] = cardView
        }
    }


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