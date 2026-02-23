package server;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.GameData;
import model.UserData;
import dataaccess.MemoryUserDAO;

import java.util.ArrayList;

public class Service {
    UserDAO myDataAccess = new MemoryUserDAO();

    public String register(UserData myUser) throws DataAccessException {
        return myDataAccess.createUser(myUser);
    }

    public String login(UserData myUser) throws DataAccessException {
        return myDataAccess.loginUser(myUser.username(), myUser.password());
    }

    public void logout(String authToken) throws DataAccessException {
        myDataAccess.deleteAuthToken(authToken);
    }

    public void clear() throws DataAccessException{
        myDataAccess.clear();
    }

    public int newGame(String gameName, String authToken) throws DataAccessException{
        return myDataAccess.makeNewGame(gameName, authToken);
    }

    public ArrayList<GameData> listGames(String authToken) throws DataAccessException{
        return myDataAccess.listGames(authToken);
    }

    public void join(String authToken, String playerColor, int gameID) throws DataAccessException{
        myDataAccess.addToGame(authToken, playerColor, gameID);
    }
}