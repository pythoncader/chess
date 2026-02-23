package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import model.ErrorMessage;

public class ChessHandler {
    Service myService = new Service();
    Gson gson = new Gson();

    public void registerUser(Context ctx) {
        UserData registerRequest = gson.fromJson(ctx.body(), UserData.class);
        ctx.contentType("application/json");
        try {
            String authToken = myService.register(registerRequest);
            AuthData response = new AuthData(registerRequest.username(), authToken);
            ctx.result(gson.toJson(response));
        } catch (DataAccessException ex){
            ctx.status(ex.getErrorCode());
            ErrorMessage errorResponse = new ErrorMessage(ex.getMessage());
            ctx.result(gson.toJson(errorResponse));
        }

    }

    public void loginUser(Context ctx) {
        UserData loginRequest = gson.fromJson(ctx.body(), UserData.class);
        ctx.contentType("application/json");
        try {
            String authToken = myService.login(loginRequest);
            AuthData response = new AuthData(loginRequest.username(), authToken);
            ctx.result(gson.toJson(response));
        } catch (DataAccessException ex){
            ctx.status(ex.getErrorCode());
            ErrorMessage errorResponse = new ErrorMessage(ex.getMessage());
            ctx.result(gson.toJson(errorResponse));
        }
    }

    public void logoutUser(Context ctx) {
        String authToken = ctx.header("Authorization");
        try {
            if (authToken != null) {
                myService.logout(authToken);
                ctx.result("{}");
            } else {
                // throw an exception or something
            }
        } catch (DataAccessException ex) {
            ctx.status(ex.getErrorCode());
            ErrorMessage errorResponse = new ErrorMessage(ex.getMessage());
            ctx.result(gson.toJson(errorResponse));
        }
    }

    public void clearDatabase(Context ctx){
        try {
            myService.clear();
            ctx.result("{}");
        } catch (DataAccessException ex){
            ctx.status(ex.getErrorCode());
            ErrorMessage errorResponse = new ErrorMessage(ex.getMessage());
            ctx.result(gson.toJson(errorResponse));
        }
    }
}