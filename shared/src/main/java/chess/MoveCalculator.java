package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCalculator {
    private boolean isOnBoard(int row, int col){
        return row >= 1 && row <= 8 && col >= 1 && col <= 8;
    }

    private Collection<ChessMove> checkPromotionPieces(ChessPosition position, ChessPosition possiblePosition, int myRow){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        if (myRow == 8 || myRow == 1) {
            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.ROOK));
            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.QUEEN));
            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.BISHOP));
            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.KNIGHT));
        } else {
            possibleMoves.add(new ChessMove(position, possiblePosition, null));
        }
        return possibleMoves;
    }

    private Collection<ChessMove> addAdjacentMoves(ChessBoard board, ChessPosition position, int vertical, int horizontal){
        int myRow = position.getRow() + vertical;
        int myCol = position.getColumn() + horizontal;
        ChessPosition possiblePosition;
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        if (isOnBoard(myRow, myCol)) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        return possibleMoves;
    }

    private Collection<ChessMove> addCapturePawnMove(ChessBoard board, ChessPosition position, int vertical, int horizontal){
        int myRow = position.getRow() + vertical;
        int myCol = position.getColumn() + horizontal;
        ChessPosition possiblePosition;
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        if (isOnBoard(myRow, myCol)) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) != null) {
                if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                    //the piece is an enemy, so we can move here
                    possibleMoves.addAll(checkPromotionPieces(position, possiblePosition, myRow));
                }
            }
        }

        return possibleMoves;
    }

    private Collection<ChessMove> addForwardPawnMove(ChessBoard board, ChessPosition position, int vertical){
        int myRow = position.getRow() + vertical;
        int myCol = position.getColumn();
        ChessPosition possiblePosition;
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here
                possibleMoves.addAll(checkPromotionPieces(position, possiblePosition, myRow));
            }
        }

        return possibleMoves;
    }

    private Collection<ChessMove> addSlideMoves(ChessBoard board, ChessPosition position, int rowIncrement, int colIncrement) {
        ChessPosition possiblePosition;
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        int row = position.getRow() + rowIncrement;
        int col = position.getColumn() + colIncrement;

        while (isOnBoard(row, col)){
            possiblePosition = new ChessPosition(row, col);
            if (board.getPiece(possiblePosition) == null){
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
                break;
            } else {
                //otherwise if the piece is the same as the rook we are moving we cannot move here
                break;
            }
            col += colIncrement;
            row += rowIncrement;
        }
        return possibleMoves;
    }

    public Collection<ChessMove> rookCalc(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        possibleMoves.addAll(addSlideMoves(board, position, 0, 1)); // moves right
        possibleMoves.addAll(addSlideMoves(board, position, 0, -1)); // moves left
        possibleMoves.addAll(addSlideMoves(board, position, 1, 0)); // moves up
        possibleMoves.addAll(addSlideMoves(board, position, -1, 0)); // moves down

        return possibleMoves;
    }

    public Collection<ChessMove> bishopCalc(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        possibleMoves.addAll(addSlideMoves(board, position, 1, 1)); // moves right
        possibleMoves.addAll(addSlideMoves(board, position, -1, -1)); // moves left
        possibleMoves.addAll(addSlideMoves(board, position, 1, -1)); // moves up
        possibleMoves.addAll(addSlideMoves(board, position, -1, 1)); // moves down

        return possibleMoves;
    }

    public Collection<ChessMove> queenCalc(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        possibleMoves.addAll(bishopCalc(board, position));
        possibleMoves.addAll(rookCalc(board, position));
        return possibleMoves;
    }

    public Collection<ChessMove> kingCalc(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        possibleMoves.addAll(addAdjacentMoves(board, position, 1, -1));
        possibleMoves.addAll(addAdjacentMoves(board, position, 1, 0));
        possibleMoves.addAll(addAdjacentMoves(board, position, 1, 1));
        possibleMoves.addAll(addAdjacentMoves(board, position, 0, 1));
        possibleMoves.addAll(addAdjacentMoves(board, position, -1, 1));
        possibleMoves.addAll(addAdjacentMoves(board, position, -1, 0));
        possibleMoves.addAll(addAdjacentMoves(board, position, -1, -1));
        possibleMoves.addAll(addAdjacentMoves(board, position, 0, -1));

        return possibleMoves;
    }

    public Collection<ChessMove> knightCalc(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        // --
        // -
        // -
        possibleMoves.addAll(addAdjacentMoves(board, position, 2, 1));

        // --
        //  -
        //  -
        possibleMoves.addAll(addAdjacentMoves(board, position, 2, -1));

        //   -
        // ---
        possibleMoves.addAll(addAdjacentMoves(board, position, 1, 2));

        // ---
        //   -
        possibleMoves.addAll(addAdjacentMoves(board, position, -1, 2));

        // -
        // -
        // --
        possibleMoves.addAll(addAdjacentMoves(board, position, -2, 1));

        //  -
        //  -
        // --
        possibleMoves.addAll(addAdjacentMoves(board, position, -2, -1));

        // -
        // ---
        possibleMoves.addAll(addAdjacentMoves(board, position, 1, -2));

        // ---
        // -
        possibleMoves.addAll(addAdjacentMoves(board, position, -1, -2));

        return possibleMoves;
    }


    public Collection<ChessMove> pawnCalc(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        ChessPosition possiblePosition;
        int myRow;
        int myCol;

        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            // pawn initial move
            if (position.getRow() == 2){
                myRow = position.getRow() + 1;
                myCol = position.getColumn();
                possiblePosition = new ChessPosition(myRow, myCol);
                if (board.getPiece(possiblePosition) == null) {
                    myRow = position.getRow() + 2;
                    possiblePosition = new ChessPosition(myRow, myCol);
                    if (board.getPiece(possiblePosition) == null) {
                        possibleMoves.add(new ChessMove(position, possiblePosition, null));
                    }
                }
            }

            // top left
            possibleMoves.addAll(addCapturePawnMove(board, position, 1, -1));

            // top middle
            possibleMoves.addAll(addForwardPawnMove(board, position, 1));

            // top right
            possibleMoves.addAll(addCapturePawnMove(board, position, 1, 1));

        } else {
            // pawn initial move
            if (position.getRow() == 7){
                myRow = position.getRow() - 1;
                myCol = position.getColumn();
                possiblePosition = new ChessPosition(myRow, myCol);
                if (board.getPiece(possiblePosition) == null) {
                    myRow = position.getRow() - 2;
                    possiblePosition = new ChessPosition(myRow, myCol);
                    if (board.getPiece(possiblePosition) == null) {
                        possibleMoves.add(new ChessMove(position, possiblePosition, null));
                    }
                }
            }

            // bottom left
            possibleMoves.addAll(addCapturePawnMove(board, position, -1, -1));

            // bottom middle
            possibleMoves.addAll(addForwardPawnMove(board, position, -1));

            // bottom right
            possibleMoves.addAll(addCapturePawnMove(board, position, -1, 1));
        }

        return possibleMoves;
    }
}

