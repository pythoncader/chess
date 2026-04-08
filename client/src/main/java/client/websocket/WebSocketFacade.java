package client.websocket;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import exception.ResponseException;
import jakarta.websocket.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static chess.ChessPiece.PieceType.*;
import static chess.ChessPiece.PieceType.ROOK;

public class WebSocketFacade extends Endpoint{
    public Session session;
    ServerMessageHandler serverMessageHandler;

    public WebSocketFacade(int port, ServerMessageHandler serverMessageHandler) throws ResponseException {
        try {
            URI uri = new URI("ws://localhost:" + port + "/ws");
            this.serverMessageHandler = serverMessageHandler;
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    serverMessageHandler.notify(notification);
                }
            });

        } catch (DeploymentException | IOException | URISyntaxException ex){
            throw new ResponseException(ResponseException.Code.ServerError, "There was a problem sending a message to the server");
        }
    }

    public void connectSocket(String authToken, int gameID, String playerColor) throws ResponseException {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, playerColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex){
            throw new ResponseException(ResponseException.Code.ServerError, "There was a problem sending a message to the server");
        }
    }

    public void resign(String authToken, int gameID) throws ResponseException {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID, null);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex){
            throw new ResponseException(ResponseException.Code.ServerError, "There was a problem sending a message to the server");
        }
    }

    public void leave(String authToken, int gameID, String playerColor) throws ResponseException {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID, playerColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex){
            throw new ResponseException(ResponseException.Code.ServerError, "There was a problem sending a message to the server");
        }
    }

    public void observe(String authToken, int gameID) throws ResponseException {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID, "NONE");
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, "There was a problem sending a message to the server");
        }
    }

    public void makeMove(String authToken, int gameID, String playerColor, String moveString, ChessPosition startPosition, ChessPosition endPosition, String promotionPieceString) throws ResponseException {
        try {
            ChessPiece.PieceType promotionPiece = null;
            if (promotionPieceString != null) {
                promotionPiece = switch (promotionPieceString) {
                    case "queen" -> QUEEN;
                    case "knight" -> KNIGHT;
                    case "bishop" -> BISHOP;
                    case "rook" -> ROOK;
                    default -> null;
                };
            }
            var command = new MakeMoveCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, gameID, playerColor, moveString, startPosition, endPosition, promotionPiece);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException ex) {
            throw new ResponseException(ResponseException.Code.ServerError, "There was a problem sending a message to the other users");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig){}
}
