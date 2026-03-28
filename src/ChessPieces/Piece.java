package ChessPieces;

import ChessBoard.*;
import Constants.ColorForChessPieces;

import javax.swing.text.Position;
import java.awt.*;
import java.awt.image.BufferedImage;

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
    public boolean isWhite() {
        return color == ColorForChessPieces.WHITE;
    }

    // check if piece on the square is enemy of friendly
    public boolean isEnemy(Piece other) {
        return other.isWhite() != this.isWhite();
    }

    // this method will return an array of all possible piece moves
    public abstract IndexPosition[] getPossibleMoves(Piece[][] board);

    public BufferedImage paint() {
        // Build the enum name: e.g. "WHITE_KING", "BLACK_PAWN"
        String colorPrefix = isWhite() ? "WHITE" : "BLACK";
        String pieceName = getClass().getSimpleName().toUpperCase(); // "King" → "KING"
        String constantName = colorPrefix + "_" + pieceName;

        // Fetch the image from ChessPieces using the Constants.Piece enum
        Constants.Piece piece = Constants.Piece.valueOf(constantName);
        return ChessPieces.get(piece);
    }

    @Override
    public String toString() {
        return "Position: " + position.getCol() + "," + position.getRow() + ", Color: " + color;
    }


}
