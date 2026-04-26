package Test;

import ChessBoard.BoardUI;
import ChessPieces.*;
import Constants.ColorForChessPieces;
import Services.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class BoardUITest {

    private BoardUI boardUI;
    private IndexPosition[][] positions;
    private GameService service;

    @BeforeEach
    void setUp() {
        boardUI = new BoardUI();
        service = new GameService(boardUI);
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

        service.setPieceAt(0, 0, piece);

        service.handleSquareClick(new IndexPosition(0, 0));

        boardUI.setSelected(piece);

        Piece selected = boardUI.getSelected();

        assertEquals(piece, selected);
    }

    @Test
    void testHandleSquareClick_MoveToEmptySquare() throws Exception {
        Piece piece = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 0));
        Piece king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));

        Piece[][] boardArray = new Piece[8][8];
        boardArray[1][0] = piece;

        service.setPieceAt(1, 0, piece);
        service.setPieceAt(0, 4, king);

        boardUI.setSelected(piece);

        IndexPosition move = new IndexPosition(2, 0);

        service.handleSquareClick(move);

        Piece[][] result = service.getPiecesOnTheBoard();

        assertNull(result[1][0]);
        assertEquals(piece, result[2][0]);
    }

    @Test
    void testHandleSquareClick_CaptureEnemy() {
        Piece white = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 0));
        Piece black = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(2, 1));
        Piece whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Piece blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 4));

        service.setPieceAt(0, 4, whiteKing);
        service.setPieceAt(7, 4, blackKing);
        service.setPieceAt(1, 0, white);
        service.setPieceAt(2, 1, black);

        service.setMoveCounter(1); // WHITE's turn

        // select white pawn
        service.handleSquareClick(new IndexPosition(1, 0));
        assertEquals(white, boardUI.getSelected(), "white pawn should be selected");

        // debug
        System.out.println("Legal moves: " + service.getLegalMoves(white, service.getPiecesOnTheBoard()));

        // capture black pawn
        service.handleSquareClick(new IndexPosition(2, 1));

        Piece[][] result = service.getPiecesOnTheBoard();
        assertNull(result[1][0], "original square should be empty");
        assertEquals(white, result[2][1], "white pawn should be on captured square");
    }

    @Test
    void testHandleSquareClick_ReselectSameColor() {
        Piece white1 = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 0));
        Piece white2 = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(2, 1));
        Piece king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));

        service.setPieceAt(1, 0, white1);
        service.setPieceAt(2, 1, white2);
        service.setPieceAt(0, 4, king);

        boardUI.setSelected(white1);

        service.handleSquareClick(new IndexPosition(2, 1));

        Piece[][] result = service.getPiecesOnTheBoard();

        assertNull(result[1][0]);
        assertEquals(white1, result[2][1]);

        assertNull(boardUI.getSelected());
    }

    @Test
    void testHandleSquareClick_InvalidMove() {
        Piece piece = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Piece king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));

        service.setPieceAt(0, 0, piece);
        service.setPieceAt(0, 4, king);
        boardUI.setSelected(piece);

        IndexPosition invalidMove = new IndexPosition(5, 5);

        service.handleSquareClick(invalidMove);

        Piece[][] result = service.getPiecesOnTheBoard();

        assertEquals(piece, result[0][0]);
        assertNull(result[5][5]);
        assertEquals(piece, boardUI.getSelected());
    }

    @Test
    void testSetUpPiecesOnTheBoard_Pawns() {
        BoardUI boardUI = new BoardUI();

        service.setUpPiecesOnTheBoard();

        Piece[][] pieces = service.getPiecesOnTheBoard();

        // --- Pawns ---
        for (int i = 0; i < 8; i++) {
            assertInstanceOf(Pawn.class, pieces[1][i]);
            assertTrue(pieces[1][i].isWhite());

            assertInstanceOf(Pawn.class, pieces[6][i]);
            assertFalse(pieces[6][i].isWhite());
        }
    }

    @Test
    void testSetUpPiecesOnTheBoard_Rooks() {
        BoardUI boardUI = new BoardUI();

        service.setUpPiecesOnTheBoard();

        Piece[][] pieces = service.getPiecesOnTheBoard();

        assertInstanceOf(Rook.class, pieces[0][0]);
        assertInstanceOf(Rook.class, pieces[0][7]);
        assertInstanceOf(Rook.class, pieces[7][0]);
        assertInstanceOf(Rook.class, pieces[7][7]);
    }

    @Test
    void testSetUpPiecesOnTheBoard_Knights() {
        BoardUI boardUI = new BoardUI();

        service.setUpPiecesOnTheBoard();

        Piece[][] pieces = service.getPiecesOnTheBoard();

        assertInstanceOf(Knight.class, pieces[0][1]);
        assertInstanceOf(Knight.class, pieces[0][6]);
        assertInstanceOf(Knight.class, pieces[7][1]);
        assertInstanceOf(Knight.class, pieces[7][6]);
    }

    @Test
    void testSetUpPiecesOnTheBoard_Bishops() {
        BoardUI boardUI = new BoardUI();

        service.setUpPiecesOnTheBoard();

        Piece[][] pieces = service.getPiecesOnTheBoard();

        assertInstanceOf(Bishop.class, pieces[0][2]);
        assertInstanceOf(Bishop.class, pieces[0][5]);
        assertInstanceOf(Bishop.class, pieces[7][2]);
        assertInstanceOf(Bishop.class, pieces[7][5]);
    }

    @Test
    void testSetUpPiecesOnTheBoard_Queen() {
        BoardUI boardUI = new BoardUI();

        service.setUpPiecesOnTheBoard();

        Piece[][] pieces = service.getPiecesOnTheBoard();

        assertInstanceOf(Queen.class, pieces[0][3]);
        assertInstanceOf(Queen.class, pieces[7][3]);
    }

    @Test
    void testSetUpPiecesOnTheBoard_King() {
        BoardUI boardUI = new BoardUI();

        service.setUpPiecesOnTheBoard();

        Piece[][] pieces = service.getPiecesOnTheBoard();

        assertInstanceOf(King.class, pieces[0][4]);
        assertInstanceOf(King.class, pieces[7][4]);
    }

    @Test
    void testPiecesTurn() {
        Piece white = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 0));
        Piece black = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(6, 0));
        Piece blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 4));
        Piece whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));

        service.setPieceAt(7, 4, blackKing);
        service.setPieceAt(0, 4, whiteKing);
        service.setPieceAt(1, 0, white);
        service.setPieceAt(6, 0, black);

        service.setMoveCounter(0);

        service.handleSquareClick(new IndexPosition(1, 0));
        assertNull(boardUI.getSelected(), "cannot select white on black's turn");

        service.handleSquareClick(new IndexPosition(6, 0));
        assertEquals(black, boardUI.getSelected(), "black should be selectable");

        service.handleSquareClick(new IndexPosition(5, 0));


        service.handleSquareClick(new IndexPosition(1, 0));
        assertEquals(white, boardUI.getSelected(), "white should be selectable");
    }
}