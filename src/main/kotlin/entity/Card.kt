package entity

/**
 * Data class for the single typ of game elements that the game "Tauchen" knows: cards.
 *
 * It is characterized by a [CardSuit] and a [CardValue]
 */

data class Card(
    var suit: CardSuit,
    var value: CardValue)
{

}