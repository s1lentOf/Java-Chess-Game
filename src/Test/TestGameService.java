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
        board[3][3]=king;
        board[6][6]=bishop;
        ArrayList<IndexPosition> moves = gameService.getKingLegalMoves(ColorForChessPieces.WHITE, board);
        assertEquals(6, moves.size());

    }

}
