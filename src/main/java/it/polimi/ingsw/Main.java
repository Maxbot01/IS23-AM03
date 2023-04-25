package it.polimi.ingsw;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.Shelf;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;

public class Main {
    public static void main(String[] args) {
        /*
        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player("Max"));
        players.add(new Player("Asso"));
        players.add(new Player("Rick"));
        players.add(new Player("Chicco"));
        Game myGame = new Game(players);
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


        /* inizio assegnamento testing con printf di calculateAdiacentPoint nella shelf */
        /*
        Shelf provaAdiacent = new Shelf();
        provaAdiacent.initializeShelfForTesting();
        System.out.println("punti ottenuti dalla shelf = " + provaAdiacent.calculateAdiacentPoints() + "\n");
        */
        /*
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        System.out.println(s);
        if(s.length() == 11 || s.length() == 3 || s.length() == 7){
            for(int i = 0; i < s.length(); i+=4) {
                System.out.println("First coordinate: "+s.charAt(i)+"\nSecond coordinate: "+s.charAt(i+2));
            }
        }else{
            System.out.println("Insert coordinates through the right pattern\n"+"Example: '5 5 5 6 5 7' where 5 5 is the first couple");
        }
        */
        ArrayList<String> arguments = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        Scanner inScan = new Scanner(s);
        while(inScan.hasNext()){
            arguments.add(inScan.next());
        }
        String[] argu = new String[arguments.size()];
        for(int i = 0; i < arguments.size(); i++){
            argu[i] = arguments.get(i);
        }
        System.out.println(Arrays.toString(argu));

    }
}