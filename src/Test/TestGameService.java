package Test;

import ChessPieces.*;
import Constants.ColorForChessPieces;
import Services.GameService;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameService {
    private static GameService gameService;

    @BeforeAll
    public static void setup() {
        gameService = new GameService();
    }

    @Test
    @DisplayName("Test filtered by check king moves ")
    public void testFilterByCheckKingMoves() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Bishop bishop = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(6, 6));
        Piece[][] board = new Piece[8][8];
        board[3][3] = king;
        board[6][6] = bishop;
        ArrayList<IndexPosition> moves = gameService.getKingLegalMoves(ColorForChessPieces.WHITE, board);
        assertEquals(6, moves.size());

    }


    @Test
    @DisplayName("Test that castle is possible if all requirements are met")
    public void testCastleIsPossible() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 7));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[0][7] = rook;
        assertTrue(gameService.canCastleKingSide(king.getColor(), board));
    }

    @Test
    @DisplayName("Test castle is not allowed through a piece")
    public void testCastleIsNotAllowedThroughAPiece() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 7));
        Knight knight = new Knight(ColorForChessPieces.WHITE, new IndexPosition(0, 6));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[0][6] = knight;
        board[0][7] = rook;
        assertFalse(gameService.canCastleKingSide(king.getColor(), board));

    }


    @Test
    @DisplayName("Castle cannot be done under check ")
    public void testCastleIsNotAllowedUnderCheck() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 7));
        Queen queen = new Queen(ColorForChessPieces.BLACK, new IndexPosition(1, 3));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[1][3] = queen;
        board[0][7] = rook;
        assertFalse(gameService.canCastleKingSide(king.getColor(), board));
    }

    @Test
    @DisplayName("Castle cannot be done trough a checked field  ")
    public void testCastleIsNotAllowedTroughCheck() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 7));
        Queen queen = new Queen(ColorForChessPieces.BLACK, new IndexPosition(2, 4));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[2][4] = queen;
        board[0][7] = rook;
        assertFalse(gameService.canCastleKingSide(king.getColor(), board));
    }


    @Test
    @DisplayName("Castle cannot be done on  a checked field  ")
    public void testCastleIsNotAllowedOnCheck() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 7));
        Queen queen = new Queen(ColorForChessPieces.BLACK, new IndexPosition(3, 6));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[3][6] = queen;
        board[0][7] = rook;
        assertFalse(gameService.canCastleKingSide(king.getColor(), board));
    }

    @Test
    @DisplayName(" Queen side  castle is possible if all requirements are met")
    public void testQueenSideIsPossible() {
        fail("Not yet implemented");
    }


}
