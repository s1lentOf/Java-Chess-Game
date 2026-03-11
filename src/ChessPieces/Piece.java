package ChessPieces;

import Constants.ColorForChessPieces;

import javax.swing.text.Position;

public abstract class Piece {
    private ColorForChessPieces color;
    private IndexPosition position;
    public Piece(ColorForChessPieces color, IndexPosition position) {
        this.color = color;
        this.position = position;
    }
    public IndexPosition getPosition() {
        return position;
    }

    public void setPosition(IndexPosition position) {
        this.position = position;
    }
    public void setColor(ColorForChessPieces color) {
        this.color = color;
    }
    // check the color of the piece
    public boolean isWhite(){
         return color == ColorForChessPieces.WHITE;
    }

    // check if piece on the square is enemy of friendly
    public boolean isEnemy(Piece other){
       return other.isWhite() == this.isWhite();
    }
    // this method will return an array of all possible piece moves
    public abstract Position[] getPossibleMoves(Piece[][] board);








}
