package ChessPieces;

import ChessBoard.Board;
import Constants.ColorForChessPieces;

public class Rook extends Piece {

    public Rook(ColorForChessPieces color,IndexPosition position) {
        super(color, position);

    }

    @Override
    public IndexPosition[] getPossibleMoves(Piece[][] board) {
        return new IndexPosition[0];
    }
}
