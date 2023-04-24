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
        Option show_lobby_link = Option.builder("show_lobby_link")
                .hasArg(false)
                .desc("shows the lobby link")
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
                .desc("selects available cards from the living room")
                .required(true)
                .build();
        Option select_column = Option.builder("select_column")
                .argName("column and order")
                .hasArg(true)
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
        options.addOption(show_lobby_link);
        options.addOption(start_match);
        options.addOption(select_cards);
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


        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try{
            cmd = parser.parse(options, args);
            if(cmd.hasOption(set_username)){
                String username = cmd.getOptionValue(set_username);
                System.out.println("CHECK: Chosen username is '"+username+"'");
                // aggiungo onSetUsername
                //invio dello username al controller e quindi creazione del player
            }else if(cmd.hasOption(show_games)){
                // gamesList has the entire hash map sent by the controller
                // HashMap<GameLobby,Game> gamesList =
                System.out.println("CHECK: ");
            }else if(cmd.hasOption(create_game)){

            }else if(cmd.hasOption(select_game)){

            }else if(cmd.hasOption(show_lobby_link)){

            }else if(cmd.hasOption(start_match)){

            }else if(cmd.hasOption(select_cards)){

            }else if(cmd.hasOption(select_column)){

            }else if(cmd.hasOption(help)){

            }else if(cmd.hasOption(show_all)){

            }
        } catch (ParseException pe){
            System.err.println("Error parsi command-line arguments");
            // formatter
        }

        if(cmd.hasOption("t")){
            System.out.println("ok");
        }else{
            System.err.println("cojone");
        }

    }

    private void printAll(){
// it prints the livingRoom and all the shelves
    }

}
