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
    void testHandleSquareClick_SelectPiece() throws Exception {
        Piece piece = new Pawn(null, new IndexPosition(0, 0));

        Field boardField = Board.class.getDeclaredField("piecesOnTheBoard");
        boardField.setAccessible(true);
        Piece[][] boardArray = new Piece[8][8];
        boardArray[0][0] = piece;
        boardField.set(board, boardArray);

        board.handleSquareClick(new IndexPosition(0, 0));

        Field selectedField = Board.class.getDeclaredField("selected");
        selectedField.setAccessible(true);
        Piece selected = (Piece) selectedField.get(board);

        assertEquals(piece, selected);
    }

    @Test
    void testHandleSquareClick_MoveToEmptySquare() throws Exception {
        Piece piece = new Pawn(null, new IndexPosition(0, 0));

        Piece[][] boardArray = new Piece[8][8];
        boardArray[0][0] = piece;

        Field boardField = Board.class.getDeclaredField("piecesOnTheBoard");
        boardField.setAccessible(true);
        boardField.set(board, boardArray);

        Field selectedField = Board.class.getDeclaredField("selected");
        selectedField.setAccessible(true);
        selectedField.set(board, piece);

        IndexPosition move = new IndexPosition(0, 1);

        board.handleSquareClick(move);

        Piece[][] result = (Piece[][]) boardField.get(board);

        assertNull(result[0][0]);
        assertEquals(piece, result[0][1]);
    }

    @Test
    void testHandleSquareClick_CaptureEnemy() throws Exception {
        Piece white = new Pawn(null, new IndexPosition(0, 0));
        Piece black = new Pawn(null, new IndexPosition(0, 1));

        Piece[][] boardArray = new Piece[8][8];
        boardArray[0][0] = white;
        boardArray[0][1] = black;

        Field boardField = Board.class.getDeclaredField("piecesOnTheBoard");
        boardField.setAccessible(true);
        boardField.set(board, boardArray);

        Field selectedField = Board.class.getDeclaredField("selected");
        selectedField.setAccessible(true);
        selectedField.set(board, white);

        IndexPosition move = new IndexPosition(0, 1);

        board.handleSquareClick(move);

        Piece[][] result = (Piece[][]) boardField.get(board);

        assertNull(result[0][0]);
        assertEquals(white, result[0][1]);
    }

    @Test
    void testHandleSquareClick_ReselectSameColor() throws Exception {
        Piece first = new Pawn(null, new IndexPosition(0, 0));
        Piece second = new Pawn(null, new IndexPosition(1, 0));

        Piece[][] boardArray = new Piece[8][8];
        boardArray[0][0] = first;
        boardArray[1][0] = second;

        Field boardField = Board.class.getDeclaredField("piecesOnTheBoard");
        boardField.setAccessible(true);
        boardField.set(board, boardArray);

        Field selectedField = Board.class.getDeclaredField("selected");
        selectedField.setAccessible(true);
        selectedField.set(board, first);

        board.handleSquareClick(new IndexPosition(1, 0));

        Piece selected = (Piece) selectedField.get(board);

        assertEquals(second, selected);
    }

    @Test
    void testHandleSquareClick_InvalidMove() throws Exception {
        Piece piece = new Pawn(null, new IndexPosition(0, 0));

        Piece[][] boardArray = new Piece[8][8];
        boardArray[0][0] = piece;

        Field boardField = Board.class.getDeclaredField("piecesOnTheBoard");
        boardField.setAccessible(true);
        boardField.set(board, boardArray);

        Field selectedField = Board.class.getDeclaredField("selected");
        selectedField.setAccessible(true);
        selectedField.set(board, piece);

        IndexPosition invalidMove = new IndexPosition(5, 5);

        board.handleSquareClick(invalidMove);

        Piece[][] result = (Piece[][]) boardField.get(board);

        assertEquals(piece, result[0][0]);
        assertNull(result[5][5]);
    }
}