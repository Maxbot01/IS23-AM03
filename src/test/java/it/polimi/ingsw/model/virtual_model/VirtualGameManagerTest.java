package it.polimi.ingsw.model.virtual_model;

import it.polimi.ingsw.server.VirtualGameManagerSerializer;
import it.polimi.ingsw.model.helpers.Pair;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class VirtualGameManagerTest {

    @Test
    void pingTest() {
        VirtualGameManager virtualGameManager = new VirtualGameManager();
        virtualGameManager.ping();
        VirtualGameManagerSerializer expectedSerializedGameManager = new VirtualGameManagerSerializer("ping", new Object[]{});
        String expectedJson = VirtualGameManagerSerializer.serializeMethod(expectedSerializedGameManager);
        System.out.println(expectedJson);
    }

    @Test
    void setCredentialsTest() {
        VirtualGameManager virtualGameManager = new VirtualGameManager();
        virtualGameManager.setCredentials("username", "password");
        VirtualGameManagerSerializer expectedSerializedGameManager = new VirtualGameManagerSerializer("setCredentials", new Object[]{"username", "password"});
        String expectedJson = VirtualGameManagerSerializer.serializeMethod(expectedSerializedGameManager);
        System.out.println(expectedJson);
    }

    @Test
    void selectGameTest() {
        VirtualGameManager virtualGameManager = new VirtualGameManager();
        virtualGameManager.selectGame("gameID", "user");
        VirtualGameManagerSerializer expectedSerializedGameManager = new VirtualGameManagerSerializer("selectGame", new Object[]{"gameID", "user"});
        String expectedJson = VirtualGameManagerSerializer.serializeMethod(expectedSerializedGameManager);
        System.out.println(expectedJson);
    }

    @Test
    void createGameTest() {
        VirtualGameManager virtualGameManager = new VirtualGameManager();
        virtualGameManager.createGame(3, "user");
        VirtualGameManagerSerializer expectedSerializedGameManager = new VirtualGameManagerSerializer("createGame", new Object[]{3, "user"});
        String expectedJson = VirtualGameManagerSerializer.serializeMethod(expectedSerializedGameManager);
        System.out.println(expectedJson);
    }

    @Test
    void sendAckTest() {
        VirtualGameManager virtualGameManager = new VirtualGameManager();
        virtualGameManager.sendAck();
        VirtualGameManagerSerializer expectedSerializedGameManager = new VirtualGameManagerSerializer("sendAck", new Object[]{});
        String expectedJson = VirtualGameManagerSerializer.serializeMethod(expectedSerializedGameManager);
        System.out.println(expectedJson);
    }

    @Test
    void startMatchTest() {
        VirtualGameManager virtualGameManager = new VirtualGameManager();
        virtualGameManager.startMatch("ID", "user");
        VirtualGameManagerSerializer expectedSerializedGameManager = new VirtualGameManagerSerializer("startMatch", new Object[]{"ID", "user"});
        String expectedJson = VirtualGameManagerSerializer.serializeMethod(expectedSerializedGameManager);
        // print
        System.out.println(expectedJson);
    }

    @Test
    void selectedCardsTest() {
        VirtualGameManager virtualGameManager = new VirtualGameManager();
        ArrayList<Pair<Integer, Integer>> selected = new ArrayList<>();
        selected.add(new Pair<>(1, 2));
        selected.add(new Pair<>(3, 4));
        virtualGameManager.selectedCards(selected, "user", "gameID");
        VirtualGameManagerSerializer expectedSerializedGameManager = new VirtualGameManagerSerializer("selectedCards", new Object[]{selected, "user", "gameID"});
        String expectedJson = VirtualGameManagerSerializer.serializeMethod(expectedSerializedGameManager);
        System.out.println(expectedJson);

    }
}
