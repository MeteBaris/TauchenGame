package service

import entity.Card
import entity.*

/**
 * This service provides functionality related to player actions, such as playing a card,
 * drawing a card, swapping cards and discarding a card
 * @param rootService The root service to which this service belongs
 * */
class PlayerActionService(private val rootService: RootService) :
    AbstractRefreshingService() {



    /**
     * The playCard(card: Card) method allows a player to play a card from their hand.
     * This card is placed in the center of the table, provided it complies with the game rules.
     * There are different situations depends on number of the cards on table(playStack)
     * */
    // first with gui side selectCard function, then playCard() or drawCard() or SwapCard() or discardCard()
    fun playCard(card: Card) {

        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }
        val currentPlayer: Player = if (game.isPlayerOneActive) {
            game.players[0]

        } else
            game.players[1]


        /**If there is not any card in the table, all cards are playable*/
        if (game.playStack.size == 0) {
            rootService.currentGame!!.playStack.add(card)
            currentPlayer.hand.remove(card)
        } else if (game.playStack.size == 1) {

            if (isCardValid(rootService.currentGame?.playStack!![0], card)) {
                rootService.currentGame!!.playStack.add(card)
                currentPlayer.hand.remove(card)
            } else// do we need throw IllegalStateException or just drawCard?
                throw IllegalStateException("No matching card that you can play.")

        }
        // playStack has 2 carts
        else if (game.playStack.size == 2) {
            trioFormedHandling(card)
        } else {
            throw IllegalStateException("No matching card that you can play.") // alternativ -> swapCard
        }
        onAllRefreshables {
            refreshAfterPlayCard()
        }


    }

    /**It controls if the card is playable*/
    fun isCardValid(stackCard: Card, card: Card): Boolean {
        return stackCard.suit == card.suit || stackCard.value == card.value
    }

    /**trioFormedHandling returns a boolean typ. It checks the trio situation*/
    //???????
    fun trioFormedHandling(card: Card): Boolean {
        val game = rootService.currentGame
        checkNotNull(game) { "No game is currently active" }

        val playStack = rootService.currentGame?.playStack!!

        val currentPlayer: Player = if (game.isPlayerOneActive) {
            game.players[0]
        } else
            game.players[1]

        if (playStack.size==2){

        if (playStack[0].suit == playStack[1].suit && playStack[0].suit == card.suit) {
            currentPlayer.collectionStack.addAll(playStack)
            currentPlayer.collectionStack.add(card)
            playStack.clear()
            currentPlayer.score += 5
        } else if (playStack[0].value == playStack[1].value && playStack[0].value == card.value) {
            currentPlayer.collectionStack.addAll(playStack)
            currentPlayer.collectionStack.add(card)
            playStack.clear()
            currentPlayer.score += 20
        } else {
            throw IllegalStateException("Invalid move. Only one matching card may be played.")
        }

        if (playStack[0].suit == playStack[1].suit && playStack[0].suit == card.suit) {
            currentPlayer.collectionStack.addAll(playStack)
            currentPlayer.collectionStack.add(card)
            playStack.clear()
            currentPlayer.score += 5
        } else if (playStack[0].value == playStack[1].value && playStack[0].value == card.value) {
            currentPlayer.collectionStack.addAll(playStack)
            currentPlayer.collectionStack.add(card)
            playStack.clear()
            currentPlayer.score += 20
        } else {
            throw IllegalStateException("Invalid move. Only one matching card may be played.")
        }

        }

        return false
    }

    /**The drawCard() method allows a player to draw a card from the draw pile.
     * This card is added to their hand.
     * */
//
    fun drawCard() {
        val game = rootService.currentGame

        checkNotNull(game) { "No game currently running." }
        print(game.drawStack.size)
        val currentPlayer =
            if (game.isPlayerOneActive) game.players[0]
            else game.players[1]

        val drawnCard = if (game.drawStack.isNotEmpty()) {
            game.drawStack.removeFirst()
        } else {
            throw IllegalStateException("The drawStack is empty.")
        }

        currentPlayer.hand.add(drawnCard)
        if (game.playStack.isEmpty())
            playCard(drawnCard)
        else if (game.playStack.size==1 && isCardValid(drawnCard,rootService.currentGame?.playStack!![0]))
            playCard(drawnCard)
        else if (trioFormedHandling(drawnCard))  {
            playCard(drawnCard)
        }

        onAllRefreshables {
            refreshAfterDrawCard()
        }
    }

    /**this is a special action. After this the property hasSpecialAction property of player will turn false*/
    //???????
    /* fun swapCard(card: Card) {
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running." }
        val currentPlayer =
            if (game.isPlayerOneActive) game.players[0]
            else game.players[1]

        val replacementCard = if (game.playStack.isNotEmpty() && game.playStack.size == 2) {
            game.playStack.removeFirst()
        }
        else if(game.playStack.isNotEmpty() && game.playStack.size == 1){
            game.playStack.removeFirst()
        }
        else {
            throw IllegalStateException("There is no card in the middle to swap.")
        }
        currentPlayer.hand.remove(card)
        currentPlayer.hand.add(replacementCard)
        game.playStack.add(card)

        onAllRefreshables {
            refreshAfterSwapCard()
        }

    }

     */


    fun swapCard(cardTaken: Card, cardPlaced: Card){
        val game = rootService.currentGame

        checkNotNull(game) {"No game is currently active"}
        val currentPlayer =
            if (game.isPlayerOneActive) game.players[0]
            else game.players[1]



        when{
            game.playStack.size == 1 -> {
                game.playStack.add(cardPlaced)
                currentPlayer.hand.remove(cardPlaced)
                currentPlayer.hand.add(cardTaken)
                game.playStack.remove(cardTaken)
            }
            game.playStack.size == 2 -> {
                if(game.playStack.last().suit == cardPlaced.suit || game.playStack.last().value == cardPlaced.value){
                    game.playStack.add(cardPlaced)
                    currentPlayer.hand.remove(cardPlaced)
                    currentPlayer.hand.add(game.playStack.first())
                    game.playStack.remove(game.playStack.first())
                }else if(game.playStack.first().suit == cardPlaced.suit || game.playStack.first().value == cardPlaced.value){
                    game.playStack.add(cardPlaced)
                    currentPlayer.hand.remove(cardPlaced)
                    currentPlayer.hand.add(game.playStack.last())
                    game.playStack.remove(game.playStack.last())
                }
            }
        }

        currentPlayer.hasSpecialAction = false

        onAllRefreshables {
            refreshAfterSwapCard()
        }
    }

    /**if there is more than 8 card in any player hand, player should discard one of them*/
    fun discardCard(card: Card) {
        checkNotNull(rootService.currentGame) { "No game currently running." }
        val currentPlayer = if (rootService.currentGame!!.isPlayerOneActive) rootService.currentGame!!.players[0]
        else rootService.currentGame!!.players[1]

        if (currentPlayer.hand.size != 9) {
            throw IllegalStateException("You can not play a card, you should discard a card")
        }
        rootService.currentGame!!.discardStack.add(card)
        currentPlayer.hand.remove(card)
        onAllRefreshables {
            refreshAfterDiscardCard()
        }
    }

}