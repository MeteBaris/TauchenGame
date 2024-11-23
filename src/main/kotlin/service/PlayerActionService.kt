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

    var isTrio:Boolean = false

    /**
     * The playCard(card: Card) method allows a player to play a card from their hand.
     * This card is placed in the center of the table, provided it complies with the game rules.
     * There are different situations depends on number of the cards on table(playStack)
     * */
    // first with gui side selectCard function, then playCard() or drawCard() or SwapCard() or discardCard()
    fun playCard(card: Card) {

        val game = rootService.currentGame
        checkNotNull(game)
        val playStack = game.playStack
        val currentPlayer: Player = if (game.isPlayerOneActive) {
            game.players[0]
        } else
            game.players[1]

        /**If there is not any card in the table, all cards are playable*/
        if (game.playStack.size == 0) {
            game.playStack.add(card)
            currentPlayer.hand.remove(card)
        } else if (game.playStack.size == 1) {

            if (isCardValid(game.playStack[0], card)) {
                game.playStack.add(card)
                currentPlayer.hand.remove(card)

            } else
                throw IllegalStateException("No matching card that you can play.")

        } else if (game.playStack.size == 2) {
            if (playStack[0].suit == playStack[1].suit && playStack[0].suit == card.suit) {
                playStack.add(card)
                currentPlayer.hand.remove(card)
                currentPlayer.collectionStack.addAll(playStack)
                println(currentPlayer.collectionStack)
                isTrio = true
                currentPlayer.score += 5

            } else if (playStack[0].value == playStack[1].value && playStack[0].value == card.value) {
                playStack.add(card)
                currentPlayer.hand.remove(card)
                currentPlayer.collectionStack.addAll(playStack)
                println(currentPlayer.collectionStack)

                isTrio = true

                currentPlayer.score += 20

            } else if (playStack[0].suit == playStack[1].suit && playStack[0].suit == card.suit) {
                playStack.add(card)
                currentPlayer.hand.remove(card)
                currentPlayer.collectionStack.addAll(playStack)
                println(currentPlayer.collectionStack)

                isTrio = true
                currentPlayer.score += 5

            } else if (playStack[0].value == playStack[1].value && playStack[0].value == card.value) {
                playStack.add(card)
                currentPlayer.hand.remove(card)
                currentPlayer.collectionStack.addAll(playStack)
                println(currentPlayer.collectionStack)

                isTrio = true
                currentPlayer.score += 20

            } else {
                throw IllegalStateException("Invalid move. Only one matching card may be played.")
            }
        } else {
            throw IllegalStateException("No matching card that you can play.") // alternative -> swapCard
        }
        currentPlayer.hasPlayed = true

        println("${game.players[0].hand.size}  first player hand size after first played card")
        println("${game.players[1].hand.size}  second player hand size after first played card")
        onAllRefreshables {
            refreshAfterPlayCard()
        }
    }

    /**It controls if the card is playable*/
    private fun isCardValid(stackCard: Card, card: Card): Boolean {
        return stackCard.suit == card.suit || stackCard.value == card.value
    }

    fun drawCard() {
        val game = rootService.currentGame
        checkNotNull(game)

        val currentPlayer =
            if (game.isPlayerOneActive) game.players[0]
            else game.players[1]

        if (!currentPlayer.hasPlayed && game.playStack.size>0){
            println(game.drawStack.size)
            println("*****")
            val drawnCard = game.drawStack.removeLast()
            println(game.drawStack.size)


            currentPlayer.hand.add(drawnCard)
            currentPlayer.lastDrawnCard = drawnCard

            println("${currentPlayer.hand.size}" )
        }else{
            println("${currentPlayer.name} played already. please use the end turn button")
        }

        onAllRefreshables {
            refreshAfterDrawCard(currentPlayer.lastDrawnCard!!, hasToDiscard(currentPlayer))
        }
       // currentPlayer.lastDrawnCard = null
    }

    private fun hasToDiscard(player: Player): Boolean {

        return player.hand.size > 8
    }

    /*  fun drawCard(){
          val game = rootService.currentGame
          checkNotNull(game)
          val currentPlayer =
              if (game.isPlayerOneActive) game.players[0]
              else game.players[1]

          if(game.drawStack.isEmpty()) {
              throw IllegalStateException("Draw pile is empty")
          }
          if(currentPlayer.hasSpecialAction){
              throw IllegalStateException("Action taken")
          }
          val drawnCard = game.drawStack.lastOrNull() // draw the card from drawPile

          if ()
          currentPlayer.hand.add(drawnCard!!)
          currentPlayer.hasSpecialAction = true
          currentPlayer.lastDrawnCard = drawnCard
          onAllRefreshable { refreshAfterDrawCard(drawnCard!!) }
          // if the drawPile is empty after the draw, we set the game over but allow playing the card
          if(game.drawStack.isEmpty()){
              playCard(drawnCard!!)
          }



     */
    /**this is a special action. After this the property hasSpecialAction property of player will turn false*/
    // I think it does not work
    fun swapCard(cardTaken: Card, cardPlaced: Card) {
        val game = rootService.currentGame
        checkNotNull(game)
        val currentPlayer =
            if (game.isPlayerOneActive) game.players[0]
            else game.players[1]
        when {
            game.playStack.size == 1 -> {
                game.playStack.add(cardPlaced)
                currentPlayer.hand.remove(cardPlaced)
                currentPlayer.hand.add(cardTaken)
                game.playStack.remove(cardTaken)
            }

            game.playStack.size == 2 -> {
                if (game.playStack.last().suit == cardPlaced.suit || game.playStack.last().value == cardPlaced.value) {
                    game.playStack.add(cardPlaced)
                    currentPlayer.hand.remove(cardPlaced)
                    currentPlayer.hand.add(game.playStack.first())
                    game.playStack.remove(game.playStack.first())
                } else if (game.playStack.first().suit == cardPlaced.suit || game.playStack.first().value == cardPlaced.value) {
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
        val game = rootService.currentGame
        checkNotNull(game)
        val currentPlayer = if (game.isPlayerOneActive) game.players[0]
        else game.players[1]

        if (currentPlayer.hand.size != 9) {
            throw IllegalStateException("You can not play a card, you should discard a card")
        }
        game.discardStack.add(card)
        currentPlayer.hand.remove(card)
        onAllRefreshables {
            refreshAfterDiscardCard()
        }
    }

}