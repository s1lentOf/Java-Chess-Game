package Game;

import ChessBoard.BoardUI;
import ChessBoard.StartScreen;
import Constants.ColorForChessPieces;
import Constants.GameMode;
import Services.GameManager;
import Services.NetworkService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

// Main class that starts up the game
public class Game {

    private JFrame window;
    private GameManager gameManager;

    public Game() {
        setupWindow();
        StartScreen startScreen = new StartScreen(window, this);
        startScreen.show();
        this.window.setVisible(true); // we make the window visible after all setup is made
    }

    public static void main(String[] args) {
        Game game = new Game();
    }

    // called by StartScreen when user picks 1v1
    public void startOneVsOne() {
        setupBoardUI(GameMode.HUMAN_VS_HUMAN, ColorForChessPieces.WHITE);
    }

    // called by StartScreen when user picks vs bot
    public void startVsBot(ColorForChessPieces playerColor, int difficulty) {
        setupBoardUI(GameMode.HUMAN_VS_BOT, playerColor);
//        if (gameManager != null) {
//            gameManager.setDifficulty(difficulty);
//        }
    }

    private void setupBoardUI(GameMode mode, ColorForChessPieces color) {
        window.getContentPane().removeAll();
        window.setLayout(new GridLayout(8, 8));

        BoardUI boardUI = new BoardUI(mode, color);
        boardUI.setupBoard(window);

        gameManager = boardUI.getGameManager();
        gameManager.start();

        window.revalidate();
        window.repaint();
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
        // Stop the chess engine before the game window is closing.
        this.window.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                gameManager.stop();
            }
        });
    }
}