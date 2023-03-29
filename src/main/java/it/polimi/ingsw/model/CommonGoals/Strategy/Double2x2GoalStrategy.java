package it.polimi.ingsw.model.CommonGoals.Strategy;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.ArrayList;

/**
 * Strategy of Double2x2: it looks for 2 groups of 4 boardCards of the same color in a square formation (the two groups can be of a different color)
 */
public class Double2x2GoalStrategy implements CommonGoalStrategy {
    /**
     * Algorithm of Double2x2
     * @param Mat
     * @return boolean
     */
    public boolean goalCompleted(BoardCard[][] Mat) {
        ArrayList<Pair<Integer, Integer>> blocksCoordinates = new ArrayList<>();
        ArrayList<colorType> colors = new ArrayList<>();
        int completed = 0;
        int rows = Mat.length;
        int cols = Mat[0].length;

        for (int i = 0; i < rows - 1 && completed == 0; i++) {
            for (int j = 0; j < cols - 1 && completed == 0; j++) {
                if (Mat[i][j].getColor() != colorType.EMPTY_SPOT && Mat[i][j+1].getColor() != colorType.EMPTY_SPOT &&
                    Mat[i+1][j].getColor() != colorType.EMPTY_SPOT && Mat[i+1][j+1].getColor() != colorType.EMPTY_SPOT) {
                    if (Mat[i][j].getColor().equals(Mat[i][j + 1].getColor()) &&
                            Mat[i][j].getColor().equals(Mat[i + 1][j].getColor()) &&
                            Mat[i][j].getColor().equals(Mat[i + 1][j + 1].getColor())) {
                        int found = 0;
                        colorType updateColor = Mat[i][j].getColor();

                        Pair<Integer, Integer> coordinatesOne = new Pair<>(i, j);
                        Pair<Integer, Integer> coordinatesTwo = new Pair<>(i, j + 1);
                        Pair<Integer, Integer> coordinatesThree = new Pair<>(i + 1, j);
                        Pair<Integer, Integer> coordinatesFour = new Pair<>(i + 1, j + 1);
                        /* potrei mettere il primo caso isempty per ridurre da 4 a 1 controllo la prima volta, ma è irrilevante */
                        if (!pairIsPresent(coordinatesOne,blocksCoordinates) && !pairIsPresent(coordinatesTwo,blocksCoordinates) &&
                                !pairIsPresent(coordinatesThree,blocksCoordinates) && !pairIsPresent(coordinatesFour,blocksCoordinates)) {
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
                        if (found == 1) {
                            j++;
                        }
                    }
                }
            }
        }
        if (completed == 0) {
            colors.removeAll(colors);
            blocksCoordinates.removeAll(blocksCoordinates);
            for (int i = rows - 1; i > 0; i--) {
                for (int j = cols - 1; j > 0; j--) {
                    if (Mat[i][j].getColor() != colorType.EMPTY_SPOT && Mat[i][j-1].getColor() != colorType.EMPTY_SPOT &&
                        Mat[i-1][j].getColor() != colorType.EMPTY_SPOT && Mat[i-1][j-1].getColor() != colorType.EMPTY_SPOT) {
                        int found = 0;
                        if (Mat[i][j].getColor().equals(Mat[i][j - 1].getColor()) &&
                                Mat[i][j].getColor().equals(Mat[i - 1][j].getColor()) &&
                                Mat[i][j].getColor().equals(Mat[i - 1][j - 1].getColor())) {

                            colorType updateColor = Mat[i][j].getColor();

                            Pair<Integer, Integer> coordinatesOne = new Pair<>(i, j);
                            Pair<Integer, Integer> coordinatesTwo = new Pair<>(i, j - 1);
                            Pair<Integer, Integer> coordinatesThree = new Pair<>(i - 1, j);
                            Pair<Integer, Integer> coordinatesFour = new Pair<>(i - 1, j - 1);
                            /* potrei mettere il primo caso isempty per ridurre da 4 a 1 controllo la prima volta, ma è irrilevante */
                            if (!pairIsPresent(coordinatesOne,blocksCoordinates) && !pairIsPresent(coordinatesTwo,blocksCoordinates) &&
                                    !pairIsPresent(coordinatesThree,blocksCoordinates) && !pairIsPresent(coordinatesFour,blocksCoordinates)) {
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
                            j--;
                        }
                    }
                }
            }
        }
        if (completed == 1) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * Private method to check if the pair of coordinates is in a given list
     * @param tmp
     * @param group
     * @return boolean
     */
    private boolean pairIsPresent(Pair<Integer,Integer> tmp, ArrayList<Pair<Integer,Integer>> group){
        int present = 0;
        for(int i = 0; i < group.size() && present == 0; i++){
            if(group.get(i).getFirst().equals(tmp.getFirst()) && group.get(i).getSecond().equals(tmp.getSecond())){
                present = 1;
            }
        }
        return present == 1;
    }
}
