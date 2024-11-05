package entity


/**
 * TauchenGame class represents the game structure of a card game "Tauchen".

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
