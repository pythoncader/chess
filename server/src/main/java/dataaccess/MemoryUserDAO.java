package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;
import java.util.*;

public class MemoryUserDAO implements UserDAO{
    // This variable is final so that it cannot point to a different map, but its contents can still be changed
    private final Map<String, UserData> Users = new HashMap<>();
    private final Map<String, String> authTokens = new HashMap<>();
    private final Map<Integer, GameData> chessGames = new HashMap<>();
    private int currentGameID = 0;

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String createUser(UserData newUser) throws DataAccessException{
        if (Users.get(newUser.username()) == null) {
            Users.put(newUser.username(), newUser);
            return this.addAuthToken(newUser.username());
        } else {
            throw new DataAccessException("Error: already taken", 403);
        }
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException{
        if (authTokens.containsKey(authToken)){
            authTokens.remove(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    @Override
    public String loginUser(String username, String password) throws DataAccessException{
        UserData myUserData = Users.get(username);
        if (myUserData == null) {
            throw new DataAccessException("Error: bad request", 400);
        } else if (Objects.equals(myUserData.password(), password)){
            return this.addAuthToken(username);
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
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
        try {
            authTokens.clear();
            Users.clear();
            this.currentGameID = 0;
            chessGames.clear();
        } catch (Exception ex){
            throw new DataAccessException("Error: Something happened while clearing the database", 500);
        }
    }

    @Override
    public int makeNewGame(String gameName, String authToken) throws DataAccessException {
        if (authTokens.containsKey(authToken)){
            this.currentGameID += 1;
            chessGames.put(this.currentGameID, new GameData(this.currentGameID, "", "", gameName, new ChessGame()));
            return currentGameID;
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    @Override
    public ArrayList<GameData> listGames(String authToken) throws DataAccessException {
        if (authTokens.containsKey(authToken)){
            ArrayList<GameData> gameDataList = new ArrayList<>(this.chessGames.values());
            ArrayList<GameData> gameList = new ArrayList<>();
            for (GameData dataGame : gameDataList){
                gameList.add(new GameData(dataGame.gameID(), dataGame.whiteUsername(), dataGame.blackUsername(), dataGame.gameName(), null));
            }
            return gameList;
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    @Override
    public void addToGame(String authToken, String playerColor, int gameID) throws DataAccessException{
        if (authTokens.containsKey(authToken)){
            if (chessGames.containsKey(gameID)){
                GameData oldGame = chessGames.get(gameID);
                if (Objects.equals(playerColor, "BLACK")) {
                    chessGames.put(oldGame.gameID(), new GameData(oldGame.gameID(), oldGame.whiteUsername(), authTokens.get(authToken), oldGame.gameName(), oldGame.game()));
                } else if (Objects.equals(playerColor, "WHITE")) {
                    chessGames.put(oldGame.gameID(), new GameData(oldGame.gameID(), authTokens.get(authToken), oldGame.blackUsername(), oldGame.gameName(), oldGame.game()));
                }
            }
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }
}
