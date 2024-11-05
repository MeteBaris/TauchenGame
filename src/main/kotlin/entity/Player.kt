package entity

/**
 * Entity to represent a player in the game "Tauchen". Besides having a [playerName] and the information
 * whether a card has been drawn in this round, the player just consists of 2
 * stacks of cards: [collectionStack], [hand]
 *
 * @property playerName The name to be displayed for this player
 * @property score defines the sum of the rounds score of the active "Tauchen"
 * @property hasSpecialAction indicates whether the special move has been played.
 * @property hand The list of cards in players hand
 * @property collectionStack The list of trio cards that player collects
 */

class Player (
    val name : String,
    var score : Int,
    var hasSpecialAction : Boolean
) {
    var hand: MutableList<Card> = mutableListOf()
    var collectionStack: MutableList<Card> = mutableListOf()
}