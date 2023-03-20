package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.enums.PersonalGoalType;
import it.polimi.ingsw.model.modelSupport.enums.TurnStateType;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * This class represents the player
 */
public class Player{
    private final String nickname;
    private Client ofClient;
    private String UID;
    private Integer score;
    private PersonalGoal personalGoal;
    private boolean hasChair;
    private final Shelf withShelf;
    private TurnStateType turnState;

    //TODO: fare l'inizializzazione migliore con input anche il client

    /**
     * The player is set up, whith its nickname, shelf and related client
     * @param nickname
     */
    Player(String nickname){
        this.nickname = nickname;
        hasChair = false;
        score = 0;
        turnState = TurnStateType.CARDS_SELECTION;
        withShelf = new Shelf();
    }
    public Integer getScore() {
        return score;
    }

    /**
     * This method sets the user's personal goal from an index (1 to 12).
     * We know that the game won't provide an unusable index.
     * @param personalGoalIndex
     */
    public void setPersonalGoalFromIndex(int personalGoalIndex){
        this.personalGoal = new PersonalGoal(personalGoalIndex);
    }

    public Shelf getPlayersShelf(){return withShelf;}

    public PersonalGoal getPersonalGoal() {
        return personalGoal;
    }

    public boolean hasChair(){
        return hasChair;
    }

    /**
     * sets the chaired user
     */
    public void setHasChair(){
        hasChair = true;
    }

    public void updateScore(Integer addedScore){
        this.score += addedScore;
    }

    public String getNickname() {
        return nickname;
    }

    /**
     * Called once at game finished, adds the personal goal score to the user score
     * @return returns the integer of the final score of tghe user
     */
    public int getFinalScore() {
        //HAS TO BE CALLED ONLY ONCE
        //TODO: possibility to put an observer from the player to the shelf to automatically update points
        this.addPersonalGoalPoints();
        return this.score;
    }

    private void addPersonalGoalPoints(){
        score += personalGoal.calculatePoints(withShelf.getShelfCards());
    }
}


