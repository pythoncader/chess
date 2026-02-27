package server;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import io.javalin.http.Context;
import model.*;

public class ChessHandler {
    UserDAO myDataAccess = new MemoryUserDAO();
    Service myService = new Service(myDataAccess);
    Gson gson = new Gson();

    public void registerUser(Context ctx) {
        UserData registerRequest = gson.fromJson(ctx.body(), UserData.class);
        ctx.contentType("application/json");
        try {
            AuthData response = myService.register(registerRequest);
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
            AuthData response = myService.login(loginRequest);
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
                myService.logout(new LogoutRequest(authToken));
                ctx.result("{}");
            }
        } catch (DataAccessException ex) {
            ctx.status(ex.getErrorCode());
            ErrorMessage errorResponse = new ErrorMessage(ex.getMessage());
            ctx.result(gson.toJson(errorResponse));
        }
    }

    public void clearDatabase(Context ctx){
        ctx.contentType("application/json");
        try {
            myService.clear();
            ctx.result("{}");
        } catch (DataAccessException ex){
            ctx.status(ex.getErrorCode());
            ErrorMessage errorResponse = new ErrorMessage(ex.getMessage());
            ctx.result(gson.toJson(errorResponse));
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
            ctx.status(ex.getErrorCode());
            ErrorMessage errorResponse = new ErrorMessage(ex.getMessage());
            ctx.result(gson.toJson(errorResponse));
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
            ctx.status(ex.getErrorCode());
            ErrorMessage errorResponse = new ErrorMessage(ex.getMessage());
            ctx.result(gson.toJson(errorResponse));
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
            ctx.status(ex.getErrorCode());
            ErrorMessage errorResponse = new ErrorMessage(ex.getMessage());
            ctx.result(gson.toJson(errorResponse));
        }
    }
}