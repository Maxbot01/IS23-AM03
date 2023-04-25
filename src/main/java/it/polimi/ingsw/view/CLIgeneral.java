package it.polimi.ingsw.view;

import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameLobby;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.LivingRoom;
import it.polimi.ingsw.model.messageModel.*;
import java.util.ArrayList;

import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import it.polimi.ingsw.model.modelSupport.Player;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import org.apache.commons.cli.*;
import java.util.*;

import static it.polimi.ingsw.model.modelSupport.PersonalGoal.personalGoals;

public class CLIgeneral extends View{
    protected GameStateType gameState;
    protected String matchID;
    private Integer commandsOrder = 0;
    private BoardCard[][] livingRoom;
    private CommonGoals commonGoals;
    private HashMap<Player, PersonalGoal> personalGoals;
    private ArrayList<Player> players;
    private Player chairedPlayer;


    public void executeLauncher(){ // method called after the client connects to the server

        Options options = new Options();

        Option set_username = Option.builder("set_username")
                .argName("nickname")
                .hasArg(true)
                .desc("sets the player's username")
                .required(true)//the option is required in order to have a process that works
                .build();
        Option show_games = Option.builder("show_games")
                .hasArg(false)
                .desc("shows all the games available")
                .required(false)
                .build();
        Option create_game = Option.builder("create_game")
                .argName("numOfPlayers")
                .hasArg(true)
                .desc("creates a new game with a maximum number of players")
                .required(false)
                .build();
        Option select_game = Option.builder("select_game")
                .argName("gameID")
                .hasArg(true)
                .desc("selects an available game")
                .required(false)
                .build();
        Option show_gameId = Option.builder("show_gameId")
                .hasArg(false)
                .desc("shows the gameId")
                .required(false)
                .build();
        Option start_match = Option.builder("start_match")
                .hasArg(false)
                .desc("starts the game, it's available only to the game creator")
                .required(false)
                .build();
        Option select_cards = Option.builder("select_cards")
                .argName("coordinates")
                .hasArg(true)
                .numberOfArgs(6)//it can have 4 arguments only (2 int for coord), with the thrid being 'null'
                .desc("selects available cards from the living room")
                .required(true)
                .build();
        Option choose_order = Option.builder("choose_order")
                .argName("order")
                .hasArg(true)
                .desc("chooses the order of the selected boardCards")
                .required(false)
                .build();
        Option select_column = Option.builder("select_column")
                .argName("column")
                .hasArg(true)
                .numberOfArgs(2)
                .desc("selects an available column of the player's shelf")
                .required(true)
                .build();
        Option help = Option.builder("help")
                .hasArg(false)
                .desc("shows the available commands")
                .required(false)
                .build();
        Option show_all = Option.builder("show_all")
                .hasArg(false)
                .desc("shows the list of all commands")
                .required(false)
                .build();

        options.addOption(set_username);
        options.addOption(show_games);
        options.addOption(create_game);
        options.addOption(select_game);
        options.addOption(show_gameId);
        options.addOption(start_match);
        options.addOption(select_cards);
        options.addOption(choose_order);
        options.addOption(select_column);
        options.addOption(help);
        options.addOption(show_all);

        // Scaning command line arguments
        ArrayList<String> arguments = new ArrayList<>();
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        Scanner inScan = new Scanner(s);
        while(inScan.hasNext()){
            arguments.add(inScan.next());
        }
        String[] args = new String[arguments.size()];
        for(int i = 0; i < arguments.size(); i++){
            args[i] = arguments.get(i);
        }

        ArrayList<Pair<Integer, Integer>> coord = new ArrayList<>();
        ArrayList<BoardCard> selected = new ArrayList<>();
        ArrayList<Option> optionList = new ArrayList<>();

        optionList.add(new Option("zero_option","starter of list"));
        optionList.addAll(options.getOptions());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try{
            cmd = parser.parse(options, args);

            if(cmd.hasOption(set_username) && commandsOrder == 0){
                String username = cmd.getOptionValue(set_username);
                System.out.println("CHECK: Chosen username is '"+username+"'");
                commandsOrder = optionList.indexOf(set_username);//1
                // qui ci va onSetUsername
                // invio dello username al controller e quindi creazione dell'utente (non del player)
            }else if(cmd.hasOption(show_games) && commandsOrder == 1){
                commandsOrder = optionList.indexOf(show_games);//2
                super.gameManagerController.onShowGamesAvailable();// da inserire nel controller
                // HashMap<GameLobby,Game> gamesList =
                // System.out.println("CHECK: Games list from server\n"+gamesList);
            }else if(cmd.hasOption(create_game) && (commandsOrder == 1 || commandsOrder == 2)){
                Integer numOfPlayers = Integer.parseInt(cmd.getOptionValue(create_game));
                System.out.println("CHECK: Number of players chosen is "+numOfPlayers);
                commandsOrder = optionList.indexOf(create_game);//3
                super.gameManagerController.onCreateGame(numOfPlayers);
                // printo la lobby (il controller lo fa)
            }else if(cmd.hasOption(select_game) && (commandsOrder == 1 || commandsOrder == 2)){
                Integer gameId = Integer.parseInt(cmd.getOptionValue(select_game));
                System.out.println("CHECK: Chosen gameId is "+gameId);
                commandsOrder = optionList.indexOf(select_game);//4
                super.gameManagerController.onSelectGame(gameId);
                // printo la lobby (il controller lo fa)
            }else if(cmd.hasOption(show_gameId) && (commandsOrder == 3 || commandsOrder == 4)){
                Integer gameIdAndLobby = Integer.parseInt(cmd.getOptionValue(show_gameId));
                System.out.println("CHECK: GameId of lobby is "+gameIdAndLobby);
                commandsOrder = optionList.indexOf(show_gameId);//5
                super.lobbyController.onShowGameId(); // da inserire nel controller
            }else if(cmd.hasOption(start_match) && commandsOrder >= 3 && commandsOrder <= 5){
                super.lobbyController.onStartMatch();
                commandsOrder = optionList.indexOf(start_match);//6
                // printo la livingRoom e le shelves (il controller lo fa)
            }else if(cmd.hasOption(select_cards) && commandsOrder == 6) { // I also need "it's your turn" message
                int resto = cmd.getArgs().length % 2;
                if (resto != 0) {
                    int numOfCoord = cmd.getArgs().length / 2;
                    for (int i = 0; i < numOfCoord; i++) {
                        int x = Integer.parseInt(cmd.getOptionValues(select_cards)[i]);
                        int y = Integer.parseInt(cmd.getOptionValues(select_cards)[i + 1]);
                        Pair<Integer, Integer> tmp = new Pair<>(x, y);
                        coord.add(tmp);
                        //inserisci le boardCard in selected (il colore)
                    }
                    System.out.println("CHECK: Chosen coordinates are " + coord);
                    commandsOrder = optionList.indexOf(select_cards);//7
                } else {
                    System.err.println("Errore: numero di coordinate inserite non valido");
                }
            }else if(cmd.hasOption(choose_order) && commandsOrder == 7){
                String order = cmd.getOptionValue(choose_order);
                System.out.println("CHECK: Chosen order is "+order);
                if(order.length() == coord.size()){
                    ArrayList<Pair<Integer,Integer>> copy = coord;
                    for(int i = 0; i < order.length(); i++){
                        coord.add(copy.get(Character.getNumericValue(order.charAt(i))));
                    }
                    commandsOrder = optionList.indexOf(choose_order);//8
                }else{
                    System.err.println("Errore: l'ordine inserito non corrisponde con il numero di boardCard scelte");
                }
                super.gameController.onSelectedCards(coord);
                // printo la livingRoom e le shelves (è la printAll) (lo fa il controller)
            }else if(cmd.hasOption(select_column) && commandsOrder == 8){
                String tmp = cmd.getOptionValue(select_column);
                if(tmp.length() == 1){
                    Integer column = Integer.parseInt(tmp);
                    // super.gameController.onSelectedColumn(,column);
                    commandsOrder = 0;//0
                }else{
                    System.err.println("Errore: indice di colonna non valido");
                }
            }else if(cmd.hasOption(help)){
                //Depending on the value of commandsOrder, I show the available commands
                HelpFormatter tmp = new HelpFormatter();
                Options available = new Options();
                available.addOption(help);
                available.addOption(show_all);

                if (commandsOrder == 1 || commandsOrder == 2) {
                    available.addOption(optionList.get(2));
                    available.addOption(optionList.get(3));
                    available.addOption(optionList.get(4));
                    tmp.printHelp("Available Commands", available);
                } else if (commandsOrder == 3 || commandsOrder == 4) {
                    available.addOption(optionList.get(5));
                    available.addOption(optionList.get(6));
                    tmp.printHelp("Available Commands", available);
                } else {
                    available.addOption(optionList.get(commandsOrder+1));
                    tmp.printHelp("Available Commands", available);
                }// after the command select_column you won't be able to do anything, it won't be your turn anymore, and the changes will be shown to everybody
            }else if(cmd.hasOption(show_all)){
                HelpFormatter tmp = new HelpFormatter();
                tmp.printHelp("All commands", options);
            }
        } catch (ParseException pe){
            System.err.println("Error parsing command-line arguments");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Commands", options);
        }
    }

