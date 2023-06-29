package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;

import java.io.Serializable;

public class BoardCard implements Serializable {

    public colorType color;

    public ornamentType ornament;


    public BoardCard(colorType color, ornamentType ornament) {
        this.color = color;
        this.ornament = ornament;

    }
    public colorType getColor() {
        return color;
    }
    public ornamentType getOrnament(){return ornament;};

    public void setColor(colorType colorType) {
        this.color = colorType;
    }
    public void setOrnament(ornamentType ornament){
        this.ornament = ornament;
    }

}
