package dataaccess;

import model.GameData;
import model.UserData;

import java.util.ArrayList;


public interface UserDAO {
    String createUser(UserData newUser) throws DataAccessException;
    void deleteAuthToken(String authToken) throws DataAccessException;
    String addAuthToken(String username);
    String loginUser(String username, String password) throws DataAccessException;
    void clear() throws DataAccessException;

    int makeNewGame(String gameName, String authToken) throws DataAccessException;

    ArrayList<GameData> listGames(String authToken) throws DataAccessException;

    void addToGame(String authToken, String playerColor, int gameID) throws DataAccessException;
}