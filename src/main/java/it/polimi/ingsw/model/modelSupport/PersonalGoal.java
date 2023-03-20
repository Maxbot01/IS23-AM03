package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class PersonalGoal {

    //TODO: riempire queste
    private static final List<Pair<colorType, Pair<Integer, Integer>>> first = Arrays.asList(new Pair(colorType.PURPLE, new Pair(0, 0)), new Pair(colorType.BLUE, new Pair(0, 2)), new Pair(colorType.GREEN, new Pair(1, 4)), new Pair(colorType.WHITE, new Pair(2, 3)), new Pair(colorType.YELLOW, new Pair(3, 1)), new Pair(colorType.LIGHT_BLUE, new Pair(5, 2)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> second = Arrays.asList(new Pair(colorType.PURPLE, new Pair(1, 1)), new Pair(colorType.GREEN, new Pair(2, 0)), new Pair(colorType.YELLOW, new Pair(2, 2)), new Pair(colorType.WHITE, new Pair(3, 4)), new Pair(colorType.LIGHT_BLUE, new Pair(4, 3)), new Pair(colorType.BLUE, new Pair(5, 4)));
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

    /**
     * List of all the possible common goals, each of them is a List composed by [card type, x coord, y coord]
     */
    private static final List<List<Pair<colorType, Pair<Integer, Integer>>>> personalGoals= Arrays.asList(first, second, third, fourth, fifth, seventh, eight, ninth, tenth, eleventh, twelve);
    private List<Pair<colorType, Pair<Integer, Integer>>> selectedGoal;

    /**
     * Initializes the selected goal for the player, caller guarantees the index be different from the other players index
     * @param withIndex personal goal card # to choose
     */
    PersonalGoal(int withIndex) {
        this.selectedGoal = personalGoals.get(withIndex);
    }

    /**
     * By looking at the shelf cards matrix the personal goals score is calculated
     * @param withCards players shelf cards
     * @return returns the calculated points
     */
    public int calculatePoints(BoardCard[][] withCards){
        int correct = 0;
        for(int i = 0; i < selectedGoal.size(); i++){
            Pair<colorType, Pair<Integer, Integer>> item = selectedGoal.get(i);
            //if same color at correct position
            if(item.getFirst() == withCards[item.getSecond().getFirst()][item.getSecond().getSecond()].getColor()){
                correct++;
            }
        }
        switch(correct) {
            case 1:
                return 1;
            case 2:
                return 2;
            case 3:
                return 4;
            case 4:
                return 6;
            case 5:
                return 9;
            case 6:
                return 12;
            default:
                return 0; // or throw an exception for invalid inputs
        }
    }
}
