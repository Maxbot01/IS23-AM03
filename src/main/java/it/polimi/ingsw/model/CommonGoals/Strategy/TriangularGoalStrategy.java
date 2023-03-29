package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

/**
 * Strategy of Triangular: it looks for a triangular disposal of the boardCards inside the player's shelf
 */
public class TriangularGoalStrategy implements CommonGoalStrategy{
    /**
     * Algorithm of Triangular
     * @param Mat
     * @return boolean
     */
    public boolean goalCompleted(BoardCard[][] Mat){
        int cols = Mat[0].length;
        int rows = Mat.length;
        int valid = 0;
/*
Al contrario di FiveDiagonalGoalStrategy, qui non ho bisogno di controllare tutti i casi,
poiché secondo l'ordine dei casi, quello prima elimina la possibilità del successivo
 */
        if(Mat[0][0].getColor() != colorType.EMPTY_SPOT) {
            int found = 1;
            for (int i = 1; i < rows-1 && found == 1; i++) {
                if(Mat[i][i].getColor() == colorType.EMPTY_SPOT || Mat[i-1][i].getColor() != colorType.EMPTY_SPOT){
                    found = 0;
                }
            }
            if(found == 1){
                valid = 1;
            }
        } else if(Mat[1][0].getColor() != colorType.EMPTY_SPOT){
            int found = 1;
            for (int i = 2; i < rows && found == 1; i++) {
                if(Mat[i][i-1].getColor() == colorType.EMPTY_SPOT || Mat[i-1][i-1].getColor() != colorType.EMPTY_SPOT) {
                    found = 0;
                }
            }
            if(found == 1){
                valid = 1;
            }
        } else if(Mat[0][cols-1].getColor() != colorType.EMPTY_SPOT){
            int found = 1;
            for (int i = 1; i < rows-1 && found == 1; i++) {
                if(Mat[i][cols-(1+i)].getColor() == colorType.EMPTY_SPOT || Mat[i-1][cols-(1+i)].getColor() != colorType.EMPTY_SPOT){
                        found = 0;
                }
            }
            if(found == 1){
                valid = 1;
            }
        } else if(Mat[1][cols-1].getColor() != colorType.EMPTY_SPOT){
            int found = 1;
            for (int i = 2; i < rows && found == 1; i++) {
                if(Mat[i][cols-i].getColor() == colorType.EMPTY_SPOT || Mat[i-1][cols-i].getColor() != colorType.EMPTY_SPOT){
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
