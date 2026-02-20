package server;

import dataaccess.UserDAO;
import model.UserData;
import dataaccess.MemoryUserDAO;

public class Service {
    UserDAO myDataAccess = new MemoryUserDAO();

    public void register(UserData myUser){
        if (myDataAccess.existsUser(myUser.username())) {
            myDataAccess.createUser(myUser);
        }
    }
}