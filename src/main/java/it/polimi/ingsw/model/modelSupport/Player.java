package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.Game;

public class Player extends Game {
    private String nickname;
    private Client ofClient;
    private String UID;
    private Integer score;
    private PersonalGoalType personalGoal;
    private boolean hasChair;
    private Shelf withShelf;
    private turnStateType turnState;

    public Integer getScore() {
        return score;
    }
}
