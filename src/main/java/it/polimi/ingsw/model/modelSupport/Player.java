package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.modelSupport.enums.TurnStateType;

import java.io.Serializable;


/**
 * This class represents the player
 */
public class Player implements Serializable {
    private final String nickname;
    private Client ofClient;
    private String UID;
    private Integer score;
    private PersonalGoal personalGoal;
    private boolean hasChair;
    private Shelf withShelf;
    private TurnStateType turnState;

    //TODO: fare l'inizializzazione migliore con input anche il client

    /**
     * The player is set up, whith its nickname, shelf and related client
     * @param nickname
     */
    public Player(String nickname){
        this.nickname = nickname;
        hasChair = false;
        score = 0;
        turnState = TurnStateType.CARDS_SELECTION;
        withShelf = new Shelf();
    }

    /**
     * Getter of the performed score up to that moment. Only contributors are CommonGoals and FullShelf.
     * @return
     */
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

    /**
     * Getter of the Shelf of a Player
     * @return withshelf, his personal Shelf
     */
    public Shelf getPlayersShelf(){return withShelf;}

    /**
     * Getter for the personalGoal of the player
     * @return personalGoal, the goal active in that specific game
     */
    public PersonalGoal getPersonalGoal() {
        return personalGoal;
    }

    /**
     * Checks whether the player has the FirstPlayer chair, thus is the first to play
     * @return boolean (true -> firstPlayer)
     */
    public boolean hasChair(){
        return hasChair;
    }

    /**
     * sets the chaired user
     */
    public void setHasChair(){
        hasChair = true;
    }

    /**
     * Updates the player's score after achieving a commonGoal or having completed the shelf.
     * @param addedScore
     */
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
        System.out.println("PUNTEGGIO: " + this.nickname);
        System.out.println("Score CommonGoals + FullShelf " + this.score);
        this.score = score + addPersonalGoalPoints() + addPersonalAdiacentPoints();
        System.out.println("Final score in getFinalScore: " + this.score);
        return this.score;
    }


    /**
     * Sums the score performed during the game with the final personalGoal score
     * @return int
     */
    private int addPersonalGoalPoints(){
        System.out.println("Personal goal points added: " + this.personalGoal.calculatePoints(withShelf.getShelfCards()));
        return this.personalGoal.calculatePoints(withShelf.getShelfCards());
    }

    /**
     * Sums the score performed during the game with the final Adjacent score
     * @return int
     */
    private int addPersonalAdiacentPoints(){
        System.out.println("Adiacent points added: " + this.withShelf.calculateAdiacentPoints());
        return this.withShelf.calculateAdiacentPoints();
    }

    /**
     * Setter for the player's Shelf
     * @param playersShelf
     */
    public void setPlayerShelf(Shelf playersShelf) {
        this.withShelf = playersShelf;
    }
}


