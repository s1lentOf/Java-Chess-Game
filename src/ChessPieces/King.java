package ChessPieces;

import Constants.ColorForChessPieces;
import Services.CheckService;


public class King extends Piece {
    public King(ColorForChessPieces color, IndexPosition position) {
        super(color, position);
    }

    @Override
    public IndexPosition[] getPossibleMoves(Piece[][] board) {
        // create an array  of all possible direction of moves from a king(assuming that he can move to every direction)
        int[][] offsets = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        int currentRow = getPosition().getRow();
        int currentCol = getPosition().getCol();

        IndexPosition[] temp = new IndexPosition[8];
        int count = 0;
        //change the position on the board by incrementing each coordinate by the value of the offset
        for (int[] offset : offsets) {
            int r = currentRow + offset[0];
            int c = currentCol + offset[1];
            // this check avoids king from leaving the board
            if (r < 0 || r >= board.length || c < 0 || c >= board[0].length) {
                continue;
            }
            // this check avoids placing the king on the square with allied piece
            if (board[r][c] != null && !isEnemy(board[r][c])) {
                continue;
            }
            // after all checks add the square to array of possible squares
            temp[count++] = new IndexPosition(r, c);
        }

        IndexPosition[] moves = new IndexPosition[count];
        System.arraycopy(temp, 0, moves, 0, count);
        return moves;
    }

    public boolean isInCheck(Piece[][] board, CheckService checkService) {
        ColorForChessPieces enemyColor = this.isWhite()
                ? ColorForChessPieces.BLACK : ColorForChessPieces.WHITE;
        return checkService.isSquareAttacked(this.getPosition(), enemyColor, board);
    }

    // this method just checks if the king has stayed on the starting squared designed by the rules of chess
    public boolean hasMoved() {
        if (this.isWhite()) {
            if (this.getPosition().getRow() != 0 || this.getPosition().getCol() != 3) {
                return true;
            }

        } else {
            if (this.getPosition().getRow() != 7 || this.getPosition().getCol() != 3) {
                return true;
            }

        }
        return false;

    }


}
