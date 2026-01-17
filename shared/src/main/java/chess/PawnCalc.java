package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnCalc extends MoveCalculator{
    public ChessBoard board;
    public ChessPosition myPosition;
    public PawnCalc(ChessBoard board, ChessPosition myPosition){
        this.board = board;
        this.myPosition = myPosition;
    }
    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();
        System.out.println(board);
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK) {
            if (myPosition.getRow() == 7){ //if the pawn is in the starting position, it can move two spaces forward
                possible_moves.addAll(checkDoubleMoveUp(this.board, this.myPosition));
            }
            possible_moves.addAll(this.checkFrontThree(this.board, this.myPosition));
        }else {
            if (myPosition.getRow() == 2){
                possible_moves.addAll(checkDoubleMoveDown(this.board, this.myPosition));
            }
            possible_moves.addAll(this.checkBackThree(this.board, this.myPosition));
        }
        System.out.println(possible_moves);
        return possible_moves;
    }
}
