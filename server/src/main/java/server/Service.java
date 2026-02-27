package server;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.*;

public class Service {
    UserDAO myDataAccess;
    public Service(UserDAO myDataAccess){
        this.myDataAccess = myDataAccess;
    }

    public AuthData register(UserData myUser) throws DataAccessException {
        return new AuthData(myUser.username(), myDataAccess.createUser(myUser));
    }

    public AuthData login(UserData myUser) throws DataAccessException {
        return new AuthData(myUser.username(), myDataAccess.loginUser(myUser.username(), myUser.password()));
    }

    public void logout(LogoutRequest request) throws DataAccessException {
        myDataAccess.deleteAuthToken(request.authToken());
    }

    public void clear() throws DataAccessException{
        myDataAccess.clear();
    }

    public GameID newGame(GameRequest request) throws DataAccessException{
        return new GameID(myDataAccess.makeNewGame(request.gameName(), request.authToken()));
    }

    public ListofGames listGames(ListRequest request) throws DataAccessException{
        return new ListofGames(myDataAccess.listGames(request.authToken()));
    }

    public void join(JoinRequest request) throws DataAccessException{
        myDataAccess.addToGame(request.authToken(), request.playerColor(), request.gameID());
    }
}