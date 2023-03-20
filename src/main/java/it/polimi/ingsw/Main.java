package it.polimi.ingsw;

import java.util.ArrayList;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;

public class Main {
    public static void main(String[] args) {
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Max"));
        players.add(new Player("Asso"));
        players.add(new Player("Rick"));
        players.add(new Player("Chicco"));
        Game myGame = new Game(players);
        ArrayList<Pair<Integer, Integer>> sel = new ArrayList<>();
        sel.add(new Pair<>(0, 3));
        sel.add(new Pair<>(0, 4));
        try {
            myGame.selectedCards(sel);
        } catch (UnselectableCardException e) {
            // TODO Auto-generated catch block
            System.out.println("Not sel");
            e.printStackTrace();
        }
        ArrayList<BoardCard> crds = new ArrayList<>();
        crds.add(new BoardCard(colorType.BLUE));
        crds.add(new BoardCard(colorType.PURPLE));
        myGame.selectedColumn(crds, 2);
        

    }
} // ciao