package it.polimi.ingsw.model;

import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.Message;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;
import it.polimi.ingsw.model.modelSupport.exceptions.lobbyExceptions.LobbyFullException;
import it.polimi.ingsw.server.RemoteUserInfo;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface MyRemoteInterface extends Remote, Serializable {
        // further remote methods

        void selectGame(String ID, String user) throws RemoteException, LobbyFullException;

        void createGame(Integer numPlayers, String username, String clientIP) throws RemoteException;

        void ping(RemoteUserInfo fromClientInfo) throws RemoteException;

        void setCredentials(String username, String password, RemoteUserInfo userInfo) throws RemoteException;

        void lookForNewGames(String username) throws RemoteException;

        void startMatch(String ID, String user, MyRemoteInterface stub) throws RemoteException;

        void createMatchFromLobby(String ID, ArrayList<String> withPlayers) throws RemoteException;

        void selectedCards(ArrayList<Pair<Integer, Integer>> selected, String user, String gameID) throws RemoteException, UnselectableCardException;

        void selectedColumn(ArrayList<BoardCard> selected, Integer column, String user, String gameID) throws RemoteException;

        void sendAck() throws RemoteException;

        void userReady(String username, String lobbyID) throws RemoteException;

        void registerClient(String ipAddress) throws RemoteException;

        Message ReceiveMessageRMI(String clientIP) throws RemoteException;

        void addRemoteUser(String username, RemoteUserInfo remoteUserInfo) throws RemoteException;

        void receiveChatMessage(String gameID, String toUser, String fromUser, String message, boolean fullChat, boolean inGame) throws RemoteException;

        String getGameLobbyHost(String gameID) throws RemoteException;
        Message getPreviousMessageRMI(String ipAddress) throws RemoteException;
        void setFlag(boolean flag) throws RemoteException;
        boolean getFlag() throws RemoteException;

}
