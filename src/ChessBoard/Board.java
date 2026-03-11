package ChessBoard;

import Constants.Colors;
import javax.swing.*;

// This class is responsible for the chess board logic
public class Board {

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
    }
}