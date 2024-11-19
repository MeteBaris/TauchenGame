package entity


/**
 * TauchenGame class represents the game structure of a card game "Tauchen".
 * @property players A mutable list of Player objects representing the participants in the game.
 * @property isPlayerOneActive A boolean flag indicating whether it is Player One's turn. Defaults to true.
 * @property playStack A mutable list of Card objects representing the cards currently in play at the center of the table.
 *                     This stack must contain no more than three cards at any time.
 * @property drawStack A mutable list of Card objects representing the deck from which players draw cards during the game.
 * @property discardStack A mutable list of Card objects representing the pile of discarded cards.
 */
data class TauchenGame(
    val players: MutableList<Player> = mutableListOf()
)
{
    var isPlayerOneActive: Boolean = true
    var playStack: MutableList<Card> = mutableListOf()
    var drawStack: MutableList<Card> = mutableListOf()
    var discardStack: MutableList<Card> = mutableListOf()
    init {
        require(playStack.size <= 3) {"Play stack must be 3 cards"}
    }


}
