package websocket.commands;

import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

public class MakeMoveCommand extends UserGameCommand{
    private final ChessMove move;
    private final String moveString;

    public ChessMove getMove() {
        return move;
    }

    public String getMoveString() {
        return moveString;
    }

    public MakeMoveCommand(CommandType commandType,
                           String authToken,
                           Integer gameID,
                           String moveString,
                           ChessMove move)
    {
        super(commandType, authToken, gameID);
        this.move = move;
        this.moveString = moveString;
    }
}
