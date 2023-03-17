package it.polimi.ingsw.model.modelSupport;
import java.util.ArrayList;
import java.util.List;


public class LivingRoom{

    private BoardCard [][] pieces;
    private List<BoardCard> allCards = new ArrayList<BoardCard>;
    private int indexOfDeckCard;
    final int DIM = 9;

    LivingRoom lr = new LivingRoom(int NumOfPLayers){

        shuffle();
        BoardCard[][] pieces = new BoardCard[DIM][DIM];
        if (NumOfPLayers == 2){
            for (int i = 1; i < righe; i++) {
            for (int j = 0; j < colonne; j++) {
                matrice[i][j] = rand.nextInt(100); // generare numeri casuali tra 0 e 99
            }
        }





    }
    BoardCard[] shuffle(){
                // Definisci la dimensione dell'array
                int size = 10;

                // Crea un array di interi
                int[] array = new int[size];

                // Crea un oggetto Random per generare numeri casuali
                Random random = new Random();

                // Popola l'array con numeri casuali
                for (int i = 0; i < size; i++) {
                    array[i] = random.nextInt(100); // Genera un numero casuale tra 0 e 99
                }

                // Stampa l'array
                for (int i = 0; i < size; i++) {
                    if (array[i] < 22)

                }
            }
        }
        }
    }
    BoardCard[][] getPieces() {
        return pieces;
    }

    void refillBoard(int NumOfPlayers){

    }

    void updateBoard(ArrayList<Pair<int,int>>){

    }
    Boolean[][] calculateSelectable(){

    }








}


