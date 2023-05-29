package it.polimi.ingsw.model.messageModel;

import it.polimi.ingsw.model.helpers.Pair;

import java.util.ArrayList;
import java.util.List;

public class ChatMessage extends Message{
    public ChatMessage(ArrayList<Pair<String, String>> messages){
        this.messages = messages;
    }
    public ArrayList<Pair<String, String>> messages;
}
