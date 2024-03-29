package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameManagerController;
import it.polimi.ingsw.controller.client.ClientManager;
import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.*;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import org.apache.commons.cli.*;
import org.fusesource.jansi.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import static it.polimi.ingsw.controller.client.ClientMain.stub;

public class CLIgeneral extends View{
    private GameStateType gameState;
    private String gameID;
    private BoardCard[][] livingRoom;
    private Boolean[][] selectables;
    private CommonGoals commonGoals;
    private PersonalGoal personalGoal;
    private ArrayList<Player> players;
    private ArrayList<Pair<String,List<String>>> availableGames;
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
    private final Option show_chat = Option.builder("show_chat")
            .hasArg(false)
            .desc("shows the last 5 messages")
            .required(false)
            .build();
    private final Option show_full_chat = Option.builder("show_full_chat")
            .hasArg(false)
            .desc("shows the entire chat")
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
    private final Option leave_game = Option.builder("leave_game")
            .hasArg(false)
            .desc("you leave the game")
            .required(false)
            .build();
    private final Option ready = Option.builder("ready")//Used for entering the game once it's created, and exiting the lobby
            .hasArg(false)
            .desc("sets the player ready to start")
            .required(false)
            .build();
    private final Option select_cards = Option.builder("select_cards")
            .hasArg(false)
            .desc("starts the cards and column selection phase")
            .required(false)
            .build();
    /**
     * It initializes the game parameters at the start of the match or, in case of reconnection it sets them based on the last changes
     * @param playersNick
     * @param commonGoals
     * @param personalGoals
     * @param livingRoom
     * @param selectables
     * @param playersShelves
     * @param playersPoints
     * @param gameState
     */
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
    /**
     * It updates the game parameters relative to the selection of cards from the living room by the playing player
     * @param livingRoom
     * @param selectables
     * @param gameState
     */
    @Override
    public void updateMatchAfterSelectedCards(BoardCard[][] livingRoom, Boolean[][] selectables, GameStateType gameState){
        this.livingRoom = livingRoom;
        this.selectables = selectables;
        this.gameState = gameState;
    }
    /**
     * It updates the game parameters relative to the selection of the column by the playing player
     * @param livingRoom
     * @param selectables
     * @param gameState
     * @param updatedPlayerPoints
     * @param updatedPlayerShelf
     */
    @Override
    public void updateMatchAfterSelectedColumn(BoardCard[][] livingRoom, Boolean[][] selectables, GameStateType gameState, Pair<String,
            Integer> updatedPlayerPoints, Pair<String, BoardCard[][]> updatedPlayerShelf) {
        this.livingRoom = livingRoom;
        this.selectables = selectables;
        this.gameState = gameState;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getNickname().equals(updatedPlayerPoints.getFirst())) {
                this.players.get(i).updateScore(updatedPlayerPoints.getSecond() - players.get(i).getScore());
                BoardCard[][] updatedShelf = updatedPlayerShelf.getSecond();
                for (int j = 0; j < updatedShelf.length; j++) {
                    for (int z = 0; z < updatedShelf[0].length; z++) {
                        this.players.get(i).getPlayersShelf().getShelfCards()[j][z] = updatedShelf[j][z];
                    }
                }
            }
        }    }
    /**
     * It updates the playing player
     * @param playingPlayer
     */
    @Override
    public void updatePlayingPlayer(String playingPlayer){
        AnsiConsole.systemInstall();
        this.playingPlayer = playingPlayer;
        if(playingPlayer.equals(userPlayer.getNickname())){
            System.out.println("\033[1;97m" + "You are playing..."+ "\033[0m");
        }else {
            System.out.println("\033[1;97m" + playingPlayer + " is playing..."+ "\033[0m");
        }
    }
    /**
     * Method relative to the activation of the available commands that the player has during the game
     */
    @Override
    public void gameCommands(){
        Options options = new Options();
        options.addOption(show_gameId);
        options.addOption(chat);
        options.addOption(show_chat);
        options.addOption(show_full_chat);
        options.addOption(show_commonGoals);
        options.addOption(show_personalGoal);
        options.addOption(help);
        options.addOption(select_cards);
        options.addOption(leave_game);
        boolean finished = false;
        // Printing section commands
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Available Commands", options);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;
        while(!finished) {
            try {
                cmd = parser.parse(options, scanf());
                if (cmd.hasOption(show_gameId)) {
                    System.out.println("Your gameId is: " + gameID);
                } else if (cmd.hasOption(show_commonGoals)) {
                    String firstGoal = commonGoals.getFirstGoal().getClass().getName().replaceAll("it.polimi.ingsw.model.CommonGoals.Strategy.","");
                    String secondGoal = commonGoals.getSecondGoal().getClass().getName().replaceAll("it.polimi.ingsw.model.CommonGoals.Strategy.","");
                    System.out.println("First common goal: " + firstGoal.replaceAll("Strategy","") +
                            "\nSecond common goal: " + secondGoal.replaceAll("Strategy",""));
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
                    System.out.print("Choose receiver\nPossible receivers: 'All', ");
                    for(int i = 0; i < players.size(); i++){
                        if(i+1 == players.size()){
                            System.out.println("'"+players.get(i).getNickname()+"'");
                        }else {
                            System.out.print("'"+players.get(i).getNickname()+"'" + ", ");
                        }
                    }
                    boolean found = false;
                    String toUser;
                    do {
                        Scanner scan = new Scanner(System.in);
                        toUser = scan.next();
                        ArrayList<String> users = new ArrayList<>();
                        for(Player p: players){
                            users.add(p.getNickname());
                        }
                        if (users.contains(toUser) || toUser.equals("All")) {
                            found = true;
                        }
                        if(!found){
                            System.out.println("Invalid Destination");
                        }
                    }while(!found);
                    System.out.println("Enter your Message");
                    String msg;
                    do {
                        Scanner scan2 = new Scanner(System.in);
                        msg = scan2.nextLine();
                        if(msg.isEmpty()){
                            System.out.println("No message inserted");
                        }
                    }while(msg.isEmpty());
                    super.gameController.onSendChatMessage(msg,toUser);
                    // It also needs to show the past messages
                } else if (cmd.hasOption(show_chat)) {
                    super.gameController.onGetChat(false);
                } else if (cmd.hasOption(show_full_chat)) {
                    super.gameController.onGetChat(true);
                } else if (cmd.hasOption(select_cards)) {
                    if (playingPlayer.equals(userPlayer.getNickname())) {
                        finished = true;
                        super.gameController.startCardsSelection();
                        break;
                    } else {
                        System.out.println("It's not your turn to pick");
                    }
                } else if (cmd.hasOption(leave_game)) {
                    if(gameState.equals(GameStateType.FINISHED)) {
                        finished = true;
                        System.out.println("You have left the game");
                        super.gameController.setReady(gameID, userPlayer.getNickname());
                    }else{
                        System.out.println("The game hasn't ended, you can't leave yet");
                    }
                }
                else {
                    System.out.println("gameCommands section");//DEBUG
                    System.out.println("Unavailable command, remember to type '-' and the desired command");
                }
            } catch (ParseException pe) {
                System.out.println("Error parsing command-line arguments, invalid command");
                formatter.printHelp("Available Commands", options);
            }
        }
    }
    /**
     * Method relative to the acquisition of the client's credentials through the command line interface
     */
    @Override
    public void requestCredentials(){
        System.out.println("Insert Username and a Password, that you will need in case of disconnection.\n" +
                "If you are reconnecting use your previously inserted password:");
        Scanner in = new Scanner(System.in);
        String username = in.next();
        String password = in.next();
        this.userPlayer = new Player(username);
        System.out.println("Your username is: "+username);
        System.out.println("Your password is: "+password);
        GameManagerController gameManagerController = ClientManager.gameManagerController;
        gameManagerController.onSetCredentials(username, password, stub);
    }
    /**
     * Method relative to the activation of the available commands that the player has during the game selection phase
     * @param availableGames
     */
    @Override
    public void launchGameManager(HashMap<String, List<String>> availableGames){
        Options options = new Options();
        options.addOption(show_games);
        options.addOption(create_game);
        options.addOption(select_game);
        options.addOption(help);
        boolean finished = false;
        ArrayList<Pair<String,List<String>>> tmp = new ArrayList<>();
        for(String s: availableGames.keySet()){
            Pair<String,List<String>> p = new Pair<>(s,availableGames.get(s));
            tmp.add(p);
        }
        this.availableGames = tmp;
        // Printing section commands
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Available Commands", options);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;

        while(!finished) {
            try {
                cmd = parser.parse(options, scanf());
                if (cmd.hasOption(show_games)) {
                    if (this.availableGames.size() == 0) {
                        System.out.println("No games available");
                    } else {
                        for(int i = 0; i < this.availableGames.size(); i++){
                            int z = i+1;
                            System.out.println("GameId " + z + ": " + this.availableGames.get(i).getFirst());
                            System.out.print("Players: ");
                            for(int j = 0; j < this.availableGames.get(i).getSecond().size(); j++){
                                System.out.print(this.availableGames.get(i).getSecond().get(j)+"\t");
                            }
                            System.out.println();
                        }
                    }
                } else if (cmd.hasOption(help)) {
                    formatter.printHelp("Available Commands", options);
                } else if (cmd.hasOption(select_game)) {
                    boolean valid = true;
                    String lobbyID = cmd.getOptionValue(select_game);
                    if (lobbyID.length() == 1) {
                        int IDnumber = Integer.parseInt(lobbyID);
                        if (IDnumber - 1 < this.availableGames.size() && IDnumber - 1 >= 0) {
                            lobbyID = this.availableGames.get(IDnumber-1).getFirst();
                        } else {
                            valid = false;
                        }
                    } else {
                        valid = false;
                        for(int i = 0; i < this.availableGames.size(); i++){
                            if(this.availableGames.get(i).getFirst().equals(lobbyID)){
                                valid = true;
                            }
                        }
                    }
                    if (valid) {
                        finished = true;
                        super.gameManagerController.onSelectGame(lobbyID, userPlayer.getNickname(), stub);
                    } else {
                        System.out.println("Incorrect ID, please select a valid ID");
                    }
                } else if (cmd.hasOption(create_game)) {
                    int numOfPlayers = Integer.parseInt(cmd.getOptionValue(create_game));
                    if (numOfPlayers >= 2 && numOfPlayers <= 4) {
                        host = userPlayer.getNickname();
                        finished = true;
                        super.gameManagerController.onCreateGame(numOfPlayers, userPlayer.getNickname(),stub);
                    } else if (numOfPlayers > 4) {
                        System.out.println("You can't have more than 4 players in a game");
                    } else {
                        System.out.println("You need to have at least 2 player for the game");
                    }
                } else {
                    System.out.println("launchGameManager section");//DEBUG
                    System.out.println("Unavailable command, remember to type '-' and the desired command");
                }
            } catch (ParseException pe) {
                System.out.println("Error parsing command-line arguments, invalid command");
                formatter.printHelp("Available Commands", options);
            }
        }
    }
    /**
     * Whenever a new game lobby is created it updates the available games
     * @param newGame
     */
    @Override
    public void addNewGame(Pair<String, List<String>> newGame){
        boolean found = false;
        int index = -1;
        if(!availableGames.isEmpty()) {
            for (Pair<String, List<String>> availableGame : availableGames) {
                if (availableGame.getFirst().equals(newGame.getFirst())) {
                    found = true;
                    index = availableGames.indexOf(availableGame)+1;
                    for(String s: newGame.getSecond()){
                        if(!availableGame.getSecond().contains(s)){
                            this.availableGames.get(index-1).getSecond().add(s);
                        }
                    }
                    break;
                }
            }
        }
        if(found) {
            System.out.println("A player has entered the following game");
        }else{
            System.out.println("A new game was created");
            Pair<String,List<String>> tmp = new Pair<>(newGame.getFirst(),newGame.getSecond());
            this.availableGames.add(tmp);
            index = this.availableGames.size();
        }
        System.out.println("GameId "+ index +": "+newGame.getFirst());
        System.out.print("Players: ");
        for (String player : newGame.getSecond()) {
            System.out.print(player + "\t");
        }
        System.out.println();
    }
    /**
     * Method relative to the activation of the available commands that the player has during the game's lobby phase
     * @param gameID
     * @param lobbyPlayers
     * @param lobbyHost
     */
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
        options.addOption(show_chat);
        options.addOption(show_full_chat);
        options.addOption(help);
        if(host.equals(userPlayer.getNickname())){
            options.addOption(start_match);
        }else{
            options.addOption(ready);
        }
        boolean finished = false;
        // Printing section commands
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Available Commands", options);
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        while(!finished) {
            try {
                cmd = parser.parse(options, scanf());
                if (cmd.hasOption(show_gameId)) {
                    System.out.println("Your gameId is: " + gameID);
                } else if (cmd.hasOption(help)) {
                    formatter.printHelp("Available Commands", options);
                } else if (cmd.hasOption(chat)) { // Example of chat implementation
                    System.out.print("Choose receiver\nPossible receivers: 'All', ");
                    for(int i = 0; i < players.size(); i++){
                        if(i+1 == players.size()){
                            System.out.println("'"+players.get(i).getNickname()+"'");
                        }else {
                            System.out.print("'"+players.get(i).getNickname()+"'" + ", ");
                        }
                    }
                    boolean found = false;
                    String toUser;
                    do {
                        Scanner scan = new Scanner(System.in);
                        toUser = scan.next();
                        ArrayList<String> users = new ArrayList<>();
                        for(Player p: players){
                            users.add(p.getNickname());
                        }
                        if (users.contains(toUser) || toUser.equals("All")) {
                            found = true;
                        }
                        if(!found){
                            System.out.println("Invalid Destination");
                        }
                    }while(!found);
                    System.out.println("Enter your Message");
                    String msg;
                    do {
                        Scanner scan2 = new Scanner(System.in);
                        msg = scan2.nextLine();
                        if(msg.isEmpty()){
                            System.out.println("No message inserted");
                        }
                    }while(msg.isEmpty());
                    super.gameController.onSendChatMessage(msg,toUser);
                } else if (cmd.hasOption(show_chat)) {
                    super.lobbyController.onGetChat(false);
                } else if (cmd.hasOption(show_full_chat)) {
                    super.lobbyController.onGetChat(true);
                } else if (cmd.hasOption(start_match)) {
                    finished = true;
                    super.lobbyController.onStartMatch(gameID, userPlayer.getNickname());
                } else if (cmd.hasOption(ready)) {
                    finished = true;
                    super.gameController.setReady(gameID, userPlayer.getNickname());//After this command the game is shown
                } else {
                    System.out.println("launchGameLobby section");
                    System.out.println("Unavailable command, remember to type '-' and the desired command");
                }
            } catch (ParseException pe) {
                System.out.println("Error parsing command-line arguments, invalid command");
                formatter.printHelp("Available Commands", options);
            }
        }
    }
    /**
     * Whenever a new player enters the lobby it updates the lobby's players
     * @param addedPlayer
     */
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
    /**
     * Method relative to the card and order selection phase
     */
    @Override
    public void chooseCards(){
        ArrayList<Pair<Integer,Integer>> coord = new ArrayList<>();
        ArrayList<BoardCard> selected = new ArrayList<>();

        System.out.println("Select Cards in couples of coordinates, maximum of three.\tThe selectable cards are those higlighted" +
                            " in white"+"\nExample: 5 4 5 5 5 6\twhere 5 4 is the first couple and so on");
        Scanner in = new Scanner(System.in);
        String s;
        do{
            s = in.nextLine();
            boolean chosenCardsCorrectly = true;
            //Check if the number of coordinates is correct
            if(s.length() != 11 && s.length() != 3 && s.length() != 7){
                System.out.println("Insert coordinates through the right pattern\n" + "Example: '5 5 5 6 5 7' where 5 5 is the first couple");
                chosenCardsCorrectly = false;
            } else {
                //Check if the spaces are in the right spot
                for(int i = 1; i < s.length(); i+=2){
                    if(s.charAt(i) != ' '){
                        chosenCardsCorrectly = false;
                        System.out.println("Insert coordinates through the right pattern\n" + "Example: '5 5 5 6 5 7' where 5 5 is the first couple");
                        break;
                    }
                }
                //Check if the coordinates are numbers
                for(int i = 0; i < s.length() && chosenCardsCorrectly; i+=2){
                    if(!Character.isDigit(s.charAt(i))){
                        chosenCardsCorrectly = false;
                        System.out.println("All coordinates must be numbers\n" + "Example: '5 5 5 6 5 7' where 5 5 is the first couple");
                        break;
                    }else if(Character.getNumericValue(s.charAt(i)) == 9){//Check if coordinate is in range
                        chosenCardsCorrectly = false;
                        System.out.println("Coordinates range from 0 to 8, choose again");
                        break;
                    }
                }
                //Check if there are equal coordinates chosen
                for(int i = 0; i < s.length() && chosenCardsCorrectly; i+=4){
                    Pair<Integer,Integer> tmp = new Pair<>(Character.getNumericValue(s.charAt(i)),Character.getNumericValue(s.charAt(i+2)));
                    boolean present = false;
                    for(int j = 0; j < coord.size() && !present; j++){
                        if(coord.get(j).getFirst().equals(tmp.getFirst()) && coord.get(j).getSecond().equals(tmp.getSecond())){
                            present = true;
                        }
                    }
                    if(present){
                        chosenCardsCorrectly = false;
                        System.out.println("You can't choose the same coordinates twice");
                    } else {
                        coord.add(tmp);
                    }
                }
                //Check if the shelf has enough spaces for the chosen cards
                if(chosenCardsCorrectly){
                    int numberOfCardsChosen = s.length()/3;
                    for(Player p: players){
                        if(p.getNickname().equals(userPlayer.getNickname()) && p.getNickname().equals(playingPlayer)){
                            boolean validColumnFound = false;
                            int maxNumberOfCardsPossible = 0;
                            for(int j = 0; j < p.getPlayersShelf().getShelfCards()[0].length && !validColumnFound; j++){
                                int freeSpots = 0;
                                for(int i = 0; i < p.getPlayersShelf().getShelfCards().length; i++) {
                                    if(p.getPlayersShelf().getShelfCards()[i][j].getColor().equals(colorType.EMPTY_SPOT)){
                                        freeSpots++;
                                    }
                                }
                                if(freeSpots > maxNumberOfCardsPossible){
                                    maxNumberOfCardsPossible = freeSpots;
                                }
                                if(freeSpots >= numberOfCardsChosen){
                                    validColumnFound = true;
                                }
                            }
                            if(!validColumnFound){
                                chosenCardsCorrectly = false;
                                if(maxNumberOfCardsPossible > 1) {
                                    System.out.println("Look at your shelf, you can't insert more than " + maxNumberOfCardsPossible + " cards in one of your columns");
                                }else{
                                    System.out.println("Look at your shelf, you can't insert more than " + maxNumberOfCardsPossible + " card in one of your columns");
                                }
                                break;
                            }
                        }
                    }
                }
                if (chosenCardsCorrectly){
                    if(!checkRule(coord) || !checkRuleDiagonal(coord)){
                        System.out.println("You can't choose cards that are not in the same row or column");
                        chosenCardsCorrectly = false;
                    }
                }
                if (chosenCardsCorrectly){
                    if (!inAdiacent(coord)){
                        System.out.println("You can't choose cards that are not adiacent");
                        chosenCardsCorrectly = false;
                    }
                }
            }
            if(chosenCardsCorrectly){
                break;
            }else{
                coord.clear();
            }
        } while(true);
        // Choose order phase:
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
    /**
     * Method relative to the column selection phase
     */
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
    /**
     * Method relative to the activation of the available commands that the player has after he has left the game
     */
    @Override
    public void endCommands(){
        Options options = new Options();
        options.addOption(help);
        options.addOption(exit);
        options.addOption(play_again);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Available Commands", options);
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        boolean finished = false;
        while(!finished) {
            try {
                cmd = parser.parse(options, scanf());
                if (cmd.hasOption(help)) {
                    formatter.printHelp("Available Commands", options);
                } else if (cmd.hasOption(exit)) {
                    //TODO: Terminates the connection to the server
                    finished = true;
                } else if (cmd.hasOption(play_again)) {
                    gameManagerController.onLookForNewGames(userPlayer.getNickname(), stub);
                    finished = true;
                } else {
                    System.out.println("Unavailable command, remember to type '-' and the desired command");
                }
            } catch (ParseException pe) {
                System.out.println("Error parsing command-line arguments, invalid command");
                formatter.printHelp("Available Commands", options);
            }
        }
    }
    /**
     * Shows the current living room / board
     */
    @Override
    public void printLivingRoom() {
        AnsiConsole.systemInstall();
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
    /**
     * Shows the chat messages. It can show the full chat or the last five messages based on the player's choice
     * @param messages
     */
    @Override
    public void printChat(ArrayList<Pair<String, Pair<String,String>>> messages) {
        AnsiConsole.systemInstall();
        if(!messages.isEmpty()) {
            for (Pair<String, Pair<String,String>> p : messages) {
                if(p.getFirst().equals("All")){
                    System.out.println("[Public]  \033[1;37m" + p.getSecond().getFirst() + "\033[0m" + ": " + p.getSecond().getSecond()); //Names in bold white
                } else if (p.getFirst().equals(userPlayer.getNickname())) {
                    System.out.println("[Private]  \033[1;37m" + p.getSecond().getFirst() + "\033[0m" + ": " + p.getSecond().getSecond()); //Names in bold white
                }
            }
        }else{
            System.out.println("Chat is empty");
        }
    }
    /**
     * Shows all the players' current shelves
     */
    @Override
    public void printShelves(){
        AnsiConsole.systemInstall();
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
    /**
     * Shows error messages
     * @param error
     */
    @Override
    public void showErrorMessage(String error){
        System.err.println(error);
    }
    /**
     * Shows the final scoreboard, winner and the players' shelves
     * @param finalScoreBoard
     * @param winner
     * @param finalGameState
     */
    @Override
    public void printScoreBoard(ArrayList<Pair<String, Integer>> finalScoreBoard, String winner, GameStateType finalGameState){
        AnsiConsole.systemInstall();
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
        AnsiConsole.systemInstall();
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
    private boolean checkRuleDiagonal(ArrayList<Pair<Integer, Integer>> coordinates) {
        // Verifica che l'ArrayList contenga almeno 1 e al massimo 3 coordinate
        if (coordinates.size() < 1 || coordinates.size() > 3) {
            return false;
        }

        // Verifica che le coordinate siano valide
        int dim = 9; // Dimensione della matrice
        for (Pair<Integer, Integer> coord : coordinates) {
            int row = coord.getFirst();
            int col = coord.getSecond();
            if (row < 0 || row >= dim || col < 0 || col >= dim) {
                return false;
            }
        }

        // Se l'ArrayList contiene solo 1 carta, ritorna true (la regola è rispettata)
        if (coordinates.size() == 1) {
            return true;
        }

        // Se l'ArrayList contiene 2 o 3 carte, verifica se formano una retta orizzontale o verticale
        Pair<Integer, Integer> coord1 = coordinates.get(0);
        Pair<Integer, Integer> coord2 = coordinates.get(1);

        // Controlla se le carte formano una retta in orizzontale o verticale
        boolean isHorizontalLine = coord1.getFirst().equals(coord2.getFirst());
        boolean isVerticalLine = coord1.getSecond().equals(coord2.getSecond());

        // Controlla se le carte formano una linea retta
        if (!isHorizontalLine && !isVerticalLine) {
            return false;
        }

        // Controlla se le carte sono adiacenti e hanno almeno un lato libero
        boolean isAdjacentAndHasFreeSide = (
                Math.abs(coord1.getSecond() - coord2.getSecond()) <= 1 ||
                        Math.abs(coord1.getFirst() - coord2.getFirst()) <= 1
        );

        return isAdjacentAndHasFreeSide;
    }
    private boolean checkRule(ArrayList<Pair<Integer, Integer>> coordinates) {
        // Verifica che l'ArrayList contenga almeno 1 e al massimo 3 coordinate
        if (coordinates.size() < 1 || coordinates.size() > 3) {
            return false;
        }

        // Verifica che le coordinate siano valide
        int dim = 9; // Dimensione della matrice
        for (Pair<Integer, Integer> coord : coordinates) {
            int row = coord.getFirst();
            int col = coord.getSecond();
            if (row < 0 || row >= dim || col < 0 || col >= dim) {
                return false;
            }
        }

        // Se l'ArrayList contiene solo 1 carta, ritorna true (la regola è rispettata)
        if (coordinates.size() == 1) {
            return true;
        }

        // Se l'ArrayList contiene 2 o 3 carte, verifica se formano una retta
        Pair<Integer, Integer> coord1 = coordinates.get(0);
        Pair<Integer, Integer> coord2 = coordinates.get(1);

        // Controlla se le carte formano una retta in orizzontale o verticale
        boolean isHorizontalLine = true;
        boolean isVerticalLine = true;

        for (int i = 2; i < coordinates.size(); i++) {
            Pair<Integer, Integer> currentCoord = coordinates.get(i);
            if (!currentCoord.getFirst().equals(coord1.getFirst()) || !currentCoord.getFirst().equals(coord2.getFirst())) {
                isHorizontalLine = false;
            }
            if (!currentCoord.getSecond().equals(coord1.getSecond()) || !currentCoord.getSecond().equals(coord2.getSecond())) {
                isVerticalLine = false;
            }
        }

        return isHorizontalLine || isVerticalLine;
    }
    private boolean inRowTwo(Pair<Integer,Integer> coordA, Pair<Integer,Integer> coordB){
        int xA = coordA.getFirst();
        int yA = coordA.getSecond();
        int xB = coordB.getFirst();
        int yB = coordB.getSecond();

        if(xA == xB && (yA == yB+1 || yB == yA+1)){
            return true;
        }
        else return yA == yB && (xA == xB + 1 || xB == xA + 1);

    }
    private boolean inAdiacent(ArrayList<Pair<Integer, Integer>> coordinates){
        if (coordinates.size() == 1){
            return true;
        } else if (coordinates.size() == 2){
            return inRowTwo(coordinates.get(0),coordinates.get(1));
        } else if (coordinates.size() == 3){
            return inRowThree(coordinates.get(0),coordinates.get(1),coordinates.get(2));
        } else {
            return false;
        }
    }
    private boolean inRowThree(Pair<Integer,Integer> coordA, Pair<Integer,Integer> coordB, Pair<Integer,Integer> coordC){
        int xA = coordA.getFirst();
        int yA = coordA.getSecond();
        int xB = coordB.getFirst();
        int yB = coordB.getSecond();
        int xC = coordC.getFirst();
        int yC = coordC.getSecond();
        if(!((xA==xB && xB==xC) || (yA == yB && yB == yC)))
            return false;
        else {
            return inRowTwo(coordA,coordB) && inRowTwo(coordB,coordC);
        }

    }
}
