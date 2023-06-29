package it.polimi.ingsw.model.modelSupport;

import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;

import java.util.Random;

public class RandomShelfTest {
    public static void main(String[] args) {
        // Creazione di una shelf casuale
        Shelf shelf = createRandomShelf();

        // Stampa della shelf
        System.out.println("Shelf:");
        printShelf(shelf);

        // Calcolo degli adiacentpoints
        int adiacentPoints = shelf.calculateAdiacentPoints();
        System.out.println("Adiacent Points: " + adiacentPoints);
//        System.out.println("Adiacent Rick: " + shelf.calculateAdiacentPointsRick());
    }

    private static Shelf createRandomShelf() {
        Shelf shelf = new Shelf();
        Random random = new Random();

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                // Generazione casuale del colore e dell'ornamento per la carta
                colorType color;
                ornamentType ornament;

                do {
                    color = colorType.values()[random.nextInt(colorType.values().length)];
                    ornament = ornamentType.values()[random.nextInt(ornamentType.values().length)];
                } while (color == colorType.TOMBSTONE || color == colorType.EMPTY_SPOT);

                // Creazione e inserimento della carta nella shelf
                BoardCard card = new BoardCard(color, ornament);
                shelf.getShelfCards()[row][col] = card;
            }
        }

//        shelf.getShelfCards()[0][0] = new BoardCard(colorType.EMPTY_SPOT, ornamentType.A);
//        shelf.getShelfCards()[0][1] = new BoardCard(colorType.PURPLE, ornamentType.A);
//        shelf.getShelfCards()[0][2] = new BoardCard(colorType.GREEN, ornamentType.A);
//        shelf.getShelfCards()[0][3] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
//        shelf.getShelfCards()[0][4] = new BoardCard(colorType.GREEN, ornamentType.A);
//        shelf.getShelfCards()[1][0] = new BoardCard(colorType.YELLOW, ornamentType.A);
//        shelf.getShelfCards()[1][1] = new BoardCard(colorType.PURPLE, ornamentType.A);
//        shelf.getShelfCards()[1][2] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
//        shelf.getShelfCards()[1][3] = new BoardCard(colorType.GREEN, ornamentType.A);
//        shelf.getShelfCards()[1][4] = new BoardCard(colorType.YELLOW, ornamentType.A);
//        shelf.getShelfCards()[2][0] = new BoardCard(colorType.PURPLE, ornamentType.A);
//        shelf.getShelfCards()[2][1] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
//        shelf.getShelfCards()[2][2] = new BoardCard(colorType.YELLOW, ornamentType.A);
//        shelf.getShelfCards()[2][3] = new BoardCard(colorType.YELLOW, ornamentType.A);
//        shelf.getShelfCards()[2][4] = new BoardCard(colorType.YELLOW, ornamentType.A);
//        shelf.getShelfCards()[3][0] = new BoardCard(colorType.PURPLE, ornamentType.A);
//        shelf.getShelfCards()[3][1] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
//        shelf.getShelfCards()[3][2] = new BoardCard(colorType.BLUE, ornamentType.A);
//        shelf.getShelfCards()[3][3] = new BoardCard(colorType.PURPLE, ornamentType.A);
//        shelf.getShelfCards()[3][4] = new BoardCard(colorType.PURPLE, ornamentType.A);
//        shelf.getShelfCards()[4][0] = new BoardCard(colorType.WHITE, ornamentType.A);
//        shelf.getShelfCards()[4][1] = new BoardCard(colorType.GREEN, ornamentType.A);
//        shelf.getShelfCards()[4][2] = new BoardCard(colorType.PURPLE, ornamentType.A);
//        shelf.getShelfCards()[4][3] = new BoardCard(colorType.YELLOW, ornamentType.A);
//        shelf.getShelfCards()[4][4] = new BoardCard(colorType.YELLOW, ornamentType.A);
//        shelf.getShelfCards()[5][0] = new BoardCard(colorType.LIGHT_BLUE, ornamentType.A);
//        shelf.getShelfCards()[5][1] = new BoardCard(colorType.BLUE, ornamentType.A);
//        shelf.getShelfCards()[5][2] = new BoardCard(colorType.WHITE, ornamentType.A);
//        shelf.getShelfCards()[5][3] = new BoardCard(colorType.YELLOW, ornamentType.A);
//        shelf.getShelfCards()[5][4] = new BoardCard(colorType.YELLOW, ornamentType.A);

        return shelf;
    }


    private static void printShelf(Shelf shelf) {
        BoardCard[][] shelfCards = shelf.getShelfCards();

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                BoardCard card = shelfCards[row][col];
                String colorCode = getColorCode(card.getColor());

                // Stampa la carta con il colore corrispondente
                System.out.print(colorCode + "\u001B[0m ");
            }
            System.out.println();
        }
    }

    private static String getColorCode(colorType color) {
        switch (color) {
            case YELLOW:
                return "Y"; // Giallo
            case GREEN:
                return "G"; // Verde
            case BLUE:
                return "B"; // Blu
            case PURPLE:
                return "P"; // Viola
            case LIGHT_BLUE:
                return "L"; // Blu chiaro
            case WHITE:
                return "W"; // Bianco
            case EMPTY_SPOT:
                return "E"; // Grigio (colore predefinito)
            case TOMBSTONE:
                return "T"; // Grigio (colore predefinito)
        }
        return null;
    }



}
