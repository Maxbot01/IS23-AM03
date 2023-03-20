package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

public class FiveDiagonalGoalStrategy implements CommonGoalStrategy{
    public boolean calculateGoalPoints(BoardCard[][] Mat){
        int cols = Mat[0].length;
        int rows = Mat.length;
        int valid = 0;
/*
Controlla tutte e quattro le possibilità a meno che valid non sia 1, a causa di un
caso favorevole
 */
        if(Mat[0][0] != null) {
            colorType chosen = Mat[0][0].getColor();
            int found = 1;
            for (int i = 0; i < Mat.length-1 && found == 1; i++) {
                if(Mat[i][i] != null){
                    if(!Mat[i][i].getColor().equals(chosen)){
                       found = 0;
                    }
                }else{
                    found = 0;
                }
            }
            if(found == 1){
                valid = 1;
            }
        }
        if(Mat[1][0] != null && valid == 0){
            colorType chosen = Mat[1][0].getColor();
            int found = 1;
            for (int i = 1; i < Mat.length && found == 1; i++) {
                if(Mat[i][i] != null){
                    if(!Mat[i][i].getColor().equals(chosen)){
                        found = 0;
                    }
                }else{
                    found = 0;
                }
            }
            if(found == 1){
                valid = 1;
            }
        }
        if(Mat[0][cols-1] != null && valid == 0){
            colorType chosen = Mat[0][cols-1].getColor();
            int found = 1;
            for (int i = 0; i < Mat.length-1 && found == 1; i++) {
                if(Mat[i][cols-(1+i)] != null){
                    if(!Mat[i][cols-(1+i)].getColor().equals(chosen)){
                        found = 0;
                    }
                }else{
                    found = 0;
                }
            }
            if(found == 1){
                valid = 1;
            }
        }
        if(Mat[1][cols-1] != null && valid == 0){
            colorType chosen = Mat[1][cols-1].getColor();
            int found = 1;
            for (int i = 1; i < Mat.length && found == 1; i++) {
                if(Mat[i][cols-i] != null){
                    if(!Mat[i][cols-i].getColor().equals(chosen)){
                        found = 0;
                    }
                }else{
                    found = 0;
                }
            }
            if(found == 1){
                valid = 1;
            }
        }
        if(valid == 1){
            return true;
        }else{
            return false;
        }
    };
}
