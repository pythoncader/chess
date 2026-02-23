package dataaccess;

import chess.ChessGame;
import model.NameAndGame;
import model.UserData;
import server.EmailandPassword;
import java.util.*;

public class MemoryUserDAO implements UserDAO{
    // This variable is final so that it cannot point to a different map, but its contents can still be changed
    private final Map<String, EmailandPassword> Users = new HashMap<>();
    private final ArrayList<String> authTokens = new ArrayList<>();
    private final Map<Integer, NameAndGame> chessGames = new HashMap<>();
    private int currentGameID = 0;

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public String createUser(UserData newUser) throws DataAccessException{
        if (Users.get(newUser.username()) == null) {
            Users.put(newUser.username(), new EmailandPassword(newUser.email(), newUser.password()));
            return this.addAuthToken();
        } else {
            throw new DataAccessException("Error: already taken", 403);
        }
    }

    @Override
    public void deleteAuthToken(String authToken) throws DataAccessException{
        if (authTokens.contains(authToken)){
            authTokens.remove(authToken);
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    @Override
    public String loginUser(String username, String password) throws DataAccessException{
        EmailandPassword myUserData = Users.get(username);
        if (myUserData == null) {
            throw new DataAccessException("Error: bad request", 400);
        } else if (Objects.equals(myUserData.password(), password)){
            return this.addAuthToken();
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

    @Override
    public String addAuthToken(){
        String authToken = generateToken();
        authTokens.add(authToken);
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
        if (authTokens.contains(authToken)){
            this.currentGameID += 1;
            chessGames.put(this.currentGameID, new NameAndGame(gameName, new ChessGame()));
            return currentGameID;
        } else {
            throw new DataAccessException("Error: unauthorized", 401);
        }
    }

}
