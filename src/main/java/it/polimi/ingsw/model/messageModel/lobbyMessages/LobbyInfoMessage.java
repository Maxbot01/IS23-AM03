package it.polimi.ingsw.model.messageModel.lobbyMessages;

import it.polimi.ingsw.model.messageModel.Message;

import java.util.ArrayList;

public class LobbyInfoMessage extends Message {
    public String ID;
    public String host;
    public int numOfPlayers;
    public ArrayList<String> players;
    public LobbyInfoMessage(String ID, String host, int numOfPlayers, ArrayList<String> players){
        this.host = host;
        this.ID = ID;
        this.numOfPlayers = numOfPlayers;
        this.players = players;
    }

}
