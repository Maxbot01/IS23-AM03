package it.polimi.ingsw.view.GUIView;

import it.polimi.ingsw.client.ClientMain;
import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GUIView extends View {

    @Override
    public void initializeGame(List<String> players, CommonGoals commonGoals, HashMap<String, PersonalGoal> personalGoals, BoardCard[][] livingRoom, Boolean[][] selectables, ArrayList<Pair<String, BoardCard[][]>> playersShelves, HashMap<String, Integer> playersPoints, GameStateType gameState) {
       //MAX
    }

    @Override
    public void updateMatchAfterSelectedCards(BoardCard[][] livingRoom, Boolean[][] selectables, GameStateType gameState) {
        //MAX
    }

    @Override
    public void updateMatchAfterSelectedColumn(BoardCard[][] livingRoom, Boolean[][] selectables, GameStateType gameState, Pair<String, Integer> updatedPlayerPoints, Pair<String, BoardCard[][]> updatedPlayerShelf) {
        //MAX
    }

    @Override
        public void waitingCommands() {
//NO
        }

    @Override
    public void requestCredentials() {
        System.out.println("requestedCred");
        ScreenSwitcher.showLoginView();
    }

    @Override
    public void launchGameManager(HashMap<String, List<String>> availableGames) {
        System.out.println("gameManager");
        ScreenSwitcher.showGameManagerView(availableGames);
    }

    @Override
    public void launchGameLobby(String gameId, ArrayList<String> players, String host) {
        System.out.println("gameLobby");
        boolean isHost = host.equals(ClientManager.userNickname);
        ScreenSwitcher.showGameLobbyView(players, host, isHost);
    }

    @Override
    public void chooseCards() {
        //MAX
    }

    @Override
    public void chooseColumn() {
        //MAX
    }

    @Override
    public void endCommands() {
        //MAX*
    }

    @Override
    public void printLivingRoom() {
      //NO
    }

    @Override
    public void printShelves() {
        //NO
    }

    @Override
    public void showErrorMessage(String error) {
        //MAX
    }

    @Override
    public void showPlayingPlayer(String playingPlayer) {
        //NO
    }

    @Override
    public void printScoreBoard(ArrayList<Pair<String, Integer>> finalScoreBoard, String winner, GameStateType gameState) {
        //NO
    }
}
