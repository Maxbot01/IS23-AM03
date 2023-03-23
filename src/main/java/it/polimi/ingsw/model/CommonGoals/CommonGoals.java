package it.polimi.ingsw.model.CommonGoals;

import it.polimi.ingsw.model.CommonGoals.Strategy.*;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CommonGoals {
    private final EnumSet<CommonGoalType> possibleGoals = EnumSet.allOf(CommonGoalType.class);
    private CommonGoalStrategy firstGoal;
    private CommonGoalStrategy secondGoal;
    private final ArrayList<Player> reachedFirstGoal = new ArrayList<>();
    private final ArrayList<Player> reachedSecondGoal = new ArrayList<>();

    public CommonGoals() {
        int[] indexes = ThreadLocalRandom.current().ints(0, 12).distinct().limit(2).toArray();
        /*switch (indexes[0]){
            case 0:
                firstGoal = new SixOfTwoGoalStrategy();
                break;
            case 1:
                firstGoal = new FiveDiagonalGoalStrategy();
                break;
            case 2:
                firstGoal = new FourOfFourGoalStrategy();
                break;
            case 3:
                firstGoal = new FourCornersGoalStrategy();
                break;
            case 4:
                firstGoal = new Double2x2GoalStrategy();
                break;
            case 5:
                firstGoal = new MaxThreeDiffGoalStrategy();
                break;
            case 6:
                firstGoal = new EightTilesGoalStrategy();
                break;
            case 7:
                firstGoal = new FourLines3DiffGoalStrategy();
                break;
            case 8:
                firstGoal = new TwoOfSixGoalStrategy();
                break;
            case 9:
                firstGoal = new TwoOf5DiffGoalStrategy();
                break;
            case 10:
                firstGoal = new FiveXGoalStrategy();
                break;
            case 11:
                firstGoal = new TriangularGoalStrategy();
        }
        switch (indexes[1]){
            case 0:
                secondGoal = new SixOfTwoGoalStrategy();
                break;
            case 1:
                secondGoal = new FiveDiagonalGoalStrategy();
                break;
            case 2:
                secondGoal = new FourOfFourGoalStrategy();
                break;
            case 3:
                secondGoal = new FourCornersGoalStrategy();
                break;
            case 4:
                secondGoal = new Double2x2GoalStrategy();
                break;
            case 5:
                secondGoal = new MaxThreeDiffGoalStrategy();
                break;
            case 6:
                secondGoal = new EightTilesGoalStrategy();
                break;
            case 7:
                secondGoal = new FourLines3DiffGoalStrategy();
                break;
            case 8:
                secondGoal = new TwoOfSixGoalStrategy();
                break;
            case 9:
                secondGoal = new TwoOf5DiffGoalStrategy();
                break;
            case 10:
                secondGoal = new FiveXGoalStrategy();
                break;
            case 11:
                secondGoal = new TriangularGoalStrategy();
        }*/
        firstGoal = new Double2x2GoalStrategy();
        secondGoal = new TriangularGoalStrategy();
    }

    public CommonGoalStrategy getFirstGoal() {
        return firstGoal;
    }

    public CommonGoalStrategy getSecondGoal() {
        return secondGoal;
    }



    public int calculateAllPoints(Player player, int numOfPlayers){
        int ris;
        int pointsOfFirst;
        int pointsOfSecond;
        /* firstGoal */
        /* potrei fare un metodo per il calcolo dei punti di un goal, così da non dover riscrivere
            la stessa cosa per entrambi i goal */

        /* CASO DI TEST: ho cambiato player.getPlayerShelf().getShelfCards() con prova, ovvero la shelf inizializzata */
        BoardCard[][] prova = new BoardCard[6][5];
        colorType[] colors = {colorType.PURPLE, colorType.BLUE, colorType.LIGHT_BLUE, colorType.YELLOW, colorType.WHITE, colorType.GREEN};
        Random random = new Random();
        int range = colors.length;
        for (int i = 0; i < 6; i++) {
            for(int j = 0; j < 5; j++) {
                int chosen = random.nextInt(range);
                BoardCard piece = new BoardCard(colors[chosen]);
                prova[i][j] = piece;
            }
        }
        System.out.println("Stampo 'prova:'");
        for(int i = 0; i < 6; i++){
            for(int j = 0;j < 5; j++){
                System.out.print(prova[i][j].getColor() + "  ");
            }
            System.out.print("\n");
        }
        System.out.println("\n");
        System.out.println("reachedFirstGoal:\n" + reachedFirstGoal + "\n");
        System.out.println("reachedSecondGoal:\n" + reachedSecondGoal + "\n");

        if(!reachedFirstGoal.contains(player)){
            if (firstGoal.goalCompleted(prova)) {/*boolean return*/
                reachedFirstGoal.add(player);
                if (numOfPlayers > 2) {
                    pointsOfFirst = 8 - (reachedFirstGoal.indexOf(player) * 2);
                } else {
                    pointsOfFirst = 8 - (reachedFirstGoal.indexOf(player) * 4);
                }
                System.out.println("firstgoal completato\n");
            } else {
                pointsOfFirst = 0;
                System.out.println("firstGoal non completato\n");
            }
        }else{
            pointsOfFirst = 0;
            System.out.println("Il player " + player.getNickname() + "ha già completato il goal\n");
        }
        /* secondGoal */
        /* CASO DI TEST: ho cambiato player.getPlayerShelf().getShelfCards() con prova, ovvero la shelf inizializzata */
        if(!reachedSecondGoal.contains(player)){
            if (secondGoal.goalCompleted(prova)) {/*boolean return*/
                reachedSecondGoal.add(player);
                if (numOfPlayers > 2) {
                    pointsOfSecond = 8 - (reachedSecondGoal.indexOf(player) * 2);
                } else {
                    pointsOfSecond = 8 - (reachedSecondGoal.indexOf(player) * 4);
                }
                System.out.println("secondGoal completato\n");
            } else {
                pointsOfSecond = 0;
                System.out.println("secondGoal non completato\n");
            }
        }else{
            pointsOfSecond = 0;
            System.out.println("Il player " + player.getNickname() + "ha già comletato il goal\n");
        }
        ris = pointsOfFirst + pointsOfSecond;
        return ris;
    }
}
