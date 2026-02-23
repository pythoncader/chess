package dataaccess;

import model.UserData;


public interface UserDAO {
    String createUser(UserData newUser) throws DataAccessException;
    void deleteAuthToken(String authToken) throws DataAccessException;
    String addAuthToken();
    String loginUser(String username, String password) throws DataAccessException;
    void clear() throws DataAccessException;
}