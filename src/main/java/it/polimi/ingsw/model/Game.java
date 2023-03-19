package it.polimi.ingsw.model;


import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.messageModel.errorMessages.SelectedColumnsMessageError;
import it.polimi.ingsw.model.messageModel.matchStateMessages.InitStateMessage;
import it.polimi.ingsw.model.messageModel.matchStateMessages.SelectedCardsMessage;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.CommonGoals;
import it.polimi.ingsw.model.modelSupport.LivingRoom;
import it.polimi.ingsw.model.modelSupport.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.Collectors;

//this class handles the state of a match
public class Game extends GameObservable{

    private  ArrayList<Player> players;
    private LivingRoom livingRoom;
    private CommonGoals commonGoals;
    private Player playingPlayer;

    private GameStateType gameState;


    public Game(ArrayList<Player> fromPlayers){
        this.players = new ArrayList<Player>(fromPlayers);
        //set up players info
        this.setPlayersUp();

        //send message of game created
        livingRoom = new LivingRoom(players.size());
        commonGoals = new CommonGoals();
        //set randomly the player that has the chair, he's starting the match
        Random ranGen = new Random();
        int ranPlayerIndx = ranGen.nextInt(players.size());
        players.get(ranPlayerIndx).setHasChair();
        playingPlayer = players.get(ranPlayerIndx);
        gameState = GameStateType.IN_PROGRESS;
        super.notifyAllObservers(new InitStateMessage(GameStateType.IN_PROGRESS, "ID",  livingRoom.getPieces(), livingRoom.calculateSelectables(), this.commonGoals, new HashMap<>(players, players.stream().map(x -> x.personalGoal).Collect(Collectors.toList())), players, players.stream().reduce(x->x.hasChair)));
    }

    private void setPlayersUp(){
        for(int i = 0; i < players.size(); i++){

        }
    }


    public void selectedCards(ArrayList<Pair> selected){
        /*
        l'utente sa che carte poteva scegliere, le ha scelte. Il metodo aggiorna la board (i pezzi) chiamando updateBoard di Livingroom.
        Invia il messaggio al controller
     */
        ArrayList<BoardCard> updatedCards = livingRoom.updateBoard(selected);
        super.notifyAllObservers(new SelectedCardsMessage(GameStateType.IN_PROGRESS, "ID", selected, livingRoom.calculateSelectables(), livingRoom.getPieces(), playingPlayer));
    }

    /*
    dalla view l'utente ha scelto l'ordine (operazione gestita dal controller) e ha selezionato la colonna.
    Il controller quindi chiama selectedColumn. Se la colonna non è selezionabile invia messaggio di errore.
    Altrimenti facciamo l'update della shelf, calcoliamo il punteggio dell'utente, aggiorniamo il giocatore,
    controlliamo se la board va refillata (livingRoom::refillBoard) mandiamo il messaggio.
    Il current player nuovo si riconosce nel messaggio e il controller sa che può scegliere le carte.
     */
    public void selectedColumn(ArrayList<BoardCard> selCards, Integer colIndex){
        try {
            shelf.insertInColumn();
        }
        catch(ColumnNotSelectable e) {
            //can't insert the items in the columns
            //send error message
            super.notifyObserver(playingPlayer, new SelectedColumnsMessageError());
            return;
        }
        //the shelf is updated
        //calculate the points of the player

    }



    public ArrayList<Player> getPlayers(){
        return new ArrayList<>(players);
    }


}
