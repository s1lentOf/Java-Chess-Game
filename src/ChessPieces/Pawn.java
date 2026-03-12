package ChessPieces;

import Constants.ColorForChessPieces;

public class Pawn extends Piece {

    public Pawn(ColorForChessPieces color,IndexPosition position) {
        super(color, position);
    }

    @Override
    public IndexPosition[] getPossibleMoves(Piece[][] board) {
        return new IndexPosition[0];


    }

    public boolean hasMoved(){
        return false;
    }

}
