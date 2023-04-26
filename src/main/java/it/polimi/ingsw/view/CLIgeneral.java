package it.polimi.ingsw.view;

import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.GameLobby;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.*;

import java.util.ArrayList;

import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;
import org.apache.commons.cli.*;

import javax.swing.plaf.synth.ColorType;
import java.util.*;

import static it.polimi.ingsw.model.modelSupport.PersonalGoal.*;

public class CLIgeneral extends View{
    private GameStateType gameState;
    private String gameID;
    private LivingRoom livingRoom;
    private Boolean[][] selectables;
    private CommonGoals commonGoals;
    private PersonalGoal personalGoal;
    private ArrayList<Player> players;
    private Player userPlayer;
    private boolean host;

// COMMANDS INITIALIZATION
    /*
    Option set_username = Option.builder("set_username")
            .argName("nickname")
            .hasArg(true)
            .desc("sets the player's username")
            .required(true)//the option is required in order to have a process that works
            .build();
    */
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
    Option help = Option.builder("help")
            .hasArg(false)
            .desc("shows the available commands")
            .required(false)
            .build();
    Option show_commonGoals = Option.builder("show_commonGoals")
            .hasArg(false)
            .desc("shows the game's common goals")
            .required(false)
            .build();
    Option show_personalGoal = Option.builder("show_personalGoal")
            .hasArg(false)
            .desc("shows the player's personal goal")
            .required(false)
            .build();
/*
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

    Option show_all = Option.builder("show_all")
            .hasArg(false)
            .desc("shows the list of all commands")
            .required(false)
            .build();
    */
    @Override
    public void initializeGame(ArrayList<Player> players, CommonGoals commonGoals, PersonalGoal personalGoal, Player userPlayer){
        this.players = players;
        this.commonGoals = commonGoals;
        this.personalGoal = personalGoal;
        this.userPlayer = userPlayer;
    }
    @Override
    public void waitingCommands(){
        Options options = new Options();
        options.addOption(show_gameId);
        //options.addOption(chat);
        options.addOption(show_commonGoals);
        options.addOption(show_personalGoal);
        options.addOption(help);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try{
            cmd = parser.parse(options, scanf());
            while(!options.hasOption(cmd.getOptions()[0].getOpt())){ // Until it receives a possible command, it continues to scan
                cmd = parser.parse(options, scanf());
            }
            if(cmd.hasOption(show_gameId)){
                System.out.println("Your gameId is: "+gameID);
            } else if (cmd.hasOption(show_commonGoals)) {
                System.out.println("First common goal: "+commonGoals.getFirstGoal()+"\nSecond commmon"+
                        "goal: "+commonGoals.getSecondGoal());
            } else if (cmd.hasOption(show_personalGoal)) {
                Shelf personalGoalShelf = new Shelf();
                for(int i = 0; i < personalGoal.getSelectedGoal().size(); i++)
                {
                    Pair<colorType,Pair<Integer,Integer>> tmp = personalGoal.getSelectedGoal().get(i);
                    BoardCard card = new BoardCard(tmp.getFirst(), ornamentType.A);
                    personalGoalShelf.getShelfCards()[tmp.getSecond().getFirst()][tmp.getSecond().getSecond()] = card;
                }
                System.out.println("Your personal goal is:");
                printShelf(personalGoalShelf);
            } else if (cmd.hasOption(help)) {
                HelpFormatter formatter = new HelpFormatter();
                formatter.printHelp("Available Commands", options);
            }
        } catch (ParseException pe){
            System.err.println("Error parsing command-line arguments");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Section Commands", options);
        }
    }
    @Override
    public void updatedMatchDetails(LivingRoom livingRoom, Boolean[][] selectables, ArrayList<Pair<String,BoardCard[][]>> playersShelves,
                                    String gameID, GameStateType gameState) {
        this.livingRoom = livingRoom;
        this.selectables = selectables;
        this.gameState = gameState;
        this. gameID = gameID;
        for(int i = 0; i < players.size(); i++){ // With the first two for cycles I avoid possible differences in the players' order between the old ArrayList and the updated
            for(int j = 0; j < players.size(); j++){
                if(players.get(i).getNickname().equals(playersShelves.get(j).getFirst())){
                    BoardCard[][] updatedShelf = playersShelves.get(j).getSecond();
                    for(int z = 0; z < updatedShelf.length; z++){
                        for(int w = 0; w < updatedShelf[0].length; w++){
                            this.players.get(i).getPlayersShelf().getShelfCards()[z][w] = updatedShelf[z][w];
                        }
                    }
                }
            }
        }
    }
    @Override
    public String requestUsername(){
        System.out.println("Insert Username:");
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        return s;
    }
    @Override
    public String requestPassword() {
        System.out.println("Insert Password:");
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        return s;
    }
    @Override
    public void launchGameManager(List<GameLobby> availableGames){
        Options options = new Options();
        options.addOption(show_games);
        options.addOption(create_game);
        options.addOption(select_game);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        try{
            cmd = parser.parse(options, scanf());
            while(!options.hasOption(cmd.getOptions()[0].getOpt())){ // Until it receives a possible command, it continues to scan
                cmd = parser.parse(options, scanf());
            }
            if(cmd.hasOption(show_games)){ // AvailableGames is only used in this method, therefore it is not saved as a parameter
                for(int i = 0; i < availableGames.size(); i++){
                    System.out.println("GameId: " + availableGames.get(i).getID());
                    for(int j = 0; j < availableGames.get(i).getPlayers().size(); j++)
                    {
                        System.out.println("\t\t"+availableGames.get(i).getPlayers().get(j));
                    }
                }
//TODO: I need to repeat the try statement after this command, because I don't exit the GameManager section
            } else if (cmd.hasOption(create_game)) {
                Integer numOfPlayers = Integer.parseInt(cmd.getOptionValue(create_game));
                host = true;
                super.gameManagerController.onCreateGame(numOfPlayers);
            } else if (cmd.hasOption(select_game)) {
                String gameSelectedId = cmd.getOptionValue(select_game);
                host = false;
                super.gameManagerController.onSelectGame(gameSelectedId);
            }

        } catch (ParseException pe){
            System.err.println("Error parsing command-line arguments");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Section Commands", options);
        }
    }
    @Override
    public void launchGameLobby(String gameID){
        this.gameID = gameID;
        Options options = new Options();
        options.addOption(show_gameId);
        //options.addOption(chat);
        if(host){
            options.addOption(start_match);
        }


        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try{
            cmd = parser.parse(options, scanf());
            while(!options.hasOption(cmd.getOptions()[0].getOpt())){ // Until it receives a possible command, it continues to scan
                cmd = parser.parse(options, scanf());
            }
            if(!host){
                if (cmd.hasOption(start_match)) {
                    super.lobbyController.onStartMatch();
                } else if (cmd.hasOption(show_gameId)) {
                    System.out.println("Your gameId is: " + gameID);
// TODO: goes back to the start of the try
                }
            }else{
                if(cmd.hasOption(show_gameId)){
                    System.out.println("Your gameId is: " + gameID);
// TODO: goes back to the start of the try
                }
            }
        } catch (ParseException pe){
            System.err.println("Error parsing command-line arguments");
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("Section Commands", options);
        }
    }
    @Override
    public void startGameSequence() throws UnselectableCardException {
        ArrayList<Pair<Integer,Integer>> coord = new ArrayList<>();
        ArrayList<BoardCard> selected = new ArrayList<>();

        System.out.println("Select Cards in couples of coordinates.\nExample: 5 4 5 5 5 6\twhere 5-4 is the first couple and so on");
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        if(s.length() == 11 || s.length() == 3 || s.length() == 7){
            for(int i = 0; i < s.length(); i+=4){
                Pair<Integer,Integer> tmp = new Pair<>(Character.getNumericValue(s.charAt(i)),Character.getNumericValue(s.charAt(i+2)));
                coord.add(tmp);
            }
        }else{
            System.out.println("Insert coordinates through the right pattern\n"+"Example: '5 5 5 6 5 7' where 5 5 is the first couple");
        }
        System.out.println("Choose order for the selected cards.\nExample: '132' -> first card, third card, second card.");
        Scanner in2 = new Scanner(System.in);
        s = in2.next();
        if(s.length() == coord.size()){
            ArrayList<Pair<Integer,Integer>> copy = new ArrayList<>();
            for(int i = 0; i < s.length(); i++){
                copy.add(coord.get(Character.getNumericValue(s.charAt(i))-1));
            }
            coord = copy;
            for(int i = 0; i < coord.size(); i++){
                selected.add(livingRoom.getBoardCardAt(coord.get(i)));
            }
            super.gameController.onSelectedCards(coord);
        }else{
            System.out.println("The chosen order must be of the same size of the cards selected.");
        }
        System.out.println("Insert the shelf's column for the selected cards. From 0 to 4.");
        Scanner in3 = new Scanner(System.in);
        int column = Integer.parseInt(in3.next());
        if(column >= 0 && column <= 4){
            super.gameController.onSelectedColumn(selected,column);
        }else{
            System.out.println("Select a column within range, from 0 to 4.");
        }
    }
    @Override
    public void printLivingRoomAndShelves(){
        BoardCard[][] pieces = livingRoom.getPieces();
        System.out.println("Game state: "+gameState.toString()+"\n");
        for(int i = 0; i < pieces.length; i++){
            System.out.print(i+" ");
            for(int j = 0; j < pieces[0].length; j++){
                BoardCard tmp = pieces[i][j];
                Pair<String,Character> color;
                color = getColor(tmp);
                if(selectables[i][j].equals(true)) {
                    System.out.print(CLIColors.BLACK_BACKGROUND+color.getFirst()+CLIColors.WHITE_UNDERLINE+color.getSecond()+CLIColors.RESET);
                } else {
                    System.out.print(CLIColors.BLACK_BACKGROUND+color.getFirst()+color.getSecond()+CLIColors.RESET);
                }
                if(j != pieces[0].length-1){
                    System.out.print(CLIColors.BLACK_BACKGROUND+" "+CLIColors.RESET);
                }
            }
            System.out.print("\n");
        }
        for(int j = 0; j < pieces[0].length; j++){
            System.out.print("  "+j+" ");
        }
        System.out.print("\n\n");

/* Printing of shelves, starting from the playingPlayer's shelf */
        System.out.println("Your shelf:");
        printShelf(userPlayer.getPlayersShelf());
/* Printing other players' shelves */
        for(int i = 0; i < players.size(); i++){
            if(!players.get(i).equals(userPlayer)){
                System.out.println(players.get(i).getNickname());
                printShelf(players.get(i).getPlayersShelf());
            }
        }
    }// TODO Testing
    private Pair<String,Character> getColor(BoardCard tmp){
        String colorHighlight;
        char colorValue;
        if(tmp.getColor().equals(colorType.TOMBSTONE) || tmp.getColor().equals(colorType.EMPTY_SPOT)){
            colorHighlight = CLIColors.BLACK;
            colorValue = 'X';
        } else if (tmp.getColor().equals(colorType.GREEN)) {
            colorHighlight = CLIColors.GREEN;
            colorValue = 'G';
        } else if (tmp.getColor().equals(colorType.WHITE)) {
            colorHighlight = CLIColors.WHITE;
            colorValue = 'W';
        } else if (tmp.getColor().equals(colorType.YELLOW)) {
            colorHighlight = CLIColors.YELLOW;
            colorValue = 'Y';
        } else if (tmp.getColor().equals(colorType.LIGHT_BLUE)) {
            colorHighlight = CLIColors.CYAN;
            colorValue = 'L';
        } else if (tmp.getColor().equals(colorType.BLUE)) {
            colorHighlight = CLIColors.BLUE;
            colorValue = 'B';
        } else { // purple case
            colorHighlight = CLIColors.PURPLE;
            colorValue = 'P';
        }
        Pair<String,Character> val = new Pair<>(colorHighlight,colorValue);
        return val;
    }
    private String[] scanf(){
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
        return args;
    }
    private void printShelf(Shelf tmp){
        for(int i = 0; i < tmp.getShelfCards().length; i++){
            for(int j = 0; j < tmp.getShelfCards()[0].length; j++){
                BoardCard card = tmp.getCardAtPosition(i,j);
                Pair<String,Character> color;
                color = getColor(card);
                System.out.print(CLIColors.BLACK_BACKGROUND+color.getFirst()+color.getSecond()+ CLIColors.RESET);
                if(j != tmp.getShelfCards()[0].length-1){
                    System.out.print(CLIColors.BLACK_BACKGROUND+" "+CLIColors.RESET);
                }
            }
            System.out.print("\n");
        }
    }

    /* Old Commands version */
    /*
    public void executeLauncher(){ // method called after the client connects to the server



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
                String gameId = cmd.getOptionValue(select_game);
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
    */
}