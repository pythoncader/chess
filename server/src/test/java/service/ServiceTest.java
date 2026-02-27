package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import server.Service;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ServiceTest {
    MemoryUserDAO dataAccessObject = new MemoryUserDAO();
    Service serviceObject = new Service(dataAccessObject);

    @BeforeEach
    void setUp() throws DataAccessException {
        serviceObject.clear();
    }

    @Test
    void registerPositive() throws DataAccessException {
        AuthData authToken = serviceObject.register(new UserData("Cade", "Rigby", "1@1.com"));
        assertTrue(dataAccessObject.getAuthTokens().containsKey(authToken.authToken()));
        assertTrue(dataAccessObject.getUsers().containsKey("Cade"));
        assertEquals("Rigby", dataAccessObject.getUsers().get("Cade").password());
    }

    @Test
    void registerNegative() throws DataAccessException {
        // user does not provide a username
        DataAccessException exception = assertThrows(DataAccessException.class, () -> serviceObject.register(new UserData("", "Rigby", "1@1.com")));
        assertEquals("Error: bad request", exception.getMessage());

        //user does not provide a password
        exception = assertThrows(DataAccessException.class, () -> serviceObject.register(new UserData("Cade", "", "1@1.com")));
        assertEquals("Error: bad request", exception.getMessage());

        //user does not provide an email
        exception = assertThrows(DataAccessException.class, () -> serviceObject.register(new UserData("Cade", "hello", "")));
        assertEquals("Error: bad request", exception.getMessage());

        serviceObject.clear();
        // duplicated username
        serviceObject.register(new UserData("User1", "hello", "123@123.com"));
        exception = assertThrows(DataAccessException.class, () -> serviceObject.register(new UserData("User1", "hello2", "different_email@123.com")));
        assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    void loginPositive() throws DataAccessException{
        serviceObject.register(new UserData("Cade", "hello", "different_email@123.com"));

        //user does not provide a password
        AuthData authToken = serviceObject.login(new UserData("Cade", "hello", null));
        assertTrue(dataAccessObject.getAuthTokens().containsKey(authToken.authToken()));
    }

    @Test
    void loginNegative() throws DataAccessException{
        serviceObject.register(new UserData("Cade", "hello", "different_email@123.com"));

        // user does not provide a username
        DataAccessException exception = assertThrows(DataAccessException.class, () -> serviceObject.login(new UserData("", "Rigby", null)));
        assertEquals("Error: bad request", exception.getMessage());

        //user does not provide a password
        exception = assertThrows(DataAccessException.class, () -> serviceObject.login(new UserData("Cade", "", null)));
        assertEquals("Error: bad request", exception.getMessage());

        //user provides incorrect username
        exception = assertThrows(DataAccessException.class, () -> serviceObject.login(new UserData("Cade2", "hello", null)));
        assertEquals("Error: unauthorized", exception.getMessage());

        exception = assertThrows(DataAccessException.class, () -> serviceObject.login(new UserData("Cade", "hello2", null)));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void logoutPositive() throws DataAccessException{
        AuthData authToken = serviceObject.register(new UserData("Cade", "hello", "different_email@123.com"));
        serviceObject.logout(new LogoutRequest(authToken.authToken()));
        assertFalse(dataAccessObject.getAuthTokens().containsKey(authToken.authToken()));
    }

    @Test
    void logoutNegative() {
        DataAccessException exception = assertThrows(DataAccessException.class, () -> serviceObject.logout(new LogoutRequest("abe827a1-8307-40f8-80ef-7055c9575380")));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void clear() throws DataAccessException {
        dataAccessObject.createUser(new UserData("test", "test_password", "test@test.com"));
        String testToken = dataAccessObject.addAuthToken("test");
        dataAccessObject.makeNewGame("helloworld", testToken);
        serviceObject.clear();
        DataAccessException exception = assertThrows(DataAccessException.class, () -> dataAccessObject.loginUser("test", "test_password"));
        assertEquals("Error: unauthorized", exception.getMessage());

        testToken = dataAccessObject.addAuthToken("test");
        assertEquals(new ArrayList<GameData>(), dataAccessObject.listGames(testToken));
    }

    @Test
    void newGamePositive() throws DataAccessException{
        AuthData authToken = serviceObject.register(new UserData("Cade", "hello", "different_email@123.com"));
        GameID id = serviceObject.newGame(new GameRequest("my_game", authToken.authToken()));
        assertTrue(dataAccessObject.getChessGames().containsKey(id.gameID()));
        assertEquals("my_game", dataAccessObject.getChessGames().get(id.gameID()).gameName());
    }

    @Test
    void newGameNegative() throws DataAccessException{
        AuthData authToken = serviceObject.register(new UserData("Cade", "hello", "different_email@123.com"));
        // user does not provide a name for the game
        DataAccessException exception = assertThrows(DataAccessException.class, () -> serviceObject.newGame(new GameRequest(null, authToken.authToken())));
        assertEquals("Error: bad request", exception.getMessage());

        // user provides incorrect authToken
        exception = assertThrows(DataAccessException.class, () -> serviceObject.newGame(new GameRequest("game_name", "no token")));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void listGamesPositive() throws DataAccessException{
        AuthData authToken = serviceObject.register(new UserData("Cade", "hello", "different_email@123.com"));
        ListofGames myList = serviceObject.listGames(new ListRequest(authToken.authToken()));
        assertEquals(new ArrayList<GameData>(), myList.games());

        GameID id = serviceObject.newGame(new GameRequest("new game", authToken.authToken()));
        myList = serviceObject.listGames(new ListRequest(authToken.authToken()));
        ArrayList<GameData> expected = new ArrayList<>();
        expected.add(new GameData(id.gameID(), null, null, "new game", null));
        assertEquals(expected, myList.games());
    }

    @Test
    void listGamesNegative() throws DataAccessException{
        serviceObject.register(new UserData("Cade", "hello", "different_email@123.com"));
        DataAccessException exception = assertThrows(DataAccessException.class, () -> serviceObject.listGames(new ListRequest("no authentication")));
        assertEquals("Error: unauthorized", exception.getMessage());

    }

    @Test
    void joinPositive() throws DataAccessException{
        AuthData authToken = serviceObject.register(new UserData("Cade", "hello", "different_email@123.com"));
        AuthData authToken2 = serviceObject.register(new UserData("Jeremy", "hello", "different_email@123.com"));
        GameID id = serviceObject.newGame(new GameRequest("my_game", authToken.authToken()));
        serviceObject.join(new JoinRequest(authToken.authToken(), "WHITE", id.gameID()));
        serviceObject.join(new JoinRequest(authToken2.authToken(), "BLACK", id.gameID()));

        ListofGames myList = serviceObject.listGames(new ListRequest(authToken.authToken()));
        ArrayList<GameData> expected = new ArrayList<>();
        expected.add(new GameData(id.gameID(), "Cade", "Jeremy", "my_game", null));
        assertEquals(expected, myList.games());
    }

    @Test
    void joinNegative() throws DataAccessException{
        AuthData authToken = serviceObject.register(new UserData("Cade", "hello", "different_email@123.com"));
        AuthData authToken2 = serviceObject.register(new UserData("Jeremy", "hello", "different_email@123.com"));
        GameID id = serviceObject.newGame(new GameRequest("my_game", authToken.authToken()));
        serviceObject.join(new JoinRequest(authToken.authToken(), "WHITE", id.gameID()));

        // request to join in a place that is already taken
        DataAccessException exception = assertThrows(DataAccessException.class, () -> serviceObject.join(new JoinRequest(authToken2.authToken(), "WHITE", id.gameID())));
        assertEquals("Error: already taken", exception.getMessage());

        // request to join without authorization
        exception = assertThrows(DataAccessException.class, () -> serviceObject.join(new JoinRequest("no authentication", "BLACK", id.gameID())));
        assertEquals("Error: unauthorized", exception.getMessage());

        // request to join without a valid player color
        exception = assertThrows(DataAccessException.class, () -> serviceObject.join(new JoinRequest(authToken2.authToken(), null, id.gameID())));
        assertEquals("Error: bad request", exception.getMessage());

        // request to join without an invalid game ID
        exception = assertThrows(DataAccessException.class, () -> serviceObject.join(new JoinRequest(authToken2.authToken(), "BLACK", 0)));
        assertEquals("Error: bad request", exception.getMessage());
    }
}