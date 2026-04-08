package websocket.messages;

import chess.ChessGame;

public class LoadGameMessage extends ServerMessage{
    public ChessGame getGame() {
        return game;
    }

    ChessGame game;
    public LoadGameMessage(String message, ServerMessageType type, ChessGame game) {
        super(message, type);
        this.message = message;
        this.serverMessageType = type;
        this.game = game;
    }
}
