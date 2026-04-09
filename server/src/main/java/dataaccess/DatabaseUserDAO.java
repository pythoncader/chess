package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class DatabaseUserDAO implements UserDAO{

    public DatabaseUserDAO() throws DataAccessException {
        configureDatabase();
    }


    @Override
    public String createUser(UserData newUser) throws DataAccessException{ // tested
        if (
                newUser.username() == null
                || newUser.password() == null
                || newUser.email() == null
                || newUser.username().isEmpty()
                || newUser.password().isEmpty()
                || newUser.email().isEmpty()
        ) {
            throw new DataAccessException("Error: bad request", 400);
        }
        if (!isInTable(newUser.username(), "username", "users")) {
            var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

            executeUpdate(statement, newUser.username(), BCrypt.hashpw(newUser.password(), BCrypt.gensalt()), newUser.email());

            return this.addAuthToken(newUser.username());
        } else {
            throw new DataAccessException("Error: already taken", 403);
        }
    }

    private boolean isInTable(Object value, String columnName, String tableName) throws DataAccessException{
        if ((!Objects.equals(tableName, "chessGames") && !Objects.equals(tableName, "users") && !Objects.equals(tableName, "authTokens"))){
            throw new DataAccessException("Error: Invalid table name", 500);
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
                    throw new DataAccessException("Error: There was a problem accessing the database", 500);
                }
            }
        } catch (Exception ex) {
            throw new DataAccessException("Error: " + ex.getMessage(), 500);
        }
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException{ // tested
        if (isInTable(authToken, "authToken", "authTokens")) {
            var statement = "DELETE FROM authTokens WHERE authToken=?";
            executeUpdate(statement, authToken);
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    @Override
    public String loginUser(String username, String password) throws DataAccessException{ // tested
        if (username == null
            || password == null
            || username.isEmpty()
            || password.isEmpty()) {
            throw new DataAccessException("Error: bad request", 400);
        }
        if (!isInTable(username, "username", "users")
            || !checkPassword(username, password)) {
            throw new DataAccessException("Error: unauthorized", 401);
        } else {
            return this.addAuthToken(username);
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
                throw new DataAccessException("Error: There was a problem accessing the database", 500);
            }

        } catch (Exception ex) {
            throw new DataAccessException("Error: " + ex.getMessage(), 500);
        }
    }

    private String addAuthToken(String username) throws DataAccessException{
        String authToken = UUID.randomUUID().toString(); // generate a new authToken
        var statement = "INSERT INTO authTokens (username, authToken) VALUES (?, ?)";
        executeUpdate(statement, username, authToken);
        return authToken;
    }

    @Override
    public void clear() throws DataAccessException { // tested
        var statement = "TRUNCATE chessGames";
        executeUpdate(statement);
        statement = "TRUNCATE users";
        executeUpdate(statement);
        statement = "TRUNCATE authTokens";
        executeUpdate(statement);
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) {
                        ps.setString(i + 1, p);
                    }
                    else if (param instanceof Integer p) {
                        ps.setInt(i + 1, p);
                    }
                    else if (param == null) {
                        ps.setNull(i + 1, NULL);
                    }
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                return 0;
            }
        } catch (Exception ex) {
            throw new DataAccessException("Error: " + ex.getMessage(), 500);
        }
    }

    @Override
    public int makeNewGame(String gameName, String authToken) throws DataAccessException { // tested
        if (gameName == null || authToken == null || gameName.isEmpty() || authToken.isEmpty()){
            throw new DataAccessException("Error: bad request", 400);
        }
        if (isInTable(authToken, "authToken", "authTokens")) {
            var statement = "INSERT INTO chessGames (name) VALUES (?)";
            int gameID = executeUpdate(statement, gameName);
            GameData myGame = new GameData(
                    gameID,
                    null,
                    null,
                    gameName,
                    new ChessGame()
            );
            String json = new Gson().toJson(myGame);
            statement = "UPDATE chessGames SET json=? WHERE id=?";
            executeUpdate(statement, json, gameID);
            return gameID;

        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    @Override
    public ArrayList<GameData> listGames(String authToken) throws DataAccessException { // tested
        if (isInTable(authToken, "authToken", "authTokens")) {
            ArrayList<GameData> gameList = new ArrayList<>();
            ArrayList<String> jsonList = new ArrayList<>();
            var statement = "SELECT json FROM chessGames";

            try (var conn = DatabaseManager.getConnection();
                 var preparedStatement = conn.prepareStatement(statement);
                 ResultSet rs = preparedStatement.executeQuery() ){

                while (rs.next()){
                    jsonList.add(rs.getString("json"));
                }
                Gson gson = new Gson();
                for (String jsonData : jsonList) {
                    gameList.add(gson.fromJson(jsonData, GameData.class));
                }

                return gameList;
            } catch (Exception ex) {
                throw new DataAccessException("Error: " + ex.getMessage(), 500);
            }
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    public String getUsername(String authToken) throws DataAccessException{
        var statement = "SELECT username FROM authTokens WHERE authToken=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement)) {
            ps.setString(1, authToken);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getString("username");
            } else {
                throw new DataAccessException("Error: There was a problem accessing the database", 500);
            }

        } catch (Exception ex) {
            throw new DataAccessException("Error: " + ex.getMessage(), 500);
        }
    }
    private String getChessGame(int gameID) throws DataAccessException {
        var statement = "SELECT json FROM chessGames WHERE id=?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(statement)) {
            ps.setInt(1, gameID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                return rs.getString("json");
            } else {
                throw new DataAccessException("Error: There was a problem accessing the database", 500);
            }

        } catch (Exception ex) {
            throw new DataAccessException("Error: " + ex.getMessage(), 500);
        }
    }

    @Override
    public void endGame(String authToken, int gameID) throws DataAccessException{
        if (isInTable(authToken, "authToken", "authTokens")) {
            var statement = "UPDATE chessGames SET json = ? WHERE id=?";
            GameData oldGameData = new Gson().fromJson(getChessGame(gameID), GameData.class);
            ChessGame newGame = oldGameData.game();
            newGame.setTeamTurn(ChessGame.TeamColor.NONE); // end the game

            GameData myGameData = new GameData(
                    gameID,
                    oldGameData.whiteUsername(),
                    oldGameData.blackUsername(),
                    oldGameData.gameName(),
                    newGame
            );

            String json = new Gson().toJson(myGameData);
            executeUpdate(statement, json, gameID);
        }
    }

    @Override
    public void makeMove(String authToken, int gameID, String playerColor, ChessMove move) throws DataAccessException, InvalidMoveException {
        if (isInTable(authToken, "authToken", "authTokens")) {
            var statement = "UPDATE chessGames SET json = ? WHERE id=?";
            GameData oldGameData = new Gson().fromJson(getChessGame(gameID), GameData.class);
            if (playerColor.equals(oldGameData.game().getTeamTurn().toString())){
                ChessGame newGame = oldGameData.game();
                newGame.makeMove(move);

                GameData myGameData = new GameData(
                        gameID,
                        oldGameData.whiteUsername(),
                        oldGameData.blackUsername(),
                        oldGameData.gameName(),
                        newGame
                );

                String json = new Gson().toJson(myGameData);
                executeUpdate(statement, json, gameID);
            } else if (oldGameData.game().getTeamTurn() == ChessGame.TeamColor.NONE){
                throw new InvalidMoveException("The game is over, no more moves can be made!");
            } else if (playerColor.equals("NONE")){
                throw new InvalidMoveException("An observer cannot make a move");
            } else {
                throw new InvalidMoveException("It's not your turn!");
            }

        }
    }


    @Override
    public void addToGame(String authToken, String playerColor, int gameID, boolean leave) throws DataAccessException { // tested
        if (authToken == null
                || authToken.isEmpty()
                || (!Objects.equals(playerColor, "BLACK") && !Objects.equals(playerColor, "WHITE"))
                || !isInTable(gameID, "id", "chessGames")) {
            throw new DataAccessException("Error: bad request", 400);
        }
        if (isInTable(authToken, "authToken", "authTokens")) {
            GameData oldGame = new Gson().fromJson(getChessGame(gameID), GameData.class);

            if (Objects.equals(playerColor, "BLACK")) {
                if (oldGame.blackUsername() == null || leave) {
                    var statement = "UPDATE chessGames SET json = ? WHERE id=?";
                    String newUsername = getUsername(authToken);
                    if (leave){
                        newUsername = null;
                    }
                    GameData myGame = new GameData(
                            gameID,
                            oldGame.whiteUsername(),
                            newUsername,
                            oldGame.gameName(),
                            oldGame.game()
                    );

                    String json = new Gson().toJson(myGame);
                    executeUpdate(statement, json, gameID);
                } else {
                    throw new DataAccessException("Error: already taken", 403);
                }
            } else {
                if (oldGame.whiteUsername() == null || leave) {
                    var statement = "UPDATE chessGames SET json = ? WHERE id=?";
                    String newUsername = getUsername(authToken);
                    if (leave){
                        newUsername = null;
                    }
                    GameData myGame = new GameData(
                            gameID,
                            newUsername,
                            oldGame.blackUsername(),
                            oldGame.gameName(),
                            oldGame.game()
                    );

                    String json = new Gson().toJson(myGame);
                    executeUpdate(statement, json, gameID);
                } else {
                    throw new DataAccessException("Error: already taken", 403);
                }
            }
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  chessGames (
              `id` int NOT NULL AUTO_INCREMENT,
              `name` varchar(256) NOT NULL,
              `json` TEXT DEFAULT NULL,
              PRIMARY KEY (`id`),
              INDEX(name)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `password` TEXT DEFAULT NULL,
              `email` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`),
              INDEX(username)
            )
            """,
            """
            CREATE TABLE IF NOT EXISTS  authTokens (
              `authToken` varchar(256) NOT NULL,
              `username` varchar(256) NOT NULL,
              PRIMARY KEY (`authToken`)
            )
            """
    };

    private void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        try (Connection conn = DatabaseManager.getConnection()) {
            for (String statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new DataAccessException(String.format("Error: Unable to configure database: %s", ex.getMessage()), 500);
        }
    }
}
