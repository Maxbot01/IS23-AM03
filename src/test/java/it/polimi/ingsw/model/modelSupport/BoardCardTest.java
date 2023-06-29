package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import org.junit.Assert;
import org.junit.Test;

public class BoardCardTest {

    @Test
    public void testGetColor() {
        BoardCard boardCard = new BoardCard(colorType.PURPLE, ornamentType.A);
        Assert.assertEquals(colorType.PURPLE, boardCard.getColor());
    }

    @Test
    public void testGetOrnament() {
        BoardCard boardCard = new BoardCard(colorType.BLUE, ornamentType.A);
        Assert.assertEquals(ornamentType.A, boardCard.getOrnament());
    }

    @Test
    public void testSetColor() {
        BoardCard boardCard = new BoardCard(colorType.GREEN, ornamentType.A);
        boardCard.setColor(colorType.YELLOW);
        Assert.assertEquals(colorType.YELLOW, boardCard.getColor());
    }

    @Test
    public void testSetOrnament() {
        BoardCard boardCard = new BoardCard(colorType.PURPLE, ornamentType.A);
        boardCard.setOrnament(ornamentType.A);
        Assert.assertEquals(ornamentType.A, boardCard.getOrnament());
    }
}
