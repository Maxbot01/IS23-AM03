package it.polimi.ingsw.CLI;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.GameLobby;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.LivingRoom;
import java.util.ArrayList;

import org.apache.commons.cli.*;
import java.util.*;
public class CLIgeneral {
    private void startingExample(){

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

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try{
            cmd = parser.parse(options, args);

            if(cmd.hasOption(set_username)){
                String username = cmd.getOptionValue(set_username);
                System.out.println("CHECK: Chosen username is '"+username+"'");
                // qui ci va onSetUsername
                // invio dello username al controller e quindi creazione del player
            }else if(cmd.hasOption(show_games)){
                // gamesList has the entire hash map sent by the controller
                // HashMap<GameLobby,Game> gamesList =
                // System.out.println("CHECK: Games list from server\n"+gamesList);
            }else if(cmd.hasOption(create_game)){
                Integer numOfPlayers = Integer.parseInt(cmd.getOptionValue(create_game));
                System.out.println("CHECK: Number of players chosen: "+numOfPlayers);
                // onCreateGame con parametro numOfPlayers
                // printo la lobby
            }else if(cmd.hasOption(select_game)){
                Integer gameId = Integer.parseInt(cmd.getOptionValue(select_game));
                System.out.println("CHECK: Chosen gameId is "+gameId);
                // onSelectGame con parametro gameId
                // printo la lobby
            }else if(cmd.hasOption(show_gameId)){
                Integer gameIdAndLobby = Integer.parseInt(cmd.getOptionValue(show_gameId));
                System.out.println("CHECK: GameId of lobby is "+gameIdAndLobby);
                // qui metto onShowGameId
            }else if(cmd.hasOption(start_match)){
                // onStartMatch con messaggio di errore in caso di numero di giocatori insufficiente
                // printo la livingRoom e le shelves
            }else if(cmd.hasOption(select_cards)) {
                int resto = cmd.getArgs().length % 2;
                if (resto != 0) {
                    int numOfCoord = cmd.getArgs().length / 2;
                    for (int i = 0; i < numOfCoord; i++) {
                        int x = Integer.parseInt(cmd.getOptionValues(select_cards)[i]);
                        int y = Integer.parseInt(cmd.getOptionValues(select_cards)[i + 1]);
                        Pair<Integer, Integer> tmp = new Pair<>(x, y);
                        coord.add(tmp);
                    }
                    System.out.println("CHECK: Chosen coordinates are " + coord);
                } else {
                    System.err.println("Errore: numero di coordinate inserite non valido");
                }
            }else if(cmd.hasOption(choose_order)){
                String order = cmd.getOptionValue(choose_order);
                System.out.println("CHECK: Chosen order is "+order);
                if(order.length() == coord.size()){
                    ArrayList<Pair<Integer,Integer>> copy = coord;
                    for(int i = 0; i < order.length(); i++){
                        coord.add(copy.get(Character.getNumericValue(order.charAt(i))));
                    }
                }else{
                    System.err.println("Errore: l'ordine inserito non corrisponde con il numero di boardCard scelte");
                }
                // onSelectedCards con parametro coord
                // printo la livingRoom e le shelves (è la printAll)
            }else if(cmd.hasOption(select_column)){
                String tmp = cmd.getOptionValue(select_column);
                if(tmp.length() == 1){
                    Integer column = Integer.parseInt(tmp);
                    // onSelectedColumn con parametri column
                }else{
                    System.err.println("Errore: indice di colonna non valido");
                }
            }else if(cmd.hasOption(help)){

            }else if(cmd.hasOption(show_all)){

            }
        } catch (ParseException pe){
            System.err.println("Error parsing command-line arguments");
            // formatter
        }

        if(cmd.hasOption("t")){
            System.out.println("ok");
        }else{
            System.err.println("cojone");
        }

    }

    private void printAll(){
// it prints the livingRoom and all the shelves, later on also the players' points
    }

}
