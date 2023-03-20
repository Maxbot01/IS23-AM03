package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;

public class TriangularGoalStrategy implements CommonGoalStrategy{
    public boolean goalCompleted(BoardCard[][] Mat){
        int cols = Mat[0].length;
        int rows = Mat.length;
        int valid = 0;
/*
Al contrario di FiveDiagonalGoalStrategy, qui non ho bisogno di controllare tutti i casi,
poiché secondo l'ordine dei casi, quello prima elimina la possibilità del successivo
 */
        if(Mat[0][0] != null) {
            int found = 1;
            for (int i = 0; i < Mat.length-1 && found == 1; i++) {
                if(Mat[i][i] == null){
                    found = 0;
                }
            }
            if(found == 1){
                valid = 1;
            }
        } else if(Mat[1][0] != null){
            int found = 1;
            for (int i = 1; i < Mat.length && found == 1; i++) {
                if(Mat[i][i] == null) {
                    found = 0;
                }
            }
            if(found == 1){
                valid = 1;
            }
        } else if(Mat[0][cols-1] != null){
            int found = 1;
            for (int i = 0; i < Mat.length-1 && found == 1; i++) {
                if(Mat[i][cols-(1+i)] == null){
                        found = 0;
                }
            }
            if(found == 1){
                valid = 1;
            }
        } else if(Mat[1][cols-1] != null){
            int found = 1;
            for (int i = 1; i < Mat.length && found == 1; i++) {
                if(Mat[i][cols-i] == null){
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
    }
}
