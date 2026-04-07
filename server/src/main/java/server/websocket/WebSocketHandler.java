package server.websocket;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final UserDAO DAO;

    public WebSocketHandler(UserDAO DAO) {
        this.DAO = DAO;
    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand command = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (command.getCommandType()) {
                case CONNECT -> joinGame(command.getAuthToken(), command.getGameID(), ctx.session);
                case MAKE_MOVE -> joinGame(command.getAuthToken()+"4", command.getGameID(), ctx.session);
                case LEAVE -> leaveGame(command.getAuthToken(), command.getGameID(), command.getPlayerColor(), ctx.session);
                case RESIGN -> resignGame(command.getAuthToken(), command.getGameID(), ctx.session);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }


    private void joinGame(String authToken, int gameID, Session session) throws IOException{
        connections.add(gameID, session);
        try {
            var message = String.format("%s has joined the game", DAO.getUsername(authToken));
            var notification = new ServerMessage(message, ServerMessage.ServerMessageType.LOAD_GAME);
            connections.broadcast(gameID, session, notification); // I'll need to do something with the gameID to get the correct sessions that are linked to it
        } catch (Exception ex){
            System.out.println("Error getting the username or broadcasting the message");
        }
    }

    private void resignGame(String authToken, int gameID, Session session) throws IOException{
        connections.add(gameID, session);
        try {
            var message = String.format("%s has resigned from the game. You win!", DAO.getUsername(authToken));
            var notification = new ServerMessage(message, ServerMessage.ServerMessageType.NOTIFICATION);
            connections.broadcast(gameID, session, notification); // I'll need to do something with the gameID to get the correct sessions that are linked to it
        } catch (DataAccessException ex){
            throw new IOException(ex.getMessage());
        }
    }

    private void leaveGame(String authToken, int gameID, String playerColor, Session session) throws IOException{
        connections.add(gameID, session);
        try {
            var message = String.format("%s has left the game", DAO.getUsername(authToken));
            DAO.addToGame(authToken, playerColor, gameID, true);
            connections.remove(gameID, session);
            var notification = new ServerMessage(message, ServerMessage.ServerMessageType.NOTIFICATION);
            connections.broadcast(gameID, session, notification); // I'll need to do something with the gameID to get the correct sessions that are linked to it
        } catch (DataAccessException ex){
            throw new IOException(ex.getMessage());
        }
    }


}