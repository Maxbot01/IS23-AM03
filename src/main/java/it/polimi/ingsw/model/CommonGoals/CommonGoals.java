package it.polimi.ingsw.model.CommonGoals;

import it.polimi.ingsw.model.CommonGoals.Strategy.*;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.*;
public class CommonGoals {
    private final EnumSet<CommonGoalType> possibleGoals = EnumSet.allOf(CommonGoalType.class);
    private CommonGoalStrategy firstGoal;
    private CommonGoalStrategy secondGoal;
    private final ArrayList<Player> reachedFirstGoal = new ArrayList<>();
    private final ArrayList<Player> reachedSecondGoal = new ArrayList<>();

    public void CommonGoal() {
        Random ran1 = new Random();
        int ran1Int = ran1.nextInt(possibleGoals.size());
        Random ran2 = new Random();
        int ran2Int;
        do {
            ran2Int = ran2.nextInt(possibleGoals.size());
        } while (ran2Int == ran1Int);

        switch (ran1Int){
            case 0:
                firstGoal = new SixOfTwoGoalStrategy();
            case 1:
                firstGoal = new FiveDiagonalGoalStrategy();
            case 2:
                firstGoal = new FourOfFourGoalStrategy();
            case 3:
                firstGoal = new FourCornersGoalStrategy();
            case 4:
                firstGoal = new Double2x2GoalStrategy();
            case 5:
                firstGoal = new MaxThreeDiffGoalStrategy();
            case 6:
                firstGoal = new EightTilesGoalStrategy();
            case 7:
                firstGoal = new FourLines3DiffGoalStrategy();
            case 8:
                firstGoal = new TwoOfSixGoalStrategy();
            case 9:
                firstGoal = new TwoOf5DiffGoalStrategy();
            case 10:
                firstGoal = new FiveXGoalStrategy();
            case 11:
                firstGoal = new TriangularGoalStrategy();
        }
        switch (ran2Int){
            case 0:
                firstGoal = new SixOfTwoGoalStrategy();
            case 1:
                firstGoal = new FiveDiagonalGoalStrategy();
            case 2:
                firstGoal = new FourOfFourGoalStrategy();
            case 3:
                firstGoal = new FourCornersGoalStrategy();
            case 4:
                firstGoal = new Double2x2GoalStrategy();
            case 5:
                firstGoal = new MaxThreeDiffGoalStrategy();
            case 6:
                firstGoal = new EightTilesGoalStrategy();
            case 7:
                firstGoal = new FourLines3DiffGoalStrategy();
            case 8:
                firstGoal = new TwoOfSixGoalStrategy();
            case 9:
                firstGoal = new TwoOf5DiffGoalStrategy();
            case 10:
                firstGoal = new FiveXGoalStrategy();
            case 11:
                firstGoal = new TriangularGoalStrategy();
        }
    }
    public int calculateAllPoints(Player player, int numOfPlayers){
        int ris;
        int pointsOfFirst;
        int pointsOfSecond;
        /* firstGoal */
        /* potrei fare un metodo per il calcolo dei punti di un goal, così da non dover riscrivere
            la stessa cosa per entrambi i goal */
        if(!reachedFirstGoal.contains(player)){
            if (firstGoal.goalCompleted(player.getPlayersShelf().getShelfCards())) {/*boolean return*/
                reachedFirstGoal.add(player);
                if (numOfPlayers > 2) {
                    pointsOfFirst = 8 - (reachedFirstGoal.indexOf(player) * 2);
                } else {
                    pointsOfFirst = 8 - (reachedFirstGoal.indexOf(player) * 4);
                }
            } else {
                pointsOfFirst = 0;
            }
        }else{
            pointsOfFirst = 0;
        }
        /* secondGoal */
        if(!reachedSecondGoal.contains(player)){
            if (secondGoal.goalCompleted(player.getPlayersShelf().getShelfCards())) {/*boolean return*/
                reachedSecondGoal.add(player);
                if (numOfPlayers > 2) {
                    pointsOfSecond = 8 - (reachedSecondGoal.indexOf(player) * 2);
                } else {
                    pointsOfSecond = 8 - (reachedSecondGoal.indexOf(player) * 4);
                }
            } else {
                pointsOfSecond = 0;
            }
        }else{
            pointsOfSecond = 0;
        }
        ris = pointsOfFirst + pointsOfSecond;
        return ris;
    }
}
