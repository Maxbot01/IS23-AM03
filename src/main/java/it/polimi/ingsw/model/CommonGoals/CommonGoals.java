package it.polimi.ingsw.model.CommonGoals;

import com.sun.jdi.IntegerValue;
import it.polimi.ingsw.model.CommonGoals.Strategy.*;
import it.polimi.ingsw.model.modelSupport.Player;

import java.util.*;
public class CommonGoals {
    private final Set<CommonGoalType> possibleGoals = new HashSet<>();
    private CommonGoalStrategy firstGoal;
    private CommonGoalStrategy secondGoal;
    private ArrayList<Player> reachedFirstGoal;
    private ArrayList<Player> reachedSecondGoal;

    public void CommonGoal() {
        /* potrei fare una addAll con un enumSet, ma è la stessa cosa */
        possibleGoals.add(CommonGoalType.SIXOFTWO);
        possibleGoals.add(CommonGoalType.FIVEDIAGONAL);
        possibleGoals.add(CommonGoalType.FOUROFFOUR);
        possibleGoals.add(CommonGoalType.FOURCORNERS);
        possibleGoals.add(CommonGoalType.DOUBLE2X2);
        possibleGoals.add(CommonGoalType.MAXTHREEDIFF);
        possibleGoals.add(CommonGoalType.EIGHTTILES);
        possibleGoals.add(CommonGoalType.FOURLINES3DIFF);
        possibleGoals.add(CommonGoalType.TWOOFSIX);
        possibleGoals.add(CommonGoalType.TWOOF5DIFF);
        possibleGoals.add(CommonGoalType.FIVEX);
        possibleGoals.add(CommonGoalType.TRIANGULAR);

        Random ran1 = new Random();
        int ran1Int = ran1.nextInt(possibleGoals.size());
        Random ran2 = new Random();
        int ran2Int;
        do {
            ran2Int = ran2.nextInt(possibleGoals.size());
        } while (ran2Int == ran1Int);
/* ran1Int
        if(ran1Int == 1){
            firstGoal = new SixOfTwoGoalStrategy();
        } else if(ran1Int == 2){
            firstGoal = new FiveDiagonalGoalStrategy();
        } else if(ran1Int == 3){
            firstGoal = new FourOfFourGoalStrategy();
        } else if(ran1Int == 4){
            firstGoal = new FourCornersGoalStrategy();
        } else if(ran1Int == 5){
            firstGoal = new Double2x2GoalStrategy();
        } else if(ran1Int == 6) {
            firstGoal = new MaxThreeDiffGoalStrategy();
        } else if(ran1Int == 7){
            firstGoal = new EightTilesGoalStrategy();
        } else if(ran1Int == 8){
            firstGoal = new FourLines3DiffGoalStrategy();
        } else if(ran1Int == 9){
            firstGoal = new TwoOfSixGoalStrategy();
        } else if(ran1Int == 10){
            firstGoal = new TwoOf5DiffGoalStrategy();
        } else if(ran1Int == 11){
            firstGoal = new FiveXGoalStrategy();
        } else if(ran1Int == 12){
            firstGoal = new TriangularGoalStrategy();
        }
*/ /* ran1Int */
/*
        if(ran2Int == 1){
            firstGoal = new SixOfTwoGoalStrategy();
        } else if(ran2Int == 2){
            firstGoal = new FiveDiagonalGoalStrategy();
        } else if(ran2Int == 3){
            firstGoal = new FourOfFourGoalStrategy();
        } else if(ran2Int == 4){
            firstGoal = new FourCornersGoalStrategy();
        } else if(ran2Int == 5){
            firstGoal = new Double2x2GoalStrategy();
        } else if(ran2Int == 6) {
            firstGoal = new MaxThreeDiffGoalStrategy();
        } else if(ran2Int == 7){
            firstGoal = new EightTilesGoalStrategy();
        } else if(ran2Int == 8){
            firstGoal = new FourLines3DiffGoalStrategy();
        } else if(ran2Int == 9){
            firstGoal = new TwoOfSixGoalStrategy();
        } else if(ran2Int == 10){
            firstGoal = new TwoOf5DiffGoalStrategy();
        } else if(ran2Int == 11){
            firstGoal = new FiveXGoalStrategy();
        } else if(ran2Int == 12){
            firstGoal = new TriangularGoalStrategy();
        }
 */ /* ran2Int */

    }
    public int calculateAllPoints(Player player, int numOfPlayers){
        int ris;
        int pointsOfFirst;
        int pointsOfSecond;
        /* firstGoal */
        /* potrei fare un metodo per il calcolo dei punti di un goal, così da non dover riscrivere
            la stessa cosa per entrambi i goal */
        if(!reachedFirstGoal.contains(player)){
            if (firstGoal.calculateGoalPoints(player.getPlayersShelf().getShelfCards())) {/*boolean return*/
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
            if (secondGoal.calculateGoalPoints(player.getPlayersShelf().getShelfCards())) {/*boolean return*/
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
