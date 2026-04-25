package ChessBoard;

import ChessPieces.*;
import Constants.ColorForChessPieces;
import Constants.Colors;
import Constants.Environment;
import Constants.GameMode;
import Services.BotService;
import Services.GameManager;
import Services.GameService;
import Services.MoveRecord;
import Services.MoveResult;
import Services.NetworkService;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

// This class is responsible for the chess board logic
public class BoardUI {

    private Piece selected = null;

    private final JButton[][] squares = new JButton[8][8];

    private final GameService service = new GameService(this);
    private final GameManager manager;

    public BoardUI() {
        this(GameMode.HUMAN_VS_BOT, ColorForChessPieces.WHITE);
    }

    public BoardUI(GameMode mode, ColorForChessPieces humanColor) {
        if (mode == GameMode.HUMAN_VS_HUMAN) {
            this.manager = new GameManager(service);
        } else {
            this.manager = new GameManager(
                    service,
                    new BotService(new NetworkService(), Environment.PROD, 1),
                    humanColor);
        }
    }

    private JFrame gameWindow;

    private boolean isPromoting = false;

    private int promotionCol;

    private int promotionRow;

    private Piece[] promotionOptions;

    public boolean isPromoting() {
        return isPromoting;
    }

    public Piece[] getPromotionOptions() {
        return promotionOptions;
    }

    public int getPromotionCol() {
        return promotionCol;
    }

    public int getPromotionRow() {
        return promotionRow;
    }

    public GameManager getGameManager() {
        return manager;
    }

    private Pawn promotionPawn;
    private IndexPosition promotionTarget;


    // getters for easier testing
    public Piece getSelected() {
        return selected;
    }

    public void setSelected(Piece piece) {
        this.selected = piece;
    }

    // Initial Setup of the chess board: coloring.
    public void setupBoard(JFrame window) {
        this.gameWindow = window;

        for (int row = 7; row >= 0; row--) {
            for (int col = 7; col >= 0; col--) {

                JButton square = new JButton();
                square.setLayout(new BorderLayout());
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
                    int moveCounterBefore = manager.getGameService().getMoveCounter();
                    manager.getGameService().handleSquareClick(new IndexPosition(currentRow, currentCol));
                    refreshBoard();
                    if (manager.getGameService().getMoveCounter() != moveCounterBefore) {
                        manager.onHumanMove();
                    }
                });

                squares[row][col] = square;
                window.add(square);
            }
        }

        manager.getGameService().setUpPiecesOnTheBoard();
        initialDrawOfPieces();
    }

    public void showGameOver(String message) {
        JOptionPane.showMessageDialog(
                this.gameWindow,
                message,
                "Game Over",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void initialDrawOfPieces() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = manager.getGameService().getPiecesOnTheBoard()[row][col];
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
        // Redraw pieces first.
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {

                JButton square = squares[row][col];
                if (square == null) continue;

                square.setIcon(null);

                Piece piece = manager.getGameService().getPiecesOnTheBoard()[row][col];

                if (piece != null) {
                    BufferedImage pieceImage = piece.paint();
                    square.setIcon(new ImageIcon(pieceImage));
                }
            }
        }

        // Then, draw elements for squares in possible moves of the selected piece.
        if (selected != null) {
            ArrayList<IndexPosition> allPossibleMoves = manager.getGameService().getLegalMoves(selected, manager.getGameService().getPiecesOnTheBoard());

            if (allPossibleMoves != null) {
                for (IndexPosition move : allPossibleMoves) {

                    int moveRow = move.getRow();
                    int moveCol = move.getCol();

                    JButton targetSquare = squares[moveRow][moveCol];
                    Piece targetPiece = manager.getGameService().getPiecesOnTheBoard()[moveRow][moveCol];

                    int size = 75; // A size of a square.

                    BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = image.createGraphics();

                    // Draw a dot for an empty square.
                    if (targetPiece == null) {
                        g.setColor(Colors.GRAY.getColor());
                        int dotSize = 12;
                        int x = (size - dotSize) / 2;
                        int y = (size - dotSize) / 2;

                        g.fillOval(x, y, dotSize, dotSize);

                    }
                    // Draw a piece with a circle around it if the square is not empty.
                    else {
                        BufferedImage pieceImg = targetPiece.paint();
                        g.drawImage(pieceImg, 0, 0, null);

                        g.setColor(Colors.GRAY.getColor());
                        g.setStroke(new BasicStroke(3));
                        g.drawOval(2, 4, size - 7, size - 7);
                    }

                    g.dispose();

                    targetSquare.setIcon(new ImageIcon(image));
                }
            }

            // drawing of the pop-up for the promotion of the piece by creating a background,
            // which depends on the color of the piece
            if (isPromoting) {
                for (int i = 0; i < 4; i++) {
                    int row = (promotionRow == 0) ? i : 7 - i;
                    JButton square = squares[row][promotionCol];

                    int size = 75;
                    BufferedImage composite = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g = composite.createGraphics();
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    boolean isWhitePromotion = promotionPawn.getColor() == ColorForChessPieces.WHITE;
                    Color bgColor  = isWhitePromotion ? new Color(50, 50, 50) : new Color(245, 245, 245);

                    g.setColor(bgColor);
                    g.fillRoundRect(0, 0, size, size, 8, 8);

                    BufferedImage pieceImage = promotionOptions[i].paint();
                    g.drawImage(pieceImage, 0, 0, null);

                    g.dispose();
                    square.setIcon(new ImageIcon(composite));
                }
            }
        }

        MoveResult result = service.getMoveResult();

        if (result.getCheckmate()) {
            System.out.println("Checkmate");
            String message = "Checkmate! " + result.getColorToWin() + " wins!";
            showGameOver(message);
        } else if (result.getDraw()) {
            String message = "Draw by " + result.getDrawReason() + "!";
            showGameOver(message);
        }
    }

    // a method to start the promotion of the pawn when it reaches the back rank
    // it creates options for the promoting piece and refreshes board to display the overlay
    public void startPromotion(Pawn pawn, IndexPosition target) {
        this.isPromoting = true;
        this.promotionPawn = pawn;
        this.promotionTarget = target;

        this.promotionCol = target.getCol();
        this.promotionRow = target.getRow();

        promotionOptions = new Piece[]{
                new Queen(pawn.getColor(), null),
                new Knight(pawn.getColor(), null),
                new Rook(pawn.getColor(), null),
                new Bishop(pawn.getColor(), null)
        };

        refreshBoard();
    }

    // a method to finish the promotion by placing the selected piece on the target square
    // and removing the original pawn from the board then it refreshes the board
    public void finishPromotion(Piece selectedPiece) {
        selectedPiece.setPosition(promotionTarget);

        Piece[][] board = manager.getGameService().getPiecesOnTheBoard();

        board[promotionPawn.getPosition().getRow()]
                [promotionPawn.getPosition().getCol()] = null;

        board[promotionTarget.getRow()]
                [promotionTarget.getCol()] = selectedPiece;
        manager.getGameService().recordPromotion(selectedPiece,promotionPawn.getPosition(),promotionTarget);

        isPromoting = false;
        selected = null;


        refreshBoard();
        manager.onHumanMove();
    }
}