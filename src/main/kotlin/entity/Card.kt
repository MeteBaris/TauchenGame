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
    /**suit typ and value will be text (String)*/
    override fun toString() = "$suit $value"


    /**
     * Checks if the given card is equal to this card.
     *
     * @param other The card to compare to
     * @return True if the cards are equal, false otherwise
     */
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Card) return false

        if (suit != other.suit) return false
        if (value != other.value) return false

        return true
    }

}