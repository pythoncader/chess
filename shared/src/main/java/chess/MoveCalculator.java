package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCalculator {
    public Collection<ChessMove> rookCalc(ChessBoard board, ChessPosition position){
        int col;
        int row;
        ChessPosition possiblePosition;
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        // check moves to the right until we hit the edge of the board
        for (col = position.getColumn()+1; col < 9; col++){
            possiblePosition = new ChessPosition(position.getRow(), col);
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
        }

        //check moves to the left until we hit the edge of the board
        for (col = position.getColumn()-1; col > 0; col--){
            possiblePosition = new ChessPosition(position.getRow(), col);
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
        }

        //check moves in front until we hit the edge of the board
        for (row = position.getRow()+1; row < 9; row++){
            possiblePosition = new ChessPosition(row, position.getColumn());
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
        }

        //check moves behind until we hit the edge of the board
        for (row = position.getRow()-1; row > 0; row--){
            possiblePosition = new ChessPosition(row, position.getColumn());
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
        }
        return possibleMoves;
    }

    public Collection<ChessMove> bishopCalc(ChessBoard board, ChessPosition position){
        int col;
        int row;
        ChessPosition possiblePosition;
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        // check moves to the right and up until we hit the edge of the board
        row = position.getRow();
        for (col = position.getColumn()+1; col < 9; col++){
            row++;
            if (row > 8){
                break;
            }
            possiblePosition = new ChessPosition(row, col);
            if (board.getPiece(possiblePosition) == null){
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
                break;
            } else {
                //otherwise if the piece is the same as the one we are moving we cannot move here
                break;
            }
        }

        // check moves to the left and up until we hit the edge of the board
        row = position.getRow();
        for (col = position.getColumn()-1; col > 0; col--){
            row++;
            if (row > 8){
                break;
            }
            possiblePosition = new ChessPosition(row, col);
            if (board.getPiece(possiblePosition) == null){
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
                break;
            } else {
                //otherwise if the piece is the same as the one we are moving we cannot move here
                break;
            }
        }

        // check moves to the right and down until we hit the edge of the board
        row = position.getRow();
        for (col = position.getColumn()+1; col < 9; col++){
            row--;
            if (row < 1){
                break;
            }
            possiblePosition = new ChessPosition(row, col);
            if (board.getPiece(possiblePosition) == null){
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
                break;
            } else {
                //otherwise if the piece is the same as the one we are moving we cannot move here
                break;
            }
        }

        // check moves to the left and down until we hit the edge of the board
        row = position.getRow();
        for (col = position.getColumn()-1; col > 0; col--){
            row--;
            if (row < 1){
                break;
            }
            possiblePosition = new ChessPosition(row, col);
            if (board.getPiece(possiblePosition) == null){
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()){
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
                break;
            } else {
                //otherwise if the piece is the same as the one we are moving we cannot move here
                break;
            }
        }

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

        ChessPosition possiblePosition;
        int myRow;
        int myCol;

        // top left
        myRow = position.getRow()+1;
        myCol = position.getColumn()-1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
//        System.out.println(String.format("[%d, %d]", myRow, myCol));

        // top middle
        myRow = position.getRow()+1;
        myCol = position.getColumn();
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        // System.out.println(String.format("[%d, %d]", myRow, myCol));

        // top right
        myRow = position.getRow()+1;
        myCol = position.getColumn()+1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        //right middle
        myRow = position.getRow();
        myCol = position.getColumn()+1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // bottom right
        myRow = position.getRow()-1;
        myCol = position.getColumn()+1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // bottom middle
        myRow = position.getRow()-1;
        myCol = position.getColumn();
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // bottom left
        myRow = position.getRow()-1;
        myCol = position.getColumn()-1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // left middle
        myRow = position.getRow();
        myCol = position.getColumn()-1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));


        return possibleMoves;
    }

    public Collection<ChessMove> knightCalc(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();

        ChessPosition possiblePosition;
        int myRow;
        int myCol;

        // --
        // -
        // -
        myRow = position.getRow()+2;
        myCol = position.getColumn()+1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));


        // --
        //  -
        //  -
        myRow = position.getRow()+2;
        myCol = position.getColumn()-1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        //   -
        // ---
        myRow = position.getRow()+1;
        myCol = position.getColumn()+2;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // ---
        //   -
        myRow = position.getRow()-1;
        myCol = position.getColumn()+2;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // -
        // -
        // --
        myRow = position.getRow()-2;
        myCol = position.getColumn()+1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        //  -
        //  -
        // --
        myRow = position.getRow()-2;
        myCol = position.getColumn()-1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // -
        // ---
        myRow = position.getRow()+1;
        myCol = position.getColumn()-2;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // ---
        // -
        myRow = position.getRow()-1;
        myCol = position.getColumn()-2;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possiblePosition = new ChessPosition(myRow, myCol);
            if (board.getPiece(possiblePosition) == null) {
                //there is nothing here, so we can move here or continue past it
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            } else if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possibleMoves.add(new ChessMove(position, possiblePosition, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

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
            myRow = position.getRow() + 1;
            myCol = position.getColumn() - 1;
            if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
                possiblePosition = new ChessPosition(myRow, myCol);
                if (board.getPiece(possiblePosition) != null) {
                    if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        //the piece is an enemy, so we can move here
                        if (myRow == 8) {
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.ROOK));
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.QUEEN));
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.BISHOP));
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.KNIGHT));
                        } else {
                            possibleMoves.add(new ChessMove(position, possiblePosition, null));
                        }
                    }
                }
            }
            // system.out.println(String.format("[%d, %d]", myRow, myCol));

            // top middle
            myRow = position.getRow() + 1;
            myCol = position.getColumn();
            if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
                possiblePosition = new ChessPosition(myRow, myCol);
                if (board.getPiece(possiblePosition) == null) {
                    //there is nothing here, so we can move here
                    if (myRow == 8) {
                        possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.ROOK));
                        possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.QUEEN));
                        possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        possibleMoves.add(new ChessMove(position, possiblePosition, null));
                    }
                }
            }
            // system.out.println(String.format("[%d, %d]", myRow, myCol));

            // top right
            myRow = position.getRow() + 1;
            myCol = position.getColumn() + 1;
            if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
                possiblePosition = new ChessPosition(myRow, myCol);
                if (board.getPiece(possiblePosition) != null) {
                    if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        //the piece is an enemy, so we can move here
                        if (myRow == 8) {
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.ROOK));
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.QUEEN));
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.BISHOP));
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.KNIGHT));
                        } else {
                            possibleMoves.add(new ChessMove(position, possiblePosition, null));
                        }
                    }
                }
            }
            // system.out.println(String.format("[%d, %d]", myRow, myCol));
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
            myRow = position.getRow() - 1;
            myCol = position.getColumn() - 1;
            if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
                possiblePosition = new ChessPosition(myRow, myCol);
                if (board.getPiece(possiblePosition) != null) {
                    if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        //the piece is an enemy, so we can move here
                        if (myRow == 1) {
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.ROOK));
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.QUEEN));
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.BISHOP));
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.KNIGHT));
                        } else {
                            possibleMoves.add(new ChessMove(position, possiblePosition, null));
                        }
                    }
                }
            }
            // system.out.println(String.format("[%d, %d]", myRow, myCol));

            // bottom middle
            myRow = position.getRow() - 1;
            myCol = position.getColumn();
            if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
                possiblePosition = new ChessPosition(myRow, myCol);
                if (board.getPiece(possiblePosition) == null) {
                    //there is nothing here, so we can move here
                    if (myRow == 1) {
                        possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.ROOK));
                        possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.QUEEN));
                        possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.BISHOP));
                        possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.KNIGHT));
                    } else {
                        possibleMoves.add(new ChessMove(position, possiblePosition, null));
                    }
                }
            }
            // system.out.println(String.format("[%d, %d]", myRow, myCol));

            // bottom right
            myRow = position.getRow() - 1;
            myCol = position.getColumn() + 1;
            if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
                possiblePosition = new ChessPosition(myRow, myCol);
                if (board.getPiece(possiblePosition) != null) {
                    if (board.getPiece(possiblePosition).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        //the piece is an enemy, so we can move here
                        if (myRow == 1) {
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.ROOK));
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.QUEEN));
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.BISHOP));
                            possibleMoves.add(new ChessMove(position, possiblePosition, ChessPiece.PieceType.KNIGHT));
                        } else {
                            possibleMoves.add(new ChessMove(position, possiblePosition, null));
                        }
                    }
                }
            }
            // system.out.println(String.format("[%d, %d]", myRow, myCol));
        }

        return possibleMoves;
    }
}

