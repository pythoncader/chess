package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseUserDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import model.*;

public class ChessHandler {
    Service myService;
    Gson gson = new Gson();

    public ChessHandler(Service myService) {
        this.myService = myService;
    }

    public void registerUser(Context ctx) {
        UserData registerRequest = gson.fromJson(ctx.body(), UserData.class);
        ctx.contentType("application/json");
        try {
            AuthData response = myService.register(registerRequest);
            ctx.result(gson.toJson(response));
        } catch (DataAccessException ex){
            throwErrorThroughJson(ctx, ex);
        }
    }

    public void loginUser(Context ctx) {
        UserData loginRequest = gson.fromJson(ctx.body(), UserData.class);
        ctx.contentType("application/json");
        try {
            AuthData response = myService.login(loginRequest);
            ctx.result(gson.toJson(response));
        } catch (DataAccessException ex){
            throwErrorThroughJson(ctx, ex);
        }
    }

    private void throwErrorThroughJson(Context ctx, DataAccessException ex) {
        ctx.status(ex.getErrorCode());
        ErrorMessage errorResponse = new ErrorMessage(ex.getMessage(), ex.getErrorCode());
        ctx.result(gson.toJson(errorResponse));
    }

    public void logoutUser(Context ctx) {
        String authToken = ctx.header("Authorization");
        try {
            if (authToken != null) {
                myService.logout(new LogoutRequest(authToken));
                ctx.result("{}");
            }
        } catch (DataAccessException ex) {
            throwErrorThroughJson(ctx, ex);
        }
    }

    public void clearDatabase(Context ctx){
        ctx.contentType("application/json");
        try {
            myService.clear();
            ctx.result("{}");
        } catch (DataAccessException ex){
            throwErrorThroughJson(ctx, ex);
        }
    }

    public void createGame(Context ctx) {
        GameData request = gson.fromJson(ctx.body(), GameData.class);
        ctx.contentType("application/json");
        String authToken = ctx.header("Authorization");
        try {
            if (authToken != null) {
                GameID newGameID = myService.newGame(new GameRequest(request.gameName(), authToken));
                ctx.result(gson.toJson(newGameID));
            }
        } catch (DataAccessException ex){
            throwErrorThroughJson(ctx, ex);
        }
    }

    public void getGames(Context ctx){
        String authToken = ctx.header("Authorization");
        ctx.contentType("application/json");
        try {
            if (authToken != null) {
                ListofGames myGames = myService.listGames(new ListRequest(authToken));
                ctx.result(gson.toJson(myGames));
            }
        } catch (DataAccessException ex){
            throwErrorThroughJson(ctx, ex);
        }
    }

    public void joinGame(Context ctx){
        String authToken = ctx.header("Authorization");
        ctx.contentType("application/json");
        PlayerInfo request = gson.fromJson(ctx.body(), PlayerInfo.class);
        try {
            if (authToken != null) {
                myService.join(new JoinRequest(authToken, request.playerColor(), request.gameID()));
                ctx.result("{}");
            }
        } catch (DataAccessException ex){
            throwErrorThroughJson(ctx, ex);
        }
    }
}