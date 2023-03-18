package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.colorType;
import java.util.*;
public class TwoOf5DiffGoalStrategy implements CommonGoalStrategy{
    public int calculateGoalPoints(BoardCard[][] Mat, Integer numOfPlayers){
        int ris = 0;

        Set<colorType> colori = new HashSet<colorType>(Collections.list(colorType));




        int[] colors = {0,0,0,0,0,0};
        int correctLines = 0;
        int Valid = 1;
        for(int i = Mat[0].length && ; i > 0; i--){
            for(int j = 0; j < Mat.length && Valid == 1; j++){
                if(Mat[i][j].getColor() == colorType.PURPLE){
                    colors[0]++;
                    if(colors[0] == 2){
                        Valid = 0;
                    }
                }else if()



            }
        }


        return ris;
    };
}
