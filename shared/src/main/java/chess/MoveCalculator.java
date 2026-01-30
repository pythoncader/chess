package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCalculator {
    public Collection<ChessMove> RookCalc(ChessBoard board, ChessPosition position){
        int col;
        int row;
        ChessPosition possible_position;
        ArrayList<ChessMove> possible_moves = new ArrayList<>();

        // check moves to the right until we hit the edge of the board
        for (col = position.getColumn()+1; col < 9; col++){
            possible_position = new ChessPosition(position.getRow(), col);
            if (board.getPiece(possible_position) == null){
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()){
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
                break;
            } else {
                //otherwise if the piece is the same as the rook we are moving we cannot move here
                break;
            }
        }

        //check moves to the left until we hit the edge of the board
        for (col = position.getColumn()-1; col > 0; col--){
            possible_position = new ChessPosition(position.getRow(), col);
            if (board.getPiece(possible_position) == null){
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()){
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
                break;
            } else {
                //otherwise if the piece is the same as the rook we are moving we cannot move here
                break;
            }
        }

        //check moves in front until we hit the edge of the board
        for (row = position.getRow()+1; row < 9; row++){
            possible_position = new ChessPosition(row, position.getColumn());
            if (board.getPiece(possible_position) == null){
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()){
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
                break;
            } else {
                //otherwise if the piece is the same as the rook we are moving we cannot move here
                break;
            }
        }

        //check moves behind until we hit the edge of the board
        for (row = position.getRow()-1; row > 0; row--){
            possible_position = new ChessPosition(row, position.getColumn());
            if (board.getPiece(possible_position) == null){
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()){
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
                break;
            } else {
                //otherwise if the piece is the same as the rook we are moving we cannot move here
                break;
            }
        }
        return possible_moves;
    }

    public Collection<ChessMove> BishopCalc(ChessBoard board, ChessPosition position){
        int col;
        int row;
        ChessPosition possible_position;
        ArrayList<ChessMove> possible_moves = new ArrayList<>();

        // check moves to the right and up until we hit the edge of the board
        row = position.getRow();
        for (col = position.getColumn()+1; col < 9; col++){
            row++;
            if (row > 8){
                break;
            }
            possible_position = new ChessPosition(row, col);
            if (board.getPiece(possible_position) == null){
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()){
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
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
            possible_position = new ChessPosition(row, col);
            if (board.getPiece(possible_position) == null){
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()){
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
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
            possible_position = new ChessPosition(row, col);
            if (board.getPiece(possible_position) == null){
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()){
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
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
            possible_position = new ChessPosition(row, col);
            if (board.getPiece(possible_position) == null){
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()){
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
                break;
            } else {
                //otherwise if the piece is the same as the one we are moving we cannot move here
                break;
            }
        }

        return possible_moves;
    }

    public Collection<ChessMove> QueenCalc(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> possible_moves = new ArrayList<>();
        possible_moves.addAll(BishopCalc(board, position));
        possible_moves.addAll(RookCalc(board, position));
        return possible_moves;
    }

    public Collection<ChessMove> KingCalc(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> possible_moves = new ArrayList<>();

        ChessPosition possible_position;
        int myRow;
        int myCol;

        // top left
        myRow = position.getRow()+1;
        myCol = position.getColumn()-1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
//        System.out.println(String.format("[%d, %d]", myRow, myCol));

        // top middle
        myRow = position.getRow()+1;
        myCol = position.getColumn();
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
        // System.out.println(String.format("[%d, %d]", myRow, myCol));

        // top right
        myRow = position.getRow()+1;
        myCol = position.getColumn()+1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        //right middle
        myRow = position.getRow();
        myCol = position.getColumn()+1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // bottom right
        myRow = position.getRow()-1;
        myCol = position.getColumn()+1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // bottom middle
        myRow = position.getRow()-1;
        myCol = position.getColumn();
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // bottom left
        myRow = position.getRow()-1;
        myCol = position.getColumn()-1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // left middle
        myRow = position.getRow();
        myCol = position.getColumn()-1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));


        return possible_moves;
    }

    public Collection<ChessMove> KnightCalc(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> possible_moves = new ArrayList<>();

        ChessPosition possible_position;
        int myRow;
        int myCol;

        // --
        // -
        // -
        myRow = position.getRow()+2;
        myCol = position.getColumn()+1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));


        // --
        //  -
        //  -
        myRow = position.getRow()+2;
        myCol = position.getColumn()-1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        //   -
        // ---
        myRow = position.getRow()+1;
        myCol = position.getColumn()+2;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // ---
        //   -
        myRow = position.getRow()-1;
        myCol = position.getColumn()+2;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // -
        // -
        // --
        myRow = position.getRow()-2;
        myCol = position.getColumn()+1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        //  -
        //  -
        // --
        myRow = position.getRow()-2;
        myCol = position.getColumn()-1;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // -
        // ---
        myRow = position.getRow()+1;
        myCol = position.getColumn()-2;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        // ---
        // -
        myRow = position.getRow()-1;
        myCol = position.getColumn()-2;
        if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
            possible_position = new ChessPosition(myRow, myCol);
            if (board.getPiece(possible_position) == null) {
                //there is nothing here, so we can move here or continue past it
                possible_moves.add(new ChessMove(position, possible_position, null));
            } else if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                //the piece is an enemy, so we can move here, but then we stop
                possible_moves.add(new ChessMove(position, possible_position, null));
            }
        }
        // system.out.println(String.format("[%d, %d]", myRow, myCol));

        return possible_moves;
    }


    public Collection<ChessMove> PawnCalc(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> possible_moves = new ArrayList<>();

        ChessPosition possible_position;
        int myRow;
        int myCol;

        if (board.getPiece(position).getTeamColor() == ChessGame.TeamColor.WHITE) {
            // pawn initial move
            if (position.getRow() == 2){
                myRow = position.getRow() + 1;
                myCol = position.getColumn();
                possible_position = new ChessPosition(myRow, myCol);
                if (board.getPiece(possible_position) == null) {
                    myRow = position.getRow() + 2;
                    possible_position = new ChessPosition(myRow, myCol);
                    if (board.getPiece(possible_position) == null) {
                        possible_moves.add(new ChessMove(position, possible_position, null));
                    }
                }
            }

            // top left
            myRow = position.getRow() + 1;
            myCol = position.getColumn() - 1;
            if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
                possible_position = new ChessPosition(myRow, myCol);
                if (board.getPiece(possible_position) != null) {
                    if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        //the piece is an enemy, so we can move here
                        if (myRow == 8) {
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.ROOK));
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.QUEEN));
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.BISHOP));
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.KNIGHT));
                        } else {
                            possible_moves.add(new ChessMove(position, possible_position, null));
                        }
                    }
                }
            }
            // system.out.println(String.format("[%d, %d]", myRow, myCol));

            // top middle
            myRow = position.getRow() + 1;
            myCol = position.getColumn();
            if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
                possible_position = new ChessPosition(myRow, myCol);
                if (board.getPiece(possible_position) == null) {
                    //there is nothing here, so we can move here
                    if (myRow == 8) {
                        possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.ROOK));
                        possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.QUEEN));
                        possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.BISHOP));
                        possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.KNIGHT));
                    } else {
                        possible_moves.add(new ChessMove(position, possible_position, null));
                    }
                }
            }
            // system.out.println(String.format("[%d, %d]", myRow, myCol));

            // top right
            myRow = position.getRow() + 1;
            myCol = position.getColumn() + 1;
            if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
                possible_position = new ChessPosition(myRow, myCol);
                if (board.getPiece(possible_position) != null) {
                    if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        //the piece is an enemy, so we can move here
                        if (myRow == 8) {
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.ROOK));
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.QUEEN));
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.BISHOP));
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.KNIGHT));
                        } else {
                            possible_moves.add(new ChessMove(position, possible_position, null));
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
                possible_position = new ChessPosition(myRow, myCol);
                if (board.getPiece(possible_position) == null) {
                    myRow = position.getRow() - 2;
                    possible_position = new ChessPosition(myRow, myCol);
                    if (board.getPiece(possible_position) == null) {
                        possible_moves.add(new ChessMove(position, possible_position, null));
                    }
                }
            }

            // bottom left
            myRow = position.getRow() - 1;
            myCol = position.getColumn() - 1;
            if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
                possible_position = new ChessPosition(myRow, myCol);
                if (board.getPiece(possible_position) != null) {
                    if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        //the piece is an enemy, so we can move here
                        if (myRow == 1) {
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.ROOK));
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.QUEEN));
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.BISHOP));
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.KNIGHT));
                        } else {
                            possible_moves.add(new ChessMove(position, possible_position, null));
                        }
                    }
                }
            }
            // system.out.println(String.format("[%d, %d]", myRow, myCol));

            // bottom middle
            myRow = position.getRow() - 1;
            myCol = position.getColumn();
            if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
                possible_position = new ChessPosition(myRow, myCol);
                if (board.getPiece(possible_position) == null) {
                    //there is nothing here, so we can move here
                    if (myRow == 1) {
                        possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.ROOK));
                        possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.QUEEN));
                        possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.BISHOP));
                        possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.KNIGHT));
                    } else {
                        possible_moves.add(new ChessMove(position, possible_position, null));
                    }
                }
            }
            // system.out.println(String.format("[%d, %d]", myRow, myCol));

            // bottom right
            myRow = position.getRow() - 1;
            myCol = position.getColumn() + 1;
            if (myRow < 9 && myRow > 0 && myCol < 9 && myCol > 0) {
                possible_position = new ChessPosition(myRow, myCol);
                if (board.getPiece(possible_position) != null) {
                    if (board.getPiece(possible_position).getTeamColor() != board.getPiece(position).getTeamColor()) {
                        //the piece is an enemy, so we can move here
                        if (myRow == 1) {
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.ROOK));
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.QUEEN));
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.BISHOP));
                            possible_moves.add(new ChessMove(position, possible_position, ChessPiece.PieceType.KNIGHT));
                        } else {
                            possible_moves.add(new ChessMove(position, possible_position, null));
                        }
                    }
                }
            }
            // system.out.println(String.format("[%d, %d]", myRow, myCol));
        }

        return possible_moves;
    }
}

