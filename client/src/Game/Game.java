package Game;

import ChessBoard.BoardUI;
import Constants.ColorForChessPieces;
import Constants.GameMode;
import Services.GameManager;

import javax.swing.*;
import java.awt.*;

// Main class that starts up the game
public class Game {

    private JFrame window;
    private final BoardUI boardUI = new BoardUI(GameMode.HUMAN_VS_HUMAN, ColorForChessPieces.WHITE);
    private GameManager gameManager;

    public Game() {
        setupWindow();
        boardUI.setupBoard(window);
        gameManager = boardUI.getGameManager();
        gameManager.start();
        this.window.setVisible(true); // we make the window visible after all setup is made
    }

    public static void main(String[] args) {
        Game game = new Game();
    }

    // Helper getter for testing purposes.
    public JFrame getWindow() {
        return window;
    }

    // Initial setup of the window
    private void setupWindow() {
        this.window = new JFrame("Chess Board");
        this.window.setSize(600, 600);
        this.window.setLayout(new GridLayout(8, 8));
        this.window.setResizable(false); // make the size fixed, which allows us to track the mouse click
        this.window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}