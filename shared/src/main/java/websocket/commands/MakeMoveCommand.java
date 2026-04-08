package websocket.commands;

import chess.ChessPiece;
import chess.ChessPosition;

public class MakeMoveCommand extends UserGameCommand{
    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final String moveString;

    public ChessPiece.PieceType getPromotionPiece() {
        return promotionPiece;
    }

    private final ChessPiece.PieceType promotionPiece;

    public ChessPosition getStartPosition() {
        return startPosition;
    }

    public ChessPosition getEndPosition() {
        return endPosition;
    }

    public String getMoveString() {
        return moveString;
    }

    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID, String playerColor, String moveString, ChessPosition startPosition, ChessPosition endPosition, ChessPiece.PieceType promotionPiece) {
        super(commandType, authToken, gameID, playerColor);
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.moveString = moveString;
        this.promotionPiece = promotionPiece;
    }
}
