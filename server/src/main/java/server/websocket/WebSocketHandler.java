package server.websocket;

import chess.*;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import io.javalin.websocket.*;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

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
                    joinGame(command.getAuthToken(), command.getGameID(), ctx.session);
                    break;
                case MAKE_MOVE:
                    MakeMoveCommand moveCommand = new Gson().fromJson(ctx.message(), MakeMoveCommand.class);
                    // re-serialize it as a MakeMoveCommand instead
                    chessMove(
                            moveCommand.getAuthToken(),
                            moveCommand.getGameID(),
                            moveCommand.getMoveString(),
                            moveCommand.getMove(),
                            ctx.session
                            );
                    break;
                case LEAVE:
                    leaveGame(command.getAuthToken(), command.getGameID(), ctx.session);
                    break;
                case RESIGN:
                    resignGame(command.getAuthToken(), command.getGameID(), ctx.session);
                    break;
            }
        } catch (IOException ex) {
            System.out.println("Internal websocket error");
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }


    private String getPlayerColor(String authToken, int gameID){
        try {
            ArrayList<GameData> gameList = dataAccess.listGames(authToken);
            for (GameData gameData : gameList) {
                if (gameData.gameID() == gameID) {
//                    System.out.println(dataAccess.getUsername(authToken));
                    if (gameData.whiteUsername() != null){
                        if (gameData.whiteUsername().equals(dataAccess.getUsername(authToken))) {
                            return "WHITE";
                        }
                    }
                    if (gameData.blackUsername() != null) {
                        if (gameData.blackUsername().equals(dataAccess.getUsername(authToken))) {
                            return "BLACK";
                        }
                    }
                    return "NONE";
                }
            }
            return null;
        } catch (Exception ex){
            return null;
        }
    }

    private GameData getGameData(String authToken, int gameID){
        try {
            ArrayList<GameData> gameList = dataAccess.listGames(authToken);
            for (GameData gameData : gameList) {
                if (gameData.gameID() == gameID) {
                    return gameData;
                }
            }
            return null;
        } catch (Exception ex){
            return null;
        }
    }

    private void joinGame(String authToken, int gameID, Session session) throws IOException{
        String playerColor = getPlayerColor(authToken, gameID);
        connections.add(gameID, session);
        try {
            String message = String.format("%s has joined the game as an observer", dataAccess.getUsername(authToken));;
            if (playerColor != null) {
                if (!playerColor.equals("NONE")) { // if the user is not just observing
                    message = String.format("%s has joined the game as " + playerColor, dataAccess.getUsername(authToken));
                }
            }

            GameData myGameData = getGameData(authToken, gameID);
            if (myGameData == null){
                sendError(session, new DataAccessException("Invalid Game ID!"));
            } else {
                var loadGame = new LoadGameMessage(null, ServerMessage.ServerMessageType.LOAD_GAME, myGameData.game());
                connections.sendLoadGameMessage(session, loadGame);
                var notification = new ServerMessage(message, ServerMessage.ServerMessageType.NOTIFICATION);
                connections.broadcast(gameID, session, notification);
            }
        } catch (Exception ex) {
            sendError(session, ex);
        }
    }

    private void resignGame(String authToken, int gameID, Session session) throws IOException{
        if (Objects.equals(getPlayerColor(authToken, gameID), "NONE")){
            sendError(session, new DataAccessException("An observer cannot resign from the game!"));
        } else {
            GameData myGameData = getGameData(authToken, gameID);
            if (myGameData != null){
                if (myGameData.game().getTeamTurn() != ChessGame.TeamColor.NONE){
                    try {
                        var message = String.format("%s has forfeited the game!", dataAccess.getUsername(authToken));
                        dataAccess.endGame(authToken, gameID);
                        var notification = new ServerMessage(message, ServerMessage.ServerMessageType.NOTIFICATION);
                        connections.broadcast(gameID, null, notification);
                    } catch (DataAccessException ex) {
                        sendError(session, ex);
                    }
                } else {
                    sendError(session, new DataAccessException("The game is already over!"));
                }
            }
        }
    }

    private void leaveGame(String authToken, int gameID, Session session ) throws IOException{
        String playerColor = getPlayerColor(authToken, gameID);
        if (playerColor != null) {
            connections.remove(gameID, session);
            try {
                String message;
                if (!playerColor.equals("NONE")) {
                    message = String.format("%s has left the game", dataAccess.getUsername(authToken));
                    dataAccess.addToGame(authToken, playerColor, gameID, true);

                } else {
                    message = String.format("%s has stopped observing the game", dataAccess.getUsername(authToken));
                }
                var notification = new ServerMessage(message, ServerMessage.ServerMessageType.NOTIFICATION);
                connections.broadcast(gameID, session, notification);
            } catch (DataAccessException ex) {
                sendError(session, ex);
            }
        }
    }

    private void chessMove(String authToken,
                           int gameID,
                           String moveString,
                           ChessMove move,
                           Session session) throws IOException {
        String playerColor = getPlayerColor(authToken, gameID);
        try {
            var message = dataAccess.getUsername(authToken) + moveString;
            dataAccess.makeMove(authToken, gameID, playerColor, move);
            GameData myGameData = getGameData(authToken, gameID);
            if (myGameData == null){
                sendError(session, new DataAccessException("Invalid Game ID!"));
            } else {
                var loadGame = new LoadGameMessage(null, ServerMessage.ServerMessageType.LOAD_GAME, myGameData.game());
                connections.broadcastLoadGameMessage(gameID, null, loadGame);

                var notification = new ServerMessage(message, ServerMessage.ServerMessageType.NOTIFICATION);
                connections.broadcast(gameID, session, notification);

                ChessGame.TeamColor oppositeColor;
                if (playerColor.equals("WHITE")) {
                    oppositeColor = ChessGame.TeamColor.BLACK;
                } else {
                    oppositeColor = ChessGame.TeamColor.WHITE;
                }

                if (myGameData.game().isInCheckmate(oppositeColor)) {
                    dataAccess.endGame(authToken, gameID);
                    var newNotification = new ServerMessage(String.format("%s is in checkmate, %s wins",
                            oppositeColor.toString().toLowerCase(),
                            playerColor.toLowerCase()),
                            ServerMessage.ServerMessageType.NOTIFICATION);
                    connections.broadcast(gameID, null, newNotification);
                } else if (myGameData.game().isInCheck(oppositeColor)){
                    var newNotification = new ServerMessage(String.format("%s is in check",
                            oppositeColor.toString().toLowerCase()),
                            ServerMessage.ServerMessageType.NOTIFICATION);
                    connections.broadcast(gameID, null, newNotification);
                } else if (myGameData.game().isInStalemate(oppositeColor)){
                    dataAccess.endGame(authToken, gameID);
                    var newNotification = new ServerMessage("The game ended in a stalemate",
                            ServerMessage.ServerMessageType.NOTIFICATION);
                    connections.broadcast(gameID, null, newNotification);
                }
            }
        } catch (InvalidMoveException | DataAccessException ex){
            sendError(session, ex);
        }
    }

    private static void sendError(Session session, Exception ex) throws IOException {
        var error = new ErrorMessage(null, ServerMessage.ServerMessageType.ERROR, ex.getMessage());
        String msg = new Gson().toJson(error);
        session.getRemote().sendString(msg);
    }

}