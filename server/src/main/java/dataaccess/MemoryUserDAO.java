package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import java.util.*;

public class MemoryUserDAO implements UserDAO{
    public Map<String, UserData> getUsers() {
        return users;
    }

    public Map<String, String> getAuthTokens() {
        return authTokens;
    }

    public Map<Integer, GameData> getChessGames() {
        return chessGames;
    }


    // This variable is final so that it cannot point to a different map, but its contents can still be changed
    private final Map<String, UserData> users = new HashMap<>();
    private final Map<String, String> authTokens = new HashMap<>();
    private final Map<Integer, GameData> chessGames = new HashMap<>();
    private int currentGameID = 0;

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
        this.currentGameID = 0;
        chessGames.clear();
    }

    @Override
    public int makeNewGame(String gameName, String authToken) throws DataAccessException {
        if (gameName == null || authToken == null || gameName.isEmpty() || authToken.isEmpty()){
            throw new DataAccessException("Error: bad request", 400);
        }
        if (authTokens.containsKey(authToken)) {
            this.currentGameID += 1;
            chessGames.put(
                    this.currentGameID,
                    new GameData(
                            this.currentGameID,
                            null,
                            null,
                            gameName,
                            new ChessGame()
                    )
            );
            return currentGameID;
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    @Override
    public ArrayList<GameData> listGames(String authToken) throws DataAccessException {
        if (authTokens.containsKey(authToken)) {
            ArrayList<GameData> gameDataList = new ArrayList<>(this.chessGames.values());
            ArrayList<GameData> gameList = new ArrayList<>();
            for (GameData dataGame : gameDataList) {
                gameList.add(
                        new GameData(
                                dataGame.gameID(),
                                dataGame.whiteUsername(),
                                dataGame.blackUsername(),
                                dataGame.gameName(),
                                null
                        )
                );
            }
            return gameList;
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
                            oldGame.gameID(),
                            new GameData(
                                oldGame.gameID(),
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
                            oldGame.gameID(),
                            new GameData(
                                oldGame.gameID(),
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
}
