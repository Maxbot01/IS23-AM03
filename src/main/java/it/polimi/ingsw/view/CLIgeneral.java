package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.controller.GameManagerController;
import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.*;
import java.util.ArrayList;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import it.polimi.ingsw.model.modelSupport.exceptions.UnselectableCardException;
import it.polimi.ingsw.model.virtual_model.VirtualGameManager;
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
    private HashMap<String,List<String>> availableGames;
    private Player userPlayer; // Remember that userPlayer does not have a personal goal or points
    private String host;
    private String playingPlayer;
    private ArrayList<BoardCard> selectedCards;
    //private String[] dataInput; CLIInputThread

    // COMMANDS INITIALIZATION
    private final Option show_games = Option.builder("show_games")// In the GUI it will be shown straight away
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
    private final Option close = Option.builder("close")//TODO: Delete after debug
            .hasArg(false)
            .desc("closes the game commands at the end of the game")
            .required(false)
            .build();
    private final Option ready = Option.builder("ready")//Used for entering the game once it's created, and exiting the lobby
            .hasArg(false)
            .desc("sets the player ready to start the game")
            .required(false)
            .build();
    private final Option select_cards = Option.builder("select_cards")
            .hasArg(false)
            .desc("starts the cards and column selection phase")
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
        if (Thread.currentThread().isInterrupted()) {
            return;
        }
        for(int i = 0; i < players.size(); i++){
            if (Thread.currentThread().isInterrupted()) {
                return;
            }
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
    public void waitingCommands() {

    }

    @Override
    public void updatePlayingPlayer(String playingPlayer){
        this.playingPlayer = playingPlayer;
        if(playingPlayer.equals(userPlayer.getNickname())){
            System.out.println("You are playing...");
        }else {
            System.out.println(playingPlayer + " is playing...");
        }
    }

    /*@Override
    public void setFinishedFlag(boolean value){
        this.finishedFlag = value;
    }*/
    //@Override
    /*public void waitingCommands(){
        Options options = new Options();
        options.addOption(show_gameId);
        options.addOption(chat);
        options.addOption(show_commonGoals);
        options.addOption(show_personalGoal);
        options.addOption(help);
        //options.addOption(stop);//TODO: Delete after debug
        // Printing section commands
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Section Commands", options);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        try{
            /*if (Thread.currentThread().isInterrupted()) {
                return; // Exit the loop gracefully
            }*/
            //while(qualcosa che mi arriva dal controller che continua a mandarlo e che successivamente lo rompe)
            //RIMETTILO cmd = parser.parse(options, scanf());
            //while(!cmd.hasOption(stop)) {
                /*if (Thread.currentThread().isInterrupted()) {
                    return;
                }*/
                /*RIMETTILOif (cmd.hasOption(show_gameId)) {
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
                /*if (Thread.currentThread().isInterrupted()) {
                    return; // Exit the loop gracefully
                }*/
            /*}
            if(cmd.hasOption(stop)){
                System.out.println("You have left waitingCommands");
            }*/
        /*} catch (ParseException pe){
            System.out.println("waitingCommands section");
            System.out.println("Error parsing command-line arguments");
            formatter.printHelp("Parsing Error Section Commands", options);
        }
        System.out.println("You have left waitingCommands, but not with 'stop'");
    }*/
    @Override
    public void gameCommands(){
        Options options = new Options();
        options.addOption(show_gameId);
        options.addOption(chat);
        options.addOption(show_commonGoals);
        options.addOption(show_personalGoal);
        options.addOption(help);
        options.addOption(select_cards);
        options.addOption(close);//TODO: Create a check with a flag variable changed when finishedGameMessage arrives
        boolean finished = false;
        // Printing section commands
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Section Commands", options);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        while(!finished) {
            try {
                cmd = parser.parse(options, scanf());
                while (!cmd.hasOption(close)) {
                    if (cmd.hasOption(show_gameId)) {
                        System.out.println("Your gameId is: " + gameID);
                    } else if (cmd.hasOption(show_commonGoals)) {
                        System.out.println("First common goal: " + commonGoals.getFirstGoal().getClass().getName()
                                .replaceAll("Strategy","") + "\nSecond commmongoal: " + commonGoals
                                .getSecondGoal().getClass().getName().replaceAll("Strategy",""));
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
                    } else if (cmd.hasOption(select_cards)) {
                        if (playingPlayer.equals(userPlayer.getNickname())) {
                            finished = true;
                            super.gameController.startCardsSelection();
                            break;
                        } else {
                            System.out.println("It's not your turn to pick");
                        }
                    } else {
                        System.out.println("gameCommands section");//DEBUG
                        System.out.println("Unavailable command, remember to type '-' and the desired command");
                    }
                    cmd = parser.parse(options, scanf());
                }
                if (cmd.hasOption(close) && !finished) {
                    finished = true;
                    System.out.println("You have left the game");
                }
            } catch (ParseException pe) {
                System.out.println("gameCommands section");//DEBUG
                System.out.println("Error parsing command-line arguments");
                formatter.printHelp("Parsing Error Section Commands, invalid command", options);
            }
        }
        System.out.println("You have left gameCommands");//DEBUG
    }
    @Override
    public void requestCredentials(){
        System.out.println("Insert Username and a Password, that you will need in case of disconnection.\n" +
                "If you are reconnecting use your previously inserted password:");
        Scanner in = new Scanner(System.in);
        if (Thread.currentThread().isInterrupted()) {
            return; // Exit the loop gracefully
        }
        String username = in.next();
        String password = in.next();
        this.userPlayer = new Player(username);
        System.out.println("GameManagerController: " + ClientManager.gameManagerController);
        System.out.println("Your username is: "+username);
        System.out.println("Your password is: "+password);
        GameManagerController gameManagerController = ClientManager.gameManagerController;
        gameManagerController.onSetCredentials(username, password);
    }
    @Override
    public void showPlayingPlayer(String playingPlayer){//TODO: Delete because I'll have the chairedPlayer now
        System.out.println(playingPlayer+" is playing...");
    }
    @Override
    public void launchGameManager(HashMap<String, List<String>> availableGames){
        Options options = new Options();
        options.addOption(show_games);
        options.addOption(create_game);
        options.addOption(select_game);
        options.addOption(help);
        boolean finished = false;
        this.availableGames = availableGames;
        // Printing section commands
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Section Commands", options);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        /*if (Thread.currentThread().isInterrupted()) {
            System.out.println("Exiting launchGameManager Thread, 1");
            return; // Exit the loop gracefully
        }*/

        /*HashMap<Integer,String> IDlist = new HashMap<>();
        int i = 1;*/
        //while(!Thread.currentThread().isInterrupted()) {
        while(!finished) {
            try {
                /*if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Exiting launchGameManager Thread, 2");
                    return; // Exit the loop gracefully
                }*/
                cmd = parser.parse(options, scanf());
                while (!cmd.hasOption(create_game)/* && !Thread.currentThread().isInterrupted()*/) { // Until it receives a possible command, it continues to scan
                    /*if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Exiting launchGameManager Thread, 3");
                        return; // Exit the loop gracefully
                    }*/
                    if (cmd.hasOption(show_games)) { // AvailableGames is only used in this method, therefore it is not saved as a parameter
                        /*if (Thread.currentThread().isInterrupted()) {
                            System.out.println("Exiting launchGameManager Thread, 4");
                            return; // Exit the loop gracefully
                        }*/
                        if (this.availableGames.size() == 0) {
                            System.out.println("No games available");
                        } else {
                            int i = 1;
                            for (String s : this.availableGames.keySet()) {
                                System.out.println("GameId " + i + ": " + s);
                                System.out.print("Players: ");
                                for (String player : availableGames.get(s)) {
                                    System.out.print(player + "\t");
                                }
                                System.out.println();
                                i++;
                            }
                        }
                    } else if (cmd.hasOption(help)) {
                        /*if (Thread.currentThread().isInterrupted()) {
                            System.out.println("Exiting launchGameManager Thread, 5");
                            return; // Exit the loop gracefully
                        }*/
                        formatter.printHelp("Section Commands", options);
                    } else if (cmd.hasOption(select_game)) {
                        boolean valid = true;
                        String lobbyID = cmd.getOptionValue(select_game);
                        if (lobbyID.length() == 1) {
                            int IDnumber = Integer.parseInt(lobbyID);
                            if (IDnumber - 1 < availableGames.size()) {
                                lobbyID = availableGames.keySet().toArray()[IDnumber - 1].toString();
                            } else {
                                valid = false;
                            }
                        } else if (!availableGames.containsKey(lobbyID)) {
                            valid = false;
                        }
                        if (valid) {
                            finished = true;//TODO: Put this check in all section to solve the parsing error quit
                            super.gameManagerController.onSelectGame(lobbyID, userPlayer.getNickname());
                            break;
                        } else {
                            System.out.println("Incorrect ID, please select a valid ID");
                        }
                    } else {
                        /*if (Thread.currentThread().isInterrupted()) {
                            System.out.println("Exiting launchGameManager Thread, 6");
                            return; // Exit the loop gracefully
                        }*/
                        System.out.println("launchGameManager section");
                        System.out.println("Unavailable command, remember to type '-' and the desired command");
                    }
                    cmd = parser.parse(options, scanf());
                    /*if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Exiting launchGameManager Thread, 7");
                        return; // Exit the loop gracefully
                    }*/
                }
                /*if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Exiting launchGameManager " + Thread.currentThread().getName() + ", XX");
                    return; // Exit the loop gracefully
                } else*/
                if (cmd.hasOption(create_game) && !finished) {
                    /*if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Exiting launchGameManager Thread, 8");
                        return; // Exit the loop gracefully
                    }*/
                    int numOfPlayers = Integer.parseInt(cmd.getOptionValue(create_game));
                    if(numOfPlayers >= 2 && numOfPlayers <= 4) {
                        host = userPlayer.getNickname();
                        finished = true;
                        super.gameManagerController.onCreateGame(numOfPlayers, userPlayer.getNickname());
                    }else if(numOfPlayers > 4){
                        System.out.println("You can't have more than 4 players in a game");
                    }else{
                        System.out.println("You need to have at least 2 player for the game");
                    }
                }
            } catch (ParseException pe) {
                System.out.println("launchGameManager section");
                System.out.println("Error parsing command-line arguments, invalid command");
                formatter.printHelp("Section Commands", options);
            }
        }/*catch (InterruptedException ie){
                System.out.println("Exiting launchGameManager " + Thread.currentThread().getName() + ", YY");
                return;
            }*/
        //}
        System.out.println("You have left the manager");
    }
    @Override
    public void addNewGame(Pair<String, List<String>> newGame){
        this.availableGames.put(newGame.getFirst(),newGame.getSecond());
        System.out.println("A new game was created");
        System.out.println("GameId "+this.availableGames.size()+": "+newGame.getFirst());
        System.out.print("Players: ");
        for (String player : this.availableGames.get(newGame.getFirst())) {//I iterate on availableGames to be sure it was added
            System.out.print(player + "\t");
        }
        System.out.println();
    }
    @Override
    public void launchGameLobby(String gameID, ArrayList<String> lobbyPlayers, String lobbyHost){
        this.gameID = gameID;
        ArrayList<Player> tmp = new ArrayList<>();
        for(String s: lobbyPlayers){

            Player p = new Player(s);
            tmp.add(p);
        }
        this.players = tmp;
        this.host = lobbyHost;
        System.out.print("You have entered the lobby\n"+"Lobby host: "+lobbyHost+"\nLobby players:"); // printing lobby and lobby players
        for(int i = 0; i < lobbyPlayers.size(); i++){
            System.out.print("\t"+lobbyPlayers.get(i));
        }
        System.out.println("\n");
        Options options = new Options();
        options.addOption(show_gameId);
        options.addOption(chat);
        options.addOption(help);
        if(host.equals(userPlayer.getNickname())){
            options.addOption(start_match);
        }else{
            options.addOption(ready);
        }
        boolean finished = false;
        // Printing section commands
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Section Commands", options);
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        while(!finished) {
            try {
            /*if (Thread.currentThread().isInterrupted()) {
                System.out.println("Exiting launchGameLobby Thread, 1");
                return; // Exit the loop gracefully
            }*/
                cmd = parser.parse(options, scanf());
                while (!cmd.hasOption(start_match) && !cmd.hasOption(ready)/* && !Thread.currentThread().isInterrupted()*/) { // Until it receives a possible command, it continues to scan
                /*if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Exiting launchGameLobby Thread, 2");
                    return; // Exit the loop gracefully
                }*/
                    if (cmd.hasOption(show_gameId)) {
                    /*if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Exiting launchGameLobby Thread, 3");
                        return; // Exit the loop gracefully
                    }*/
                        System.out.println("Your gameId is: " + gameID);
                    } else if (cmd.hasOption(help)) {
                    /*if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Exiting launchGameLobby Thread, 4");
                        return; // Exit the loop gracefully
                    }*/
                        formatter.printHelp("Section Commands", options);
                    } else if (cmd.hasOption(chat)) { // Example of chat implementation
                    /*if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Exiting launchGameLobby Thread, 5");
                        return; // Exit the loop gracefully
                    }*/
                        Scanner scan = new Scanner(System.in);
                        String msg = scan.nextLine();
                        super.lobbyController.onGetChatMessage(msg);
                        // It also needs to show the past messages
                    } else {
                    /*if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Exiting launchGameLobby Thread, 6");
                        return; // Exit the loop gracefully
                    }*/
                        System.out.println("launchGameLobby section");
                        System.out.println("Unavailable command, remember to type '-' and the desired command");
                    }
                    cmd = parser.parse(options, scanf());
                }
            /*if(Thread.currentThread().isInterrupted()){
                System.out.println("Exiting launchGameLobby "+Thread.currentThread().getName()+", XX");
                return; // Exit the loop gracefully
            } else*/
                if (cmd.hasOption(start_match)) {
                /*if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Exiting launchGameLobby Thread, 7");
                    return; // Exit the loop gracefully
                }*/
                    finished = true;
                    super.lobbyController.onStartMatch(gameID, userPlayer.getNickname());
                } else if (cmd.hasOption(ready)) {
                    finished = true;
                    super.gameController.setReady();
                    //TODO: We could show the game after this command
                }
            } catch (ParseException pe) {
                System.out.println("launchGameLobby section");
                System.out.println("Error parsing command-line arguments, invalid command");
                formatter.printHelp("Section Commands", options);
            } /*catch (InterruptedException ie){
            System.out.println("Exiting launchGameManager " + Thread.currentThread().getName() + ", YY");
            return;
        }*/
        }
        System.out.println("You have left the lobby");
    }
    @Override
    public void addNewLobbyPlayer(String addedPlayer){
        Player p = new Player(addedPlayer);
        this.players.add(p);
        System.out.println(addedPlayer+" has entered the lobby");
        System.out.print("Lobby players: ");
        for(Player t: players){
            System.out.print(t.getNickname()+"\t");
        }
        System.out.println();
    }
    @Override
    public void chooseCards(){
        ArrayList<Pair<Integer,Integer>> coord = new ArrayList<>();
        ArrayList<BoardCard> selected = new ArrayList<>();

        System.out.println("Select Cards in couples of coordinates, maximum of three.\tThe selectable cards are those higlighted" +
                            " in white"+"\nExample: 5 4 5 5 5 6\twhere 5 4 is the first couple and so on");
        /*if (Thread.currentThread().isInterrupted()) {
            return; // Exit the loop gracefully
        }*/
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
        if(coord.size() > 1) {
            System.out.println("Choose order for the selected cards.\nExample: '132' -> first card, third card, second card.");
            Scanner in2 = new Scanner(System.in);
            s = in2.next();
            boolean valid = false;
            while(!valid){
                boolean correct = true;
                if(s.length() != coord.size()){
                    System.out.println("The chosen order must be of the same size of the cards selected.");
                    correct = false;
                }else{
                    int max = s.length();
                    List<Integer> sToList = new ArrayList<>();
                    for(char c: s.toCharArray()){
                        sToList.add(Character.getNumericValue(c));
                    }
                    for(int i = max; i > 0; i--){
                        if(!sToList.contains(i)){
                            correct = false;
                            System.out.println("The chosen order must refer to the number of cards selected\nExample:" +
                                    " 3 cards selected -> 312, NOT 314");
                            break;
                        }
                    }
                }
                if(!correct){
                    s = in2.next();
                }else{
                    valid = true;
                }
            }
            ArrayList<Pair<Integer, Integer>> copy = new ArrayList<>();
            for (int i = 0; i < s.length(); i++) {
                copy.add(coord.get(Character.getNumericValue(s.charAt(i)) - 1));
            }
            coord = copy;
        }
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
    public void newChatMessage(ArrayList<Pair<String, String>> messages) {

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
            for(int i = 0; i < players.size(); i++){
                if(p.getFirst().equals(players.get(i).getNickname())){
                    this.players.get(i).updateScore(p.getSecond()-players.get(i).getScore());
                }
            }
            System.out.println(p.getFirst()+"\tFinal Score: "+p.getSecond());
        }
    }
    /*@Override CLIInputThread
    public void readInput() throws InterruptedException {
        while(!Thread.currentThread().isInterrupted()){
            this.dataInput = scanf();
        }
        throw new InterruptedException();
    }*/

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

}
