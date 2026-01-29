package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor currentTurn = TeamColor.WHITE;
    private ChessBoard myBoard = new ChessBoard();


    public ChessGame() {
        this.myBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.currentTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition){
        Collection<ChessMove> myValidMoves = this.myBoard.getPiece(startPosition).pieceMoves(this.myBoard, startPosition);
        this.myBoard = this.myBoard.clone();
        for (int i = 0; i < myValidMoves.size(); i++){
            if (isInCheck(this.myBoard.getPiece(startPosition).getTeamColor())){

            }
        }
        return myValidMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece myPiece = myBoard.getPiece(move.getStartPosition());
        if (myPiece != null){
            if (myPiece.getTeamColor() != this.currentTurn){
                throw new InvalidMoveException("You can't move out of turn");
            }
            Collection<ChessMove> myMoves = validMoves(move.getStartPosition());

            if (myMoves.contains(move)){
                if (myPiece.getPieceType() == ChessPiece.PieceType.PAWN && move.getPromotionPiece() != null){
                    myBoard.addPiece(move.getEndPosition(), new ChessPiece(myPiece.getTeamColor(), move.getPromotionPiece()));
                } else {
                    myBoard.addPiece(move.getEndPosition(), myBoard.getPiece(move.getStartPosition()));
                }
                myBoard.addPiece(move.getStartPosition(), null);
                if (this.currentTurn == TeamColor.WHITE) {
                    this.setTeamTurn(TeamColor.BLACK);
                } else {
                    this.setTeamTurn(TeamColor.WHITE);
                }
            } else {
                throw new InvalidMoveException("You can't move to this square: " + move.getEndPosition());
            }
        } else { // if there is no piece here on the board
            throw new InvalidMoveException("There is no piece at this position");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.myBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.myBoard;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return currentTurn == chessGame.currentTurn && Objects.equals(myBoard, chessGame.myBoard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currentTurn, myBoard);
    }
}
