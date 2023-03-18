package it.polimi.ingsw.model.CommonGoals;

import com.sun.jdi.IntegerValue;
import it.polimi.ingsw.model.CommonGoals.Strategy.*;
import it.polimi.ingsw.model.modelSupport.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
public class CommonGoals {
    private Set<CommonGoalType> possibleGoals = new HashSet<>();
    private CommonGoalStrategy firstGoal = new MaxThreeDiffGoalStrategy();
    private CommonGoalStrategy secondGoal;
    private ArrayList<Player> reachedFirstGoal;
    private ArrayList<Player> reachedSecondGoal;

    public void CommonGoal() {
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


    }
    public int calculateAllPoints(Player player, int numOfPlayers){
        int ris = 0;
        ris = firstGoal.calculateGoalPoints(Player.getShelf(),numOfPlayers) +
                secondGoal.calculateGoalPoints(Player.getShelf(), numOfPlayers);




        return ris;
    }


}
