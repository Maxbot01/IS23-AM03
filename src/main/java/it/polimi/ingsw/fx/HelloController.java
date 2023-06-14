package it.polimi.ingsw.fx;

import it.polimi.ingsw.client.ClientManager;
import it.polimi.ingsw.model.CommonGoals.CommonGoals;
import it.polimi.ingsw.model.CommonGoals.Strategy.*;
import it.polimi.ingsw.model.GameStateType;
import it.polimi.ingsw.model.helpers.Pair;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.PersonalGoal;
import it.polimi.ingsw.view.UpdateHandler;
import it.polimi.ingsw.model.modelSupport.LivingRoom;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.plaf.synth.ColorType;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class HelloController {
    /*@FXML
    private Label welcomeText;
    @FXML
    public ImageView shelf3;

    @FXML
    public Label player1;
    @FXML
    public Label player2;
    @FXML
    public Label player3;
    @FXML
    public Label player4;
    @FXML
    public ImageView common1;
    @FXML
    public ImageView common2;
    @FXML
    public GridPane grid;
    @FXML
    public GridPane gridshelf1;
    @FXML
    public GridPane gridshelf2;
    @FXML
    public GridPane gridshelf3;
    @FXML
    public GridPane gridshelf4;
    @FXML
    public GridPane commongrid;
    @FXML
    public GridPane personalgrid;
    @FXML
    public Button trophyButton;


    public void mostraGoals(javafx.event.ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("myshelfie/Common_Goals.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
            stage.setFullScreen(false);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void returnHome(javafx.event.ActionEvent actionEvent) {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("myshelfie/main_view.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load());
            Stage stage = new Stage();
            stage.setTitle("Hello!");
            stage.setScene(scene);
            stage.show();
            stage.setFullScreen(false);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void swap(javafx.event.ActionEvent actionEvent) {
    }


    public void increase(MouseEvent mouseEvent) {
        shelf3.setFitHeight(300);
        shelf3.setFitWidth(300);
        shelf3.toFront();
    }

    public void decrease(MouseEvent mouseEvent) {
        shelf3.setFitWidth(180);
        shelf3.setFitWidth(180);
    }

    UpdateHandler handler = new UpdateHandler() {

        @Override
        public void initializeGame(List<String> players, CommonGoals commonGoals, HashMap<String, PersonalGoal> personalGoals, BoardCard[][] livingRoom, Boolean[][] selectables, ArrayList<Pair<String, BoardCard[][]>> playersShelves, HashMap<String, Integer> playersPoints, GameStateType gameState) throws IOException {

            for (int p = 0; p < players.size(); p++) {
                String name = players.get(p);
                if (name == ClientManager.userNickname) {
                    int y = labels.get(p).getLayoutY;
                    int x = labels.get(p).getLayoutX;
                    labels.get[p].setOpacity(0);
                    String folder = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goals_cards\\PersonalGoals";
                    String target = personalGoals.get(p).getSelectedGoal().toString();
                    Image image = new Image(folder + target);
                    ImageView personalview = new ImageView();
                    personalview.setFitWidth(200);
                    personalview.setFitHeight(200);
                    personalview.setLayoutY(y);
                    personalview.setLayoutX(x);
                    personalview.setImage(image);
                    personalgrid.setLayoutY(y);
                    PersonalGoal goal = personalGoals.get(name);

                } else labels.get[p].setText(name);
            }

            CommonGoalStrategy first = commonGoals.getFirstGoal();
            CommonGoalStrategy second = commonGoals.getSecondGoal();
            String string = null;
            int index1 = 0;
            int index2 = 0;

            if (first instanceof Double2x2GoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\1.jpg";
                index1 = 1;
            if (first instanceof EightTilesGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\2.jpg";
                index1 = 2;
            if (first instanceof FiveDiagonalGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\3.jpg";
                index1 = 3;
            if (first instanceof FiveXGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\4.jpg";
                index1 = 4;
            if (first instanceof FourCornersGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\5.jpg";
                index1 = 5;
            if (first instanceof FourLines3DiffGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\6.jpg";
                index1 = 6;
            if (first instanceof FourOfFourGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\7.jpg";
                index1 = 7;
            if (first instanceof SixOfTwoGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\8.jpg";
                index1 = 8;
            if (first instanceof ThreeColumns3DiffGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\9.jpg";
                index1 = 9;
            if (first instanceof TriangularGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\10.jpg";
                index1 = 10;
            if (first instanceof TwoOf5DiffGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\11.jpg";
                index1 = 11;
            if (first instanceof TwoOf6DiffGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\12.jpg";
                index1 = 12;


            if (second instanceof Double2x2GoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\1.jpg";
                index2 = 1;
            if (second instanceof EightTilesGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\2.jpg";
                index2 = 2;
            if (second instanceof FiveDiagonalGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\3.jpg";
                index2 = 3;
            if (second instanceof FiveXGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\4.jpg";
                index2 = 4;
            if (second instanceof FourCornersGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\5.jpg";
                index2 = 5;
            if (second instanceof FourLines3DiffGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\6.jpg";
                index2 = 6;
            if (second instanceof FourOfFourGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\7.jpg";
                index2 = 7;
            if (second instanceof SixOfTwoGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\8.jpg";
                index2 = 8;
            if (second instanceof ThreeColumns3DiffGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\9.jpg";
                index2 = 9;
            if (second instanceof TriangularGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\10.jpg";
                index2 = 10;
            if (second instanceof TwoOf5DiffGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\11.jpg";
                index2 = 11;
            if (second instanceof TwoOf6DiffGoalStrategy)
                //string = "C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\common_goal_cards\\12.jpg";
                index2 = 12;
            commongrid.getChildren().get(index2).setOpacity(1);

            for (int i = 0; i < grid.getRowCount(); i++) {
                for (int j = 0; j < grid.getColumnCount(); j++) {
                    colorType color = livingRoom[i][j].getColor();
                    ornamentType ornament = livingRoom[i][j].getOrnament();
                    Image image = new Image(scegliBoardCard(color, ornament));
                    ImageView view = new ImageView();
                    view.setFitHeight(62);
                    view.setFitWidth(62);
                    view.setImage(image);
                    view.setOnMouseClicked(select(i,j);
                    grid.add(view, i, j);
                }
            }

        }

        public EventHandler
                <? super MouseEvent> select(int i, int j) {
            Pair<Integer,Integer> pair = new Pair<>(i,j);
            ArrayList<BoardCard> selected = new ArrayList<>();
            BoardCard card = new BoardCard(color,ornament);
            selected.add(card);
            ClientManager.gameController.onSelectedCards(selected, ClientManager.userNickname);

            return null;
        }



        @Override
        public void updateMatchAfterSelectedCards(BoardCard[][] livingRoom, Boolean[][] selectables, GameStateType gameState) {
            for (int i = 0; i < grid.getRowCount(); i++) {
                for (int j = 0; j < grid.getColumnCount(); j++) {
                    colorType color = livingRoom[i][j].getColor();
                    ornamentType ornament = livingRoom[i][j].getOrnament();
                    Image image = new Image(scegliBoardCard(color, ornament));
                    ImageView view = new ImageView();
                    view.setFitHeight(62);
                    view.setFitWidth(62);
                    view.setImage(image);
                    grid.add(view, i, j);
                }
            }
        }

        @Override
        public void updateMatchAfterSelectedColumn(BoardCard[][] livingRoom, Boolean[][] selectables, GameStateType gameState, Pair<String, Integer> updatedPlayerPoints, Pair<String, BoardCard[][]> updatedPlayerShelf) {

          //  TODO: capire di chi è il turno;

            for (int i = 0; i < grid.getRowCount(); i++) {
                for (int j = 0; j < grid.getColumnCount(); j++) {
                    colorType color = livingRoom[i][j].getColor();
                    ornamentType ornament = livingRoom[i][j].getOrnament();
                    Image image = new Image(scegliBoardCard(color, ornament));
                    ImageView view = new ImageView();
                    view.setFitHeight(62);
                    view.setFitWidth(62);
                    view.setImage(image);
                    grid.add(view, i, j);
                }
            }
        }

                @Override
                public void waitingCommands () {

                }

                @Override
                public void requestCredentials () {

                }

                @Override
                public void launchGameManager (HashMap < String, List < String >> availableGames){

                }

                @Override
                public void launchGameLobby (String gameId, ArrayList < String > players, String host){

                }

                @Override
                public void chooseCards () {

                }

                @Override
                public void chooseColumn () {

                }

                @Override
                public void endCommands () {

                }

                @Override
                public void printLivingRoom () {

                }

                @Override
                public void printShelves () {

                }

                @Override
                public void showErrorMessage (String error){

                }

                @Override
                public void showPlayingPlayer (String playingPlayer){

                }

                @Override
                public void printScoreBoard (ArrayList < Pair < String, Integer >> finalScoreBoard, String winner, GameStateType gameState){

                }
            } ;

*/
   /* int trovaindex(int i, int j){
        int range = 0;
        int bonus = 3;
        switch (j){
            case(0):
                range = 0;
                bonus = 3;
            case(1):
                range = 2;
                bonus = 3;
            case(2):
                range = 5;
                bonus = 2;
            case(3):
                range = 10;
                bonus = 1;
            case(4):
                range = 18;
                bonus = 0;
            case(5):
                range = 27;
                bonus = 0;
            case(6):
                range = 36;
                bonus = 0;
            case(7):
                range = 41;
                bonus = 2;
            case(8):
                range = 44;
                bonus = 4;
        }
        int index = range + bonus + i;
        return index;
    }

    */

/*        }

        public String scegliBoardCard(colorType color, ornamentType ornament) {
            String start = new String("C:\\Users\\Maxbot\\IdeaProjects\\IS23-AM03\\src\\main\\resources\\it\\polimi\\ingsw\\fx\\myshelfie\\item_tiles\\");
            String middle = new String();
            String finish = new String();
            if (color.equals(colorType.BLUE))
                middle = "Cornici";
            else if (color.equals(colorType.GREEN))
                middle = "Gatti";
            else if (color.equals(colorType.YELLOW))
                middle = "Giochi";
            else if (color.equals(colorType.WHITE))
                middle = "Libri";
            else if (color.equals(colorType.PURPLE))
                middle = "Piante";
            else if (color.equals(colorType.LIGHT_BLUE))
                middle = "Trofei";

            if (ornament.equals(ornamentType.A))
                finish = ".1";
            if (ornament.equals(ornamentType.B))
                finish = ".2";
            if (ornament.equals(ornamentType.C))
                finish = ".3";

            String scelta = start + middle + finish;
            return scelta;

        }
    };*/
}

