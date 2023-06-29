package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;


public class PersonalGoal implements Serializable {

    /**
     * All the possible common goals, each of them is a List composed by [card type, x coord, y coord]
     */

    private static final List<Pair<colorType, Pair<Integer, Integer>>> first = Arrays.asList(new Pair<>(colorType.LIGHT_BLUE, new Pair<>(5, 2)), new Pair<>(colorType.YELLOW, new Pair<>(3, 1)), new Pair<>(colorType.WHITE, new Pair<>(2, 3)), new Pair<>(colorType.GREEN, new Pair<>(1, 4)), new Pair<>(colorType.PURPLE, new Pair<>(0, 0)), new Pair<>(colorType.BLUE, new Pair<>(0, 2)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> second = Arrays.asList(new Pair<>(colorType.BLUE, new Pair<>(5, 4)), new Pair<>(colorType.LIGHT_BLUE, new Pair<>(4, 3)), new Pair<>(colorType.WHITE, new Pair<>(3, 4)), new Pair<>(colorType.GREEN, new Pair<>(2, 0)), new Pair<>(colorType.YELLOW, new Pair<>(2, 2)), new Pair<>(colorType.PURPLE, new Pair<>(1, 1)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> third = Arrays.asList(new Pair<>(colorType.WHITE, new Pair<>(5, 0)), new Pair<>(colorType.GREEN, new Pair<>(3, 1)), new Pair<>(colorType.LIGHT_BLUE, new Pair<>(3, 4)), new Pair<>(colorType.PURPLE, new Pair<>(2, 2)), new Pair<>(colorType.BLUE, new Pair<>(1, 0)), new Pair<>(colorType.YELLOW, new Pair<>(1, 3)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> fourth = Arrays.asList(new Pair<>(colorType.WHITE, new Pair<>(4, 1)), new Pair<>(colorType.GREEN, new Pair<>(4, 2)), new Pair<>(colorType.PURPLE, new Pair<>(3, 3)), new Pair<>(colorType.LIGHT_BLUE, new Pair<>(2, 0)), new Pair<>(colorType.BLUE, new Pair<>(2, 2)), new Pair<>(colorType.YELLOW, new Pair<>(0, 4)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> fifth = Arrays.asList(new Pair<>(colorType.YELLOW, new Pair<>(5, 0)), new Pair<>(colorType.GREEN, new Pair<>(5, 3)), new Pair<>(colorType.PURPLE, new Pair<>(4, 4)), new Pair<>(colorType.BLUE, new Pair<>(3, 1)), new Pair<>(colorType.WHITE, new Pair<>(3, 2)), new Pair<>(colorType.LIGHT_BLUE, new Pair<>(1, 1)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> sixth = Arrays.asList(new Pair<>(colorType.PURPLE, new Pair<>(5, 0)), new Pair<>(colorType.YELLOW, new Pair<>(4, 1)), new Pair<>(colorType.BLUE, new Pair<>(4, 3)), new Pair<>(colorType.WHITE, new Pair<>(2, 3)), new Pair<>(colorType.LIGHT_BLUE, new Pair<>(0, 2)), new Pair<>(colorType.GREEN, new Pair<>(0, 4)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> seventh = Arrays.asList(new Pair<>(colorType.WHITE, new Pair<>(5, 2)), new Pair<>(colorType.YELLOW, new Pair<>(4, 4)), new Pair<>(colorType.LIGHT_BLUE, new Pair<>(3, 0)), new Pair<>(colorType.PURPLE, new Pair<>(2, 1)), new Pair<>(colorType.BLUE, new Pair<>(1, 3)), new Pair<>(colorType.GREEN, new Pair<>(0, 0)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> eight = Arrays.asList(new Pair<>(colorType.YELLOW, new Pair<>(5, 3)), new Pair<>(colorType.WHITE, new Pair<>(4, 3)), new Pair<>(colorType.PURPLE, new Pair<>(3, 0)), new Pair<>(colorType.LIGHT_BLUE, new Pair<>(2, 2)), new Pair<>(colorType.GREEN, new Pair<>(1, 1)), new Pair<>(colorType.BLUE, new Pair<>(0, 4)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> ninth = Arrays.asList(new Pair<>(colorType.BLUE, new Pair<>(5, 0)), new Pair<>(colorType.LIGHT_BLUE, new Pair<>(4, 1)), new Pair<>(colorType.PURPLE, new Pair<>(4, 4)), new Pair<>(colorType.WHITE, new Pair<>(3, 4)), new Pair<>(colorType.GREEN, new Pair<>(2, 2)), new Pair<>(colorType.YELLOW, new Pair<>(0, 2)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> tenth = Arrays.asList(new Pair<>(colorType.PURPLE, new Pair<>(5, 3)), new Pair<>(colorType.BLUE, new Pair<>(4, 1)), new Pair<>(colorType.GREEN, new Pair<>(3, 3)), new Pair<>(colorType.WHITE, new Pair<>(2, 0)), new Pair<>(colorType.YELLOW, new Pair<>(1, 1)), new Pair<>(colorType.LIGHT_BLUE, new Pair<>(0, 4)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> eleventh = Arrays.asList(new Pair<>(colorType.LIGHT_BLUE, new Pair<>(5, 3)), new Pair<>(colorType.GREEN, new Pair<>(4, 4)), new Pair<>(colorType.BLUE, new Pair<>(3, 2)), new Pair<>(colorType.YELLOW, new Pair<>(2, 0)), new Pair<>(colorType.WHITE, new Pair<>(1, 1)), new Pair<>(colorType.PURPLE, new Pair<>(0, 2)));
    private static final List<Pair<colorType, Pair<Integer, Integer>>> twelve = Arrays.asList(new Pair<>(colorType.GREEN, new Pair<>(5, 0)), new Pair<>(colorType.YELLOW, new Pair<>(4, 4)), new Pair<>(colorType.LIGHT_BLUE, new Pair<>(3, 3)), new Pair<>(colorType.BLUE, new Pair<>(2, 2)), new Pair<>(colorType.PURPLE, new Pair<>(1, 1)), new Pair<>(colorType.WHITE, new Pair<>(0, 2)));

    /**
     * Compact List of the available CommonGoals
     */
    private static final List<List<Pair<colorType, Pair<Integer, Integer>>>> personalGoals= Arrays.asList(first, second, third, fourth, fifth, sixth, seventh, eight, ninth, tenth, eleventh, twelve);
    private final List<Pair<colorType, Pair<Integer, Integer>>> selectedGoal;

    /**
     * Initializes the selected goal for the player, caller guarantees the index be different from the other players index
     * @param withIndex personal goal card # to choose
     */

    private int goalIndex;

    /**
     * Initializes the selected goal for the player, caller guarantees the index be different from the other players index
     * @param withIndex personal goal card # to choose
     */
    public PersonalGoal(int withIndex) {
        this.goalIndex = withIndex;
        this.selectedGoal = personalGoals.get(withIndex);
    }

    /**
     * By looking at the shelf cards matrix the personal goals score is calculated
     * @param shelfCards players shelf cards
     * @return returns the calculated points
     */
    // ...

    public int calculatePoints(BoardCard[][] shelfCards) {
        int points = 0;

        // Iterate over the shelfCards matrix
        for (int i = 0; i < shelfCards.length; i++) {
            for (int j = 0; j < shelfCards[i].length; j++) {
                // Check if the current card is not null
                if (shelfCards[i][j] != null) {
                    // Iterate over the selectedGoal list
                    for (Pair<colorType, Pair<Integer, Integer>> pair : selectedGoal) {
                        // Get the color and coordinates from the pair
                        colorType color = pair.getFirst();
                        Pair<Integer, Integer> coordinates = pair.getSecond();
                        // Compare the color and coordinates with the shelf card
                        if (shelfCards[i][j].getColor().equals(color) &&
                                i == coordinates.getFirst() && j == coordinates.getSecond()) {
                            System.out.println("Found a match at " + i + " " + j);
                            points++;
                        }
                    }
                }
            }
        }

        // Final score is determined from the number of matches
        switch (points) {
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
                System.out.println("Error in personal goal calculation");
                return 0; // or throw an exception for invalid inputs
        }
    }


    /**
     * Getter for the PersonalGoal in use
     * @return selectedGoal
     */
    public List<Pair<colorType, Pair<Integer, Integer>>> getSelectedGoal() {
        return selectedGoal;
    }

    /**
     * Getter for the index of the PersonalGoal in use
     * @return the index of the actual goal used in the game
     */
    public int getSelectedGoalIndex(){return goalIndex;}
}
