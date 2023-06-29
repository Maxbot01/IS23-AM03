

package it.polimi.ingsw.model.modelSupport.exceptions;

import java.io.Serializable;

public class ColumnNotSelectable extends Exception implements Serializable {
    public String info;
    public ColumnNotSelectable(String info) {
        this.info = info;
    }

    public String getInfo() {
        return info;
    }
}
