package Test;

import ChessBoard.Board;
import ChessPieces.*;
import Constants.ColorForChessPieces;
import Services.CheckService;
import Services.DrawService;
import Services.GameService;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;

import static Constants.ColorForChessPieces.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestDrawService {

    private DrawService drawService;
    private GameService gameService;
    private Board board;
    private Piece[][] pieces;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        pieces = new Piece[8][8];
        setBoard(board, pieces);
        gameService = new GameService(board);
        drawService = new DrawService(new CheckService(), gameService);
        setCurrentColor(gameService, WHITE);
    }

    private void setBoard(Board board, Piece[][] pieces) throws Exception {
        Field f = Board.class.getDeclaredField("piecesOnTheBoard");
        f.setAccessible(true);
        f.set(board, pieces);
    }

    private void setCurrentColor(GameService gs, ColorForChessPieces color) throws Exception {
        Field f = GameService.class.getDeclaredField("currentColorToMove");
        f.setAccessible(true);
        f.set(gs, color);
    }

    @Test
    @DisplayName("Stalemate: player not in check but has no legal moves")
    void testStalemate() {
        //Black king on a8 (7,0), white queen on b6 (5,1)
        //Black king has no legal moves and is not in check = stalemate

        King blackKing = new King(BLACK, new IndexPosition(7, 0));
        Queen whiteQueen = new Queen(WHITE, new IndexPosition(5, 1));
        King whiteKing = new King(WHITE, new IndexPosition(5, 3));

        pieces[7][0] = blackKing;
        pieces[5][1] = whiteQueen;
        pieces[5][3] = whiteKing;

        assertTrue(drawService.isStalemate(BLACK, pieces));
    }

    @Test
    @DisplayName("Stalemate: player in check is not stalemate")
    void testNotStalemateWhenInCheck() {

        // Black king boxed in and in check = checkmate, not stalemate

        King blackKing = new King(BLACK, new IndexPosition(7, 0));
        Rook whiteRook1 = new Rook(WHITE, new IndexPosition(7, 7));
        Rook whiteRook2 = new Rook(WHITE, new IndexPosition(6, 7));

        pieces[7][0] = blackKing;
        pieces[7][7] = whiteRook1;
        pieces[6][7] = whiteRook2;

        assertFalse(drawService.isStalemate(BLACK, pieces));
    }

    @Test
    @DisplayName("Stalemate: player has legal moves is not stalemate")
    void testNotStalemateWhenHasMoves() {
        King whiteKing = new King(WHITE, new IndexPosition(0, 4));
        pieces[0][4] = whiteKing;

        assertFalse(drawService.isStalemate(WHITE, pieces));
    }

}