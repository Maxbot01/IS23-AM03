package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.*;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;
import it.polimi.ingsw.view.CLIColors;
import it.polimi.ingsw.view.CLIgeneral;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        /*
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Max"));
        players.add(new Player("Asso"));
        players.add(new Player("Rick"));
        players.add(new Player("Chicco"));
        Game myGame = new Game(players, "1.1");
        ArrayList<Pair<Integer, Integer>> sel = new ArrayList<>();
        sel.add(new Pair<>(0, 3));
        sel.add(new Pair<>(0, 4));
        System.out.println("\n\n\nNEW PLAYER 1");
        try {
            myGame.selectedCards(sel);
        } catch (UnselectableCardException e) {
            // TODO Auto-generated catch block
            System.out.println("Not sel");
            e.printStackTrace();
        }
        ArrayList<BoardCard> crds = new ArrayList<>();
        crds.add(new BoardCard(colorType.BLUE, ornamentType.A));
        crds.add(new BoardCard(colorType.PURPLE, ornamentType.A));
        myGame.selectedColumn(crds, 1);

        //---- new round
        System.out.println("\n\n\nNEW PLAYER 2");
        sel = new ArrayList<>();
        sel.add(new Pair<>(1, 4));
        sel.add(new Pair<>(1, 5));

        try {
            myGame.selectedCards(sel);
        } catch (UnselectableCardException e) {
            // TODO Auto-generated catch block
            System.out.println("Not sel");
            e.printStackTrace();
        }
        crds = new ArrayList<>();
        crds.add(new BoardCard(colorType.LIGHT_BLUE, ornamentType.A));
        crds.add(new BoardCard(colorType.YELLOW, ornamentType.A));
        myGame.selectedColumn(crds, 1);

        //---- new round
        System.out.println("\n\n\nNEW PLAYER 3");
        sel = new ArrayList<>();
        sel.add(new Pair<>(4, 0));
        sel.add(new Pair<>(5, 0));

        try {
            myGame.selectedCards(sel);
        } catch (UnselectableCardException e) {
            // TODO Auto-generated catch block
            System.out.println("Not sel");
            e.printStackTrace();
        }
        crds = new ArrayList<>();
        crds.add(new BoardCard(colorType.LIGHT_BLUE, ornamentType.A));
        crds.add(new BoardCard(colorType.YELLOW, ornamentType.A));
        myGame.selectedColumn(crds, 1);

        //---- new round
        System.out.println("\n\n\nNEW PLAYER 4");
        sel = new ArrayList<>();
        sel.add(new Pair<>(2, 4));
        sel.add(new Pair<>(2, 5));
        sel.add(new Pair<>(2, 6));

        try {
            myGame.selectedCards(sel);
        } catch (UnselectableCardException e) {
            // TODO Auto-generated catch block
            System.out.println("Not sel");
            e.printStackTrace();
        }
        crds = new ArrayList<>();
        crds.add(new BoardCard(colorType.BLUE, ornamentType.A));
        crds.add(new BoardCard(colorType.YELLOW, ornamentType.A));
        myGame.selectedColumn(crds, 1);

        //---- new round
        System.out.println("\n\n\nNEW PLAYER 12");
        sel = new ArrayList<>();
        sel.add(new Pair<>(1, 3));
        sel.add(new Pair<>(2, 3));

        try {
            myGame.selectedCards(sel);
        } catch (UnselectableCardException e) {
            // TODO Auto-generated catch block
            System.out.println("Not sel");
            e.printStackTrace();
        }
        crds = new ArrayList<>();
        crds.add(new BoardCard(colorType.BLUE, ornamentType.A));
        crds.add(new BoardCard(colorType.YELLOW, ornamentType.A));
        myGame.selectedColumn(crds, 1);

        //---- new round
        System.out.println("\n\n\nNEW PLAYER 22");
        sel = new ArrayList<>();
        sel.add(new Pair<>(3, 3));
        sel.add(new Pair<>(3, 4));

        try {
            myGame.selectedCards(sel);
        } catch (UnselectableCardException e) {
            // TODO Auto-generated catch block
            System.out.println("Not sel");
            e.printStackTrace();
        }
        crds = new ArrayList<>();
        crds.add(new BoardCard(colorType.YELLOW, ornamentType.A));
        crds.add(new BoardCard(colorType.LIGHT_BLUE, ornamentType.A));
        myGame.selectedColumn(crds, 3);
        */
        // TESTING CLI

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Max"));
        players.add(new Player("Asso"));
        players.add(new Player("Rick"));
        players.add(new Player("Chicco"));
        Game myGame = new Game(players, "1.1");

        ArrayList<BoardCard[][]> shelves = new ArrayList<>();
        Shelf tmp = new Shelf();
        for(int i = 0; i < 4; i++){
            shelves.add(tmp.getShelfCards());
        }
        CommonGoals commonGoals = new CommonGoals();

        LivingRoom livingRoom = myGame.getLivingRoom();
        CLIgeneral cligeneral = new CLIgeneral();
        Boolean[][] selectables = livingRoom.calculateSelectable();
        ArrayList<Pair<String,BoardCard[][]>> playersShelves = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            playersShelves.add(new Pair<>(players.get(i).getNickname(),shelves.get(i)));
        }
        cligeneral.initializeGame(players,commonGoals,players.get(2).getPersonalGoal(),players.get(2));
        cligeneral.updatedMatchDetails(livingRoom,selectables,playersShelves,"1.1", GameStateType.IN_PROGRESS);
        cligeneral.printLivingRoomAndShelves();

        /* inizio assegnamento testing con printf di calculateAdiacentPoint nella shelf */
        /*
        Shelf provaAdiacent = new Shelf();
        provaAdiacent.initializeShelfForTesting();
        System.out.println("punti ottenuti dalla shelf = " + provaAdiacent.calculateAdiacentPoints() + "\n");
        */
    }
}