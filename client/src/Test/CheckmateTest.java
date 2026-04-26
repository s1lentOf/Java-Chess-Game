package Test;

import ChessBoard.BoardUI;
import ChessPieces.*;
import Services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static Constants.ColorForChessPieces.*;
import static org.junit.jupiter.api.Assertions.*;

public class CheckmateTest {

    private GameService gameService;
    private BoardUI boardUI;

    @BeforeEach
    void setUp() throws Exception {
        boardUI = new BoardUI();
        gameService = new GameService(boardUI);
    }

    @Test
    @DisplayName("Should not be a checkmate if the king is not in check.")
    void isCheckmate_kingIsNotInCheck_shouldReturnFalse() {
        gameService.setPieceAt(0, 4, new King(WHITE, new IndexPosition(0, 4)));
        gameService.setPieceAt(7, 4, new King(BLACK, new IndexPosition(7, 4)));

        /*
            7  . . . . k . .
            6  . . . . . . . .
            5  . . . . . . . .
            4  . . . . . . . .
            3  . . . . . . . .
            2  . . . . . . . .
            1  . . . . . . . .
            0  . . . . K . . .
               0 1 2 3 4 5 6 7
         */

        assertFalse(gameService.isCheckmate());
    }

    @Test
    @DisplayName("Should not be a checkmate if the king is in check, but has escape move.")
    void isCheckmate_kingIsInCheck_kingHasEscapeMove_shouldReturnFalse() {
        gameService.setPieceAt(0, 4, new King(WHITE, new IndexPosition(0, 4)));
        gameService.setPieceAt(1, 4, new Rook(BLACK, new IndexPosition(1, 4)));
        gameService.setPieceAt(7, 7, new King(BLACK, new IndexPosition(7, 7)));

        /*
            7  . . . . . . . k
            6  . . . . . . . .
            5  . . . . . . . .
            4  . . . . . . . .
            3  . . . . . . . .
            2  . . . . . . . .
            1  . . . . r . . .
            0  . . . . K . . .
               0 1 2 3 4 5 6 7
         */

        assertFalse(gameService.isCheckmate());
    }

    @Test
    @DisplayName("Should not be a checkmate if the king is in check, has no escape move, but can be protected.")
    void isCheckmate_kingIsInCheck_noEscapeMove_butCanBeProtected_shouldReturnFalse() {
        gameService.setPieceAt(0, 0, new King(WHITE, new IndexPosition(0, 0)));
        gameService.setPieceAt(1, 0, new Pawn(WHITE, new IndexPosition(1, 0)));
        gameService.setPieceAt(0, 7, new Rook(BLACK, new IndexPosition(0, 7)));
        gameService.setPieceAt(7, 7, new King(BLACK, new IndexPosition(7, 7)));
        gameService.setPieceAt(1, 1, new Rook(WHITE, new IndexPosition(1, 1)));

        /*
            7  . . . . . . . k
            6  . . . . . . . .
            5  . . . . . . . .
            4  . . . . . . . .
            3  . . . . . . . .
            2  . . . . . . . .
            1  P R . . . . . .
            0  K . . . . . . r
               0 1 2 3 4 5 6 7
         */

        assertFalse(gameService.isCheckmate());
    }

    @Test
    @DisplayName("Should not be a checkmate if the king is in check, has no escape move, but other piece can capture attacker.")
    void isCheckmate_kingIsInCheck_noEscapeMove_canCaptureAttacker_shouldReturnFalse() {
        gameService.setPieceAt(0, 0, new King(WHITE, new IndexPosition(0, 0)));
        gameService.setPieceAt(1, 0, new Pawn(WHITE, new IndexPosition(1, 0)));
        gameService.setPieceAt(0, 1, new Bishop(WHITE, new IndexPosition(0, 1)));
        gameService.setPieceAt(7, 7, new King(BLACK, new IndexPosition(7, 7)));
        gameService.setPieceAt(1, 1, new Bishop(BLACK, new IndexPosition(1, 1)));
        gameService.setPieceAt(7, 1, new Queen(WHITE, new IndexPosition(7, 1)));

        /*
            7  . Q . . . . . k
            6  . . . . . . . .
            5  . . . . . . . .
            4  . . . . . . . .
            3  . . . . . . . .
            2  . . . . . . . .
            1  P b . . . . . .
            0  K B . . . . .
               0 1 2 3 4 5 6 7
         */

        assertFalse(gameService.isCheckmate());
    }

}