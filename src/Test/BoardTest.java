package Test;

import ChessBoard.Board;
import ChessPieces.*;
import Constants.ColorForChessPieces;
import Services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;
    private IndexPosition[][] positions;
    private GameService service;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        board.setUpMatrix();
        service = new GameService(board);

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

        assertTrue(service.isMovePossible(possibleMoves,validMove));
        assertFalse(service.isMovePossible(possibleMoves,invalidMove));

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

        board.setPieceAt(0, 0, piece);

        service.handleSquareClick(new IndexPosition(0, 0));

        board.setSelected(piece);

        Piece selected = board.getSelected();

        assertEquals(piece, selected);
    }

    @Test
    void testHandleSquareClick_MoveToEmptySquare() throws Exception {
        Piece piece = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 0));

        Piece[][] boardArray = new Piece[8][8];
        boardArray[1][0] = piece;

        board.setPieceAt(1, 0, piece);

        board.setSelected(piece);

        IndexPosition move = new IndexPosition(2, 0);

        service.handleSquareClick(move);

        Piece[][] result = board.getPiecesOnTheBoard();

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

        service.handleSquareClick(move);

        Piece[][] result = (Piece[][]) boardField.get(board);

        assertNull(result[1][0]);
        assertEquals(white, result[2][1]);
    }

    @Test
    void testHandleSquareClick_ReselectSameColor() throws Exception {
        Piece white1 = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 0));
        Piece white2 = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(2, 1));

        board.setPieceAt(1, 0, white1);
        board.setPieceAt(2, 1, white2);

        board.setSelected(white1);

        service.handleSquareClick(new IndexPosition(2, 1));

        Piece[][] result = board.getPiecesOnTheBoard();

        assertNull(result[1][0]);
        assertEquals(white1, result[2][1]);

        assertNull(board.getSelected());
    }

    @Test
    void testHandleSquareClick_InvalidMove() throws Exception {
        Piece piece = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(0, 0));

        board.setPieceAt(0, 0, piece);
        board.setSelected(piece);

        IndexPosition invalidMove = new IndexPosition(5, 5);

        service.handleSquareClick(invalidMove);

        Piece[][] result = board.getPiecesOnTheBoard();

        assertEquals(piece, result[0][0]);
        assertNull(result[5][5]);
        assertEquals(piece, board.getSelected());
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

        Field moveCounterField = GameService.class.getDeclaredField("moveCounter");
        moveCounterField.setAccessible(true);

        moveCounterField.set(service, 0);

        service.handleSquareClick(new IndexPosition(1, 0));
        assertNull(selectedField.get(board), "cannot select white on blacks turn");

        service.handleSquareClick(new IndexPosition(6, 0));
        assertEquals(black, selectedField.get(board), "black should be selectable");

        IndexPosition blackMove = new IndexPosition(5, 0);
        service.handleSquareClick(blackMove);

        selectedField.set(board, null);

        service.handleSquareClick(new IndexPosition(1, 0));
        assertEquals(white, selectedField.get(board), "white should be selectable");
    }
}