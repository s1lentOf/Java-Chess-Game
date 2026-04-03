package ChessPieces;

// this class will store indexes for figure moves
public class IndexPosition {
    private int row;
    private int col;

    public IndexPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setCol(int col) {
        this.col = col;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        IndexPosition other = (IndexPosition) obj;
        return this.row == other.row && this.col == other.col;
    }


    @Override
    public String toString() {
        return "row: " + this.row + " col: " + this.col;
    }

}
