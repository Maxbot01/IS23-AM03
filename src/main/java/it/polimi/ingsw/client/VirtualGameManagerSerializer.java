package it.polimi.ingsw.client;

import com.google.gson.reflect.TypeToken;
import it.polimi.ingsw.model.GameManager;
import it.polimi.ingsw.model.helpers.*;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import com.google.gson.Gson;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;

import javax.swing.plaf.synth.ColorType;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

/**
 * A class for serializing and deserializing VirtualGameManager method calls.
 */
public class VirtualGameManagerSerializer {
    private String method;
    private Object[] args;

    /**
     * Constructs a VirtualGameManagerSerializer object with the given method and arguments.
     * @param method the name of the method to be called
     * @param args the arguments for the method
     */
    public VirtualGameManagerSerializer(String method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    /**
     * Returns the name of the method.
     * @return the name of the method
     */
    public String getMethod() {
        return method;
    }

    /**
     * Returns the arguments for the method.
     * @return the arguments for the method
     */
    public Object[] getArgs() {
        return args;
    }

    /**
     * Sets the name of the method.
     * @param method the name of the method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * Sets the arguments for the method.
     * @param args the arguments for the method
     */
    public void setArgs(Object[] args) {
        this.args = args;
    }

    /**
     * Serializes a VirtualGameManagerSerializer object to JSON.
     * @param virtualGameManagerSerializer the object to serialize
     * @return the JSON string representation of the object
     */
    public static String serializeMethod(VirtualGameManagerSerializer virtualGameManagerSerializer) {
        Gson gson = new Gson();
        return gson.toJson(virtualGameManagerSerializer);
    }

    /**
     * Deserializes a JSON string representation of a VirtualGameManagerSerializer object and calls the corresponding
     * VirtualGameManager method with the arguments.
     * @param jsonString the JSON string representation of the object
     */
    public static void deserializeMethod(String jsonString) {
        GameManager gameManager = GameManager.getInstance();
        Gson gson = new Gson();
        VirtualGameManagerSerializer virtualGameManagerSerializer = gson.fromJson(jsonString, VirtualGameManagerSerializer.class);

        switch(virtualGameManagerSerializer.getMethod()) {
            case "ping":
                String uid = (String) virtualGameManagerSerializer.getArgs()[0];
                gameManager.ping(uid);
                break;
            case "setCredentials":
                String username = (String) virtualGameManagerSerializer.getArgs()[0];
                String password = (String) virtualGameManagerSerializer.getArgs()[1];
                uid = (String) virtualGameManagerSerializer.getArgs()[2];
                gameManager.setCredentials(username, password, uid);
                break;
            case "selectGame":
                String gameID = (String) virtualGameManagerSerializer.getArgs()[0];
                String user = (String) virtualGameManagerSerializer.getArgs()[1];
                gameManager.selectGame(gameID, user);
                break;
            case "createGame":
                Double numPlayersDouble = (Double) virtualGameManagerSerializer.getArgs()[0];
                int numPlayers = numPlayersDouble.intValue();
                String user1 = (String) virtualGameManagerSerializer.getArgs()[1];
                gameManager.createGame(numPlayers, user1);
                break;
            case "sendAck":
                gameManager.sendAck();
                break;
            case "startMatch":
                String ID = (String) virtualGameManagerSerializer.getArgs()[0];
                String user2 = (String) virtualGameManagerSerializer.getArgs()[1];
                gameManager.startMatch(ID, user2);
                break;
            case "selectedCards":
                String user3 = (String) virtualGameManagerSerializer.getArgs()[1];
                String gameID1 = (String) virtualGameManagerSerializer.getArgs()[2];
                //Object obj = virtualGameManagerSerializer.getArgs()[0];
                //System.out.println("OBJECT: "+obj.toString());
                gameManager.selectedCards(getPairFormat(getElementsFromObject(virtualGameManagerSerializer.getArgs()[0])), user3, gameID1);
                //gameManager.selectedCards((ArrayList<Pair<Integer,Integer>>) virtualGameManagerSerializer.getArgs()[0], user3, gameID1);
                break;
            case "selectedColumn":
                String user4 = (String) virtualGameManagerSerializer.getArgs()[2];
                String gameID2 = (String) virtualGameManagerSerializer.getArgs()[3];
                gameManager.selectedColumn(getBoardCardFormat(getElementsFromObject(virtualGameManagerSerializer.getArgs()[0])),
                        Character.getNumericValue(virtualGameManagerSerializer.getArgs()[1].toString().charAt(0)), user4, gameID2);
                //gameManager.selectedColumn((ArrayList<BoardCard>) virtualGameManagerSerializer.getArgs()[0], (Integer)virtualGameManagerSerializer.getArgs()[1], user4, gameID2);
        }
    }
    /**
     * It converts an object into an ArrayList of strings
     * @param obj
     * @return ArrayList<String>
     */
    private static ArrayList<String> getElementsFromObject(Object obj){
        ArrayList<String> strings = new ArrayList<>();
        String s = obj.toString();
        char c;
        for(int i = 0; i < s.length(); i++){
            c = s.charAt(i);
            if(c == '{'){
                String tmp = "";
                i++;
                while(s.charAt(i) != '}'){
                    c = s.charAt(i);
                    tmp = tmp.concat(String.valueOf(c));
                    i++;
                }
                if(tmp.length() != 0){
                    strings.add(tmp);
                }
            }
        }
        return strings;
    }
    /**
     * It converts an ArrayList of strings into an ArrayList of Pair<Integer,Integer>. Be sure the format you want to change is compatible
     * @param elements
     * @return ArrayList<Pair<Integer,Integer>>
     */
    private static ArrayList<Pair<Integer,Integer>> getPairFormat(ArrayList<String> elements){
        ArrayList<Pair<Integer,Integer>> pairs = new ArrayList<>();
        for(String s: elements){
            int first = 0;
            int second = 0;
            boolean firstFound = false;
            for(int i = 0; i < s.length(); i++){
                if(s.charAt(i) == '='){
                    if(!firstFound) {
                        first = i + 1;
                        firstFound = true;
                    } else {
                        second = i + 1;
                        break;
                    }
                }
            }
            Pair<Integer,Integer> p = new Pair<>(Character.getNumericValue(s.charAt(first)),Character.getNumericValue(s.charAt(second)));
            pairs.add(p);
        }
        return pairs;
    }

    /**
     * It converts an ArrayList of string into an ArrayList of BoardCards. Be sure the format you want to change is compatible
     * @param elements
     * @return ArrayList<BoardCard>
     */
    private static ArrayList<BoardCard> getBoardCardFormat(ArrayList<String> elements){
        ArrayList<BoardCard> boardCards = new ArrayList<>();

        boolean foundColor = false;
        for(String s: elements){
            String color = "";
            String ornament = "";
            for(int i = 0; i < s.length(); i++){
                if(s.charAt(i) == ':'){
                    System.out.println("':' found at index "+i+" with foundColor at "+foundColor);
                    if(!foundColor){
                        i += 2;
                        while(s.charAt(i) != '"'){
                            color = color.concat(String.valueOf(s.charAt(i)));
                            i++;
                        }
                        foundColor = true;
                    }else{
                        i += 2;
                        ornament = String.valueOf(s.charAt(i));
                    }
                }
            }
            if(color.length() != 0 && ornament.length() != 0){
                BoardCard b = new BoardCard(colorType.valueOf(color), ornamentType.valueOf(ornament));
                System.out.println(b);
                colorType.valueOf(color);
                boardCards.add(b);
                System.out.println(boardCards);
            }else{
                System.out.println("il formato delle stringhe non era corretto");
            }
        }
        return boardCards;
    }
}
