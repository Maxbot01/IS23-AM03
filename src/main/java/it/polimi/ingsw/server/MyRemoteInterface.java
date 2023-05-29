package it.polimi.ingsw.server;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;
import it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions.LobbyFullException;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface MyRemoteInterface extends Remote {
        // Altri metodi remoti...

        void selectGame(String ID, String user) throws RemoteException, LobbyFullException;
        void createGame(Integer numPlayers, String username) throws RemoteException;
        void ping(RemoteUserInfo fromClientInfo) throws RemoteException;
        void setCredentials(String username, String password, RemoteUserInfo userInfo) throws RemoteException;
        void lookForNewGames(String username) throws RemoteException;
        void startMatch(String ID, String user) throws RemoteException;
        void createMatchFromLobby(String ID, ArrayList<String> withPlayers) throws RemoteException;
        void selectedCards(ArrayList<Pair<Integer, Integer>> selected, String user, String gameID) throws RemoteException, UnselectableCardException;
        void selectedColumn(ArrayList<BoardCard> selected, Integer column, String user, String gameID) throws RemoteException;
        void sendAck();
        void receiveMessage(Message withMessage, String rmiUID);
        void receiveChatMessage(String gameID, String fromUser, String message, boolean fullChat, boolean inGame);
}
