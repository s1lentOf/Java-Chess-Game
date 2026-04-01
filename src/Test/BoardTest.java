package Test;

import ChessBoard.Board;
import ChessPieces.*;
import Constants.ColorForChessPieces;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;
    private IndexPosition[][] positions;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        board.setUpMatrix();

        Field field = Board.class.getDeclaredField("boardPositions");
        field.setAccessible(true);
        positions = (IndexPosition[][]) field.get(board);
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
                assertEquals((row + 1) * 75, positions[row][col].getRow(),
                        "row mismatch at [" + row + "][" + col + "]");

                assertEquals((col + 1) * 75, positions[row][col].getCol(),
                        "col mismatch at [" + row + "][" + col + "]");
            }
        }
    }

    @Test
    void testFirstPosition() {
        assertEquals(75, positions[0][0].getCol());
        assertEquals(75, positions[0][0].getRow());
    }

    @Test
    void testLastPosition() {
        assertEquals(600, positions[7][7].getCol());
        assertEquals(600, positions[7][7].getRow());
    }

    @Test
    void testSetUpMatrixIsIdempotent() {
        board.setUpMatrix();
        board.setUpMatrix();

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                assertNotNull(positions[row][col],
                        "Position should not be null at [" + row + "][" + col + "]");
                assertEquals((row + 1) * 75, positions[row][col].getRow(),
                        "row mismatch at [" + row + "][" + col + "]");

                assertEquals((col + 1) * 75, positions[row][col].getCol(),
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

        IndexPosition result = board.getPixelsToDraw(piece);

        assertNotNull(result);
        assertEquals(75, result.getCol());
        assertEquals(75, result.getRow());
    }

    @Test
    void testGetPixelsToDraw_MiddlePosition() {
        IndexPosition position = new IndexPosition(3, 4);
        Piece piece = new Rook(ColorForChessPieces.WHITE, position);

        Board board = new Board();
        board.setUpMatrix();

        IndexPosition result = board.getPixelsToDraw(piece);

        System.out.println(result.getRow() + " " + result.getCol());

        assertNotNull(result);
        assertEquals(300, result.getRow());
        assertEquals(375, result.getCol());
    }

    @Test
    void testGetPixelsToDraw_LastSquare() {
        IndexPosition position = new IndexPosition(7, 7);
        Piece piece = new Rook(ColorForChessPieces.BLACK, position);

        Board board = new Board();
        board.setUpMatrix();

        IndexPosition result = board.getPixelsToDraw(piece);

        assertNotNull(result);
        assertEquals(600, result.getCol());
        assertEquals(600, result.getRow());
    }

    @Test
    void testHandleSquareClick_SelectPiece() throws Exception {
        Piece piece = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(0, 0));

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
        Piece piece = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 0));

        Piece[][] boardArray = new Piece[8][8];
        boardArray[1][0] = piece;

        Field boardField = Board.class.getDeclaredField("piecesOnTheBoard");
        boardField.setAccessible(true);
        boardField.set(board, boardArray);

        Field selectedField = Board.class.getDeclaredField("selected");
        selectedField.setAccessible(true);
        selectedField.set(board, piece);

        IndexPosition move = new IndexPosition(2, 0);

        board.handleSquareClick(move);

        Piece[][] result = (Piece[][]) boardField.get(board);

        assertNull(result[1][0]);
        assertEquals(piece, result[2][0]);
    }

    @Test
    void testHandleSquareClick_CaptureEnemy() throws Exception {
        Piece white = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 0));
        Piece black = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(2, 1));

        Piece[][] boardArray = new Piece[8][8];
        boardArray[1][0] = white;
        boardArray[2][1] = black;

        Field boardField = Board.class.getDeclaredField("piecesOnTheBoard");
        boardField.setAccessible(true);
        boardField.set(board, boardArray);

        Field selectedField = Board.class.getDeclaredField("selected");
        selectedField.setAccessible(true);
        selectedField.set(board, white);

        IndexPosition move = new IndexPosition(2, 1);

        board.handleSquareClick(move);

        Piece[][] result = (Piece[][]) boardField.get(board);

        assertNull(result[1][0]);
        assertEquals(white, result[2][1]);
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

    @Test
    void testSetUpPiecesOnTheBoard_Pawns() {
        Board board = new Board();

        board.setUpPiecesOnTheBoard();

        Piece[][] pieces = board.getPiecesOnTheBoard();

        // --- Pawns ---
        for (int i = 0; i < 8; i++) {
            assertTrue(pieces[1][i] instanceof Pawn);
            assertTrue(pieces[1][i].isWhite());

            assertTrue(pieces[6][i] instanceof Pawn);
            assertFalse(pieces[6][i].isWhite());
        }
    }

    @Test
    void testSetUpPiecesOnTheBoard_Rooks() {
        Board board = new Board();

        board.setUpPiecesOnTheBoard();

        Piece[][] pieces = board.getPiecesOnTheBoard();

        assertTrue(pieces[0][0] instanceof Rook);
        assertTrue(pieces[0][7] instanceof Rook);
        assertTrue(pieces[7][0] instanceof Rook);
        assertTrue(pieces[7][7] instanceof Rook);
    }

    @Test
    void testSetUpPiecesOnTheBoard_Knights() {
        Board board = new Board();

        board.setUpPiecesOnTheBoard();

        Piece[][] pieces = board.getPiecesOnTheBoard();

        assertTrue(pieces[0][1] instanceof Knight);
        assertTrue(pieces[0][6] instanceof Knight);
        assertTrue(pieces[7][1] instanceof Knight);
        assertTrue(pieces[7][6] instanceof Knight);
    }

    @Test
    void testSetUpPiecesOnTheBoard_Bishops() {
        Board board = new Board();

        board.setUpPiecesOnTheBoard();

        Piece[][] pieces = board.getPiecesOnTheBoard();

        assertTrue(pieces[0][2] instanceof Bishop);
        assertTrue(pieces[0][5] instanceof Bishop);
        assertTrue(pieces[7][2] instanceof Bishop);
        assertTrue(pieces[7][5] instanceof Bishop);
    }

    @Test
    void testSetUpPiecesOnTheBoard_Queen() {
        Board board = new Board();

        board.setUpPiecesOnTheBoard();

        Piece[][] pieces = board.getPiecesOnTheBoard();

        assertTrue(pieces[0][3] instanceof Queen);
        assertTrue(pieces[7][3] instanceof Queen);
    }

    @Test
    void testSetUpPiecesOnTheBoard_King() {
        Board board = new Board();

        board.setUpPiecesOnTheBoard();

        Piece[][] pieces = board.getPiecesOnTheBoard();

        assertTrue(pieces[0][4] instanceof King);
        assertTrue(pieces[7][4] instanceof King);
    }

    @Test
    void testPiecesTurn() throws Exception {
        Piece white = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 0));
        Piece black = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(6, 0));

        Piece[][] boardArray = new Piece[8][8];
        boardArray[1][0] = white;
        boardArray[6][0] = black;

        Field boardField = Board.class.getDeclaredField("piecesOnTheBoard");
        boardField.setAccessible(true);
        boardField.set(board, boardArray);

        Field selectedField = Board.class.getDeclaredField("selected");
        selectedField.setAccessible(true);

        Field moveCounterField = Board.class.getDeclaredField("moveCounter");
        moveCounterField.setAccessible(true);

        moveCounterField.set(board, 0);

        board.handleSquareClick(new IndexPosition(1, 0));
        assertNull(selectedField.get(board), "cannot select white on blacks turn");

        board.handleSquareClick(new IndexPosition(6, 0));
        assertEquals(black, selectedField.get(board), "black should be selectable");

        IndexPosition blackMove = new IndexPosition(5, 0);
        board.handleSquareClick(blackMove);

        selectedField.set(board, null);

        board.handleSquareClick(new IndexPosition(1, 0));
        assertEquals(white, selectedField.get(board), "white should be selectable");
    }
}