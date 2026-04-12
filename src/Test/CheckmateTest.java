package Test;

import ChessBoard.Board;
import ChessPieces.*;
import Constants.ColorForChessPieces;
import Services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static Constants.ColorForChessPieces.*;
import static org.junit.jupiter.api.Assertions.*;

public class CheckmateTest {

    private GameService gameService;
    private Board board;
    private Piece[][] pieces;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        pieces = new Piece[8][8]; // empty board
        setBoard(board, pieces);
        gameService = new GameService(board);
        setCurrentColor(gameService, WHITE); // default: WHITE to move
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
    void notInCheck_returnsFalse() throws Exception {
        // White king alone, not attacked
        King whiteKing = new King(WHITE, new IndexPosition(0, 4));
        pieces[0][4] = whiteKing;

        assertFalse(gameService.isCheckmate());
    }

    @Test
    void inCheckKingCanEscape_returnsFalse() throws Exception {
        King whiteKing = new King(WHITE, new IndexPosition(0, 4));
        Rook blackRook = new Rook(BLACK, new IndexPosition(1, 4));

        pieces[0][4] = whiteKing;
        pieces[1][4] = blackRook;

        assertFalse(gameService.isCheckmate());
    }

    @Test
    void inCheckCanBlock_returnsFalse() throws Exception {
        King whiteKing = new King(WHITE, new IndexPosition(0, 7));
        Rook blackRook  = new Rook(BLACK, new IndexPosition(0, 0));
        Rook whiteRook  = new Rook(WHITE, new IndexPosition(3, 3));

        // box the king so it can't move (friendly pieces on adjacent squares)
        Pawn wp1 = new Pawn(WHITE, new IndexPosition(1, 6));
        Pawn wp2 = new Pawn(WHITE, new IndexPosition(1, 7));
        Pawn wp3 = new Pawn(WHITE, new IndexPosition(0, 6));

        pieces[0][7] = whiteKing;
        pieces[0][0] = blackRook;
        pieces[3][3] = whiteRook;
        pieces[1][6] = wp1;
        pieces[1][7] = wp2;
        pieces[0][6] = wp3;

        assertFalse(gameService.isCheckmate());
    }

    @Test
    void inCheckCanCaptureAttacker_returnsFalse() throws Exception {
        King whiteKing  = new King(WHITE, new IndexPosition(0, 7));
        Queen blackQueen = new Queen(BLACK, new IndexPosition(1, 6));
        Rook whiteRook  = new Rook(WHITE, new IndexPosition(1, 0));

        // box the king
        Pawn wp1 = new Pawn(WHITE, new IndexPosition(0, 6));
        Pawn wp2 = new Pawn(WHITE, new IndexPosition(1, 7));

        pieces[0][7] = whiteKing;
        pieces[1][6] = blackQueen;
        pieces[1][0] = whiteRook;
        pieces[0][6] = wp1;
        pieces[1][7] = wp2;

        assertFalse(gameService.isCheckmate());
    }

    @Test
    void backRankCheckmate_returnsTrue() throws Exception {
        King whiteKing = new King(WHITE, new IndexPosition(0, 7));
        Rook blackRook = new Rook(BLACK, new IndexPosition(0, 0));

        Pawn wp1 = new Pawn(WHITE, new IndexPosition(1, 5));
        Pawn wp2 = new Pawn(WHITE, new IndexPosition(1, 6));
        Pawn wp3 = new Pawn(WHITE, new IndexPosition(1, 7));

        pieces[0][7] = whiteKing;
        pieces[0][0] = blackRook;
        pieces[1][5] = wp1;
        pieces[1][6] = wp2;
        pieces[1][7] = wp3;

        assertTrue(gameService.isCheckmate());
    }

    @Test
    void doubleCheckNoEscape_returnsTrue() throws Exception {
        King whiteKing   = new King(WHITE, new IndexPosition(0, 7));
        Rook blackRook   = new Rook(BLACK, new IndexPosition(0, 0));
        Bishop blackBishop = new Bishop(BLACK, new IndexPosition(2, 5));

        Pawn wp1 = new Pawn(WHITE, new IndexPosition(1, 5));
        Pawn wp2 = new Pawn(WHITE, new IndexPosition(1, 6));
        Pawn wp3 = new Pawn(WHITE, new IndexPosition(1, 7));

        pieces[0][7] = whiteKing;
        pieces[0][0] = blackRook;
        pieces[2][5] = blackBishop;
        pieces[1][5] = wp1;
        pieces[1][6] = wp2;
        pieces[1][7] = wp3;

        assertTrue(gameService.isCheckmate());
    }

    @Test
    void blackKingCheckmate_returnsTrue() throws Exception {
        setCurrentColor(gameService, BLACK);

        King blackKing  = new King(BLACK, new IndexPosition(7, 0));
        Rook whiteRook1 = new Rook(WHITE, new IndexPosition(7, 7)); // checks along rank 7
        Rook whiteRook2 = new Rook(WHITE, new IndexPosition(6, 7)); // covers all of rank 6

        pieces[7][0] = blackKing;
        pieces[7][7] = whiteRook1;
        pieces[6][7] = whiteRook2;

        assertTrue(gameService.isCheckmate());
    }
}