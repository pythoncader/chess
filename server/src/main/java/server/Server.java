package server;

import io.javalin.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here
        try {
            ChessHandler myHandler = new ChessHandler();
            javalin.post("/user", myHandler::registerUser);
            javalin.post("/session", myHandler::loginUser);
            javalin.delete("/session", myHandler::logoutUser);
            javalin.delete("/db", myHandler::clearDatabase);
            javalin.post("/game", myHandler::createGame);
            javalin.get("/game", myHandler::getGames);
            javalin.put("/game", myHandler::joinGame);
            javalin.ws("/ws", ws -> {
                ws.onConnect(ctx -> System.out.println("Websocket connected"));
                ws.onMessage(ctx -> ctx.send("Websocket response:" + ctx.message()));
                ws.onClose(ctx -> System.out.println("Websocket closed"));
            });
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
