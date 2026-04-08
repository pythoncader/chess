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
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.ArrayList;

import static chess.ChessPiece.PieceType.*;

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
                    // re-serialize it as a MakeMoveCommand instead
                    chessMove(
                            moveCommand.getAuthToken(),
                            moveCommand.getGameID(),
                            moveCommand.getPlayerColor(),
                            moveCommand.getMoveString(),
                            moveCommand.getStartPosition(),
                            moveCommand.getEndPosition(),
                            moveCommand.getPromotionPiece(),
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
            System.out.println("Internal websocket error");
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
                sendError(session, ex);
            }
        }
    }

    private void resignGame(String authToken, int gameID, Session session) throws IOException{
        try {
            var message = String.format("%s has forfeited the game!", dataAccess.getUsername(authToken));
            dataAccess.endGame(authToken, gameID);
            var notification = new ServerMessage(message, ServerMessage.ServerMessageType.NOTIFICATION);
            connections.broadcast(gameID, session, notification);
        } catch (DataAccessException ex){
            sendError(session, ex);
        }
    }

    private void leaveGame(String authToken, int gameID, String playerColor, Session session ) throws IOException{
        try {
            var message = String.format("%s has left the game", dataAccess.getUsername(authToken));
            dataAccess.addToGame(authToken, playerColor, gameID, true);
            connections.remove(gameID, session);
            var notification = new ServerMessage(message, ServerMessage.ServerMessageType.NOTIFICATION);
            connections.broadcast(gameID, session, notification);

        } catch (DataAccessException ex){
            sendError(session, ex);
        }
    }

    private void chessMove(String authToken,
                           int gameID,
                           String playerColor,
                           String moveString,
                           ChessPosition startPosition,
                           ChessPosition endPosition,
                           ChessPiece.PieceType promotionPiece,
                           Session session) throws IOException {
        try {
            var message = dataAccess.getUsername(authToken) + moveString;
            dataAccess.makeMove(authToken, gameID, playerColor, new ChessMove(startPosition, endPosition, promotionPiece));
            ArrayList<GameData> gameList = dataAccess.listGames(authToken);
            ChessGame.TeamColor oppositeColor;
            if (playerColor.equals("WHITE")) {
                oppositeColor = ChessGame.TeamColor.BLACK;
            } else {
                oppositeColor = ChessGame.TeamColor.WHITE;
            }

            var notification = new ServerMessage(message, ServerMessage.ServerMessageType.LOAD_GAME);
            connections.broadcast(gameID, session, notification);

            for (GameData gameData : gameList){
                if (gameData.gameID() == gameID){
                    if (gameData.game().isInCheckmate(oppositeColor)) {
                        dataAccess.endGame(authToken, gameID);
                        notification = new ServerMessage(String.format("%s is in checkmate, %s wins",
                                oppositeColor.toString().toLowerCase(),
                                playerColor.toLowerCase()),
                                ServerMessage.ServerMessageType.NOTIFICATION);
                        connections.broadcast(gameID, null, notification);
                    } else if (gameData.game().isInCheck(oppositeColor)){
                        notification = new ServerMessage(String.format("%s is in check",
                                oppositeColor.toString().toLowerCase()),
                                ServerMessage.ServerMessageType.NOTIFICATION);
                        connections.broadcast(gameID, null, notification);
                    } else if (gameData.game().isInStalemate(oppositeColor)){
                        dataAccess.endGame(authToken, gameID);
                        notification = new ServerMessage("The game ended in a stalemate",
                                ServerMessage.ServerMessageType.NOTIFICATION);
                        connections.broadcast(gameID, null, notification);
                    }
                }
            }


        } catch (InvalidMoveException | DataAccessException ex){
            sendError(session, ex);
        }
    }

    private static void sendError(Session session, Exception ex) throws IOException {
        var error = new ServerMessage(ex.getMessage(), ServerMessage.ServerMessageType.ERROR);
        String msg = new Gson().toJson(error);
        session.getRemote().sendString(msg);
    }

}