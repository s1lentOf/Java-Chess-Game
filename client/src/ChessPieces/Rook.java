package ChessPieces;

import Constants.ColorForChessPieces;

public class Rook extends Piece {

    public Rook(ColorForChessPieces color,IndexPosition position) {
        super(color, position);

    }

    @Override
    public IndexPosition[] getPossibleMoves(Piece[][] board) {
        int[] directionRow =    { 1, -1,  0,  0};
        int[] directionColumn = { 0,  0,  1, -1};

        int currentRow = getPosition().getRow();
        int currentCol = getPosition().getCol();

        int dirIndex = 0;   // which direction (0–3): down, up, right, left
        int step = 1;       // how far along that direction
        int count = 0;
        IndexPosition[] temp = new IndexPosition[14];


        while (dirIndex < 4) {
            int r = currentRow + directionRow[dirIndex] * step;
            int c = currentCol + directionColumn[dirIndex] * step;

            if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) {
                // hit the wall — move to next direction, reset step
                dirIndex++;
                step = 1;
                continue;
            }

            if (board[r][c] != null) {
                if (isEnemy(board[r][c])) {
                    temp[count++] = new IndexPosition(r, c); // can capture
                }
                // ally or enemy both block further movement on this diagonal
                dirIndex++;
                step = 1;
                continue;
            }

            temp[count++] = new IndexPosition(r, c);
            step++;
        }
        IndexPosition[] moves = new IndexPosition[count];
        System.arraycopy(temp, 0, moves, 0, count);
        return moves;
    }
}
