package websocket.commands;

import chess.ChessPosition;

public class MakeMoveCommand extends UserGameCommand{
    private final ChessPosition startPosition;
    private final ChessPosition endPosition;

    public ChessPosition getStartPosition() {
        return startPosition;
    }

    public ChessPosition getEndPosition() {
        return endPosition;
    }

    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID, String playerColor, ChessPosition startPosition, ChessPosition endPosition) {
        super(commandType, authToken, gameID, playerColor);
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }
}
