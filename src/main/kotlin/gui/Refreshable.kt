package gui

import entity.Card
import entity.Player

/**
 * The `Refreshable` interface provides a mechanism for GUI components to be
 * updated based on changes in the game state. Classes implementing this
 * interface can override specific methods to handle various game events
 * triggered by the service layer.
 *
 * Each method corresponds to a particular event in the game and can be
 * implemented as needed by the GUI components.
 */
interface Refreshable {

    /**
     * Called after the game has started.
     * Implementations should update the GUI to reflect the initialized game state.
     */
    fun refreshAfterStartGame() {}

    /**
     * Called at the start of a player's turn.
     * Implementations should update the GUI to indicate the active player's turn.
     */
    fun refreshAfterStartTurn() {}

    /**
     * Called at the end of a player's turn.
     * Implementations should update the GUI to handle turn transitions.
     */
    fun refreshAfterEndTurn() {}

    /**
     * Called after a card is played.
     * Implementations should update the GUI to show the updated state of the play area.
     */
    fun refreshAfterPlayCard() {}

    /**
     * Called after a card is drawn.
     *
     * @param lastCard The card that was drawn.
     * @param hasToDiscard Indicates whether the player must discard a card due to exceeding the hand limit.
     * Implementations should update the GUI to reflect the changes in the player's hand and draw pile.
     */
    fun refreshAfterDrawCard(lastCard: Card, hasToDiscard: Boolean){}

    /**
     * Called after a card swap action is performed.
     * Implementations should update the GUI to reflect the changes in the play area and the player's hand.
     */
    fun refreshAfterSwapCard() {}

    /**
     * Called after a card is discarded.
     * Implementations should update the GUI to reflect the changes in the discard pile and the player's hand.
     */
    fun refreshAfterDiscardCard(){}

    /**
     * Called after the game has ended.
     * Implementations should update the GUI to display the final results and scores.
     */
    fun refreshAfterEndGame(){}

    /**
     * Called when the "Play Again" option is selected.
     * Implementations should reset the GUI to prepare for a new game.
     */
    fun refreshAfterPlayAgain(){}





}