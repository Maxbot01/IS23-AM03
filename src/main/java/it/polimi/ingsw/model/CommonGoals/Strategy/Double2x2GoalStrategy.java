package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class Double2x2GoalStrategy implements CommonGoalStrategy{
    public boolean calculateGoalPoints(BoardCard[][] Mat){
        ArrayList<Pair<Integer,Integer>> blocksCoordinates = new ArrayList<>();
        ArrayList<Pair<colorType,Integer>> colors = new ArrayList<>();
        int completed = 0;

        for(int i = 0; i < Mat.length-1 && completed == 0; i++){
            for(int j = 0; i< Mat[0].length-1 && completed == 0; j++){
                if(Mat[i][j] != null && Mat[i][j+1] != null && Mat[i+1][j] != null && Mat[i+1][j+1] != null) {
                    int found = 0;
                    if (Mat[i][j].getColor().equals(Mat[i][j + 1].getColor()) &&
                            Mat[i][j].getColor().equals(Mat[i + 1][j].getColor()) &&
                            Mat[i][j].getColor().equals(Mat[i + 1][j + 1].getColor())) {

                        Pair<colorType, Integer> updateColor = new Pair<>(Mat[i][j].getColor(), 1);

                        Pair<Integer, Integer> coordinatesOne = new Pair<>(i, j);
                        Pair<Integer, Integer> coordinatesTwo = new Pair<>(i, j + 1);
                        Pair<Integer, Integer> coordinatesThree = new Pair<>(i + 1, j);
                        Pair<Integer, Integer> coordinatesFour = new Pair<>(i + 1, j + 1);
                        /* potrei mettere il primo caso isempty per ridurre da 4 a 1 controllo la prima volta, ma è irrilevante */
                        if (!blocksCoordinates.contains(coordinatesOne) && !blocksCoordinates.contains(coordinatesTwo) &&
                                !blocksCoordinates.contains(coordinatesThree) && !blocksCoordinates.contains(coordinatesFour)) {
                            blocksCoordinates.add(coordinatesOne);
                            blocksCoordinates.add(coordinatesTwo);
                            blocksCoordinates.add(coordinatesThree);
                            blocksCoordinates.add(coordinatesFour);
                            found = 1;
                            if (!colors.contains(updateColor)) {
                                colors.add(updateColor);
                            } else {
                                completed = 1;
                            }
                        }
                    }
                    if (found == 1) {
                        j++;
                        found = 0;
                    }
                }
            }
        }
        if(completed == 1){
            return true;
        }else{
            return false;
        }
    };
}
