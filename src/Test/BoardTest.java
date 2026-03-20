package Test;

import ChessBoard.Board;
import ChessBoard.BoardSquarePosition;
import ChessPieces.IndexPosition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;
    private BoardSquarePosition[][] positions;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        board.setUpMatrix();

        Field field = Board.class.getDeclaredField("boardPositions");
        field.setAccessible(true);
        positions = (BoardSquarePosition[][]) field.get(board);
    }

    @Test
    void testAllPositionsAreNotNull() {
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                assertNotNull(positions[i][j], "Position [" + i + "][" + j + "] should not be null");
    }

    @Test
    void testAllPositionsHaveCorrectCoordinates() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertEquals((col + 1) * 75, positions[row][col].getX(),
                        "row mismatch at [" + row + "][" + col + "]");

                assertEquals((row + 1) * 75, positions[row][col].getY(),
                        "col mismatch at [" + row + "][" + col + "]");
            }
        }
    }

    @Test
    void testFirstPosition() {
        assertEquals(75, positions[0][0].getX());
        assertEquals(75, positions[0][0].getY());
    }

    @Test
    void testLastPosition() {
        assertEquals(600, positions[7][7].getX());
        assertEquals(600, positions[7][7].getY());
    }

    @Test
    void testSetUpMatrixIsIdempotent() {
        board.setUpMatrix();
        board.setUpMatrix();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertNotNull(positions[row][col],
                        "Position should not be null at [" + row + "][" + col + "]");
                assertEquals((col + 1) * 75, positions[row][col].getX(),
                        "row mismatch at [" + row + "][" + col + "]");

                assertEquals((row + 1) * 75, positions[row][col].getY(),
                        "col mismatch at [" + row + "][" + col + "]");
            }
        }
    }

    @Test
    void testisMovePossible(){
        IndexPosition[] possibleMoves = {
                new IndexPosition(2, 3),
                new IndexPosition(3, 3)
        };

        IndexPosition validMove = new IndexPosition(2, 3);
        IndexPosition invalidMove = new IndexPosition(4, 4);

        assertTrue(board.isMovePossible(possibleMoves,validMove));
        assertFalse(board.isMovePossible(possibleMoves,invalidMove));

    }
}