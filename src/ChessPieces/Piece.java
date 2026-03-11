package ChessPieces;

import Constants.ColorForChessPieces;

public abstract class Piece {
    private ColorForChessPieces color;
    public Piece(ColorForChessPieces color) {
        this.color = color;
    }
    public void setColor(ColorForChessPieces color) {
        this.color = color;
    }

    public boolean isWhite(){
         return color == ColorForChessPieces.WHITE;
    }


    public boolean isEnemy(Piece other){
       return other.isWhite() == this.isWhite();
    }








}
