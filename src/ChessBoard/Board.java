package ChessBoard;

import ChessPieces.*;
import Constants.ColorForChessPieces;
import Constants.Colors;
import Services.GameService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Arrays;

// This class is responsible for the chess board logic
public class Board {

    // Stores elements that implement the Piece abstract class.
    private Piece[][] piecesOnTheBoard = new Piece[8][8]; // Store

    private Piece selected = null;

    private JButton[][] squares = new JButton[8][8];

    private GameService service = new GameService(this);

    // getters for easier testing
    public Piece getSelected() {
        return selected;
    }

    public void setSelected(Piece piece) {
        this.selected = piece;
    }

    public Piece getPieceAt(int row, int col) {
        return piecesOnTheBoard[row][col];
    }

    public Piece[][] getPiecesOnTheBoard() { return this.piecesOnTheBoard; }

    public void setPieceAt(int row, int col, Piece piece) {
        piecesOnTheBoard[row][col] = piece;
    }

    // Initial Setup of the chess board: coloring.
    public void setupBoard(JFrame window) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                JButton square = new JButton();
                square.setLayout(new BorderLayout());

                // Style
                square.setFocusPainted(false);
                square.setBorderPainted(false);
                square.setOpaque(true);

                if ((row + col) % 2 == 0) {
                    square.setBackground(Colors.WHITE.getColor());
                } else {
                    square.setBackground(Colors.BROWN.getColor());
                }

                int currentRow = row;
                int currentCol = col;

                square.addActionListener(e -> {
                    service.handleSquareClick(new IndexPosition(currentRow, currentCol));
                    refreshBoard();
                });

                squares[row][col] = square;
                window.add(square);
            }
        }

        setUpPiecesOnTheBoard();
        initialDrawOfPieces();
    }

    /*
    Initial setup of the pieces on the board
     */
    public void setUpPiecesOnTheBoard() {

        // Pawns
        for (int i = 0; i < 8; i++) {
            Pawn whitePawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, i));
            Pawn blackPawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(6, i));

            piecesOnTheBoard[1][i] = whitePawn;
            piecesOnTheBoard[6][i] = blackPawn;
        }

        // Rooks
        piecesOnTheBoard[7][0] = new Rook(ColorForChessPieces.BLACK, new IndexPosition(7, 0));
        piecesOnTheBoard[7][7] = new Rook(ColorForChessPieces.BLACK, new IndexPosition(7, 7));

        piecesOnTheBoard[0][0] = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        piecesOnTheBoard[0][7] = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 7));

        // Knights
        piecesOnTheBoard[7][1] = new Knight(ColorForChessPieces.BLACK, new IndexPosition(7, 1));
        piecesOnTheBoard[7][6] = new Knight(ColorForChessPieces.BLACK, new IndexPosition(7, 6));

        piecesOnTheBoard[0][1] = new Knight(ColorForChessPieces.WHITE, new IndexPosition(0, 1));
        piecesOnTheBoard[0][6] = new Knight(ColorForChessPieces.WHITE, new IndexPosition(0, 6));

        // Bishops
        piecesOnTheBoard[7][2] = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(7, 2));
        piecesOnTheBoard[7][5] = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(7, 5));

        piecesOnTheBoard[0][2] = new Bishop(ColorForChessPieces.WHITE, new IndexPosition(0, 2));
        piecesOnTheBoard[0][5] = new Bishop(ColorForChessPieces.WHITE, new IndexPosition(0, 5));

        // Queens
        piecesOnTheBoard[7][3] = new Queen(ColorForChessPieces.BLACK, new IndexPosition(7, 3));
        piecesOnTheBoard[0][3] = new Queen(ColorForChessPieces.WHITE, new IndexPosition(0, 3));

        // Kings
        piecesOnTheBoard[7][4] = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 4));
        piecesOnTheBoard[0][4] = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
    }

    private void initialDrawOfPieces() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = piecesOnTheBoard[row][col];
                if (piece != null) {
                    drawPiece(piece);
                }
            }
        }
    }

    public void drawPiece(Piece piece) {
        BufferedImage image = piece.paint();

        if (image != null) {
            int row = piece.getPosition().getRow();
            int col = piece.getPosition().getCol();

            squares[row][col].setIcon(new ImageIcon(image));
        }
    }

    public void refreshBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                JButton square = squares[row][col];
                if (square == null) continue;

                square.setIcon(null);

                Piece piece = piecesOnTheBoard[row][col];
                if (piece != null) {
                    BufferedImage img = piece.paint();
                    square.setIcon(new ImageIcon(img));
                }
            }
        }
    }
}