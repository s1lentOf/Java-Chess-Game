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

        assertFalse(gameService.isCheckmate());
    }

    @Test
    @DisplayName("Should not be a checkmate if the king is in check, but has escape move.")
    void isCheckmate_kingIsInCheck_kingHasEscapeMove_shouldReturnFalse() {
        gameService.setPieceAt(0, 4, new King(WHITE, new IndexPosition(0, 4)));
        gameService.setPieceAt(1, 4, new Rook(BLACK, new IndexPosition(1, 4)));
        gameService.setPieceAt(7, 7, new King(BLACK, new IndexPosition(7, 7)));

        assertFalse(gameService.isCheckmate());
    }

}