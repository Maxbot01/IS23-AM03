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

        Message selectGame(String ID, String user) throws RemoteException, LobbyFullException;
        Message createGame(Integer numPlayers, String username) throws RemoteException;
        Message ping(RemoteUserInfo fromClientInfo, MyRemoteInterface stub) throws RemoteException;
        Message setCredentials(String username, String password, RemoteUserInfo userInfo) throws RemoteException;
        Message lookForNewGames(String username) throws RemoteException;
        Message startMatch(String ID, String user) throws RemoteException;
        void createMatchFromLobby(String ID, ArrayList<String> withPlayers) throws RemoteException;
        Message selectedCards(ArrayList<Pair<Integer, Integer>> selected, String user, String gameID) throws RemoteException, UnselectableCardException;
        Message selectedColumn(ArrayList<BoardCard> selected, Integer column, String user, String gameID) throws RemoteException;
        void sendAck() throws RemoteException;
        void receiveChatMessage(String gameID, String fromUser, String message) throws RemoteException;
        void registerClient(String ipAddress) throws RemoteException;
}
