package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MemoryUserDAO implements UserDAO{
    // This variable is final so that it cannot point to a different map, but its contents can still be changed
    private final Map<String, String> Users = new HashMap<>();
    private final ArrayList<String> authTokens = new ArrayList<>();

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public Boolean existsUser(String username) {
        return Users.get(username) != null;
    }

    @Override
    public String createUser(UserData newUser) {
        Users.put(newUser.username(), newUser.password());
        String authToken = generateToken();
        this.addAuthData(authToken);
        return authToken;
    }

    @Override
    public void addAuthData(String authData) {
        authTokens.add(authData);
    }

    @Override
    public void deleteAuthToken(String authData) {
        authTokens.remove(authData);
    }
}
