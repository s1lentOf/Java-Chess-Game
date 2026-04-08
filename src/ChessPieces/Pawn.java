package ChessPieces;

import Constants.ColorForChessPieces;

public class Pawn extends Piece {

    public Pawn(ColorForChessPieces color, IndexPosition position) {
        super(color, position);
    }

    @Override
    public IndexPosition[] getPossibleMoves(Piece[][] board) {
        IndexPosition[] temp = new IndexPosition[4];
        int count = 0;

        int row = getPosition().getRow();
        int col = getPosition().getCol();
        // basic pawn move for both black and white
        int dir = this.isWhite() ? 1 : -1;

        // one step forward (only if square is empty)
        int nextRow = row + dir;
        if (nextRow >= 0 && nextRow < board.length && board[nextRow][col] == null) {
            temp[count++] = new IndexPosition(nextRow, col);

            // two steps forward from starting row (only if path is clear)
            if (!this.hasMoved()) {
                int twoRow = row + 2 * dir;
                if (board[twoRow][col] == null) {
                    temp[count++] = new IndexPosition(twoRow, col);
                }
            }
        }

        // diagonal captures
        int[] captureCols = {col - 1, col + 1};
        for (int captureCol : captureCols) {
            // this check avoids any illegal moves outside the board
            if (nextRow >= 0 && nextRow < board.length && captureCol >= 0 && captureCol < board[0].length) {
                Piece target = board[nextRow][captureCol];
                if (target != null && isEnemy(target)) {
                    temp[count++] = new IndexPosition(nextRow, captureCol);
                }
            }
        }

        IndexPosition[] moves = new IndexPosition[count];
        System.arraycopy(temp, 0, moves, 0, count);
        return moves;
    }

    public IndexPosition[] getPossibleMoves(Piece[][] board, Pawn lastDoubleStepPawn) {
        IndexPosition[] temp = new IndexPosition[6];
        int count = 0;

        int row = getPosition().getRow();
        int col = getPosition().getCol();
        // basic pawn move for both black and white
        int dir = this.isWhite() ? 1 : -1;

        // one step forward (only if square is empty)
        int nextRow = row + dir;
        if (nextRow >= 0 && nextRow < board.length && board[nextRow][col] == null) {
            temp[count++] = new IndexPosition(nextRow, col);

            // two steps forward from starting row (only if path is clear)
            if (!this.hasMoved()) {
                int twoRow = row + 2 * dir;
                if (board[twoRow][col] == null) {
                    temp[count++] = new IndexPosition(twoRow, col);
                }
            }
        }

        //En passant
        if (false){
            //implementation
        }

        // diagonal captures
        int[] captureCols = {col - 1, col + 1};
        for (int captureCol : captureCols) {
            // this check avoids any illegal moves outside the board
            if (nextRow >= 0 && nextRow < board.length && captureCol >= 0 && captureCol < board[0].length) {
                Piece target = board[nextRow][captureCol];
                if (target != null && isEnemy(target)) {
                    temp[count++] = new IndexPosition(nextRow, captureCol);
                }
            }
        }

        IndexPosition[] moves = new IndexPosition[count];
        System.arraycopy(temp, 0, moves, 0, count);
        return moves;
    }

    public boolean hasMoved() {
        if (this.isWhite()) {
            return this.getPosition().getRow() != 1;
        } else {
            return this.getPosition().getRow() != 6;
        }
    }
}
