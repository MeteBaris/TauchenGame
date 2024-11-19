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
    private var deck: MutableList<Card> = createDeck().toMutableList()
/**createDeck() function creates a shuffled deck for the game*/
    internal fun createDeck(): List<Card> {
        return CardSuit.values().flatMap { suit ->
            CardValue.values().map { value -> Card(suit, value) }
        }.shuffled()
    }

    /**it creates hands for players in order*/
    internal fun dealCards(): MutableList<Card> {
        val hand = deck.takeLast(5).toMutableList()
        deck = deck.dropLast(5).toMutableList()
        return hand
    }

    /**it creates a Collection stack from trios. Score will be calculated with this Collection stack*/
    fun createCollectionStack(): List<Card> {
        val collectionStack: MutableList<Card> = mutableListOf()
        return collectionStack
    }

    /** It creates draw stack after dealing cards (I hope) (??? -> does it work?)*/
    fun createDrawStack(): List<Card> {
        return deck
    }
    /** It creates discard stack after dealing cards (I hope) */
    fun createDiscardStack(): List<Card> {
        val game = rootService.currentGame
        val discardStack: MutableList<Card> = mutableListOf()
        return discardStack
    }
}