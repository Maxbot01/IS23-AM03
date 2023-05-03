package it.polimi.ingsw.client;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import it.polimi.ingsw.model.modelSupport.BoardCard;
import it.polimi.ingsw.model.modelSupport.Shelf;
import it.polimi.ingsw.model.modelSupport.enums.colorType;
import it.polimi.ingsw.model.modelSupport.enums.ornamentType;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static java.lang.System.out;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

public class DeserializeDynamicClassTest {


    @Test
    public void testDeserializeDynamicClass() {
        // Creazione dell'oggetto Shelf
        Shelf shelf = new Shelf();
        BoardCard[][] shelfCards = shelf.getShelfCards();
        shelfCards[5][0] = new BoardCard(colorType.BLUE, ornamentType.A);
        shelfCards[4][0] = new BoardCard(colorType.BLUE, ornamentType.A);
        shelfCards[3][0] = new BoardCard(colorType.BLUE, ornamentType.A);
        shelfCards[2][0] = new BoardCard(colorType.BLUE, ornamentType.A);
        shelfCards[1][0] = new BoardCard(colorType.BLUE, ornamentType.A);
        shelfCards[0][0] = new BoardCard(colorType.BLUE, ornamentType.A);

        // Conversione dell'oggetto Shelf in un oggetto JSON con "classe" e "oggetto"
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("classe", shelf.getClass().getName());
        jsonObject.add("oggetto", gson.toJsonTree(shelf));
        String json = jsonObject.toString();
        out.println(json);

        // Deserializzazione dell'oggetto JSON e verifica dell'istanza dell'oggetto originale
        JsonElement root = new JsonParser().parse(json);
        if (root.isJsonObject()) {
            JsonObject jsonObj = root.getAsJsonObject();
            String className = jsonObj.get("classe").getAsString();
            JsonElement jsonElement = jsonObj.get("oggetto");
            try {
                Class<?> clazz = Class.forName(className);
                Object instance = gson.fromJson(jsonElement, clazz);
                Assertions.assertEquals(Shelf.class, instance.getClass());
                Shelf deserializedShelf = (Shelf) instance;
                for (int i = 0; i < 6; i++) {
                    for (int j = 0; j < 5; j++) {
                        System.out.print(deserializedShelf.getCardAtPosition(i, j).getColor() + " ");
                    }
                    System.out.println();
                }
            } catch (ClassNotFoundException e) {
                fail("Classe non trovata: " + className);
            }
        } else {
            fail("Il JSON non è un oggetto");
        }
    }


}

