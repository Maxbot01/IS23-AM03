package it.polimi.ingsw.model.modelSupport;

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

    public ornamentType getOrnament() {
        return ornament;
    }
}
