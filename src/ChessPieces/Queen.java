package ChessPieces;

import Constants.ColorForChessPieces;


public class Queen extends Piece {
    public Queen(ColorForChessPieces color, IndexPosition position) {
        super(color, position);
    }

    @Override
    public IndexPosition[] getPossibleMoves(Piece[][] board) {
        return new IndexPosition[0];
    }
}
