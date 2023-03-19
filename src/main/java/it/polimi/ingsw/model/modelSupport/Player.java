package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.modelSupport.enums.PersonalGoalType;
import it.polimi.ingsw.model.modelSupport.enums.TurnStateType;

public class Player{
    private String nickname;
    private Client ofClient;
    private String UID;
    private Integer score;
    private PersonalGoalType personalGoal;
    private boolean hasChair;
    private Shelf withShelf;
    private TurnStateType turnState;

    //TODO: fare l'inizializzazione migliore con input anche il client

    Player(){
        hasChair = false;
    }
    public Integer getScore() {
        return score;
    }
    public void setHasChair(){
        hasChair = true;
    }
}
