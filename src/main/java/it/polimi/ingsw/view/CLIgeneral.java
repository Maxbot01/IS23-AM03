package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ClientManager;
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
import java.util.*;
public class CLIgeneral extends View{
    private GameStateType gameState;
    private String gameID;
    private BoardCard[][] livingRoom;
    private Boolean[][] selectables;
    private CommonGoals commonGoals;
    private PersonalGoal personalGoal;
    private ArrayList<Player> players;
    private Player userPlayer; // Remember that userPlayer does not have a personal goal or points
    private boolean host;
    private ArrayList<BoardCard> selectedCards;

    // COMMANDS INITIALIZATION
    private final Option show_games = Option.builder("show_games")
            .hasArg(false)
            .desc("shows all the games available")
            .required(false)
            .build();
    private final Option create_game = Option.builder("create_game")
            .argName("numOfPlayers")
            .hasArg(true)
            .desc("creates a new game with a maximum number of players")
            .required(false)
            .build();
    private final Option select_game = Option.builder("select_game")
            .argName("gameID")
            .hasArg(true)
            .desc("selects an available game, by inserting its ID")
            .required(false)
            .build();
    private final Option show_gameId = Option.builder("show_gameId")
            .hasArg(false)
            .desc("shows the gameId")
            .required(false)
            .build();
    private final Option start_match = Option.builder("start_match")
            .hasArg(false)
            .desc("starts the game, it's available only to the game creator")
            .required(false)
            .build();
    private final Option help = Option.builder("help")
            .hasArg(false)
            .desc("shows the available commands")
            .required(false)
            .build();
    private final Option show_commonGoals = Option.builder("show_commonGoals")
            .hasArg(false)
            .desc("shows the game's common goals")
            .required(false)
            .build();
    private final Option show_personalGoal = Option.builder("show_personalGoal")
            .hasArg(false)
            .desc("shows the player's personal goal")
            .required(false)
            .build();
    private final Option chat = Option.builder("chat")
            .hasArg(false)
            .desc("activates chat input")
            .required(false)
            .build();
    private final Option exit = Option.builder("exit")
            .hasArg(false)
            .desc("terminates the connection with the server")
            .required(false)
            .build();
    private final Option play_again = Option.builder("play_again")
            .hasArg(false)
            .desc("takes you back to the game selection and creation section")
            .required(false)
            .build();
    private final Option stop = Option.builder("stop")//TODO: Delete after debug
            .hasArg(false)
            .desc("stop the waiting Commands, used for debugging")
            .required(false)
            .build();
    @Override
    public void initializeGame(List<String> playersNick, CommonGoals commonGoals, HashMap<String,PersonalGoal> personalGoals,
                               BoardCard[][] livingRoom, Boolean[][] selectables, ArrayList<Pair<String,BoardCard[][]>>
                               playersShelves, HashMap<String, Integer> playersPoints, GameStateType gameState){
        ArrayList<Player> tmp = new ArrayList<>();
        for(String s: playersNick){
            Player p = new Player(s);
            tmp.add(p);
        }
        this.players = tmp;
        this.commonGoals = commonGoals;
        this.personalGoal = personalGoals.get(userPlayer.getNickname());
        this.livingRoom = livingRoom;
        this.selectables = selectables;
        this.gameState = gameState;
        for(int j = 0; j < players.size(); j++) {
            //Players' shelves update
            for (int i = 0; i < playersShelves.size(); i++) {
                if (playersShelves.get(i).getFirst().equals(players.get(j).getNickname())) {
                    BoardCard[][] updatedShelf = playersShelves.get(j).getSecond();
                    for (int z = 0; z < updatedShelf.length; z++) {
                        for (int w = 0; w < updatedShelf[0].length; w++) {
                            this.players.get(i).getPlayersShelf().getShelfCards()[z][w] = updatedShelf[z][w];
                        }
                    }
                }
            }
            //Players' points update
            this.players.get(j).updateScore(playersPoints.get(players.get(j).getNickname())-players.get(j).getScore());
        }
    }
    @Override
    public void updateMatchAfterSelectedCards(BoardCard[][] livingRoom, Boolean[][] selectables, GameStateType gameState){
        this.livingRoom = livingRoom;
        this.selectables = selectables;
        this.gameState = gameState;
    }
    @Override
    public void updateMatchAfterSelectedColumn(BoardCard[][] livingRoom, Boolean[][] selectables, GameStateType gameState, Pair<String,
            Integer> updatedPlayerPoints, Pair<String, BoardCard[][]> updatedPlayerShelf){
        this.livingRoom = livingRoom;
        this.selectables = selectables;
        this.gameState = gameState;
        for(int i = 0; i < players.size(); i++){
            if(players.get(i).getNickname().equals(updatedPlayerPoints.getFirst())){
                this.players.get(i).updateScore(updatedPlayerPoints.getSecond()-players.get(i).getScore());
                BoardCard[][] updatedShelf = updatedPlayerShelf.getSecond();
                for(int j = 0; j < updatedShelf.length; j++){
                    for(int z = 0; z < updatedShelf[0].length; z++){
                        this.players.get(i).getPlayersShelf().getShelfCards()[j][z] = updatedShelf[j][z];
                    }
                }
            }
        }

    }
    @Override
    public void waitingCommands(){
        Options options = new Options();
        options.addOption(show_gameId);
        options.addOption(chat);
        options.addOption(show_commonGoals);
        options.addOption(show_personalGoal);
        options.addOption(help);
        options.addOption(stop);//TODO: Delete after debug
        // Printing section commands
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Section Commands", options);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try{
            //while(qualcosa che mi arriva dal controller che continua a mandarlo e che successivamente lo rompe)
            cmd = parser.parse(options, scanf());
            while(!cmd.hasOption(stop)) {
                if (cmd.hasOption(show_gameId)) {
                    System.out.println("Your gameId is: " + gameID);
                } else if (cmd.hasOption(show_commonGoals)) {
                    System.out.println("First common goal: " + commonGoals.getFirstGoal().toString() + "\nSecond commmon" +
                            "goal: " + commonGoals.getSecondGoal().toString());
                } else if (cmd.hasOption(show_personalGoal)) {
                    Shelf personalGoalShelf = new Shelf();
                    for (int i = 0; i < this.personalGoal.getSelectedGoal().size(); i++) {
                        Pair<colorType, Pair<Integer, Integer>> tmp = this.personalGoal.getSelectedGoal().get(i);
                        BoardCard card = new BoardCard(tmp.getFirst(), ornamentType.A);
                        personalGoalShelf.getShelfCards()[tmp.getSecond().getFirst()][tmp.getSecond().getSecond()] = card;
                    }
                    System.out.println("Your personal goal is:");
                    printShelf(personalGoalShelf);
                } else if (cmd.hasOption(help)) {
                    formatter.printHelp("Available Commands", options);
                } else if (cmd.hasOption(chat)) { // Example of chat implementation
                    Scanner scan = new Scanner(System.in);
                    String msg = scan.nextLine();
                    super.gameController.onGetChatMessage(msg);
                    // It also needs to show the past messages
                } else {
                    System.out.println("waitingCommands section");
                    System.out.println("Unavailable command, remember to type '-' and the desired command");
                }
                cmd = parser.parse(options, scanf());
            }
            if(cmd.hasOption(stop)){
                System.out.println("You have left waitingCommands");
            }
        } catch (ParseException pe){
            System.out.println("waitingCommands section");
            System.out.println("Error parsing command-line arguments");
            formatter.printHelp("Parsing Error Section Commands", options);
        }
    }
    @Override
    public void requestCredentials(){
        System.out.println("Insert Username and a Password, that you will need in case of disconnection.\n" +
                "If you are reconnecting use your previously inserted password:");
        Scanner in = new Scanner(System.in);
        String username = in.next();
        String password = in.next();
        this.userPlayer = new Player(username);
        ClientManager.gameManagerController.onSetCredentials(username, password);
    }
    @Override
    public void showPlayingPlayer(String playingPlayer){
        System.out.println(playingPlayer+" is playing...");
    }
    @Override
    public void launchGameManager(HashMap<String, List<String>> availableGames){
        Options options = new Options();
        options.addOption(show_games);
        options.addOption(create_game);
        options.addOption(select_game);
        options.addOption(help);
        // Printing section commands
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Section Commands", options);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        HashMap<Integer,String> IDlist = new HashMap<>();
        int i = 1;
        try{
            cmd = parser.parse(options, scanf());
            while(!cmd.hasOption(create_game) && !cmd.hasOption(select_game)){ // Until it receives a possible command, it continues to scan
                if(cmd.hasOption(show_games)) { // AvailableGames is only used in this method, therefore it is not saved as a parameter
                    if(availableGames.size() == 0){
                        System.out.println("No games available");
                    }else {
                        for(String s: availableGames.keySet()){
                            System.out.println("GameId "+i+": "+s);
                            System.out.print("Players: ");
                            for(String player: availableGames.get(s)){
                                System.out.print(player+"\t");
                            }
                            System.out.println();
                            IDlist.put(i,s);
                        }
                        i++;
                    }
                }else if(cmd.hasOption(help)){
                    formatter.printHelp("Section Commands", options);
                }else{
                    System.out.println("launchGameManager section");
                    System.out.println("Unavailable command, remember to type '-' and the desired command");
                }
                cmd = parser.parse(options, scanf());
            }
            if (cmd.hasOption(create_game)) {
                int numOfPlayers = Integer.parseInt(cmd.getOptionValue(create_game));
                host = true;
                super.gameManagerController.onCreateGame(numOfPlayers, userPlayer.getNickname());
            } else if (cmd.hasOption(select_game)) {
                String lobbyID = cmd.getOptionValue(select_game);
                if(lobbyID.length() == 1){
                    int IDnumber = Integer.parseInt(lobbyID);
                    lobbyID = IDlist.get(IDnumber);
                }
                host = false;
                super.gameManagerController.onSelectGame(lobbyID, userPlayer.getNickname());
            }
        } catch (ParseException pe){
            System.out.println("launchGameManager section");
            System.out.println("Error parsing command-line arguments");
            formatter.printHelp("Parsing Error Section Commands", options);
        }
    }
    @Override
    public void launchGameLobby(String gameID, ArrayList<String> lobbyPlayers, String lobbyHost){
        this.gameID = gameID;
        System.out.print("You have entered the lobby\n"+"Lobby host: "+lobbyHost+"\nLobby players:"); // printing lobby and lobby players
        for(int i = 0; i < lobbyPlayers.size(); i++){
            System.out.print(" "+lobbyPlayers.get(i)+"\t");
        }
        System.out.println("\n");
        Options options = new Options();
        options.addOption(show_gameId);
        options.addOption(chat);
        options.addOption(help);
        if(host && lobbyHost.equals(userPlayer.getNickname())){
            options.addOption(start_match);
        }
        // Printing section commands
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Section Commands", options);
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try{
            cmd = parser.parse(options, scanf());
            while(!cmd.hasOption(start_match)){ // Until it receives a possible command, it continues to scan
                if(cmd.hasOption(show_gameId)){
                    System.out.println("Your gameId is: " + gameID);
                }else if(cmd.hasOption(help)){
                    formatter.printHelp("Section Commands", options);
                }else if(cmd.hasOption(chat)) { // Example of chat implementation
                    Scanner scan = new Scanner(System.in);
                    String msg = scan.nextLine();
                    super.lobbyController.onGetChatMessage(msg);
                    // It also needs to show the past messages
                }else{
                    System.out.println("launchGameLobby section");
                    System.out.println("Unavailable command, remember to type '-' and the desired command");
                }
                cmd = parser.parse(options, scanf());
            }
            if (cmd.hasOption(start_match)) {
                    super.lobbyController.onStartMatch(gameID, userPlayer.getNickname());
            }
        } catch (ParseException pe){
            System.out.println("launchGameLobby section");
            System.out.println("Error parsing command-line arguments");
            formatter.printHelp("Parsing Error Section Commands", options);
        }
        System.out.println("You have left the lobby");
    }
    @Override
    public void chooseCards(){
        ArrayList<Pair<Integer,Integer>> coord = new ArrayList<>();
        ArrayList<BoardCard> selected = new ArrayList<>();

        System.out.println("Select Cards in couples of coordinates.\t\tThe selectable cards are those higlighted" +
                            " in white"+"\nExample: 5 4 5 5 5 6\twhere 5 4 is the first couple and so on");
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
        while(s.length() != 11 && s.length() != 3 && s.length() != 7)
        {
            System.out.println("Insert coordinates through the right pattern\n" + "Example: '5 5 5 6 5 7' where 5 5 is the first couple");
            s = in.nextLine();
        }
        for(int i = 0; i < s.length(); i+=4){
            Pair<Integer,Integer> tmp = new Pair<>(Character.getNumericValue(s.charAt(i)),Character.getNumericValue(s.charAt(i+2)));
            coord.add(tmp);
        }
        System.out.println("Choose order for the selected cards.\nExample: '132' -> first card, third card, second card.");
        Scanner in2 = new Scanner(System.in);
        s = in2.next();
        while(s.length() != coord.size()){
            System.out.println("The chosen order must be of the same size of the cards selected.");
            s = in2.next();
        }
        ArrayList<Pair<Integer,Integer>> copy = new ArrayList<>();
        for(int i = 0; i < s.length(); i++){
            copy.add(coord.get(Character.getNumericValue(s.charAt(i))-1));
        }
        coord = copy;
        for(int i = 0; i < coord.size(); i++){
            selected.add(livingRoom[coord.get(i).getFirst()][coord.get(i).getSecond()]);
        }
        this.selectedCards = selected;
        super.gameController.onSelectedCards(coord, userPlayer.getNickname());
    }
    @Override
    public void chooseColumn(){
        System.out.println("Insert the shelf's column for the selected cards. From 0 to 4.");
        Scanner in3 = new Scanner(System.in);
        int column = Integer.parseInt(in3.next());
        while(column < 0 || column > 4){
            System.out.println("Select a column within range, from 0 to 4.");
            column = Integer.parseInt(in3.next());
        }
        super.gameController.onSelectedColumn(selectedCards, column, userPlayer.getNickname());
    }
    @Override
    public void endCommands(){
        Options options = new Options();
        options.addOption(help);
        options.addOption(exit);
        options.addOption(play_again);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Section Commands", options);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        try{
            cmd = parser.parse(options, scanf());
            while(!cmd.hasOption(exit) && !cmd.hasOption(play_again)){
                if(cmd.hasOption(help)){
                    formatter.printHelp("Section Commands", options);
                }else{
                    System.out.println("Unavailable command, remember to type '-' and the desired command");
                }
                cmd = parser.parse(options, scanf());
            }
            if(cmd.hasOption(exit)){
                //TODO: Terminates the connection to the server
            }else if(cmd.hasOption(play_again)){
                gameManagerController.onLookForNewGames(userPlayer.getNickname());
            }
        } catch (ParseException pe){
            System.out.println("endCommands section");
            System.out.println("Error parsing command-line arguments");
            formatter.printHelp("Parsing Error Section Commands", options);
        }
    }
    @Override
    public void printLivingRoom() {
        BoardCard[][] pieces = livingRoom;
        System.out.println("\n"+"Game state: " + gameState.toString());
        System.out.print("   0");
        for (int j = 1; j < pieces[0].length; j++) {
            System.out.print("  " + j);
        }
        System.out.println();
        for(int i = 0; i < pieces.length; i++){
            System.out.print(i + " ");
            for (int j = 0; j < pieces[0].length; j++) {
                BoardCard tmp = pieces[i][j];
                Pair<String, String> color;
                color = getColor(tmp);
                if (selectables[i][j].equals(true)) {
                    System.out.print(CLIColors.WHITE + CLIColors.BASE + color.getFirst() + color.getSecond()+CLIColors.RESET);
                } else {
                    System.out.print(CLIColors.BLACK + CLIColors.BASE + color.getFirst() + color.getSecond()+CLIColors.RESET);
                }
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }
    @Override
    public void printShelves(){
        System.out.println("\n"+"Game State: "+gameState.toString());
/* Printing of shelves, starting from the playingPlayer's shelf */
        for(Player p: players){
            if(p.getNickname().equals(userPlayer.getNickname())){
                System.out.println("Your shelf:\t\tYour score: "+p.getScore());
                printShelf(p.getPlayersShelf());
            }
        }
/* Printing other players' shelves */
        for(int i = 0; i < players.size(); i++){
            if(!players.get(i).getNickname().equals(userPlayer.getNickname())){
                System.out.println(players.get(i).getNickname()+"'s shelf:\t\t"+players.get(i).getNickname()+"'s score: "
                        +players.get(i).getScore());
                printShelf(players.get(i).getPlayersShelf());
            }
        }
    }
    @Override
    public void showErrorMessage(String error){
        System.out.println(error);
    }
    @Override
    public void printScoreBoard(ArrayList<Pair<String, Integer>> finalScoreBoard, String winner, GameStateType finalGameState){
        System.out.println("\n"+"Game State: "+finalGameState.toString());
        System.out.println("\n"+"The winner is "+winner);
        System.out.println();
        for(Pair<String,Integer> p: finalScoreBoard){
            System.out.println(p.getFirst()+"\tFinal Score: "+p.getSecond());
        }
    }
    private Pair<String,String> getColor(BoardCard tmp){
        String colorBackground;
        String colorValue;
        if(tmp.getColor().equals(colorType.TOMBSTONE) || tmp.getColor().equals(colorType.EMPTY_SPOT)){
            colorBackground = CLIColors.BLACK_BACKGROUND;
            colorValue = " X ";
        } else if (tmp.getColor().equals(colorType.GREEN)) {
            colorBackground = CLIColors.GREEN_BACKGROUND;
            colorValue = " G ";
        } else if (tmp.getColor().equals(colorType.WHITE)) {
            colorBackground = CLIColors.WHITE_BACKGROUND;
            colorValue = " W ";
        } else if (tmp.getColor().equals(colorType.YELLOW)) {
            colorBackground = CLIColors.YELLOW_BACKGROUND;
            colorValue = " Y ";
        } else if (tmp.getColor().equals(colorType.LIGHT_BLUE)) {
            colorBackground = CLIColors.CYAN_BACKGROUND;
            colorValue = " L ";
        } else if (tmp.getColor().equals(colorType.BLUE)) {
            colorBackground = CLIColors.BLUE_BACKGROUND;
            colorValue = " B ";
        } else { // purple case
            colorBackground = CLIColors.PURPLE_BACKGROUND;
            colorValue = " P ";
        }
        Pair<String,String> val = new Pair<>(colorBackground,colorValue);
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
                Pair<String,String> color;
                color = getColor(card);
                System.out.print(CLIColors.BLACK+CLIColors.BASE+color.getFirst()+color.getSecond()+CLIColors.RESET);
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