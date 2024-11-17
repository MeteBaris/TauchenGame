package gui

import entity.Card
import entity.Player

interface Refreshable {

    fun refreshAfterStartGame() {}

    fun refreshAfterStartTurn() {}

    fun refreshAfterEndTurn() {}

    fun refreshAfterPlayCard() {}

    fun refreshAfterDrawCard(){}

    fun refreshAfterSwapCard() {}

    fun refreshAfterDiscardCard(){}

    fun refreshAfterEndGame(winner : Player){}

    fun refreshAfterPlayAgain(){}


}