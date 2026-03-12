package ChessPieces;

import Constants.ColorForChessPieces;

public class Bishop extends Piece {
    public Bishop(ColorForChessPieces color, IndexPosition position) {
        super(color,position);
    }

    @Override
    public IndexPosition[] getPossibleMoves(Piece[][] board) {
        return new IndexPosition[0];
    }
}
