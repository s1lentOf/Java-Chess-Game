package ChessPieces;

import Constants.ColorForChessPieces;


public class Queen extends Piece {
    public Queen(ColorForChessPieces color, IndexPosition position) {
        super(color, position);
    }

    @Override
    public IndexPosition[] getPossibleMoves(Piece[][] board) {
        // straight (rook) + diagonal (bishop) = 8 directions
        int[] directionRow =    { 1, -1,  0,  0,  1,  1, -1, -1};
        int[] directionColumn = { 0,  0,  1, -1,  1, -1,  1, -1};

        int currentRow = getPosition().getRow();
        int currentCol = getPosition().getCol();

        int dirIndex = 0;
        int step = 1;
        int count = 0;
        IndexPosition[] temp = new IndexPosition[27];

        while (dirIndex < 8) {
            int r = currentRow + directionRow[dirIndex] * step;
            int c = currentCol + directionColumn[dirIndex] * step;

            if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) {
                dirIndex++;
                step = 1;
                continue;
            }

            if (board[r][c] != null) {
                if (isEnemy(board[r][c])) {
                    temp[count++] = new IndexPosition(r, c);
                }
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
