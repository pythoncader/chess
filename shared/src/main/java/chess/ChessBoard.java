package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    ChessPiece[][] squares = new ChessPiece[8][8];
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];
        ChessPosition position1;
        ChessPiece mypiece;
        for (int i = 1; i < 9; i++) { // add both the white and the black pawns
            position1 = new ChessPosition(2, i);
            mypiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
            this.addPiece(position1, mypiece);
            System.out.println("Added pawn at " + position1);
            position1 = new ChessPosition(7, i);
            mypiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN);
            this.addPiece(position1, mypiece);
        }

        //add the rooks
        position1 = new ChessPosition(1, 1);
        mypiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        this.addPiece(position1, mypiece);
        position1 = new ChessPosition(1, 8);
        mypiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK);
        this.addPiece(position1, mypiece);

        position1 = new ChessPosition(8, 1);
        mypiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        this.addPiece(position1, mypiece);
        position1 = new ChessPosition(8, 8);
        mypiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK);
        this.addPiece(position1, mypiece);

        position1 = new ChessPosition(1, 2);
        mypiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        this.addPiece(position1, mypiece);
        position1 = new ChessPosition(1, 7);
        mypiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT);
        this.addPiece(position1, mypiece);

        position1 = new ChessPosition(8, 2);
        mypiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        this.addPiece(position1, mypiece);
        position1 = new ChessPosition(8, 7);
        mypiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT);
        this.addPiece(position1, mypiece);

        position1 = new ChessPosition(1, 3);
        mypiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        this.addPiece(position1, mypiece);
        position1 = new ChessPosition(1, 6);
        mypiece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP);
        this.addPiece(position1, mypiece);

        position1 = new ChessPosition(8, 3);
        mypiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        this.addPiece(position1, mypiece);
        position1 = new ChessPosition(8, 6);
        mypiece = new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP);
        this.addPiece(position1, mypiece);

    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }
}
