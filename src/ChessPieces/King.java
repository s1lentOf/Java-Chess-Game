package ChessPieces;

import Constants.ColorForChessPieces;

public class King extends Piece {
    public King(ColorForChessPieces color,IndexPosition position){
        super( color, position);
    }

    @Override
    public IndexPosition[] getPossibleMoves(Piece[][] board) {
        int[][] offsets = {
            {-1, -1}, {-1, 0}, {-1, 1},
            { 0, -1},          { 0, 1},
            { 1, -1}, { 1, 0}, { 1, 1}
        };

        int currentRow = getPosition().getRow();
        int currentCol = getPosition().getCol();

        IndexPosition[] temp = new IndexPosition[8];
        int count = 0;

        for (int[] offset : offsets) {
            int r = currentRow + offset[0];
            int c = currentCol + offset[1];

            if (r < 0 || r >= board.length || c < 0 || c >= board[0].length){
                continue;
            }
            if (board[r][c] != null && board[r][c].isWhite() == this.isWhite()){
                continue;
            }

            temp[count++] = new IndexPosition(r, c);
        }

        IndexPosition[] moves = new IndexPosition[count];
        System.arraycopy(temp, 0, moves, 0, count);
        return moves;
    }

    public boolean hasMoved(){
        if((this.getPosition().getRow() == 0 && this.getPosition().getCol() == 3)||
                (this.getPosition().getRow() == 7 && this.getPosition().getCol() == 3)){
            return true;
        }
        return false;

    }


}
