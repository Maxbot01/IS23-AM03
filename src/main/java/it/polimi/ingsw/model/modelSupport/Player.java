package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.enums.PersonalGoalType;
import it.polimi.ingsw.model.modelSupport.enums.TurnStateType;
import it.polimi.ingsw.model.modelSupport.enums.colorType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Player{
    private String nickname;
    private Client ofClient;
    private String UID;
    private Integer score;
    private PersonalGoal personalGoal;
    private boolean hasChair;
    private Shelf withShelf;
    private TurnStateType turnState;

    //TODO: fare l'inizializzazione migliore con input anche il client

    Player(){
        hasChair = false;
        turnState = TurnStateType.CARDS_SELECTION;
        withShelf = new Shelf();
        personalGoal = new PersonalGoal();
    }
    public Integer getScore() {
        return score;
    }
    public Shelf getPlayersShelf(){return withShelf;}
    public void setHasChair(){
        hasChair = true;
    }

    public void setScore(Integer score){
        this.score = score;
    }

}


