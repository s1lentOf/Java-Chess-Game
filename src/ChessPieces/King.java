package ChessPieces;

import Constants.ColorForChessPieces;

public class King extends Piece {
    public King(ColorForChessPieces color,IndexPosition position){
        super( color, position);
    }

    @Override
    public IndexPosition[] getPossibleMoves(Piece[][] board) {
        return new IndexPosition[0];
    }

}
