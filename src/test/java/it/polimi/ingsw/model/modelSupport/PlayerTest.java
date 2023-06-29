package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import it.polimi.ingsw.model.modelSupport.exceptions.ColumnNotSelectable;
import it.polimi.ingsw.model.modelSupport.exceptions.ShelfFullException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class PlayerTest {
    private Player player;

    @Before
    public void setUp() {
        player = new Player("John");
    }

    @Test
    public void testGetNickname() {
        assertEquals("John", player.getNickname());
    }

    @Test
    public void testGetScore() {
        assertEquals(0, player.getScore().intValue());
    }

    @Test
    public void testUpdateScore() {
        player.updateScore(50);
        assertEquals(50, player.getScore().intValue());
    }

    @Test
    public void testHasChair() {
        assertFalse(player.hasChair());
        player.setHasChair();
        assertTrue(player.hasChair());
    }

    @Test
    public void testGetPlayersShelf() {
        assertNotNull(player.getPlayersShelf());
    }

    @Test
    public void testGetPersonalGoal() {
        player.setPersonalGoalFromIndex(1);
        assertNotNull(player.getPersonalGoal());
    }

    @Test
    public void testGetFinalScore() throws ShelfFullException, ColumnNotSelectable {
        player.setPersonalGoalFromIndex(1);
        player.updateScore(8);
        Shelf shelf = new Shelf();
        shelf.getShelfCards()[0][0] = new BoardCard(colorType.YELLOW, ornamentType.A);
        shelf.getShelfCards()[0][1] = new BoardCard(colorType.YELLOW, ornamentType.A);
        shelf.getShelfCards()[0][2] = new BoardCard(colorType.BLUE, ornamentType.A);
        shelf.getShelfCards()[0][3] = new BoardCard(colorType.BLUE, ornamentType.A);
        shelf.getShelfCards()[0][4] = new BoardCard(colorType.BLUE, ornamentType.A);
        shelf.getShelfCards()[1][0] = new BoardCard(colorType.YELLOW, ornamentType.A);
        shelf.getShelfCards()[1][1] = new BoardCard(colorType.GREEN, ornamentType.A);
        shelf.getShelfCards()[1][2] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        shelf.getShelfCards()[1][3] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        shelf.getShelfCards()[1][4] = new BoardCard(colorType.WHITE, ornamentType.A);
        shelf.getShelfCards()[2][0] = new BoardCard(colorType.YELLOW, ornamentType.A);
        shelf.getShelfCards()[2][1] = new BoardCard(colorType.GREEN, ornamentType.A);
        shelf.getShelfCards()[2][2] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        shelf.getShelfCards()[2][3] = new BoardCard(colorType.WHITE, ornamentType.A);
        shelf.getShelfCards()[2][4] = new BoardCard(colorType.WHITE, ornamentType.A);
        shelf.getShelfCards()[3][0] = new BoardCard(colorType.PURPLE, ornamentType.A);
        shelf.getShelfCards()[3][1] = new BoardCard(colorType.PURPLE, ornamentType.A);
        shelf.getShelfCards()[3][2] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        shelf.getShelfCards()[3][3] = new BoardCard(colorType.YELLOW, ornamentType.A);
        shelf.getShelfCards()[3][4] = new BoardCard(colorType.BLUE, ornamentType.A);
        shelf.getShelfCards()[4][0] = new BoardCard(colorType.PURPLE, ornamentType.A);
        shelf.getShelfCards()[4][1] = new BoardCard(colorType.WHITE, ornamentType.A);
        shelf.getShelfCards()[4][2] = new BoardCard(colorType.WHITE, ornamentType.A);
        shelf.getShelfCards()[4][3] = new BoardCard(colorType.WHITE, ornamentType.A);
        shelf.getShelfCards()[4][4] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        shelf.getShelfCards()[5][0] = new BoardCard(colorType.PURPLE, ornamentType.A);
        shelf.getShelfCards()[5][1] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
        shelf.getShelfCards()[5][2] = new BoardCard(colorType.BLUE, ornamentType.A);
        shelf.getShelfCards()[5][3] = new BoardCard(colorType.YELLOW, ornamentType.A);
        shelf.getShelfCards()[5][4] = new BoardCard(colorType.YELLOW, ornamentType.A);
        player.setPlayerShelf(shelf);
        assertEquals(23, player.getFinalScore());
    }
}
