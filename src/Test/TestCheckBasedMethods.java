package Test;


import ChessPieces.*;
import Constants.ColorForChessPieces;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import Services.GameService;



public class TestCheckBasedMethods {
    private static GameService service;
    @BeforeAll
    static void beforeAll() {
        service = new GameService();
    }
    @Test
    @DisplayName("Check if white king is in check")
    public void testWhiteKingIsInCheck() {
        King king = new King(ColorForChessPieces.WHITE,new IndexPosition(3,3));
        Bishop bishop = new Bishop(ColorForChessPieces.BLACK,new IndexPosition(4,4));
        Piece[][] board = new Piece[8][8];
        board[3][3] = king;
        board[4][4] = bishop;
        assertTrue(service.isInCheck(ColorForChessPieces.WHITE,board));

    }

    @Test
    @DisplayName("Check if white king is not in check if piece is allied")
    public void testWhiteKingIsNotInCheck() {
        King king = new King(ColorForChessPieces.WHITE,new IndexPosition(3,3));
        Bishop bishop = new Bishop(ColorForChessPieces.WHITE,new IndexPosition(4,4));
        Piece[][] board = new Piece[8][8];
        board[3][3] = king;
        board[4][4] = bishop;
        assertFalse(service.isInCheck(ColorForChessPieces.WHITE,board));

    }



    @Test
    @DisplayName("Check if square is under attack of the piece")
    public void testSuquare() {
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Piece[][] board = new Piece[8][8];
        board[3][3] = rook;
        assertTrue(service.isSquareAttacked(new IndexPosition(7,3),ColorForChessPieces.WHITE,board));

    }


    @Test
    @DisplayName("Check if square is not under attack of the piece")
    public void testSuquareIsNotAttacked() {
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Piece[][] board = new Piece[8][8];
        board[3][3] = rook;
        assertFalse(service.isSquareAttacked(new IndexPosition(4,4),ColorForChessPieces.WHITE,board));

    }


}
