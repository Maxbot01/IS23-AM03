package it.polimi.ingsw.model;

import it.polimi.ingsw.client.MessageSerializer;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.server.MyRemoteInterface;
import it.polimi.ingsw.server.RemoteUserInfo;
import it.polimi.ingsw.server.ServerMain;

import java.util.List;

public abstract class GameObservable {

    protected void notifyObserver(String toPlayer, Message withMessage, boolean inLobbyOrGame, String gameID){
        //if we are in a lobby or in a game needs to send the id of the lobby/game

        sendMessageToNetworkUser(toPlayer, withMessage, gameID);


        //withMessage.printMessage();
    }

    protected void notifyNetworkClient(RemoteUserInfo client, Message withMessage){
        if(client.isConnectionSocket()){
            //send socket
            MessageSerializer messageSerializer = new MessageSerializer();
            String serializedMessage;
            //GameManager.getInstance().getUID(t

            serializedMessage = messageSerializer.serialize(withMessage, "", "");
            ServerMain.server.sendMessageToSocket(serializedMessage, client.getSocketID());
        }else{
            //send rmi
            // Ottenere una referenza all'oggetto remoto associato al client
            MyRemoteInterface remoteObject = client.getRemoteObject();

            // Invocare il metodo desiderato sull'oggetto remoto
            remoteObject.receiveMessage(withMessage, client.getRmiUID());
        }

    }

    protected void notifyAllObservers(List<String> observers, Message withMessage, boolean inLobbyOrGame, String gameID){
        //send the message for every given nick (TO CHANGE MAYBE)
        System.out.println("Sending message to everyone" + observers);
        //TODO: change format for this
        for(String player: observers){
            sendMessageToNetworkUser(player, withMessage, gameID);
        }

        withMessage.printMessage();
    }

    private void sendMessageToNetworkUser(String toPlayer, Message withMessage, String gameID) {
        if(GameManager.getInstance().userIdentification.get(toPlayer).isConnectionSocket()){
            //user is socket
            MessageSerializer messageSerializer = new MessageSerializer();
            String serializedMessage;
            //GameManager.getInstance().getUID(t

            serializedMessage = messageSerializer.serialize(withMessage, toPlayer, gameID);
            System.out.println("Sending message to " + toPlayer + ": " + serializedMessage.toString());

            ServerMain.server.sendMessageToSocket(serializedMessage, GameManager.getInstance().userIdentification.get(toPlayer).getSocketID());
        }else{
            //TODO: user is RMI
            //send rmi
            // Ottenere una referenza all'oggetto remoto associato al client
            MyRemoteInterface remoteObject = GameManager.getInstance().userIdentification.get(toPlayer).getRemoteObject();

            // Invocare il metodo desiderato sull'oggetto remoto
            remoteObject.receiveMessage(withMessage, GameManager.getInstance().userIdentification.get(toPlayer).getRmiUID());
        }
    }

    /*
    protected void mapReceivedCall(String, String[]) throws Exception{
        //send the message
    }*/
}
