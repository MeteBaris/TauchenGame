package gui.scenes


import gui.Refreshable
import service.RootService
import tools.aqua.bgw.core.MenuScene


class ResultMenuScene(val rootService: RootService) : MenuScene(1920, 1080), Refreshable {
    // This pane is used to hold all components of the scene and easily center them on the screen
  /*  private val contentPane = Pane<UIComponent>(
        width = 700,
        height = 500,
        posX = 1920 / 2 - 700 / 2,
        posY = 1080 / 2 - 500 / 2,
        visual = ColorVisual(Color(0x0C2027))
    )

    // This label is used to display the title of the scene
    private val titleLabel = Label(
        text = "GEWINNER",
        width = 700,
        height = 100,
        posX = 0,
        posY = 30,
        alignment = Alignment.CENTER,
        font = Font(30, Color(0xFFFFFFF), "JetBrains Mono ExtraBold")
    )

    // This label is used to display the name of the winner
    private val winnerLabel = Label(
        text = "",
        width = 600,
        height = 200,
        posX = 50,
        posY = 150,
        alignment = Alignment.CENTER,
        font = Font(45, Color(0xFFFFFFF), "JetBrains Mono ExtraBold"),
        visual = ColorVisual(Color(0x49585D))
    )

    // This button is used to restart the game
    private val restartButton = Button(
        text = "NEUSTART",
        width = 280,
        height = 60,
        posX = 700 / 2 - 280 / 2,
        posY = 390,
        font = Font(22, Color(0xFFFFFFF), "JetBrains Mono ExtraBold"),
        visual = ColorVisual(Color(0x49585D))
    ).apply {
        // When the button is clicked, restart the game
        onMouseClicked = {
            // Access the onAllRefreshables method of the game service to call the refreshAfterGameRestart method
            rootService.gameService.onAllRefreshables { refreshAfterPlayAgain() }
        }
    }

    // Initialize the scene by setting the background color and adding all components to the content pane
    init {
        background = ColorVisual(Color(12, 32, 39, 240))
        contentPane.addAll(titleLabel, winnerLabel, restartButton)
        addComponents(contentPane)
    }

    /**
     * The refreshAfterGameEnd method is called by the service layer after a game has ended.
     * It sets the name of the winner.
     *
     * @param winner The [Player] who has won the game
     */
    override fun refreshAfterEndGame(winner: Player) {
        winnerLabel.text = winner.name
    }
*/
}