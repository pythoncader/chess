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
}
