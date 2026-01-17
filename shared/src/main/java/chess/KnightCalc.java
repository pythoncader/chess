package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightCalc extends MoveCalculator{
    public ChessBoard board;
    public ChessPosition myPosition;
    public KnightCalc(ChessBoard board, ChessPosition myPosition){
        this.board = board;
        this.myPosition = myPosition;
    }
    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();
        System.out.println(board);
        possible_moves.addAll(this.checkL(this.board, this.myPosition));
        System.out.println(possible_moves);
        return possible_moves;
    }
}
