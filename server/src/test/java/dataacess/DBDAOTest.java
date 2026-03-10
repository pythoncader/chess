package dataacess;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.DatabaseUserDAO;
import model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class DBDAOTest {
    DatabaseUserDAO dataAccessObject = new DatabaseUserDAO();

    DBDAOTest() throws DataAccessException {
    }

    @BeforeEach
    void setUp() throws DataAccessException {
        dataAccessObject.clear();
    }

    private boolean isInTable(Object value, String columnName, String tableName) throws DataAccessException{
        if ((!Objects.equals(tableName, "chessGames") && !Objects.equals(tableName, "users") && !Objects.equals(tableName, "authTokens"))){
            throw new DataAccessException("Invalid table name", 500);
        }
        var statement = "SELECT COUNT(*) FROM "+tableName+" WHERE "+columnName+" = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                if (value instanceof String) {
                    ps.setString(1, (String)value);
                } else if (value instanceof Integer) {
                    ps.setInt(1, (int)value);
                }
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                } else {
                    throw new DataAccessException("There was a problem accessing the database", 500);
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }

    private boolean checkPassword(String username, String password) throws DataAccessException {
        var statement = "SELECT username, password FROM users WHERE username=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                String hashedPassword = rs.getString("password");
                return BCrypt.checkpw(password, hashedPassword);
            } else {
                throw new DataAccessException("There was a problem accessing the database", 500);
            }

        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }

    @Test
    void registerPositive() throws DataAccessException {
        String authToken = dataAccessObject.createUser(new UserData("Cade", "Rigby", "1@1.com"));
        assertTrue(isInTable(authToken, "authToken", "authTokens"));
        assertTrue(isInTable("Cade", "username", "authTokens"));
        assertTrue(checkPassword("Cade", "Rigby"));
    }

    @Test
    void registerNegative() throws DataAccessException {
        // user does not provide a username
        DataAccessException exception = assertThrows(DataAccessException.class, () -> dataAccessObject.createUser(new UserData("", "Rigby", "1@1.com")));
        assertEquals("Error: bad request", exception.getMessage());

        //user does not provide a password
        exception = assertThrows(DataAccessException.class, () -> dataAccessObject.createUser(new UserData("Cade", "", "1@1.com")));
        assertEquals("Error: bad request", exception.getMessage());

        //user does not provide an email
        exception = assertThrows(DataAccessException.class, () -> dataAccessObject.createUser(new UserData("Cade", "hello", "")));
        assertEquals("Error: bad request", exception.getMessage());

        dataAccessObject.clear();
        // duplicated username
        dataAccessObject.createUser(new UserData("User1", "hello", "123@123.com"));
        exception = assertThrows(DataAccessException.class, () -> dataAccessObject.createUser(new UserData("User1", "hello2", "different_email@123.com")));
        assertEquals("Error: already taken", exception.getMessage());
    }

    @Test
    void loginPositive() throws DataAccessException{
        dataAccessObject.createUser(new UserData("Cade", "hello", "different_email@123.com"));

        String authToken = dataAccessObject.loginUser("Cade", "hello");
        assertTrue(isInTable(authToken, "authToken", "authTokens"));
    }

    @Test
    void loginNegative() throws DataAccessException{
        dataAccessObject.createUser(new UserData("Cade", "hello", "different_email@123.com"));

        // user does not provide a username
        DataAccessException exception = assertThrows(DataAccessException.class, () -> dataAccessObject.loginUser("", "Rigby"));
        assertEquals("Error: bad request", exception.getMessage());

        //user does not provide a password
        exception = assertThrows(DataAccessException.class, () -> dataAccessObject.loginUser("Cade", ""));
        assertEquals("Error: bad request", exception.getMessage());

        //user provides incorrect username
        exception = assertThrows(DataAccessException.class, () -> dataAccessObject.loginUser("Cade2", "hello"));
        assertEquals("Error: unauthorized", exception.getMessage());

        exception = assertThrows(DataAccessException.class, () -> dataAccessObject.loginUser("Cade", "hello2"));
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void logoutPositive() throws DataAccessException{
        String authToken = dataAccessObject.createUser(new UserData("Cade", "hello", "different_email@123.com"));
        dataAccessObject.deleteAuthToken(authToken);
        assertFalse(isInTable(authToken, "authToken", "authTokens"));
    }

    @Test
    void logoutNegative() {
        DataAccessException exception = assertThrows(
                DataAccessException.class, () -> dataAccessObject.deleteAuthToken(
                        "abe827a1-8307-40f8-80ef-7055c9575380"
                )
        );
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void clear() throws DataAccessException {
        dataAccessObject.createUser(new UserData("test", "test_password", "test@test.com"));
        String testToken = dataAccessObject.addAuthToken("test");
        dataAccessObject.makeNewGame("helloworld", testToken);
        dataAccessObject.clear();
        DataAccessException exception = assertThrows(DataAccessException.class, () -> dataAccessObject.loginUser("test", "test_password"));
        assertEquals("Error: unauthorized", exception.getMessage());

        testToken = dataAccessObject.addAuthToken("test");
        assertEquals(new ArrayList<GameData>(), dataAccessObject.listGames(testToken));
    }

    private String getChessGame(int gameID) throws DataAccessException {
        var statement = "SELECT name FROM chessGames WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement)) {
            ps.setInt(1, gameID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getString("name");
            } else {
                throw new DataAccessException("There was a problem accessing the database", 500);
            }

        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }

    @Test
    void newGamePositive() throws DataAccessException{
        String authToken = dataAccessObject.createUser(new UserData("Cade", "hello", "different_email@123.com"));
        int id = dataAccessObject.makeNewGame("my_game", authToken);
        assertTrue(isInTable(id, "id", "chessGames"));
        assertEquals("my_game", getChessGame(id));
    }

    @Test
    void newGameNegative() throws DataAccessException{
        String authToken = dataAccessObject.createUser(new UserData("Cade", "hello", "different_email@123.com"));
        // user does not provide a name for the game
        DataAccessException exception = assertThrows(
                DataAccessException.class, () -> dataAccessObject.makeNewGame(null, authToken)
        );
        assertEquals("Error: bad request", exception.getMessage());

        // user provides incorrect authToken
        exception = assertThrows(DataAccessException.class, () -> dataAccessObject.makeNewGame(
                "game_name", "no token"
            )
        );
        assertEquals("Error: unauthorized", exception.getMessage());
    }

    @Test
    void listGamesPositive() throws DataAccessException{
        String authToken = dataAccessObject.createUser(new UserData("Cade", "hello", "different_email@123.com"));
        ArrayList<GameData> myArrayList = dataAccessObject.listGames(authToken);
        ListofGames myList = new ListofGames(myArrayList);
        assertEquals(new ArrayList<GameData>(), myList.games());

        dataAccessObject.makeNewGame("new game", authToken);
        myArrayList = dataAccessObject.listGames(authToken);
        myList = new ListofGames(myArrayList);
        ArrayList<GameData> expected = new ArrayList<>();
        expected.add(new GameData(null, null, "new game", null));
        assertEquals(expected, myList.games());
    }

    @Test
    void listGamesNegative() throws DataAccessException{
        dataAccessObject.createUser(new UserData("Cade", "hello", "different_email@123.com"));
        DataAccessException exception = assertThrows(DataAccessException.class, () -> dataAccessObject.listGames("no authentication"));
        assertEquals("Error: unauthorized", exception.getMessage());

    }

    @Test
    void joinPositive() throws DataAccessException{
        String authToken = dataAccessObject.createUser(new UserData("Cade", "hello", "different_email@123.com"));
        String authToken2 = dataAccessObject.createUser(new UserData("Jeremy", "hello", "different_email@123.com"));
        int id = dataAccessObject.makeNewGame("my_game", authToken);
        dataAccessObject.addToGame(authToken, "WHITE", id);
        dataAccessObject.addToGame(authToken2, "BLACK", id);

        ArrayList<GameData> myArrayList = dataAccessObject.listGames(authToken);
        ListofGames myList = new ListofGames(myArrayList);
        ArrayList<GameData> expected = new ArrayList<>();
        expected.add(new GameData("Cade", "Jeremy", "my_game", null));
        assertEquals(expected, myList.games());
    }

    @Test
    void joinNegative() throws DataAccessException{
        String authToken = dataAccessObject.createUser(new UserData("Cade", "hello", "different_email@123.com"));
        String authToken2 = dataAccessObject.createUser(new UserData("Jeremy", "hello", "different_email@123.com"));
        int id = dataAccessObject.makeNewGame("my_game", authToken);
        dataAccessObject.addToGame(authToken, "WHITE", id);

        // request to join in a place that is already taken
        DataAccessException exception = assertThrows(DataAccessException.class, () -> dataAccessObject.addToGame(
                authToken2, "WHITE", id
            )
        );
        assertEquals("Error: already taken", exception.getMessage());

        // request to join without authorization
        exception = assertThrows(DataAccessException.class, () -> dataAccessObject.addToGame("no authentication", "BLACK", id));
        assertEquals("Error: unauthorized", exception.getMessage());

        // request to join without a valid player color
        exception = assertThrows(DataAccessException.class, () -> dataAccessObject.addToGame(authToken2, null, id));
        assertEquals("Error: bad request", exception.getMessage());

        // request to join without an invalid game ID
        exception = assertThrows(DataAccessException.class, () -> dataAccessObject.addToGame(authToken2, "BLACK", 0));
        assertEquals("Error: bad request", exception.getMessage());
    }
}