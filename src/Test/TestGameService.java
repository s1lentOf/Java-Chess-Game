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
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[0][0] = rook;
        assertTrue(gameService.canCastleQueenSide(king.getColor(), board));
    }


    @Test
    @DisplayName("queen side  castle is not allowed through a piece")
    public void testQueenSideCastleIsNotAllowedThroughAPiece() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Knight knight = new Knight(ColorForChessPieces.WHITE, new IndexPosition(0, 2));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[0][2] = knight;
        board[0][0] = rook;
        assertFalse(gameService.canCastleQueenSide(king.getColor(), board));

    }


    @Test
    @DisplayName(" Queen side castle cannot be done under check ")
    public void testQueenSideCastleIsNotAllowedUnderCheck() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Queen queen = new Queen(ColorForChessPieces.BLACK, new IndexPosition(1, 3));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[1][3] = queen;
        board[0][0] = rook;
        assertFalse(gameService.canCastleQueenSide(king.getColor(), board));
    }


    @Test
    @DisplayName("Queen side castle cannot be done trough a checked field  ")
    public void testQueenSideCastleIsNotAllowedTroughCheck() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Bishop bishop = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(2, 4));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[2][4] = bishop;
        board[0][7] = rook;
        assertFalse(gameService.canCastleQueenSide(king.getColor(), board));
    }



    @Test
    @DisplayName("Retrieve filtered legal moves of the king with one possible castle")
    public void testRetrievedLegalKingMoves() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[0][0] = rook;
        int size = gameService.getLegalMoves(king,board).size();
        assertEquals(size,6);
    }

    @Test
    @DisplayName("Retrieve filtered legal moves of the king with two possible castles")
    public void testRetrievedLegalKingMovesWithCastles() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Rook rook2 = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 7));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[0][0] = rook;
        board[0][7] = rook2;
        int size = gameService.getLegalMoves(king,board).size();
        assertEquals(size,7);
    }

    @Test
    @DisplayName("pinned piece cannot move")
    public void testPinnedPieceCannotMove() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Bishop bishop = new Bishop(ColorForChessPieces.WHITE, new IndexPosition(1, 4));
        Rook rook = new Rook(ColorForChessPieces.BLACK, new IndexPosition(7, 4));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[1][4] = bishop;
        board[0][0] = rook;
        assertEquals(gameService.getLegalMoves(bishop,board).size(),0);
    }

    @Test
    @DisplayName("Piined piece can only move on the direction of teh pin and capture enemy piece")
    public void  testPinnedMovement(){
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(1, 4));
        Rook rook2 = new Rook(ColorForChessPieces.BLACK, new IndexPosition(2, 4));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[1][4] = rook;
        board[2][4] = rook2;
        assertEquals(gameService.getLegalMoves(rook,board).size(),1);
    }





}
