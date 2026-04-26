package Test;

import ChessBoard.BoardUI;
import ChessPieces.IndexPosition;
import ChessPieces.Pawn;
import ChessPieces.Piece;
import ChessPieces.Queen;
import Constants.ColorForChessPieces;
import Services.GameService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestPawn {
    @Test
    @DisplayName(" White Pawn is on the starting square")
    public void testWhitePawnOnStartSquare() {
        Piece[][] board = new Piece[8][8];
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 3));
        board[1][3] = pawn;

        IndexPosition[] moves = pawn.getPossibleMoves(board);

        assertEquals(2, moves.length);


    }


    @Test
    @DisplayName(" Black Pawn is on the starting square")
    public void testBlackPawnOnStartSquare() {
        Piece[][] board = new Piece[8][8];
        Pawn pawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(6, 3));
        board[6][3] = pawn;

        IndexPosition[] moves = pawn.getPossibleMoves(board);

        assertEquals(2, moves.length);


    }


    @Test
    @DisplayName("Pawn cannot move to the squares occupied by pieces")
    public void testPawnCannotMoveToTheSquaresOccupiedByPieces() {
        Piece[][] board = new Piece[8][8];
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 3));
        Pawn pawn2 = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(2, 3));

        IndexPosition[] moves = pawn.getPossibleMoves(board);

        assertEquals(2, moves.length);
    }


    @Test
    @DisplayName("Pawn can capture pieces on diagonal squares")
    public void testPawnCapturePiecesOnDiagonalSquares() {
        Piece[][] board = new Piece[8][8];
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 3));
        Pawn pawn1 = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(2, 3));
        Pawn pawn2 = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(2, 2));
        Pawn pawn3 = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(2, 4));

        board[2][3] = pawn;
        board[2][2] = pawn2;
        board[2][4] = pawn3;

        IndexPosition[] moves = pawn.getPossibleMoves(board);

        assertEquals(2, moves.length);
    }


    @Test
    @DisplayName("Pawn cannot capture the allied piece")
    public void testPawnCannotCaptureTheAlliedPiece() {
        Piece[][] board = new Piece[8][8];
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 3));
        board[1][3] = pawn;
        // place allied pawns on both diagonal capture squares
        board[2][2] = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(2, 2));
        board[2][4] = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(2, 4));

        IndexPosition[] moves = pawn.getPossibleMoves(board);

        assertEquals(2, moves.length);

    }


    @Test
    @DisplayName("White or black piece hasn't moved")
    public void testWhiteOrBlackPieceHasNotMoved() {
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 3));
        Pawn pawn2 = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(6, 3));

        assertFalse(pawn.hasMoved());
        assertFalse(pawn2.hasMoved());
    }

    @Test
    @DisplayName("White or black piece has moved")
    public void testWhiteOrBlackPieceHasMoved() {
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(2, 3));
        Pawn pawn2 = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(5, 3));

        assertTrue(pawn.hasMoved());
        assertTrue(pawn2.hasMoved());
    }

    @Test
    @DisplayName("Pawn promotes to a Queen")
    public void testPromotion() {
        BoardUI boardUI = new BoardUI();
        GameService service = new GameService(boardUI);
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(6, 3));
        service.getPiecesOnTheBoard()[6][3] = pawn;
        service.selectPiece(6,3);
        service.moveSelectedPiece(new IndexPosition(7,3));

        assertInstanceOf(Queen.class, service.getPiecesOnTheBoard()[7][3]);
    }

    @Test
    void whitePawnCanCaptureEnPassant() {
        Piece[][] board = new Piece[8][8];
        Pawn whitePawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(4,3));
        Pawn blackPawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(4,4));

        // positions
        board[4][3] = whitePawn;
        board[4][4] = blackPawn;

        IndexPosition[] moves = whitePawn.getPossibleMoves(board, blackPawn);

        // en passant square should be (5,4)
        boolean found = false;
        for (IndexPosition move : moves) {
            if (move.getRow() == 5 && move.getCol() == 4) {
                found = true;
                break;
            }
        }
        assertTrue(found, "White pawn should be able to capture en passant");
    }

    @Test
    void blackPawnCanCaptureEnPassant() {
        Piece[][] board = new Piece[8][8];
        Pawn whitePawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(3,3));
        Pawn blackPawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(3,4));

        board[3][3] = blackPawn;   // black pawn
        board[3][4] = whitePawn;   // white pawn just moved two steps

        IndexPosition[] moves = blackPawn.getPossibleMoves(board, whitePawn);

        boolean found = false;
        for (IndexPosition move : moves) {
            if (move.getRow() == 2 && move.getCol() == 4) {
                found = true;
                break;
            }
        }
        assertTrue(found, "Black pawn should be able to capture en passant");
    }

    @Test
    void enPassantNotPossibleIfNoDoubleStep() {
        Piece[][] board = new Piece[8][8];
        Pawn whitePawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(4,3));
        Pawn blackPawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(4,4));

        board[4][3] = whitePawn;
        board[4][4] = blackPawn;  // black pawn, but assume it didn't just move 2 steps

        IndexPosition[] moves = whitePawn.getPossibleMoves(board, null);

        boolean found = false;
        for (IndexPosition move : moves) {
            if (move.getRow() == 5 && move.getCol() == 4) {
                found = true;
                break;
            }
        }
        assertFalse(found, "En passant should not be possible if no pawn moved two squares last turn");
    }

    @Test
    void enPassantNotPossibleIfNotAdjacent() {
        Piece[][] board = new Piece[8][8];
        Pawn whitePawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(4,3));
        Pawn blackPawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(4,5));

        board[4][3] = whitePawn;
        board[4][5] = blackPawn;  // two squares away, not adjacent

        IndexPosition[] moves = whitePawn.getPossibleMoves(board, blackPawn);

        boolean found = false;
        for (IndexPosition move : moves) {
            if (move.getRow() == 5 && move.getCol() == 5) {
                found = true;
                break;
            }
        }
        assertFalse(found, "En passant should not be possible if the pawn is not adjacent");
    }








}
