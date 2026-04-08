package server;

import dataaccess.DataAccessException;
import dataaccess.DatabaseUserDAO;
import dataaccess.MemoryUserDAO;
import dataaccess.UserDAO;
import io.javalin.*;
import server.websocket.WebSocketHandler;

public class Server {
    UserDAO myDataAccess;
    Service myService;
    ChessHandler myHandler;
    private final Javalin javalin;
    private WebSocketHandler webSocketHandler = null;

    public Server() {

        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here
        try {
            //        this.myDataAccess = new MemoryUserDAO();
            this.myDataAccess = new DatabaseUserDAO();
            this.myService = new Service(myDataAccess);
            this.myHandler = new ChessHandler(myService);
            this.webSocketHandler = new WebSocketHandler(this.myDataAccess);

            javalin.post("/user", myHandler::registerUser);
            javalin.post("/session", myHandler::loginUser);
            javalin.delete("/session", myHandler::logoutUser);
            javalin.delete("/db", myHandler::clearDatabase);
            javalin.post("/game", myHandler::createGame);
            javalin.get("/game", myHandler::getGames);
            javalin.put("/game", myHandler::joinGame);

            javalin.ws("/ws", ws -> {
                ws.onConnect(webSocketHandler);
                ws.onMessage(webSocketHandler);
                ws.onClose(webSocketHandler);
                }
            );
        } catch (Exception ex) {
            System.out.println("Could not start server");
        }
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
