package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public interface UserDAO {
    public Boolean existsUser(String username);
    public String createUser(UserData newUser);
    public void addAuthData(String authData);
    public void deleteAuthToken(String authData);
}