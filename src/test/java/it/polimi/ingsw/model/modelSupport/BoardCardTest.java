package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardCardTest {

    @Test
    void get_every_Color() {
        ArrayList<BoardCard> al = new ArrayList<>();
        BoardCard bc = new BoardCard(colorType.BLUE, ornamentType.A);

        assertTrue(bc.getColor() == colorType.BLUE);
    }
}