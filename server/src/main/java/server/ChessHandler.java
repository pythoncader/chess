package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class ChessHandler {
    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public void addUser(Context ctx){
        Gson gson = new Gson();
        UserData loginRequest = gson.fromJson(ctx.body(), UserData.class);
        ctx.contentType("application/json");
        AuthData response = new AuthData(loginRequest.username(), generateToken());
        ctx.result(new Gson().toJson(response));
    }
}