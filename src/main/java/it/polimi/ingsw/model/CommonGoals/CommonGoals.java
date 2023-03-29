package it.polimi.ingsw.model.CommonGoals;

import it.polimi.ingsw.model.CommonGoals.Strategy.*;
import it.polimi.ingsw.model.modelSupport.Player;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class CommonGoals {
    /**
     * It refers to the first goal chosen
     */
    /* private final EnumSet<CommonGoalType> possibleGoals = EnumSet.allOf(CommonGoalType.class); non viene usata */
    private CommonGoalStrategy firstGoal;
    /**
     * It refers to the second goal chosen
     */
    private CommonGoalStrategy secondGoal;
    /**
     * It consists in the ordered list of players that have reached the first goal
     */
    private final ArrayList<Player> reachedFirstGoal = new ArrayList<>();
    /**
     * It consists in the ordered list of players that have reached the second goal
     */
    private final ArrayList<Player> reachedSecondGoal = new ArrayList<>();

    /**
     * Creator of CommonGoals: chooses randomly two goal strategies out of 12 of the enumeration
     */
    public CommonGoals() {
        int[] indexes = ThreadLocalRandom.current().ints(0, 12).distinct().limit(2).toArray();
        switch (indexes[0]){
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
                firstGoal = new ThreeColumns3DiffGoalStrategy();
                break;
            case 6:
                firstGoal = new EightTilesGoalStrategy();
                break;
            case 7:
                firstGoal = new FourLines3DiffGoalStrategy();
                break;
            case 8:
                firstGoal = new TwoOf6DiffGoalStrategy();
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
                secondGoal = new ThreeColumns3DiffGoalStrategy();
                break;
            case 6:
                secondGoal = new EightTilesGoalStrategy();
                break;
            case 7:
                secondGoal = new FourLines3DiffGoalStrategy();
                break;
            case 8:
                secondGoal = new TwoOf6DiffGoalStrategy();
                break;
            case 9:
                secondGoal = new TwoOf5DiffGoalStrategy();
                break;
            case 10:
                secondGoal = new FiveXGoalStrategy();
                break;
            case 11:
                secondGoal = new TriangularGoalStrategy();
        }
        /* Assegnamento Strategy per i casi di Test */
        /* firstGoal = new TriangularGoalStrategy();
        secondGoal = new FiveDiagonalGoalStrategy(); */
    }

    /**
     * @return CommonGoalStrategy
     */
    public CommonGoalStrategy getFirstGoal() {
        return firstGoal;
    }

    /**
     * @return CommonGoalStrategy
     */
    public CommonGoalStrategy getSecondGoal() {
        return secondGoal;
    }

    /**
     * Calculates player's points based on the number of players and the goals' completion
     * @param player
     * @param numOfPlayers
     * @return int
     */
    public int calculateAllPoints(Player player, int numOfPlayers){
        int ris;
        int pointsOfFirst;
        int pointsOfSecond;
        /* TEST RANDOMICO */
        /*
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
        */
        /* TESTING FourOfFour SPECIFICI: NON ELIMINARE*/
        /*
        Caso funzionante senza 4-adiac (guarda il foglio):
        colorType[] firstLine = {colorType.BLUE,colorType.PURPLE,colorType.GREEN,colorType.LIGHT_BLUE,colorType.YELLOW};
        colorType[] secondLine = {colorType.BLUE,colorType.BLUE,colorType.PURPLE,colorType.GREEN,colorType.LIGHT_BLUE};
        colorType[] thirdLine = {colorType.PURPLE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};
        colorType[] fifthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.LIGHT_BLUE,colorType.GREEN};
        colorType[] sixthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        */
        /*
        Caso killer senza 4-adiac (guarda il foglio):
        colorType[] firstLine = {colorType.GREEN,colorType.PURPLE,colorType.GREEN,colorType.LIGHT_BLUE,colorType.YELLOW};
        colorType[] secondLine = {colorType.BLUE,colorType.BLUE,colorType.PURPLE,colorType.GREEN,colorType.LIGHT_BLUE};
        colorType[] thirdLine = {colorType.PURPLE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};
        colorType[] fifthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.LIGHT_BLUE,colorType.BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        */
        /*
        Caso funzionante con 4-adiac (guarda il foglio):
        colorType[] firstLine = {colorType.GREEN,colorType.BLUE,colorType.GREEN,colorType.LIGHT_BLUE,colorType.YELLOW};
        colorType[] secondLine = {colorType.YELLOW,colorType.BLUE,colorType.BLUE,colorType.GREEN,colorType.LIGHT_BLUE};
        colorType[] thirdLine = {colorType.PURPLE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};
        colorType[] fifthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.LIGHT_BLUE,colorType.YELLOW};
        colorType[] sixthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        */
        /*
        Caso killer con 4-adiac (guard il foglio):
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.BLUE,colorType.LIGHT_BLUE,colorType.YELLOW};
        colorType[] secondLine = {colorType.YELLOW,colorType.BLUE,colorType.BLUE,colorType.GREEN,colorType.LIGHT_BLUE};
        colorType[] thirdLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW};
        colorType[] fourthLine = {colorType.GREEN,colorType.GREEN,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};
        colorType[] fifthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW};
        colorType[] sixthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        */
        /*
        Caso funzionante senza 4-adiac con 3-adiac vicine (guarda il foglio):
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.BLUE,colorType.YELLOW};
        colorType[] secondLine = {colorType.YELLOW,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        colorType[] thirdLine = {colorType.LIGHT_BLUE,colorType.BLUE,colorType.GREEN,colorType.YELLOW,colorType.BLUE};
        colorType[] fourthLine = {colorType.GREEN,colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.BLUE};
        colorType[] fifthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW};
        colorType[] sixthLine = {colorType.GREEN,colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.LIGHT_BLUE};
        */
        /*
        Caso funionante con 16 carte che restituiscono 3 combinazioni più due di un altro colore (sono partito dal caso killer 4-adiac):
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] secondLine = {colorType.YELLOW,colorType.BLUE,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] thirdLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW};
        colorType[] fourthLine = {colorType.GREEN,colorType.GREEN,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] fifthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW};
        colorType[] sixthLine = {colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        */
        /* TESTING Double2x2 SPECIFICI: NON ELIMINARE */
        /*
        Caso limite 10 carte con disposizione killer in verticale verso dx:
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.GREEN};
        colorType[] secondLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.GREEN};
        colorType[] thirdLine = {colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.YELLOW,colorType.BLUE};
        colorType[] fourthLine = {colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        colorType[] fifthLine = {colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW};
        colorType[] sixthLine = {colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};
        */
        /*
        Caso limite 10 carte con disposizione killer in orizzontale verso l'alto:
        colorType[] firstLine = {colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.YELLOW,colorType.GREEN};
        colorType[] secondLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.GREEN};
        colorType[] thirdLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW};
        colorType[] fourthLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.GREEN};
        colorType[] fifthLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.GREEN};
        colorType[] sixthLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.GREEN};
        */
        /* TESTING EightTiles SPECIFICI: NON ELIMINARE */
        /* Caso non funzionante con 7 carte casuali:
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.WHITE};
        colorType[] secondLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.WHITE};
        colorType[] thirdLine = {colorType.GREEN,colorType.WHITE,colorType.BLUE,colorType.WHITE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};
        colorType[] fifthLine = {colorType.GREEN,colorType.BLUE,colorType.GREEN,colorType.WHITE,colorType.LIGHT_BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.WHITE,colorType.LIGHT_BLUE};
        */
        /* Caso funzionante con 8 carte casuali:
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.WHITE};
        colorType[] secondLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.WHITE};
        colorType[] thirdLine = {colorType.GREEN,colorType.WHITE,colorType.BLUE,colorType.WHITE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] fifthLine = {colorType.GREEN,colorType.BLUE,colorType.GREEN,colorType.WHITE,colorType.LIGHT_BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.WHITE,colorType.LIGHT_BLUE};
        */
        /* TESTING Triangular SPECIFICI: NON ELIMINARE */
        /* Caso funzionante:
        colorType[] firstLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT};
        colorType[] secondLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.WHITE};
        colorType[] thirdLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.WHITE,colorType.BLUE};
        colorType[] fourthLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] fifthLine = {colorType.EMPTY_SPOT,colorType.BLUE,colorType.GREEN,colorType.WHITE,colorType.LIGHT_BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.WHITE,colorType.LIGHT_BLUE};
        */
        /* Caso non funzionante:
        colorType[] firstLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT};
        colorType[] secondLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.WHITE};
        colorType[] thirdLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.BLUE,colorType.WHITE,colorType.BLUE};
        colorType[] fourthLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] fifthLine = {colorType.EMPTY_SPOT,colorType.BLUE,colorType.GREEN,colorType.WHITE,colorType.LIGHT_BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.WHITE,colorType.LIGHT_BLUE};
        */
        /* TESTING FiveDiagonal SPECIFICI: NON ELIMINARE */
        /* Caso funzionante:
        colorType[] firstLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.GREEN};
        colorType[] secondLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.BLUE};
        colorType[] thirdLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.BLUE,colorType.BLUE};
        colorType[] fourthLine = {colorType.LIGHT_BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] fifthLine = {colorType.YELLOW,colorType.BLUE,colorType.GREEN,colorType.WHITE,colorType.LIGHT_BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.WHITE,colorType.LIGHT_BLUE};
        */
        /* Caso non funzionante:
        colorType[] firstLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT};
        colorType[] secondLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.WHITE};
        colorType[] thirdLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.WHITE,colorType.BLUE};
        colorType[] fourthLine = {colorType.EMPTY_SPOT,colorType.EMPTY_SPOT,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] fifthLine = {colorType.EMPTY_SPOT,colorType.BLUE,colorType.GREEN,colorType.WHITE,colorType.LIGHT_BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.WHITE,colorType.LIGHT_BLUE};
        */
        /* TESTING FiveX SPECIFICI: NON ELIMINARE */
        /* Caso funzionante:
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.WHITE};
        colorType[] secondLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.WHITE};
        colorType[] thirdLine = {colorType.GREEN,colorType.WHITE,colorType.BLUE,colorType.WHITE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.YELLOW,colorType.YELLOW};
        colorType[] fifthLine = {colorType.GREEN,colorType.BLUE,colorType.GREEN,colorType.WHITE,colorType.LIGHT_BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.WHITE,colorType.LIGHT_BLUE};
        */
        /* Caso non funzionante:
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.WHITE};
        colorType[] secondLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.WHITE};
        colorType[] thirdLine = {colorType.GREEN,colorType.WHITE,colorType.BLUE,colorType.WHITE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.YELLOW,colorType.WHITE,colorType.YELLOW,colorType.YELLOW};
        colorType[] fifthLine = {colorType.GREEN,colorType.BLUE,colorType.GREEN,colorType.WHITE,colorType.LIGHT_BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.WHITE,colorType.LIGHT_BLUE};
        */
        /* TESTING SixOfTwo SPECIFICI: NON ELIMINARE */
        /* Caso non funzionante:
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.GREEN};
        colorType[] secondLine = {colorType.WHITE,colorType.LIGHT_BLUE,colorType.WHITE,colorType.LIGHT_BLUE,colorType.WHITE};
        colorType[] thirdLine = {colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};
        colorType[] fifthLine = {colorType.GREEN,colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.BLUE,colorType.LIGHT_BLUE};
        */
        /* Caso funzionante:
        colorType[] firstLine = {colorType.GREEN,colorType.YELLOW,colorType.GREEN,colorType.YELLOW,colorType.GREEN};
        colorType[] secondLine = {colorType.GREEN,colorType.LIGHT_BLUE,colorType.WHITE,colorType.LIGHT_BLUE,colorType.WHITE};
        colorType[] thirdLine = {colorType.GREEN,colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.BLUE};
        colorType[] fourthLine = {colorType.BLUE,colorType.BLUE,colorType.BLUE,colorType.YELLOW,colorType.LIGHT_BLUE};
        colorType[] fifthLine = {colorType.GREEN,colorType.BLUE,colorType.GREEN,colorType.BLUE,colorType.BLUE};
        colorType[] sixthLine = {colorType.BLUE,colorType.YELLOW,colorType.BLUE,colorType.BLUE,colorType.LIGHT_BLUE};
        */
        /* CASI DI TEST: sezione di assegnamento delle carte desiderate sulla shelf */
        /*
        ArrayList<colorType[]> testColors = new ArrayList<>();
        testColors.add(firstLine);
        testColors.add(secondLine);
        testColors.add(thirdLine);
        testColors.add(fourthLine);
        testColors.add(fifthLine);
        testColors.add(sixthLine);
        BoardCard[][] prova = new BoardCard[6][5];
        for(int i = 0; i < 6; i++){
            for(int j = 0; j < 5; j++){
                BoardCard piece = new BoardCard(testColors.get(i)[j]);
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
        */

        /* Vanno eliminate le println negli if */
        if(!reachedFirstGoal.contains(player)){
            if (firstGoal.goalCompleted(player.getPlayersShelf().getShelfCards())) {/*boolean return*/
                reachedFirstGoal.add(player);
                if (numOfPlayers > 2) {
                    pointsOfFirst = 8 - (reachedFirstGoal.indexOf(player) * 2);
                } else {
                    pointsOfFirst = 8 - (reachedFirstGoal.indexOf(player) * 4);
                }
                /* System.out.println("firstgoal completato\n"); */
            } else {
                pointsOfFirst = 0;
                /* System.out.println("firstGoal non completato\n"); */
            }
        }else{
            pointsOfFirst = 0;
            /* System.out.println("Il player " + player.getNickname() + " ha già completato il goal\n"); */
        }
        /* secondGoal */
        /* CASO DI TEST: ho cambiato player.getPlayerShelf().getShelfCards() con prova, ovvero la shelf inizializzata */
        if(!reachedSecondGoal.contains(player)){
            if (secondGoal.goalCompleted(player.getPlayersShelf().getShelfCards())) {/*boolean return*/
                reachedSecondGoal.add(player);
                if (numOfPlayers > 2) {
                    pointsOfSecond = 8 - (reachedSecondGoal.indexOf(player) * 2);
                } else {
                    pointsOfSecond = 8 - (reachedSecondGoal.indexOf(player) * 4);
                }
                /* System.out.println("secondGoal completato\n"); */
            } else {
                pointsOfSecond = 0;
                /* System.out.println("secondGoal non completato\n"); */
            }
        }else{
            pointsOfSecond = 0;
            /* System.out.println("Il player " + player.getNickname() + " ha già completato il goal\n"); */
        }
        ris = pointsOfFirst + pointsOfSecond;
        return ris;
    }
}
