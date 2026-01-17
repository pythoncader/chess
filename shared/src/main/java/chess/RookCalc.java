package chess;

import java.util.ArrayList;
import java.util.Collection;

public class RookCalc extends MoveCalculator{
    public ChessBoard board;
    public ChessPosition myPosition;
    public RookCalc(ChessBoard board, ChessPosition myPosition){
        this.board = board;
        this.myPosition = myPosition;
    }
    public Collection<ChessMove> pieceMoves() {
        ArrayList<ChessMove> possible_moves = new ArrayList<ChessMove>();
        possible_moves.addAll(this.left(this.board, this.myPosition));
        System.out.println(possible_moves);
        possible_moves.addAll(this.right(this.board, this.myPosition));
        System.out.println(possible_moves);
        possible_moves.addAll(this.forward(this.board, this.myPosition));
        System.out.println(possible_moves);
        possible_moves.addAll(this.backward(this.board, this.myPosition));
        System.out.println(possible_moves);

        return possible_moves;
    }
}
