package Test;

import ChessBoard.Board;
import ChessPieces.*;
import Constants.ColorForChessPieces;
import Services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    private Board board;
    private IndexPosition[][] positions;
    private GameService service;

    @BeforeEach
    void setUp() {
        board = new Board();
        service = new GameService(board);
    }

    @Test
    void testIsMovePossible() {
        ArrayList<IndexPosition> possibleMoves = new ArrayList<>();

        possibleMoves.add(new IndexPosition(2, 3));
        possibleMoves.add(new IndexPosition(3, 3));

        IndexPosition validMove = new IndexPosition(2, 3);
        IndexPosition invalidMove = new IndexPosition(4, 4);

        assertTrue(service.isMovePossible(possibleMoves, validMove));
        assertFalse(service.isMovePossible(possibleMoves, invalidMove));
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
        Piece king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));

        Piece[][] boardArray = new Piece[8][8];
        boardArray[1][0] = piece;

        board.setPieceAt(1, 0, piece);
        board.setPieceAt(0, 4, king);

        board.setSelected(piece);

        IndexPosition move = new IndexPosition(2, 0);

        service.handleSquareClick(move);

        Piece[][] result = board.getPiecesOnTheBoard();

        assertNull(result[1][0]);
        assertEquals(piece, result[2][0]);
    }

    @Test
    void testHandleSquareClick_CaptureEnemy() {
        Piece white = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 0));
        Piece black = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(2, 1));
        Piece whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Piece blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 4));

        board.setPieceAt(0, 4, whiteKing);
        board.setPieceAt(7, 4, blackKing);
        board.setPieceAt(1, 0, white);
        board.setPieceAt(2, 1, black);

        service.setMoveCounter(1); // WHITE's turn

        // select white pawn
        service.handleSquareClick(new IndexPosition(1, 0));
        assertEquals(white, board.getSelected(), "white pawn should be selected");

        // debug
        System.out.println("Legal moves: " + service.getLegalMoves(white, board.getPiecesOnTheBoard()));

        // capture black pawn
        service.handleSquareClick(new IndexPosition(2, 1));

        Piece[][] result = board.getPiecesOnTheBoard();
        assertNull(result[1][0], "original square should be empty");
        assertEquals(white, result[2][1], "white pawn should be on captured square");
    }

    @Test
    void testHandleSquareClick_ReselectSameColor() {
        Piece white1 = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 0));
        Piece white2 = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(2, 1));
        Piece king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));

        board.setPieceAt(1, 0, white1);
        board.setPieceAt(2, 1, white2);
        board.setPieceAt(0, 4, king);

        board.setSelected(white1);

        service.handleSquareClick(new IndexPosition(2, 1));

        Piece[][] result = board.getPiecesOnTheBoard();

        assertNull(result[1][0]);
        assertEquals(white1, result[2][1]);

        assertNull(board.getSelected());
    }

    @Test
    void testHandleSquareClick_InvalidMove() {
        Piece piece = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Piece king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));

        board.setPieceAt(0, 0, piece);
        board.setPieceAt(0, 4, king);
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
    void testPiecesTurn() {
        Piece white = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 0));
        Piece black = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(6, 0));
        Piece blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 4));
        Piece whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));

        board.setPieceAt(7, 4, blackKing);
        board.setPieceAt(0, 4, whiteKing);
        board.setPieceAt(1, 0, white);
        board.setPieceAt(6, 0, black);

        service.setMoveCounter(0);

        service.handleSquareClick(new IndexPosition(1, 0));
        assertNull(board.getSelected(), "cannot select white on black's turn");

        service.handleSquareClick(new IndexPosition(6, 0));
        assertEquals(black, board.getSelected(), "black should be selectable");

        service.handleSquareClick(new IndexPosition(5, 0));


        service.handleSquareClick(new IndexPosition(1, 0));
        assertEquals(white, board.getSelected(), "white should be selectable");
    }
}