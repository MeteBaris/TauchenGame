package service

import entity.*
import gui.Refreshable
import service.*

class TestRefreshable(val rootService: RootService) : Refreshable{

    var refreshAfterStartGame : Boolean =false
        private set

    var refreshAfterStartTurn : Boolean =false
        private set

    var refreshAfterEndTurn : Boolean =false
        private set

    var refreshAfterPlayCard : Boolean =false
        private set

    var refreshAfterDrawCard : Boolean =false
        private set

    var refreshAfterSwapCard : Boolean =false
        private set

    var refreshAfterDiscardCard : Boolean =false
        private set

    var refreshAfterEndGame : Boolean =false
        private set



    fun reset(){
        refreshAfterStartTurn= false

        refreshAfterEndTurn= false

        refreshAfterPlayCard= false

        refreshAfterDrawCard= false

        refreshAfterSwapCard= false

        refreshAfterDiscardCard= false

        refreshAfterEndGame= false

    }

    override fun refreshAfterStartGame() {
        refreshAfterStartGame = true
    }

    override fun refreshAfterStartTurn() {
        refreshAfterStartTurn = true
    }

    override fun refreshAfterEndTurn() {
        refreshAfterEndTurn = true
       // rootService.gameService.endTurn()
    }

    override fun refreshAfterPlayCard() {
        refreshAfterPlayCard = true
    }

    override fun refreshAfterDrawCard(card: Card) {
        refreshAfterDrawCard = true
    }

    override fun refreshAfterSwapCard(){
        refreshAfterSwapCard = true
    }

    override fun refreshAfterDiscardCard(){
        refreshAfterDiscardCard = true
    }

    override fun refreshAfterEndGame(winner : Player){
        refreshAfterEndGame = true
    }

}