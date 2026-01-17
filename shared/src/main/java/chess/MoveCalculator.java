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
        ChessPosition possible_space;
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();

        //top left
        int row = current_row-1;
        int col = current_col-1;
        if (row > 0 & col > 0 & row < 9 & col < 9) {
            possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) == null) {
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            } else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()) { //if the piece is on our team, we can't move there
                //don't add anything
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            }
        }

        //top middle
        row = current_row-1;
        col = current_col;
        if (row > 0 & col > 0 & row < 9 & col < 9) {
            possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) == null) {
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            } else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()) { //if the piece is on our team, we can't move there
                //don't add anything
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            }
        }

        //top right
        row = current_row-1;
        col = current_col+1;
        if (row > 0 & col > 0 & row < 9 & col < 9) {
            possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) == null) {
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            } else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()) { //if the piece is on our team, we can't move there
                //don't add anything
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            }
        }

        //right middle
        row = current_row;
        col = current_col+1;
        if (row > 0 & col > 0 & row < 9 & col < 9) {
            possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) == null) {
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            } else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()) { //if the piece is on our team, we can't move there
                //don't add anything
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            }
        }

        //bottom right
        row = current_row+1;
        col = current_col+1;
        if (row > 0 & col > 0 & row < 9 & col < 9) {
            possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) == null) {
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            } else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()) { //if the piece is on our team, we can't move there
                //don't add anything
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            }
        }

        //bottom middle
        row = current_row+1;
        col = current_col;
        if (row > 0 & col > 0 & row < 9 & col < 9) {
            possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) == null) {
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            } else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()) { //if the piece is on our team, we can't move there
                //don't add anything
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            }
        }

        //bottom left
        row = current_row+1;
        col = current_col-1;
        if (row > 0 & col > 0 & row < 9 & col < 9) {
            possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) == null) {
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            } else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()) { //if the piece is on our team, we can't move there
                //don't add anything
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            }
        }

        //left middle
        row = current_row;
        col = current_col-1;
        if (row > 0 & col > 0 & row < 9 & col < 9) {
            possible_space = new ChessPosition(row, col);
            if (board.getPiece(possible_space) == null) {
                //there is nothing there, so we can move the piece here!
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            } else if (board.getPiece(possible_space).getTeamColor() == board.getPiece(myPosition).getTeamColor()) { //if the piece is on our team, we can't move there
                //don't add anything
            } else if (board.getPiece(possible_space).getTeamColor() != board.getPiece(myPosition).getTeamColor()) { //if it's an enemy piece, we can move there, but we can't go past it
                possible_moves.add(new ChessMove(myPosition, possible_space, null));
                System.out.println(String.format("king_space: [%s, %s]", row, col));
            }
        }


        return possible_moves;
    }
}
