package server;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import dataaccess.MemoryUserDAO;

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
}