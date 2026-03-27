package Test;

import ChessBoard.Board;
import ChessBoard.BoardSquarePosition;
import ChessPieces.IndexPosition;
import ChessPieces.Pawn;
import ChessPieces.Piece;
import ChessPieces.Rook;
import Constants.ColorForChessPieces;
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

    @Test
    void testGetPixelsToDraw_FirstPosition() {
        IndexPosition position = new IndexPosition(0, 0);
        Piece piece = new Rook(ColorForChessPieces.WHITE, position);

        Board board = new Board();
        board.setUpMatrix();

        BoardSquarePosition result = board.getPixelsToDraw(piece);

        assertNotNull(result);
        assertEquals(75, result.getX());
        assertEquals(75, result.getY());
    }

    @Test
    void testGetPixelsToDraw_MiddlePosition() {
        IndexPosition position = new IndexPosition(3, 4);
        Piece piece = new Rook(ColorForChessPieces.WHITE, position);

        Board board = new Board();
        board.setUpMatrix();


        BoardSquarePosition result = board.getPixelsToDraw(piece);

        assertNotNull(result);
        assertEquals(375, result.getX());
        assertEquals(300, result.getY());
    }

    @Test
    void testGetPixelsToDraw_LastSquare() {
        IndexPosition position = new IndexPosition(7, 7);
        Piece piece = new Rook(ColorForChessPieces.BLACK, position);

        Board board = new Board();
        board.setUpMatrix();

        BoardSquarePosition result = board.getPixelsToDraw(piece);

        assertNotNull(result);
        assertEquals(600, result.getX());
        assertEquals(600, result.getY());
    }


    @Test
    void testHandleSquareClick_NoSelection_EmptySquare_DoesNothing() {
        board.handleSquareClick(new IndexPosition(4, 4));

        assertNull(board.getSelected(), "selected should remain null when clicking empty square");
    }

    @Test
    void testHandleSquareClick_NoSelection_ClickPiece_SelectsPiece() {
        Piece rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        board.setPieceAt(0, 0, rook);

        board.handleSquareClick(new IndexPosition(0, 0));

        assertEquals(rook, board.getSelected(), "Clicking a piece with nothing selected should select it");
    }

    @Test
    void testHandleSquareClick_SameColor_Reselects() {
        Piece rook1 = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Piece rook2 = new Rook(ColorForChessPieces.WHITE, new IndexPosition(1, 0));
        board.setPieceAt(0, 0, rook1);
        board.setPieceAt(1, 0, rook2);

        board.handleSquareClick(new IndexPosition(0, 0));
        board.handleSquareClick(new IndexPosition(1, 0));

        assertEquals(rook2, board.getSelected(), "Clicking same-color piece should reselect to that piece");
    }

    @Test
    void testHandleSquareClick_MoveToEmptySquare() {
        Piece rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        board.setPieceAt(0, 0, rook);

        board.handleSquareClick(new IndexPosition(0, 0));
        board.handleSquareClick(new IndexPosition(0, 4));

        assertNull(board.getPieceAt(0, 0), "Origin square should be empty after move");
        assertEquals(rook, board.getPieceAt(0, 4), "Rook should now be at destination");
        assertNull(board.getSelected(), "selected should be cleared after move");
    }

    @Test
    void testHandleSquareClick_InvalidMove_DoesNotMove() {
        Piece rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        board.setPieceAt(0, 0, rook);

        board.handleSquareClick(new IndexPosition(0, 0));
        board.handleSquareClick(new IndexPosition(5, 5)); // diagonal — invalid for Rook

        assertEquals(rook, board.getPieceAt(0, 0), "Rook should not have moved");
        assertNull(board.getPieceAt(5, 5), "Nothing should appear at invalid destination");
        assertEquals(rook, board.getSelected(), "selected should still be the rook");
    }

    @Test
    void testHandleSquareClick_CapturesEnemyPiece() {
        Piece whiteRook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Piece blackRook = new Rook(ColorForChessPieces.BLACK, new IndexPosition(0, 4));
        board.setPieceAt(0, 0, whiteRook);
        board.setPieceAt(0, 4, blackRook);

        board.handleSquareClick(new IndexPosition(0, 0));
        board.handleSquareClick(new IndexPosition(0, 4));

        assertNull(board.getPieceAt(0, 0), "Origin should be empty after capture");
        assertEquals(whiteRook, board.getPieceAt(0, 4), "White rook should occupy the captured square");
        assertNull(board.getSelected(), "selected should be cleared after capture");
    }

    @Test
    void testHandleSquareClick_EnemyPiece_MoveNotPossible_NoCapture() {
        Piece whiteRook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Piece blackRook = new Rook(ColorForChessPieces.BLACK, new IndexPosition(5, 5)); // diagonal
        board.setPieceAt(0, 0, whiteRook);
        board.setPieceAt(5, 5, blackRook);

        board.handleSquareClick(new IndexPosition(0, 0));
        board.handleSquareClick(new IndexPosition(5, 5));

        assertEquals(whiteRook, board.getPieceAt(0, 0), "White rook should not have moved");
        assertEquals(blackRook, board.getPieceAt(5, 5), "Black rook should not have been captured");
    }
}