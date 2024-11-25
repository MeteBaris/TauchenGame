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
     * Indicates whether a trio has been completed.
     */
    var isTrio: Boolean = false
    var hasDrawn: Boolean = false

    /**
     * The playCard(card: Card) method allows a player to play a card from their hand.
     * This card is placed in the center of the table, provided it complies with the game rules.
     * There are different situations depends on number of the cards on table
     * @param card The card to be played.
     * @throws IllegalStateException if the card does not match the requirements to
     * */
    fun playCard(card: Card) {

        val game = rootService.currentGame
        checkNotNull(game)


        val currentPlayer: Player = if (game.isPlayerOneActive) {
            game.players[0]
        } else
            game.players[1]
        if (!currentPlayer.hand.contains(card)) {
            throw IllegalArgumentException("Card not found in hand")
        }
        /**If there is not any card in the table, all cards are playable*/
        if (game.playStack.size == 0) {
            game.playStack.add(card)
            currentPlayer.hand.remove(card)
            currentPlayer.hasPlayed = true

            println("${game.players[0].hand.size}  first player hand size after first played card")
            println("${game.players[1].hand.size}  second player hand size after first played card")
            onAllRefreshables {
                refreshAfterPlayCard()
            }
        } else if (game.playStack.size == 1) {

            if (isCardValid(game.playStack[0], card)) {
                game.playStack.add(card)
                currentPlayer.hand.remove(card)

                println("${game.players[0].hand.size}  first player hand size after first played card")
                println("${game.players[1].hand.size}  second player hand size after first played card")
                onAllRefreshables {
                    refreshAfterPlayCard()
                }

            } else
                throw IllegalStateException("No matching card that you can play.")

        } else if (game.playStack.size == 2) {
            if (game.playStack[0].suit == game.playStack[1].suit && game.playStack[0].suit == card.suit &&
                game.playStack[1].suit == card.suit
            ) {

                game.playStack.add(card)
                currentPlayer.hand.remove(card)
                currentPlayer.collectionStack.addAll(game.playStack)
                isTrio = true
                currentPlayer.score += 5

                game.players[0].hasSpecialAction = true
                game.players[1].hasSpecialAction = true

                println("${game.players[0].hand.size}  first player hand size after first played card")
                println("${game.players[1].hand.size}  second player hand size after first played card")
                onAllRefreshables {
                    refreshAfterPlayCard()
                }

            } else if (game.playStack[0].value == game.playStack[1].value && game.playStack[0].value == card.value &&
                game.playStack[1].value == card.value
            ) {
                game.playStack.add(card)
                currentPlayer.hand.remove(card)
                currentPlayer.collectionStack.addAll(game.playStack)

                isTrio = true
                currentPlayer.hasPlayed = true
                game.players[0].hasSpecialAction = true
                game.players[1].hasSpecialAction = true

                println("${game.players[0].hand.size}  first player hand size after first played card")
                println("${game.players[1].hand.size}  second player hand size after first played card")
                onAllRefreshables {
                    refreshAfterPlayCard()
                }

            } else if (game.playStack[0].suit == game.playStack[1].suit && game.playStack[0].suit == card.suit &&
                game.playStack[1].suit == card.suit
            ) {
                game.playStack.add(card)
                currentPlayer.hand.remove(card)
                currentPlayer.collectionStack.addAll(game.playStack)

                isTrio = true
                currentPlayer.score += 5
                currentPlayer.hasPlayed = true
                game.players[0].hasSpecialAction = true
                game.players[1].hasSpecialAction = true

                println("${game.players[0].hand.size}  first player hand size after first played card")
                println("${game.players[1].hand.size}  second player hand size after first played card")
                onAllRefreshables {
                    refreshAfterPlayCard()
                }

            } else if (game.playStack[0].value == game.playStack[1].value && game.playStack[0].value == card.value &&
                game.playStack[1].value == card.value
            ) {
                game.playStack.add(card)
                currentPlayer.hand.remove(card)
                currentPlayer.collectionStack.addAll(game.playStack)

                isTrio = true
                currentPlayer.score += 20

                currentPlayer.hasPlayed = true

                game.players[0].hasSpecialAction = true
                game.players[1].hasSpecialAction = true

                println("${game.players[0].hand.size}  first player hand size after first played card")
                println("${game.players[1].hand.size}  second player hand size after first played card")
                onAllRefreshables {
                    refreshAfterPlayCard()
                }
            }

        } else {
            throw IllegalStateException("No matching card that you can play.")
        }

    }

    /**
     * Validates if a card can be played on top of another card in the play stack.
     *
     * @param stackCard The card already in the play stack.
     * @param card The card the player intends to play.
     * @return `true` if the card can be played, `false` otherwise.
     */
    fun isCardValid(stackCard: Card, card: Card): Boolean {
        return stackCard.suit == card.suit || stackCard.value == card.value
    }

    /**
     * Allows the player to draw a card from the draw stack. The drawn card is
     * added to the player's hand. If the player's hand exceeds 8 cards after
     * drawing, they will need to discard a card.
     */
    fun drawCard() {
        val game = rootService.currentGame
        checkNotNull(game)

        if (game.drawStack.isNotEmpty()) {
            if (!hasDrawn) {


                val currentPlayer =
                    if (game.isPlayerOneActive) game.players[0]
                    else game.players[1]

                if (!currentPlayer.hasPlayed) {
                    val drawnCard = game.drawStack.removeLast()

                    currentPlayer.lastDrawnCard = drawnCard
                    currentPlayer.hand.add(currentPlayer.lastDrawnCard!!)


                } else {
                    println("${currentPlayer.name} played already. please use the end turn button")
                }
                hasDrawn = true
                onAllRefreshables {
                    refreshAfterDrawCard(currentPlayer.lastDrawnCard!!, hasToDiscard(currentPlayer))
                }
                currentPlayer.lastDrawnCard = null
            }
        } else
            rootService.gameService.endGame()
    }

    /**
     * Determines whether a player needs to discard a card, based on the size
     * of their hand.
     *
     * @param player The player to check.
     * @return `true` if the player needs to discard a card, `false` otherwise.
     */
    fun hasToDiscard(player: Player): Boolean {

        return player.hand.size > 8
    }

    /**this is a special action. After this the property hasSpecialAction property of player will turn false*/

    fun swapCard(cardTaken: Card, cardPlaced: Card) {
        val game = rootService.currentGame
        checkNotNull(game)
        val currentPlayer =
            if (game.isPlayerOneActive) game.players[0]
            else game.players[1]
        if (currentPlayer.hasSpecialAction) {
            if (game.playStack.all {
                    it.suit == cardTaken.suit || it.value == cardTaken.value
                            && (game.playStack.size in 1..2)
                }) {
                game.playStack.add(cardPlaced)
                currentPlayer.hand.remove(cardPlaced)
                currentPlayer.hand.add(cardTaken)
                game.playStack.remove(cardTaken)
                currentPlayer.hasSpecialAction = false
                currentPlayer.hasPlayed = true
                onAllRefreshables {
                    refreshAfterSwapCard()
                }
            } else
                throw IllegalArgumentException("Selected card is not valid")
        }
    }


    /**
     * Allows the player to discard a card from their hand to the discard stack
     * if their hand size exceeds the allowed limit.
     *
     * @param card The card to be discarded.
     */
    fun discardCard(card: Card) {
        val game = rootService.currentGame
        checkNotNull(game)
        val currentPlayer = if (game.isPlayerOneActive) game.players[0]
        else game.players[1]

        if (hasToDiscard(currentPlayer)) {
            game.discardStack.add(card)
            currentPlayer.hand.remove(card)
        }

        onAllRefreshables {
            refreshAfterDiscardCard()
        }
    }

}