package server;

import com.google.gson.Gson;
import io.javalin.http.Context;
import model.AuthData;
import model.UserData;
import model.ErrorMessage;

public class ChessHandler {
    Service myService = new Service();
    public void addUser(Context ctx) {
        Gson gson = new Gson();
        UserData loginRequest = gson.fromJson(ctx.body(), UserData.class);
        ctx.contentType("application/json");
        try {
            String authToken = myService.register(loginRequest);
            AuthData response = new AuthData(loginRequest.username(), authToken);
            ctx.result(gson.toJson(response));
        } catch (Exception ex){
            ctx.status(403);
            ErrorMessage errorResponse = new ErrorMessage("Error: already taken");
            ctx.result(gson.toJson(errorResponse));
        }

    }
}