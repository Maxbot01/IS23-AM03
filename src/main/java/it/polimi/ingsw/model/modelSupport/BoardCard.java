package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.modelSupport.enums.colorType;

public class BoardCard {

    public final colorType color;


    public BoardCard(colorType color) {
        this.color = color;

    }
    public colorType getColor() {
        return color;
    }

}
