package Test;


import ChessPieces.*;
import Constants.ColorForChessPieces;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
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
        fail();
    }

    @Test
    @DisplayName("Check if square is under attack of the piece")
    public void testSuquare() {
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Piece[][] board = new Piece[8][8];
        board[3][3] = pawn;
        assertTrue(service.isSquareAttacked(new IndexPosition(2,4),ColorForChessPieces.WHITE,board));

    }
}