    private void printAll(){
// it prints the livingRoom and all the shelves, later on also the players' points
    }

    @Override
    public void updatedLivingRoom(BoardCard[][] cards) {
        System.out.println("InitStateMessage:");
        System.out.println("GameStateType: " + super.gameState);
        System.out.println("MatchID: " + super.gameState);
        System.out.println("Pieces:");
        for (int i = 0; i < pieces.length; i++) {
            for (int j = 0; j < pieces[i].length; j++) {
                if(pieces[i][j].getColor() != colorType.EMPTY_SPOT){
                    System.out.print(pieces[i][j].getColor() + " ");
                }else{
                    System.out.print(" null ");
                }
            }
            System.out.println();
        }
        System.out.println("Selecectables:");
        for (int i = 0; i < selecectables.length; i++) {
            for (int j = 0; j < selecectables[i].length; j++) {
                if(selecectables[i][j]){
                    System.out.print("[] ");
                }else{
                    System.out.print(" * ");
                }
            }
            System.out.println();
        }
        System.out.println("CommonGoals 1: " + commonGoals.getFirstGoal().toString());
        System.out.println("CommonGoals 2: " + commonGoals.getSecondGoal().toString());
        System.out.println("PersonalGoals:");
        for (Map.Entry<Player, PersonalGoal> entry : personalGoals.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
        System.out.println("Players: " + players);
        System.out.println("ChairedPlayer: " + chairedPlayer);
        System.out.println("PlayersShelves:");
        for (Pair<Player, BoardCard[][]> pair : playersShelves) {
            System.out.println(pair.getFirst().getNickname() + ":");
            for (int i = 0; i < pair.getSecond().length; i++) {
                for (int j = 0; j < pair.getSecond()[i].length; j++) {
                    System.out.print(pair.getSecond()[i][j].getColor() + " ");
                }
                System.out.println();
            }
        }
    }






    public void printMessage() {

    }
}