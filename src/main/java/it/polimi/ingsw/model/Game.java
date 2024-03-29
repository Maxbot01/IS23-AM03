package it.polimi.ingsw.model;


import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorMessage;
import it.polimi.ingsw.model.messageModel.errorMessages.ErrorType;
import it.polimi.ingsw.model.messageModel.matchStateMessages.FinishedGameMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.InitStateMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedCardsMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedColumnsMessage;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.LivingRoom;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.exceptions.ColumnNotSelectable;
import it.polimi.ingsw.model.modelSupport.exceptions.NoMoreCardsException;
import it.polimi.ingsw.model.modelSupport.exceptions.ShelfFullException;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;

import java.io.Serializable;
import java.rmi.Remote;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

//this class handles the state of a match

/**
 * This class is the core of a game.
 * Represents the implementation of the API that the controller can use during the game, updates the state after the clients calls
 */
public class Game extends GameObservable implements Serializable, Remote {

    /**
     * A list of all the players playing the game, the order of the list is also the order of the match
     */
    private final ArrayList<Player> players;
    /**
     * Represents the livingroom, used with its methods to update the game state
     */
    private final LivingRoom livingRoom;
    /**
     * Represents the common goals in a match, they are set at init. Used also to update the players score
     */
    private final CommonGoals commonGoals;
    /**
     * Currently playing player
     */
    private Player playingPlayer;

    /**
     * Current state of the game
     */
    private GameStateType gameState;

    private String ID;
    private String host;



    /**
     * Initialization of the game.
     * The players have already been created in the lobby, here each player is given its personal goal randomly.
     * The livingroom, common goals, random chaired player are set.
     * The game state is set as IN_PROGRESS and everyone is properly notified with an InitStateMessage
     *
     * @param fromPlayers players playing the game
     */
    public Game(ArrayList<Player> fromPlayers, String ID, String host){
        this.ID = ID;
        this.players = new ArrayList<Player>(fromPlayers);
        //all the players need to have a separate commonGoal, generates different indexes from 0 to 11 for creation
        int[] indexes = ThreadLocalRandom.current().ints(0, 12).distinct().limit(fromPlayers.size()).toArray();
        //sets the different common goal for every player
        for (int i = 0; i < indexes.length; i++) {
            players.get(i).setPersonalGoalFromIndex(indexes[i]);
        }

        //set the livingroom
        this.host = host;
        this.livingRoom = new LivingRoom(players.size());
        //set common goals
        this.commonGoals = new CommonGoals();
        //set randomly the player that has the chair, he's starting the match
        Random ranGen = new Random();
        int ranPlayerIndx = ranGen.nextInt(players.size());
        this.players.get(ranPlayerIndx).setHasChair();
        //the chaired player is starting the match
        this.playingPlayer = players.get(ranPlayerIndx);
        this.gameState = GameStateType.IN_PROGRESS;
        //init done, can update the clients with all the needed data
        HashMap<String, PersonalGoal> personalGoals = new HashMap<>();
        ArrayList<Pair<String, BoardCard[][]>> playersShelves = new ArrayList<>();
        for (Player p: this.players) {
            playersShelves.add(new Pair<>(p.getNickname(), p.getPlayersShelf().getShelfCards()));
            personalGoals.put(p.getNickname(), p.getPersonalGoal());
        }
        BoardCard[] cur = {};
        System.out.println("started match, sending message");
        super.notifyAllObservers(getAllNicks(), new InitStateMessage(GameStateType.IN_PROGRESS, "ID",  livingRoom.getPieces(), livingRoom.calculateSelectable(), this.commonGoals, personalGoals, this.players.stream().map(x -> x.getNickname()).collect(Collectors.toList()), this.playingPlayer.getNickname(), playersShelves), true, this.ID);
    }

    public String getID(){
        return ID;
    }

    //getter host
    public String getHost(){
        return host;
    }


    /**
     * The player knew which cards he could select, he chose them. This method updates the board and returns the update to everyone.
     * Broadcasts the selected cards so that everyone can see them via a SelectedCardsMessage.
     * @param selected selected cards from the user
     */
    public void selectedCards(ArrayList<Pair<Integer, Integer>> selected, String user) throws UnselectableCardException {
        /*
        l'utente sa che carte poteva scegliere, le ha scelte. Il metodo aggiorna la board (i pezzi) chiamando updateBoard di Livingroom.
        Invia il messaggio al controller
         */
        //TODO: check to be sure that the right player played
        ArrayList<BoardCard> selectedCardsTypes = new ArrayList<>();
        //try {//Exception handled in GameManager
        for (Pair<Integer, Integer> pr: selected) {
            selectedCardsTypes.add(this.livingRoom.getBoardCardAt(pr));
        }
        this.livingRoom.updateBoard(selected);
        super.notifyAllObservers(getAllNicks(), new SelectedCardsMessage(GameStateType.IN_PROGRESS, "ID", selectedCardsTypes, livingRoom.calculateSelectable(), livingRoom.getPieces(), playingPlayer), true, this.ID);
        /*} catch (UnselectableCardException e) {
            super.notifyObserver(user,new ErrorMessage(ErrorType.selectedCardsMessageError, e.info),true,ID);
            //throw new RuntimeException(e);
        }*/
        /*try {
            this.livingRoom.updateBoard(selected);//It would send two error messages, making two chooseCards call
        } catch (UnselectableCardException e) {
            super.notifyObserver(user,new ErrorMessage(ErrorType.selectedCardsMessageError),true,ID);
            throw new RuntimeException(e);
        }*/
        //It is sent only if successful -> super.notifyAllObservers(getAllNicks(), new SelectedCardsMessage(GameStateType.IN_PROGRESS, "ID", selectedCardsTypes, livingRoom.calculateSelectable(), livingRoom.getPieces(), playingPlayer), true, this.ID);

    }

