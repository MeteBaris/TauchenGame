package entity

data class TauchenGame(
    var isPlayerOneActive: Boolean = true,
    val players: MutableList<Player> = mutableListOf(),
    val drawStack: MutableList<Card> = mutableListOf(),
    val discardStack: MutableList<Card> = mutableListOf(),
    val collectionStack: MutableList<Card> = mutableListOf(),)
{

}