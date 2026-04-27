package ChessPieces;

import Constants.ColorForChessPieces;

import java.awt.*;

public class Knight extends Piece {
    public Knight(ColorForChessPieces color, IndexPosition position) {
        super(color, position);
    }

    @Override
    public IndexPosition[] getPossibleMoves(Piece[][] board) {
        // offset of all legal knight moves
        int[][] offsets = {
                {2, 1}, {2, -1},
                {1, 2}, {1, -2},
                {-2, 1}, {-2, -1},
                {-1, 2}, {-1, -2},

        };

        int currentRow = getPosition().getRow();
        int currentCol = getPosition().getCol();

        int count = 0;
        IndexPosition[] temp = new IndexPosition[8];

        for (int[] offset : offsets) {
            int r = currentRow + offset[0];
            int c = currentCol + offset[1];

            // this checks avoids knight from moving out of the board and moving on the allied occupied square
            if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) {
                continue;
            }
            if (board[r][c] != null && !isEnemy(board[r][c])) {
                continue;
            }

            temp[count++] = new IndexPosition(r, c);
        }
        IndexPosition[] moves = new IndexPosition[count];
        System.arraycopy(temp, 0, moves, 0, count);
        return moves;
    }
}
