package Test;


import ChessPieces.*;
import Constants.ColorForChessPieces;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import Services.GameService;

import java.util.ArrayList;


public class TestCheckBasedMethods {
    private static GameService service;

    @BeforeAll
    static void beforeAll() {
        service = new GameService();
    }

    @Test
    @DisplayName("Check if white king is in check")
    public void testWhiteKingIsInCheck() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Bishop bishop = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(4, 4));
        Piece[][] board = new Piece[8][8];
        board[3][3] = king;
        board[4][4] = bishop;
        assertTrue(service.isInCheck(ColorForChessPieces.WHITE, board));

    }

    @Test
    @DisplayName("Check if white king is not in check if piece is allied")
    public void testWhiteKingIsNotInCheck() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Bishop bishop = new Bishop(ColorForChessPieces.WHITE, new IndexPosition(4, 4));
        Piece[][] board = new Piece[8][8];
        board[3][3] = king;
        board[4][4] = bishop;
        assertFalse(service.isInCheck(ColorForChessPieces.WHITE, board));

    }


    @Test
    @DisplayName("Check if square is under attack of the piece")
    public void testSuquare() {
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Piece[][] board = new Piece[8][8];
        board[3][3] = rook;
        assertTrue(service.isSquareAttacked(new IndexPosition(7, 3), ColorForChessPieces.WHITE, board));

    }


    @Test
    @DisplayName("Check if square is not under attack of the piece")
    public void testSuquareIsNotAttacked() {
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Piece[][] board = new Piece[8][8];
        board[3][3] = rook;
        assertFalse(service.isSquareAttacked(new IndexPosition(4, 4), ColorForChessPieces.WHITE, board));

    }


    @Test
    @DisplayName("White king is found if it exists")
    public void testWhiteKingIsFoundIfItExists() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Piece[][] board = new Piece[8][8];
        board[3][3] = king;
        assertNotNull(service.findKing(ColorForChessPieces.WHITE, board));
    }

    @Test
    @DisplayName("White king is not found if it does not exists")
    public void testWhiteKingIsNotFound() {
        Piece[][] board = new Piece[8][8];
        assertNull(service.findKing(ColorForChessPieces.WHITE, board));
    }

    @Test
    @DisplayName("The piece that attacks king is returned if it exists")
    public void testPieceThatAtacksKingIsReturned() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Bishop bishop = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(4, 4));
        Queen queen = new Queen(ColorForChessPieces.BLACK, new IndexPosition(4, 3));
        Piece[][] board = new Piece[8][8];
        board[3][3] = king;
        board[4][4] = bishop;
        board[4][3] = queen;
        int length = service.findAttackers(king.getColor(), board).size();
        assertEquals(2, length);

    }

    @Test
    @DisplayName("The piece that atacks king is not  returned if does not exist")
    public void testPieceThatAtacksKingIsNotReturned() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Bishop bishop = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(3, 4));
        Piece[][] board = new Piece[8][8];
        board[3][3] = king;
        board[3][4] = bishop;
        int length = service.findAttackers(king.getColor(), board).size();
        assertEquals(0, length);
    }

    @Test
    @DisplayName("Allied piece can capture piece whihc checks king")
    public void testAlliedPieceCanCapturePiece() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Bishop bishop = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(4, 4));
        Queen queen = new Queen(ColorForChessPieces.WHITE, new IndexPosition(4, 3));
        Piece[][] board = new Piece[8][8];
        board[3][3] = king;
        board[4][4] = bishop;
        board[4][3] = queen;
        assertTrue(service.canCaptureAttacker(ColorForChessPieces.WHITE, board));
    }

    @Test
    @DisplayName("Allied piece cannot capture piece which checks king")
    public void testAlliedPieceCannotCapturePiece() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Bishop bishop = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(5, 5));
        Piece[][] board = new Piece[8][8];
        board[3][3] = king;
        board[5][5] = bishop;
        assertFalse(service.canCaptureAttacker(ColorForChessPieces.WHITE, board));
    }

    @Test
    @DisplayName("King can capture piece which delivers check ")
    public void testKingCanCapturePiece() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Bishop bishop = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(4, 4));
        Piece[][] board = new Piece[8][8];
        board[3][3] = king;
        board[4][4] = bishop;
        assertTrue(service.canCaptureAttacker(ColorForChessPieces.WHITE, board));
    }


    @Test
    @DisplayName("test all squares between two particular squares horizontally ")
    public void testAllSquaresHorisontally() {
        IndexPosition position = new IndexPosition(0, 0);
        IndexPosition position2 = new IndexPosition(0, 7);
        ArrayList<IndexPosition> positions = service.getSquaresBetween(position, position2);
        assertEquals(6, positions.size());
    }

    @Test
    @DisplayName("test all squares between two particular squares vertically ")
    public void testAllSquaresVertically() {
        IndexPosition position = new IndexPosition(7, 0);
        IndexPosition position2 = new IndexPosition(7, 7);
        ArrayList<IndexPosition> positions = service.getSquaresBetween(position, position2);
        assertEquals(6, positions.size());
    }

    @Test
    @DisplayName("test all squares between two particular squares diagonally ")
    public void testAllSquaresDiagonally() {
        IndexPosition position = new IndexPosition(0, 0);
        IndexPosition position2 = new IndexPosition(7, 7);
        ArrayList<IndexPosition> positions = service.getSquaresBetween(position, position2);
        assertEquals(6, positions.size());
    }

    @Test
    @DisplayName("check can be blocked if possible")
    public void testCanBeBlocked() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Bishop bishop = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(5, 5));
        Queen queen = new Queen(ColorForChessPieces.WHITE, new IndexPosition(4, 3));
        Piece[][] board = new Piece[8][8];
        board[3][3] = king;
        board[5][5] = bishop;
        board[4][3] = queen;
        assertTrue(service.canBlockCheck(king.getColor(), board));
    }

    @Test
    @DisplayName("check cannot be blocked if not  possible")
    public void testCannotBeBlocked() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Bishop bishop = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(5, 5));
        Queen queen = new Queen(ColorForChessPieces.WHITE, new IndexPosition(2, 2));
        Piece[][] board = new Piece[8][8];
        board[3][3] = king;
        board[5][5] = bishop;
        board[2][2] = queen;
        assertFalse(service.canBlockCheck(king.getColor(), board));
    }


}
