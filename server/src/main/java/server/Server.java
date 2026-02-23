package server;

import io.javalin.*;

public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        // Register your endpoints and exception handlers here
        ChessHandler myHandler = new ChessHandler();
        javalin.post("/user", myHandler::registerUser);
        javalin.post("/session", myHandler::loginUser);
        javalin.delete("/session", myHandler::logoutUser);
        javalin.delete("/db", myHandler::clearDatabase);
        javalin.post("/game", myHandler::createGame);
    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
