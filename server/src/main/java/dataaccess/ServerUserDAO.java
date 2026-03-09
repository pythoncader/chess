package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ServerUserDAO implements UserDAO{

    public ServerUserDAO() throws DataAccessException {
        configureDatabase();
    }

    // This variable is final so that it cannot point to a different map, but its contents can still be changed
    private final Map<String, UserData> users = new HashMap<>();
    private final Map<String, String> authTokens = new HashMap<>();
    private final Map<Integer, GameData> chessGames = new HashMap<>();

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String createUser(UserData newUser) throws DataAccessException{
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
        if (users.get(newUser.username()) == null) {
            users.put(newUser.username(), newUser);
            return this.addAuthToken(newUser.username());
        } else {
            throw new DataAccessException("Error: already taken", 403);
        }
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException{
        if (authTokens.containsKey(authToken)) {
            authTokens.remove(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    @Override
    public String loginUser(String username, String password) throws DataAccessException{
        UserData myUserData = users.get(username);
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new DataAccessException("Error: bad request", 400);
        }
        if (myUserData == null || !Objects.equals(myUserData.password(), password)){
            throw new DataAccessException("Error: unauthorized", 401);
        } else {
            return this.addAuthToken(username);
        }
    }

    @Override
    public String addAuthToken(String username){
        String authToken = generateToken();
        authTokens.put(authToken, username);
        return authToken;
    }

    @Override
    public void clear() {
        authTokens.clear();
        users.clear();
        chessGames.clear();
    }

    @Override
    public int makeNewGame(String gameName, String authToken) throws DataAccessException {
        if (gameName == null || authToken == null || gameName.isEmpty() || authToken.isEmpty()){
            throw new DataAccessException("Error: bad request", 400);
        }
        if (authTokens.containsKey(authToken)) {
            try (var conn = DatabaseManager.getConnection()) {
                var statement = "INSERT INTO chessGames (name, json) VALUES (?, ?)";
                GameData myGame = new GameData(
                        null,
                        null,
                        gameName,
                        new ChessGame()
                );
                String json = new Gson().toJson(myGame);
                try (var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)) {
                    preparedStatement.setString(1, gameName);
                    preparedStatement.setString(2, json);
                    preparedStatement.executeUpdate();
                    ResultSet rs = preparedStatement.getGeneratedKeys();
                    if (rs.next()) {
                        return rs.getInt(1);
                    } else {
                        throw new DataAccessException("ID not generated", 500);
                    }
                }

            } catch (Exception ex) {
                throw new DataAccessException(ex.getMessage(), 500);
            }
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    @Override
    public ArrayList<GameData> listGames(String authToken) throws DataAccessException {
        if (authTokens.containsKey(authToken)) {
            ArrayList<GameData> gameDataList = new ArrayList<>();
            ArrayList<GameData> gameList = new ArrayList<>();
            ArrayList<String> jsonList = new ArrayList<>();
            var statement = "SELECT json FROM chessGames";

            try (var conn = DatabaseManager.getConnection(); var preparedStatement = conn.prepareStatement(statement);
                 ResultSet rs = preparedStatement.executeQuery() ){

                while (rs.next()){
                    jsonList.add(rs.getString("json"));
                }
                Gson gson = new Gson();
                for (String jsonData : jsonList) {
                    gameDataList.add(gson.fromJson(jsonData, GameData.class));
                }

                for (GameData dataGame : gameDataList) {
                    gameList.add(
                            new GameData(
                                    dataGame.whiteUsername(),
                                    dataGame.blackUsername(),
                                    dataGame.gameName(),
                                    null
                            )
                    );
                }
                return gameList;
            } catch (Exception ex) {
                throw new DataAccessException(ex.getMessage(), 500);
            }
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    @Override
    public void addToGame(String authToken, String playerColor, int gameID) throws DataAccessException {
        if (authToken == null
                || authToken.isEmpty()
                || (!Objects.equals(playerColor, "BLACK") && !Objects.equals(playerColor, "WHITE"))
                || !chessGames.containsKey(gameID)) {
            throw new DataAccessException("Error: bad request", 400);
        }
        if (authTokens.containsKey(authToken)) {
            GameData oldGame = chessGames.get(gameID);
            if (Objects.equals(playerColor, "BLACK")) {
                if (oldGame.blackUsername() == null) {
                    chessGames.put(
                            gameID,
                            new GameData(
                                oldGame.whiteUsername(),
                                authTokens.get(authToken),
                                oldGame.gameName(),
                                oldGame.game()
                            )
                    );
                } else {
                    throw new DataAccessException("Error: already taken", 403);
                }
            } else {
                if (oldGame.whiteUsername() == null) {
                    chessGames.put(
                            gameID,
                            new GameData(
                                authTokens.get(authToken),
                                oldGame.blackUsername(),
                                oldGame.gameName(),
                                oldGame.game()
                            )
                    );
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
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
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
            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()), 500);
        }
    }
}
