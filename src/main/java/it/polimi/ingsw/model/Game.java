package it.polimi.ingsw.model;


import it.polimi.ingsw.model.messageModel.InitStateMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

//this class handles the state of a match
public class Game extends GameObservable{

    private ArrayList<Player> players;
    private LivingRoom livingRoom;

    private CommonGoals commonGoals;
    private Player playingPlayer;

    private GameStateType gameState;



    Game(ArrayList<Player> fromPlayers){
        this.players = new ArrayList<Player>(fromPlayers);
        //send message of game created
        super.notifyAllObservers(new InitStateMessage(livingRoom.getPieces(), livingRoom.calculateSelectables(), this.commonGoals, new HashMap<>(players, players.stream().map(x -> x.personalGoal).Collect(Collectors.toList())), players, players.stream().reduce(x->x.hasChair)));
    }


    public ArrayList<Player> getPlayers(){
        return new ArrayList<>(players);
    }


}
