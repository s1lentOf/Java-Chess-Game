import ChessBoard.Board;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Main class that starts up the game
public class Game {

    private JFrame window;
    private Board board = new Board();

    public Game() {
        setupWindow();
        board.setupBoard(window);
        this.window.setVisible(true); // we make the window visible after all setup is made
    }

    public static void main(String[] args) {
        Game game = new Game();
    }

    // Initial setup of the window
    private void setupWindow() {
        this.window = new JFrame("Chess Board");
        this.window.setSize(600, 600);
        this.window.setLayout(new GridLayout(8, 8));
        this.window.setResizable(false); // make the size fixed, which allows us to track the mouse click
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.window.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                board.detectMouseClickPosition(me);
            }
        });
    }
}