    private List<String> getAllNicks(){
        return players.stream().map(x -> x.getNickname()).collect(Collectors.toList());
    }

    /**
     * Gets called from the controller when the user has chosen the order and the column to put the cards (operation managed by the controller).
     * If the column is not selectable an error message is sent, otherwise the playing player's shelf is updated and the points gained from the players move are calculated.
     * If the player has completed its board LAST_ROUND is set as the new state. If the player is the last of the list and it's the last round the game ends.
     * Otherwise, the new player is set, the board is checked for an eventual refill managed by LivingRoom and finally round finished message (SelectedColumnsMessage) is sent.
     * @param selCards selected cards by the player
     * @param colIndex selected column by the player
     */
    public void selectedColumn(ArrayList<BoardCard> selCards, Integer colIndex, String user) { //TODO: See if user is necessary
        try {
            this.playingPlayer.getPlayersShelf().insertInColumn(selCards, colIndex);
            this.playingPlayer.setPlayerShelf(this.playingPlayer.getPlayersShelf());
        }catch(ColumnNotSelectable e) {
            //can't insert the items in the columns, send error message to client
            super.notifyObserver(playingPlayer.getNickname()/*TODO: Same as signature 'user'? be sure*/, new ErrorMessage(ErrorType.selectedColumnsError,e.info), true, this.ID);
            return;
        }catch (ShelfFullException e1){
            //TODO: handle game over
            //the shelf is full, the state changes to LAST_ROUND
            this.gameState = GameStateType.LAST_ROUND;
            //the current player gets the bonus point for finishing
            this.playingPlayer.updateScore(1);
            super.notifyObserver(playingPlayer.getNickname(), new ErrorMessage(ErrorType.shelfFullError,e1.info), true, this.ID);
        }
        //the playing players shelf is updated
        //calculate players points gained from the move
        this.playingPlayer.updateScore(this.commonGoals.calculateAllPoints(playingPlayer, players.size()));
        //if we are in the last round and the next player has the chair we can set the state as FINISHED and terminate the match
        if(gameState == GameStateType.LAST_ROUND && getNextPlayer().hasChair()){
            this.gameState = GameStateType.FINISHED;
            ArrayList<Pair<String, Integer>> finalScoreBoard = new ArrayList<>();
            //Sending the last shelf update before the final score because the calculateAdiacentPoints sosbstitutes the cards with empty_spot
            super.notifyAllObservers(getAllNicks(), new SelectedColumnsMessage(gameState, "ID", new Pair<>(playingPlayer.getNickname(),
                    playingPlayer.getScore()), getNextPlayer().getNickname(), new Pair<>(playingPlayer.getNickname(),this.playingPlayer.getPlayersShelf().
                    getShelfCards()),this.livingRoom.getPieces(), this.livingRoom.calculateSelectable()), true, this.ID);

            for (Player pl:players){
                //TODO: possibility to put an observer from the player to the shelf to automatically update points
                //get final score adds personal points to the score so it has to be called only once
                finalScoreBoard.add(new Pair<>(pl.getNickname(), pl.getFinalScore()));
            }
            String winnerNickname = finalScoreBoard.stream().reduce((a, b) -> a.getSecond() > b.getSecond() ? a : b).get().getFirst();
            //Sending the last shelf update
            super.notifyAllObservers(getAllNicks(), new FinishedGameMessage(gameState, "ID", finalScoreBoard, winnerNickname), true, this.ID);
            return;
        }
        //refill board if needed
        try{
            this.livingRoom.refillBoard();
        }catch(NoMoreCardsException e){
            this.gameState = GameStateType.LAST_ROUND;//TODO: Error -> it's not the last round yet
        }
        //se il game non è finito posso procedere ed inviare l'update a tutti
        super.notifyAllObservers(getAllNicks(), new SelectedColumnsMessage(gameState, "ID", new Pair<>(playingPlayer.getNickname(),
                playingPlayer.getScore()), getNextPlayer().getNickname(), new Pair<>(playingPlayer.getNickname(),this.playingPlayer.getPlayersShelf().
                getShelfCards()),this.livingRoom.getPieces(), this.livingRoom.calculateSelectable()), true, this.ID);
        this.playingPlayer = getNextPlayer();
    }
    public ArrayList<Player> getPlayers(){
        return new ArrayList<>(players);
    }
    public LivingRoom getLivingRoom(){ return this.livingRoom;}

    /**
     * Support method that locates and returns the next player in the game
     * @return returns the next player
     */
    private Player getNextPlayer(){
        int indx = players.indexOf(playingPlayer);
        return indx+1 == players.size() ? players.get(0) : players.get(indx+1);
    }



}
