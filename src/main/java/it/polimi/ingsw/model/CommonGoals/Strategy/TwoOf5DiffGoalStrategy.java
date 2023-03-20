package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import java.util.*;
public class TwoOf5DiffGoalStrategy implements CommonGoalStrategy{
    public boolean goalCompleted(BoardCard[][] Mat){
/* si può fare come twoofsix, in maniera più leggibile e semplice */

/* Ho creato un set a partire dall'enum e per iterarci con un indice l'ho resa una lista */
        EnumSet<colorType> enumColori = EnumSet.of(colorType.PURPLE,colorType.BLUE,colorType.LIGHT_BLUE,colorType.YELLOW,colorType.WHITE,colorType.GREEN);
        List<colorType> colori = new ArrayList<>(enumColori);

        int[] colors = {0,0,0,0,0,0};
        int correctLines = 0;
        int completed = 0;
        for(int i = Mat.length; i > 0 && completed == 0; i--){
            int valid = 1;
            for(int j = 0; j < Mat[0].length && valid == 1; j++){
                if(Mat[i][j] != null) {
                    int found = 0;
                    int z;
/* scorro la lista formata dall'enum, tenendo l'indice per poi aumentare il corrispondente in colors */
                    for (z = 0; z < colori.size() && found == 0; z++) {
                        if (colori.get(z).equals(Mat[i][j].getColor())) {
                            found = 1;
                        }
                    }
                    colors[z-1]++;
                    if (colors[z-1] == 2) {
                        valid = 0;
                    }
                }else{
                    valid = 0;
                }
            }
            if(valid == 1){
                correctLines++;
/* "completato" lo uso per evitare di controllare più di due righe */
                if(correctLines == 2){
                    completed = 1;
                }
            }
/* resetto colors per la prossima riga */
            for(int j = 0; j < colors.length; j++){
                colors[j] = 0;
            }
        }
        if(completed == 1){
            return true;
        }else{
            return false;
        }
    };
}
