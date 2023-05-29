package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.helpers.Pair;

import java.util.ArrayList;
import java.util.List;

public class ChatMessage extends Message{
    public ArrayList<Pair<String, String>> messages;
    public boolean inGame;
    public ChatMessage(ArrayList<Pair<String, String>> messages, boolean inGame){
        this.messages = messages;
        this.inGame = inGame;
    }
}
