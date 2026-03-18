package ChessBoard;

import ChessPieces.Piece;
import Constants.Colors;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.Arrays;

// This class is responsible for the chess board logic
public class Board {

    private BoardSquarePosition[][] boardPositions = new BoardSquarePosition[8][8];

    // Initial Setup of the chess board: coloring.
    public void setupBoard(JFrame window) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                JPanel square = new JPanel();

                if ((row + col) % 2 == 0) {
                    square.setBackground(Colors.BROWN.getColor());
                } else {
                    square.setBackground(Colors.WHITE.getColor());
                }

                window.add(square);
            }
        }

        setUpMatrix();


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

    /*
        This method returns a board indices where the mouse was tapped.
     */
    public void detectMouseClickPosition(MouseEvent mouseEvent) {
        int mouse_x = mouseEvent.getX();
        int mouse_y = mouseEvent.getY();
    }
}