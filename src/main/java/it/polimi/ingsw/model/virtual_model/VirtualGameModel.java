package it.polimi.ingsw.model.virtual_model;
import com.google.gson.Gson;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;

import java.util.ArrayList;

public class VirtualGameModel {

    public void selectedCards(ArrayList<Pair<Integer, Integer>> selectedCards) {
        Gson gson = new Gson();
        String json = gson.toJson(selectedCards);
        // TODO: Invia la stringa JSON agli altri giocatori
    }

    public void selectedColumn(ArrayList<BoardCard> selCards, Integer colIndex) {
        Gson gson = new Gson();
        String jsonSelCards = gson.toJson(selCards);
        String jsonColIndex = gson.toJson(colIndex);
        // TODO: Invia le due stringhe JSON agli altri giocatori
    }

    public void selectedCards() {
        // ?????
    }

    public void acceptFinishedGame(String fromPlayer) {
        // ??????
        // e.g. aggiorna lo stato del gioco per riflettere che la partita è finita e registra chi l'ha accettata
    }
}
