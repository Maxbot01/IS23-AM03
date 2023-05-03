package it.polimi.ingsw.client;

import it.polimi.ingsw.client.SerializeDynamicClass;
import it.polimi.ingsw.model.modelSupport.Shelf;
import org.junit.Test;

import static java.lang.System.out;
import static org.junit.Assert.*;

public class SerializeDynamicClassTest {

    @Test
    public void testToJson() {
        Shelf shelf = new Shelf();
        String json = SerializeDynamicClass.toJson(shelf);
        assertNotNull(json);
        assertTrue(json.contains("classe"));
        assertTrue(json.contains("oggetto"));
        out.println(json);

    }
}
