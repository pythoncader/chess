package dataaccess;

import model.UserData;


public interface UserDAO {
    Boolean existsUser(String username);
    String createUser(UserData newUser);
    void addAuthToken(String authToken);
    void deleteAuthToken(String authToken);
}