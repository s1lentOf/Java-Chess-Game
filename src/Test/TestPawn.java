package Test;

import ChessBoard.Board;
import ChessPieces.IndexPosition;
import ChessPieces.Pawn;
import ChessPieces.Piece;
import Constants.ColorForChessPieces;
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








}
