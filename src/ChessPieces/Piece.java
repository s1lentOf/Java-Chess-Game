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
        BufferedImage original = ChessPieces.get(piece);

        //Resize
        //Creates a new blank image at the size you requested. TYPE_INT_ARGB means it supports transparency
        int width = 70;
        int height = 70;
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = resized.createGraphics();
        //Tells the graphics context to use bilinear interpolation when resizing to make the result look smooth
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.drawImage(original, 0, 0, width, height, null);
        g.dispose();

        return resized;
    }

    @Override
    public String toString() {
        return "Position: " + position.getCol() + "," + position.getRow() + ", Color: " + color;
    }


}
