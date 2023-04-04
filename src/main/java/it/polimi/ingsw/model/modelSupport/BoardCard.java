package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;

public class BoardCard {

    public final colorType color;

    public final ornamentType ornament;


    public BoardCard(colorType color, ornamentType ornament) {
        this.color = color;
        this.ornament = ornament;

    }
    public colorType getColor() {
        return color;
    }
    public ornamentType getOrnament(){return ornament;};

}
