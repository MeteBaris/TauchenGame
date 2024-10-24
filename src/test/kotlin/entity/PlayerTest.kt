package entity

import kotlin.test.Test
import kotlin.test.assertNotEquals

//Test class for players
class PlayerTest(){

    val player1 = Player("Tom",5,false)
    val player2 = Player("Tom",2,true)
    val player3 = Player("Tom",5,false)
    val player4 = Player("Tom",2,false)

    // this function controls if both of players are the same player
    @Test
    fun testPlayers(){

        //check the same named player
        assertNotEquals(player1, player2)

        //check the same named, same score, same inavailable_specialAction players
        assertNotEquals(player1, player3)

        //check the same named, same score, but different specialAction players
        assertNotEquals(player2, player4)
    }
}