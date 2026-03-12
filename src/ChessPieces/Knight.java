package ChessPieces;

import Constants.ColorForChessPieces;

import java.awt.*;

public class Knight extends Piece {
    public Knight(ColorForChessPieces color,IndexPosition position) {
        super(color, position);
    }

    @Override
    public IndexPosition[] getPossibleMoves(Piece[][] board) {
        return new IndexPosition[0];
    }
}
