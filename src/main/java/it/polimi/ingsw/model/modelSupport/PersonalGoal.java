package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class PersonalGoal {

    //TODO: riempire queste
    private static final List<Pair<colorType, Pair<Integer, Integer>>> first = Arrays.asList(new Pair(colorType.PURPLE, new Pair(0, 0)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> second = Arrays.asList(new Pair(colorType.PURPLE, new Pair(0, 0)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> third = Arrays.asList(new Pair(colorType.PURPLE, new Pair(0, 0)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> fourth = Arrays.asList(new Pair(colorType.PURPLE, new Pair(0, 0)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> fifth = Arrays.asList(new Pair(colorType.PURPLE, new Pair(0, 0)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> sixth = Arrays.asList(new Pair(colorType.PURPLE, new Pair(0, 0)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> seventh = Arrays.asList(new Pair(colorType.PURPLE, new Pair(0, 0)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> eight = Arrays.asList(new Pair(colorType.PURPLE, new Pair(0, 0)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> ninth = Arrays.asList(new Pair(colorType.PURPLE, new Pair(0, 0)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> tenth = Arrays.asList(new Pair(colorType.PURPLE, new Pair(0, 0)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> eleventh = Arrays.asList(new Pair(colorType.PURPLE, new Pair(0, 0)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> twelve = Arrays.asList(new Pair(colorType.PURPLE, new Pair(0, 0)));

    private List<Pair<colorType, Pair<Integer, Integer>>> selectedGoal;

    PersonalGoal() {
        Random rand = new Random();
        // generate a random integer between 0 and 11 (inclusive)
        int randomNum = rand.nextInt(12);
        switch (randomNum) {
            case 0:
                this.selectedGoal = first;
                break;
            case 1:
                this.selectedGoal = second;
                break;
            case 2:
                this.selectedGoal = third;
                break;
            case 3:
                this.selectedGoal = fourth;
                break;
            case 4:
                this.selectedGoal = fifth;
                break;
            case 5:
                this.selectedGoal = sixth;
                break;
            case 6:
                this.selectedGoal = seventh;
                break;
            case 7:
                this.selectedGoal = eight;
                break;
            case 8:
                this.selectedGoal = ninth;
                break;
            case 9:
                this.selectedGoal = tenth;
                break;
            case 10:
                this.selectedGoal = eleventh;
                break;
            case 11:
                this.selectedGoal = twelve;
                break;
            default:
                break;
        }
    }
}
