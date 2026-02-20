package server;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import dataaccess.MemoryUserDAO;

public class Service {
    UserDAO myDataAccess = new MemoryUserDAO();

    public String register(UserData myUser) throws DataAccessException {
        if (!myDataAccess.existsUser(myUser.username())) {
            return myDataAccess.createUser(myUser);
        } else {
            throw new DataAccessException("A user with this username already exists!");
        }
    }
}