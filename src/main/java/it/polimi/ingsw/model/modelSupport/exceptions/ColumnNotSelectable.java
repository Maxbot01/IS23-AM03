

package it.polimi.ingsw.model.modelSupport.exceptions;

public class ColumnNotSelectable extends Exception {
    public String info;
    public ColumnNotSelectable(String info) {
        this.info = info;
    }

}
