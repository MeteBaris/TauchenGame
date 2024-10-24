package entity


/**
 * TauchenGame class represents the game stucture of a card game "Tauchen".

 */
data class TauchenGame(
    var isPlayerOneActive: Boolean = true
)
{
    val players: MutableList<Player> = mutableListOf()
    val playStack: MutableList<Card> = mutableListOf()
    val drawStack: MutableList<Card> = mutableListOf()
    val discardStack: MutableList<Card> = mutableListOf()
    init {
        require(playStack.size < 3) {"Play stack must be 3 cards"}
    }

}
