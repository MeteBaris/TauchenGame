package gui

import entity.Card
import entity.Player


interface Refreshable {

    fun refreshAfterStartGame() {}

    fun refreshAfterStartTurn() {}

    fun refreshAfterEndTurn() {}

    fun refreshAfterPlayCard() {}

    fun refreshAfterDrawCard(lastCard: Card, hasToDiscard: Boolean){}

    fun refreshAfterSwapCard() {}

    fun refreshAfterDiscardCard(){}

    fun refreshAfterEndGame(winner : Player){}

    fun refreshAfterPlayAgain(){}
    fun refreshAfterTakeTrio() {
    }



}