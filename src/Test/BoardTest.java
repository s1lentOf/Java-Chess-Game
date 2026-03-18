package Test;

import ChessBoard.Board;
import ChessBoard.BoardSquarePosition;
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
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                assertEquals((i + 1) * 75, positions[i][j].getX(),
                        "Row mismatch at [" + i + "][" + j + "]");
                assertEquals((j + 1) * 75, positions[i][j].getY(),
                        "Col mismatch at [" + i + "][" + j + "]");
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
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                assertNotNull(positions[i][j]);
                assertEquals((i + 1) * 75, positions[i][j].getX());
                assertEquals((j + 1) * 75, positions[i][j].getY());
            }
        }
    }
}