package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

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
        if (!isInTable(newUser.username(), "username", "users")) {
            var statement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?)";

            executeUpdate(statement, newUser.username(), BCrypt.hashpw(newUser.password(), BCrypt.gensalt()), newUser.email());

            return this.addAuthToken(newUser.username());
        } else {
            throw new DataAccessException("Error: already taken", 403);
        }
    }

    private boolean isInTable(String value, String columnName, String tableName) throws DataAccessException{
        var statement = "SELECT COUNT(*) FROM " + tableName + " WHERE "+columnName+" = ?";
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement)) {
                ps.setString(1, value);
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
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new DataAccessException("Error: bad request", 400);
        }
        if (!isInTable(username, "username", "users") || !checkPassword(username, password)){
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
                throw new DataAccessException("There was a problem accessing the database", 500);
            }

        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }

    @Override
    public String addAuthToken(String username){
        String authToken = generateToken();
        authTokens.put(authToken, username);
        return authToken;
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE chessGames";
        executeUpdate(statement);
        statement = "TRUNCATE users";
        executeUpdate(statement);
        statement = "TRUNCATE authTokens";
        executeUpdate(statement);

        authTokens.clear();
        users.clear();
    }

    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (Connection conn = DatabaseManager.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (int i = 0; i < params.length; i++) {
                    Object param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage(), 500);
        }
    }

    @Override
    public int makeNewGame(String gameName, String authToken) throws DataAccessException {
        if (gameName == null || authToken == null || gameName.isEmpty() || authToken.isEmpty()){
            throw new DataAccessException("Error: bad request", 400);
        }
        if (authTokens.containsKey(authToken)) {
            var statement = "INSERT INTO chessGames (name, json) VALUES (?, ?)";
            GameData myGame = new GameData(
                    null,
                    null,
                    gameName,
                    new ChessGame()
            );
            String json = new Gson().toJson(myGame);
            return executeUpdate(statement, gameName, json);

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

            try (var conn = DatabaseManager.getConnection();
                 var preparedStatement = conn.prepareStatement(statement);
                 ResultSet rs = preparedStatement.executeQuery() ){

                while (rs.next()){
                    jsonList.add(rs.getString("json"));
                }
                Gson gson = new Gson();
                for (String jsonData : jsonList) {
                    gameDataList.add(gson.fromJson(jsonData, GameData.class));
                }

                removeGameObject(gameDataList, gameList);
                return gameList;
            } catch (Exception ex) {
                throw new DataAccessException(ex.getMessage(), 500);
            }
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    private static void removeGameObject(ArrayList<GameData> gameDataList, ArrayList<GameData> gameList) {
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
            """,
            """
            CREATE TABLE IF NOT EXISTS  users (
              `username` varchar(256) NOT NULL,
              `password` TEXT DEFAULT NULL,
              `email` TEXT DEFAULT NULL,
              PRIMARY KEY (`username`),
              INDEX(username)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """,
            """
            CREATE TABLE IF NOT EXISTS  authTokens (
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
