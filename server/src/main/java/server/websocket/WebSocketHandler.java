package server.websocket;

import chess.ChessMove;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import io.javalin.websocket.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final UserDAO dataAccess;

    public WebSocketHandler(UserDAO dataAccess) {
        this.dataAccess = dataAccess;
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
                case CONNECT:
                    joinGame(command.getAuthToken(), command.getGameID(), command.getPlayerColor(), ctx.session);
                    break;
                case MAKE_MOVE:
                    MakeMoveCommand moveCommand = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
                    chessMove(
                            moveCommand.getAuthToken(),
                            moveCommand.getGameID(),
                            moveCommand.getPlayerColor(),
                            moveCommand.getStartPosition(),
                            moveCommand.getEndPosition(),
                            ctx.session
                            );
                    break;
                case LEAVE:
                    leaveGame(command.getAuthToken(), command.getGameID(), command.getPlayerColor(), ctx.session);
                    break;
                case RESIGN:
                    resignGame(command.getAuthToken(), command.getGameID(), ctx.session);
                    break;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }


    private void joinGame(String authToken, int gameID, String playerColor, Session session) throws IOException{
        connections.add(gameID, session);
        if (!playerColor.equals("NONE")) { // if the user is not just observing
            try {
                var message = String.format("%s has joined the game as " + playerColor, dataAccess.getUsername(authToken));
                var notification = new ServerMessage(message, ServerMessage.ServerMessageType.LOAD_GAME);
                connections.broadcast(gameID, session, notification);
            } catch (Exception ex) {
                System.out.println("Error getting the username or broadcasting the message");
            }
        }
    }

    private void resignGame(String authToken, int gameID, Session session) throws IOException{
        connections.add(gameID, session);
        try {
            var message = String.format("%s has forfeited the game!", dataAccess.getUsername(authToken));
            dataAccess.endGame(authToken, gameID);
            var notification = new ServerMessage(message, ServerMessage.ServerMessageType.NOTIFICATION);
            connections.broadcast(gameID, session, notification);
        } catch (DataAccessException ex){
            throw new IOException(ex.getMessage());
        }
    }

    private void leaveGame(String authToken, int gameID, String playerColor, Session session) throws IOException{
        connections.add(gameID, session);
        try {
            var message = String.format("%s has left the game", dataAccess.getUsername(authToken));
            dataAccess.addToGame(authToken, playerColor, gameID, true);
            connections.remove(gameID, session);
            var notification = new ServerMessage(message, ServerMessage.ServerMessageType.NOTIFICATION);
            connections.broadcast(gameID, session, notification);
        } catch (DataAccessException ex){
            throw new IOException(ex.getMessage());
        }
    }

    private void chessMove(String authToken, int gameID, String playerColor, ChessPosition startPosition, ChessPosition endPosition, Session session) throws IOException{
        connections.add(gameID, session);
        try {
            var message = String.format("%s moved a PieceType from startLocation to endLocation", dataAccess.getUsername(authToken));
            dataAccess.makeMove(authToken, gameID, new ChessMove(startPosition, endPosition, null));
            var notification = new ServerMessage(message, ServerMessage.ServerMessageType.LOAD_GAME);
            connections.broadcast(gameID, session, notification);
        } catch (Exception ex){
            throw new IOException(ex.getMessage());
        }
    }


}