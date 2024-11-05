package gui

import entity.Card
import entity.Player

interface Refreshable {

    fun refreshAfterStartGame() {}

    fun refreshAfterStartTurn() {}

    fun refreshAfterEndTurn() {}

    fun refreshAfterPlayCard() {}

    fun refreshAfterDrawCard(card: Card)

    fun refreshAfterSwapCard() {}

    fun refreshAfterDiscardCard(){}

    fun refreshAfterEndGame(winner : Player){}


}