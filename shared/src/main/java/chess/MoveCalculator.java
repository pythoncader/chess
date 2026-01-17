package chess;

import java.util.ArrayList;
import java.util.Collection;

public class MoveCalculator {
    public Collection<ChessMove> right(ChessBoard board, ChessPosition myPosition){
        int current_row = myPosition.getRow();
        int current_col = myPosition.getColumn();
        ChessPosition possible_space;
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();
        int col;
        for (col = current_col+1; col < 9; col++){ //check possible moves to the right of the rook
            possible_space = new ChessPosition(current_row, col);
            if (board.getPiece(possible_space) == null){
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
            }else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()){ //if the piece is on our team, we can't move there
                break;
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()){ //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                break;
            }
        }
        return possible_moves;
    }

    public Collection<ChessMove> left(ChessBoard board, ChessPosition myPosition){
        int current_row = myPosition.getRow();
        int current_col = myPosition.getColumn();
        System.out.println(myPosition);
        System.out.println(board);
        ChessPosition possible_space;
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();
        int col;
        for (col = current_col-1; col > 0; col--){ //check possible moves to the left of the rook
            possible_space = new ChessPosition(current_row, col);
            if (board.getPiece(possible_space) == null){
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
            }else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()){ //if the piece is on our team, we can't move there
                break;
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()){ //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                break;
            }
        }
        return possible_moves;
    }

    public Collection<ChessMove> forward(ChessBoard board, ChessPosition myPosition){
        int current_row = myPosition.getRow();
        int current_col = myPosition.getColumn();
        ChessPosition possible_space;
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();
        int row;
        for (row = current_row-1; row > 0; row--){ //check possible moves to the right of the rook
            possible_space = new ChessPosition(row, current_col);
            if (board.getPiece(possible_space) == null){
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
            }else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()){ //if the piece is on our team, we can't move there
                break;
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()){ //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                break;
            }
        }
        return possible_moves;
    }

    public Collection<ChessMove> backward(ChessBoard board, ChessPosition myPosition){
        int current_row = myPosition.getRow();
        int current_col = myPosition.getColumn();
        ChessPosition possible_space;
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();
        int row;
        for (row = current_row+1; row < 9; row++){ //check possible moves to the right of the rook
            possible_space = new ChessPosition(row, current_col);
            if (board.getPiece(possible_space) == null){
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
            }else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()){ //if the piece is on our team, we can't move there
                break;
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()){ //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                break;
            }
        }
        return possible_moves;
    }

    public Collection<ChessMove> diagonal_backward_right(ChessBoard board, ChessPosition myPosition){
        int current_row = myPosition.getRow();
        int current_col = myPosition.getColumn();
        ChessPosition possible_space;
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();
        int row = current_row+1;
        for (int col = current_col+1; col < 9; col++){ // increment column towards the right of the chessboard
            if (row == 9){
                break;
            }
            possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) == null){
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("backward_right: [%s, %s]", row, col));
            }else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()){ //if the piece is on our team, we can't move there
                break;
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()){ //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("backward_right: [%s, %s]", row, col));
                break;
            }
            row++;
        }
        return possible_moves;
    }
    public Collection<ChessMove> diagonal_forward_left(ChessBoard board, ChessPosition myPosition){
        int current_row = myPosition.getRow();
        int current_col = myPosition.getColumn();
        ChessPosition possible_space;
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();
        int row = current_row-1;
        for (int col = current_col-1; col > 0; col--){ // increment column towards the right of the chessboard
            if (row == 0){
                break;
            }
            possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) == null){
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("forward_left: [%s, %s]", row, col));
            }else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()){ //if the piece is on our team, we can't move there
                break;
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()){ //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("forward_left: [%s, %s]", row, col));
                break;
            }
            row--;
        }
        return possible_moves;
    }

    public Collection<ChessMove> diagonal_backward_left(ChessBoard board, ChessPosition myPosition){
        int current_row = myPosition.getRow();
        int current_col = myPosition.getColumn();
        ChessPosition possible_space;
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();
        int col = current_col-1;
        for (int row = current_row+1; row < 9; row++){ // increment column towards the right of the chessboard
            if (col == 0){
                break;
            }
            possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) == null){
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("backward_left: [%s, %s]", row, col));
            }else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()){ //if the piece is on our team, we can't move there
                break;
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()){ //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("backward_left: [%s, %s]", row, col));
                break;
            }
            col--;
        }
        return possible_moves;
    }

    public Collection<ChessMove> diagonal_forward_right(ChessBoard board, ChessPosition myPosition){
        int current_row = myPosition.getRow();
        int current_col = myPosition.getColumn();
        ChessPosition possible_space;
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();
        int col = current_col+1;
        for (int row = current_row-1; row > 0; row--){ // increment column towards the right of the chessboard
            if (col == 9){
                break;
            }
            possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) == null){
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("backward_left: [%s, %s]", row, col));
            }else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()){ //if the piece is on our team, we can't move there
                break;
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()){ //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("backward_left: [%s, %s]", row, col));
                break;
            }
            col++;
        }
        return possible_moves;
    }

    public Collection<ChessMove> checkAdjacent(ChessBoard board, ChessPosition myPosition){
        int current_row = myPosition.getRow();
        int current_col = myPosition.getColumn();
        System.out.println(String.format("here is the king: [row: %s, col: %s]", current_row, current_col));
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();
        ChessMove my_move;

        //top left
        int row = current_row-1;
        int col = current_col-1;
        my_move = checkValidMove(board, myPosition, row, col);
        if (my_move != null) {
            possible_moves.add(my_move);
        }
        System.out.println("top left");
        System.out.println(my_move);

        //top middle
        row = current_row-1;
        col = current_col;
        my_move = checkValidMove(board, myPosition, row, col);
        if (my_move != null) {
            possible_moves.add(my_move);
        }

        //top right
        row = current_row-1;
        col = current_col+1;
        my_move = checkValidMove(board, myPosition, row, col);
        if (my_move != null) {
            possible_moves.add(my_move);
        }

        //right middle
        row = current_row;
        col = current_col+1;
        my_move = checkValidMove(board, myPosition, row, col);
        if (my_move != null) {
            possible_moves.add(my_move);
        }

        //bottom right
        row = current_row+1;
        col = current_col+1;
        my_move = checkValidMove(board, myPosition, row, col);
        if (my_move != null) {
            possible_moves.add(my_move);
        }

        //bottom middle
        row = current_row+1;
        col = current_col;
        my_move = checkValidMove(board, myPosition, row, col);
        if (my_move != null) {
            possible_moves.add(my_move);
        }

        //bottom left
        row = current_row+1;
        col = current_col-1;
        my_move = checkValidMove(board, myPosition, row, col);
        if (my_move != null) {
            possible_moves.add(my_move);
        }

        //left middle
        row = current_row;
        col = current_col-1;
        my_move = checkValidMove(board, myPosition, row, col);
        if (my_move != null) {
            possible_moves.add(my_move);
        }

        return possible_moves;
    }

    public Collection<ChessMove> checkFrontThree(ChessBoard board, ChessPosition myPosition){
        int current_row = myPosition.getRow();
        int current_col = myPosition.getColumn();
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();

        //top left
        int row = current_row-1;
        int col = current_col-1;
        if (row > 0 & col > 0 & row < 9 & col < 9) {
            boolean promote = false;
            if (row == 1 | row == 8){
                promote = true;
            }
            ChessPosition possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) != null) {
                if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //if it's an enemy piece, we can move there
                    System.out.println(String.format("space: [%s, %s]", row, col));
                    if (!promote) {
                        possible_moves.add(new ChessMove(myPosition, possible_space, null));
                    }else {
                            possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.QUEEN));
                            possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.ROOK));
                            possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.KNIGHT));
                            possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.BISHOP));
                    }
                }
            }
        }

        //top middle
        row = current_row-1;
        col = current_col;
        if (row > 0 & col > 0 & row < 9 & col < 9) {
            boolean promote = false;
            if (row == 1 | row == 8){
                promote = true;
            }
            ChessPosition possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) == null) {
                //there is nothing there, so we can move the piece here!
                System.out.println(String.format("space: [%s, %s]", row, col));
                if (!promote) {
                    possible_moves.add(new ChessMove(myPosition, possible_space, null));
                }else {
                    possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.QUEEN));
                    possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.ROOK));
                    possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.KNIGHT));
                    possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.BISHOP));
                }
            }
        }

        //top right
        row = current_row-1;
        col = current_col+1;
        if (row > 0 & col > 0 & row < 9 & col < 9) {
            boolean promote = false;
            if (row == 1 | row == 8){
                promote = true;
            }
            ChessPosition possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) != null) {
                if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //if it's an enemy piece, we can move there
                    System.out.println(String.format("space: [%s, %s]", row, col));
                    if (!promote) {
                        possible_moves.add(new ChessMove(myPosition, possible_space, null));
                    }else {
                        possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.QUEEN));
                        possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.ROOK));
                        possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.KNIGHT));
                        possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.BISHOP));
                    }
                }
            }
        }

        return possible_moves;
    }

    public Collection<ChessMove> checkBackThree(ChessBoard board, ChessPosition myPosition){
        int current_row = myPosition.getRow();
        int current_col = myPosition.getColumn();
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();

        //bottom right
        int row = current_row+1;
        int col = current_col+1;
        if (row > 0 & col > 0 & row < 9 & col < 9) {
            boolean promote = false;
            if (row == 1 | row == 8){
                promote = true;
            }
            ChessPosition possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) != null) {
                if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //if it's an enemy piece, we can move there
                    System.out.println(String.format("space: [%s, %s]", row, col));
                    if (!promote) {
                        possible_moves.add(new ChessMove(myPosition, possible_space, null));
                    }else {
                        possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.QUEEN));
                        possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.ROOK));
                        possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.KNIGHT));
                        possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.BISHOP));
                    }
                }
            }
        }

        //bottom middle
        row = current_row+1;
        col = current_col;
        if (row > 0 & col > 0 & row < 9 & col < 9) {
            boolean promote = false;
            if (row == 1 | row == 8){
                promote = true;
            }
            ChessPosition possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) == null) {
                //there is nothing there, so we can move the piece here!
                System.out.println(String.format("space: [%s, %s]", row, col));
                if (!promote) {
                    possible_moves.add(new ChessMove(myPosition, possible_space, null));
                }else {
                    possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.QUEEN));
                    possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.ROOK));
                    possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.KNIGHT));
                    possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.BISHOP));
                }
            }
        }

        //bottom left
        row = current_row+1;
        col = current_col-1;
        if (row > 0 & col > 0 & row < 9 & col < 9) {
            boolean promote = false;
            if (row == 1 | row == 8){
                promote = true;
            }
            ChessPosition possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) != null) {
                if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //if it's an enemy piece, we can move there
                    System.out.println(String.format("space: [%s, %s]", row, col));
                    if (!promote) {
                        possible_moves.add(new ChessMove(myPosition, possible_space, null));
                    }else {
                        possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.QUEEN));
                        possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.ROOK));
                        possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.KNIGHT));
                        possible_moves.add(new ChessMove(myPosition, possible_space, ChessPiece.PieceType.BISHOP));
                    }
                }
            }
        }

        return possible_moves;
    }

    public ChessMove checkValidMove(ChessBoard board, ChessPosition myPosition, int row, int col) {
        if (row > 0 & col > 0 & row < 9 & col < 9) {
            ChessPosition possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) == null) {
                //there is nothing there, so we can move the piece here!
                System.out.println(String.format("space: [%s, %s]", row, col));
                return new ChessMove(myPosition, possible_space, null);
            } else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()) { //if the piece is on our team, we can't move there
                //don't add anything
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //if it's an enemy piece, we can move there
                System.out.println(String.format("space: [%s, %s]", row, col));
                return new ChessMove(myPosition, possible_space, null);
            }
        }
        return null;
    }

    public Collection<ChessMove> checkDoubleMoveUp(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();

        ChessPosition direct_space = new ChessPosition(6, myPosition.getColumn());
        if (board.getPiece(direct_space) == null) {
            ChessPosition double_space = new ChessPosition(5, myPosition.getColumn());
            if (board.getPiece(double_space) == null) {
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, double_space, null));
            }
        }

        return possible_moves;
    }
    public Collection<ChessMove> checkDoubleMoveDown(ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();

        ChessPosition direct_space = new ChessPosition(3, myPosition.getColumn());
        if (board.getPiece(direct_space) == null) {
            ChessPosition double_space = new ChessPosition(4, myPosition.getColumn());
            if (board.getPiece(double_space) == null) {
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, double_space, null));
            }
        }

        return possible_moves;
    }
}
