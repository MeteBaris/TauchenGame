package service

import entity.*

/**
 * This service provides functionality related to card stacks, such as creating a
 * shuffled draw stack, creating Collection Stacks and dealing card .
 *
 * @param rootService The root service to which this service belongs
 */

class CardService(private val rootService: RootService) :
    AbstractRefreshingService() {

/**createDeck() function creates a shuffled deck for the game*/
    internal fun createDeck(): List<Card> {
        return CardSuit.values().flatMap { suit ->
            CardValue.values().map { value -> Card(suit, value) }
        }.shuffled()
    }

    /**it creates hands for players in order*/
    internal fun dealCards(): MutableList<Card> {
        val game = rootService.currentGame
        val deck = createDeck().toMutableList()
        return deck.extract(5)
    }

    /**help function for extract 5 card on the drawStack for deals card*/
     private fun <T> MutableList<T>.extract(n: Int): MutableList<T> {
        val deck =createDeck().toMutableList()
        val extractedElements = this.take(n).toMutableList() // Convert to MutableList
        this.subList(0, n).clear() // Remove these elements from the original list
        return extractedElements
    }

    /**it creates a Collection stack from trios. Score will be calculated with this Collection stack*/
    fun createCollectionStack(): List<Card> {
        val collectionStack: MutableList<Card> = mutableListOf()
        return collectionStack
    }

    /** It creates draw stack after dealing cards (I hope) (??? -> does it work?)*/
    fun createDrawStack(): List<Card> {
        val game = rootService.currentGame

        return game!!.drawStack
    }
    /** It creates discard stack after dealing cards (I hope) */
    fun createDiscardStack(): List<Card> {
        val game = rootService.currentGame
        val discardStack: MutableList<Card> = mutableListOf()
        return discardStack
    }
}