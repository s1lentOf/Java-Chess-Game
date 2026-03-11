package ChessBoard;

import Constants.Colors;
import javax.swing.*;

// This class is responsible for the chess board logic
public class Board {

    private BoardSquarePosition[][] boardPositions = new BoardSquarePosition[8][8];

    // Initial Setup of the chess board: coloring.
    public static void setupBoard(JFrame window) {
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

    // TODO: implement logic
    public static void setUpMatrix(){
//        for(int row = 75; row<=600;row+=75){
//            for(int col = 75; col<=600;col+=75){
//
//            }
//        }
    }

}