package ChessBoard;

import ChessPieces.*;
import Constants.ColorForChessPieces;
import Constants.Colors;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;

// This class is responsible for the chess board logic
public class Board {

    // Stores elements with its x and y coordinates of the square on the chess board.
    private BoardSquarePosition[][] boardPositions = new BoardSquarePosition[8][8];

    // Stores elements that implement the Piece abstract class.
    private Piece[][] piecesOnTheBoard = new Piece[8][8]; // Store

    // Initial Setup of the chess board: coloring.
    public void setupBoard(JFrame window) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                JPanel square = new JPanel();

                if ((row + col) % 2 == 0) {
                    square.setBackground(Colors.WHITE.getColor());
                } else {
                    square.setBackground(Colors.BROWN.getColor());
                }

                window.add(square);
            }
        }

        setUpMatrix();
        setUpPiecesOnTheBoard();

    }
    /*
     This method creates matrix, which represents a chess board.
     Each element stores x and y coordinates of the square on the chess board

     How it works: we create two loops, for x and y;
     for each square the pixel coordinates is calculated:
     - x = row/75 -1;
     - y = col/75 -1;
     75 is the size of each square in pixels.

     This method gives helps us to know the exact pixel position of each square for drawing
     pieces or handling mouse clicks.
     */


    public void setUpMatrix(){
        for(int row = 75; row<=600;row+=75){
            for(int col = 75; col<=600;col+=75){
                BoardSquarePosition temp = new BoardSquarePosition(row, col);
                int x_matrix = row/75 -1;
                int y_matrix = col/75 -1;
                boardPositions[x_matrix][y_matrix] = temp;
            }
        }
        System.out.println(Arrays.deepToString(boardPositions));
    }

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

        System.out.println(Arrays.deepToString(piecesOnTheBoard));
    }

    /*
        This method returns a board indices where the mouse was tapped.
     */
    public void detectMouseClickPosition(MouseEvent mouseEvent) {
        int mouse_x = mouseEvent.getX();
        int mouse_y = mouseEvent.getY();
    }


    /*  a method which checks if the move is possible for the piece
        by taking the array of all possible moves and checking if the move that the user wants to do is in that array
     */

    public boolean isMovePossible(IndexPosition[] possibleMoves, IndexPosition nextMove){
        for (IndexPosition move : possibleMoves) {
            if (move.getRow() == nextMove.getRow() && move.getCol() == nextMove.getCol()) {
                return true;
            }
        }
        return false;

    }

}