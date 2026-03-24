package client;

import model.AuthData;
import model.ListofGames;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.assertThrows;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void RegisterPositive() throws Exception {
        AuthData authData;
        authData = facade.register("RegisterPositive", "password", "p1@email.com");
        Assertions.assertTrue(authData.authToken().length() > 10);
    }

    @Test public void RegisterNegative() throws Exception {
        assertThrows(Exception.class, () -> facade.register(null, null, null));
    }

    @Test public void loginPositive() throws Exception {
        facade.register("loginPositive", "password", "p1@email.com");
        var authData = facade.login("loginPositive", "password");
        Assertions.assertTrue(authData.authToken().length() > 10);
    }
    @Test public void loginNegative() throws Exception {
        assertThrows(Exception.class, () -> facade.login("username_does_not_exist", "none"));
    }

    @Test public void createGamePositive() throws Exception {
        var authData = facade.register("createGamePositive", "password", "p1@email.com");
        Assertions.assertDoesNotThrow(() -> facade.createGame("hello", authData.authToken()));
    }

    @Test public void createGameNegative() throws Exception {
        var authData = facade.register("createGameNegative", "password", "p1@email.com");
        Assertions.assertDoesNotThrow(() -> facade.createGame("hello", authData.authToken()+"nope"));
    }

    @Test public void addToGamePositive() throws Exception {
        var authData = facade.register("addToGamePositive", "password", "p1@email.com");
        facade.createGame("addToGamePositive", authData.authToken());
        Assertions.assertDoesNotThrow(() -> facade.addToGame(1, "BLACK", authData.authToken()));
    }

    @Test public void addToGameNegative() throws Exception {
        var authData = facade.register("addToGameNegative", "password", "p1@email.com");
        facade.createGame("addToGameNegative", authData.authToken());
        Assertions.assertThrows(Exception.class, () -> facade.addToGame(15, "BLACK", authData.authToken()));
    }

    @Test public void listGamesPositive() throws Exception {
        var authData = facade.register("listGamesPositive", "password", "p1@email.com");
        facade.createGame("game1", authData.authToken());
        facade.createGame("game2", authData.authToken());
        facade.createGame("game3", authData.authToken());
        facade.createGame("game4", authData.authToken());
        ListofGames myList = facade.listGames(authData.authToken());
        Assertions.assertTrue(myList.games().size() >= 4);
    }

    @Test public void listGamesNegative() throws Exception {
        var authData = facade.register("listGamesNegative", "password", "p1@email.com");
        facade.createGame("game5", authData.authToken());
        facade.createGame("game6", authData.authToken());
        facade.createGame("game7", authData.authToken());
        facade.createGame("game8", authData.authToken());
        Assertions.assertThrows(Exception.class, () -> facade.listGames(authData.authToken()+"not authorized"));
    }

    @Test public void logoutPositive() throws Exception {
        var authData = facade.register("logoutPositive", "password", "p1@email.com");
        Assertions.assertDoesNotThrow(() -> facade.logout(authData.authToken()));
    }

    @Test public void logoutNegative() throws Exception {
        var authData = facade.register("logoutNegative", "password", "p1@email.com");
        Assertions.assertThrows(Exception.class, () -> facade.logout(authData.authToken()+"not an authToken"));
        facade.logout(authData.authToken());
        Assertions.assertThrows(Exception.class, () -> facade.logout(authData.authToken()));
    }
}
