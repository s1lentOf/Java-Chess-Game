package Test;

import ChessPieces.IndexPosition;
import ChessPieces.King;
import ChessPieces.Piece;
import Constants.ColorForChessPieces;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestKing {

    // helper to convert moves array to a set of "row,col" strings for easy comparison
    private Set<String> toSet(IndexPosition[] moves) {
        Set<String> set = new HashSet<>();
        for (IndexPosition pos : moves) {
            set.add(pos.getRow() + "," + pos.getCol());
        }
        return set;
    }

    @Test
    @DisplayName("King in center of empty board has 8 moves")
    public void testKingCenterEmptyBoard() {
        Piece[][] board = new Piece[8][8];
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(4, 4));
        board[4][4] = king;

        IndexPosition[] moves = king.getPossibleMoves(board);

        assertEquals(8, moves.length);
    }

    @Test
    @DisplayName("King in top-left corner has 3 moves")
    public void testKingCorner() {
        Piece[][] board = new Piece[8][8];
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        board[0][0] = king;

        IndexPosition[] moves = king.getPossibleMoves(board);

        assertEquals(3, moves.length);
        Set<String> expected = new HashSet<>(Arrays.asList("0,1", "1,0", "1,1"));
        assertEquals(expected, toSet(moves));
    }

    @Test
    @DisplayName("King on edge (not corner) has 5 moves")
    public void testKingEdge() {
        Piece[][] board = new Piece[8][8];
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        board[0][4] = king;

        IndexPosition[] moves = king.getPossibleMoves(board);

        assertEquals(5, moves.length);
    }

    @Test
    @DisplayName("King cannot move to square occupied by ally")
    public void testKingBlockedByAlly() {
        Piece[][] board = new Piece[8][8];
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(4, 4));
        board[4][4] = king;
        // place a white ally on every surrounding square
        board[3][3] = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        board[3][4] = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 4));
        board[3][5] = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 5));
        board[4][3] = new King(ColorForChessPieces.WHITE, new IndexPosition(4, 3));
        board[4][5] = new King(ColorForChessPieces.WHITE, new IndexPosition(4, 5));
        board[5][3] = new King(ColorForChessPieces.WHITE, new IndexPosition(5, 3));
        board[5][4] = new King(ColorForChessPieces.WHITE, new IndexPosition(5, 4));
        board[5][5] = new King(ColorForChessPieces.WHITE, new IndexPosition(5, 5));

        IndexPosition[] moves = king.getPossibleMoves(board);

        assertEquals(0, moves.length);
    }

    @Test
    @DisplayName("King can capture enemy piece")
    public void testKingCapturesEnemy() {
        Piece[][] board = new Piece[8][8];
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(4, 4));
        board[4][4] = king;
        board[3][3] = new King(ColorForChessPieces.BLACK, new IndexPosition(3, 3));

        IndexPosition[] moves = king.getPossibleMoves(board);

        Set<String> moveSet = toSet(moves);
        assertTrue(moveSet.contains("3,3"), "King should be able to capture enemy at (3,3)");
    }

    @Test
    @DisplayName("King cannot capture ally piece")
    public void testKingCannotCaptureAlly() {
        Piece[][] board = new Piece[8][8];
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(4, 4));
        board[4][4] = king;
        board[3][3] = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 3));

        IndexPosition[] moves = king.getPossibleMoves(board);

        Set<String> moveSet = toSet(moves);

        assertFalse(moveSet.contains("3,3"), "King should not be able to move to ally square at (3,3)");
    }

    @Test
    @DisplayName("Black king works the same as white king")
    public void testBlackKingCenter() {
        Piece[][] board = new Piece[8][8];
        King king = new King(ColorForChessPieces.BLACK, new IndexPosition(4, 4));
        board[4][4] = king;

        IndexPosition[] moves = king.getPossibleMoves(board);

        assertEquals(8, moves.length);
    }


    @Test
    @DisplayName(" White King hasn't moved from starting square")
    public void testWhiteKingHasStayedOnThsStartingSquare() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 3));

        assertFalse(king.hasMoved());


    }

    @Test
    @DisplayName(" White King has moved from the starting square")
    public void testWhiteKingHasMoved() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(5, 2));

        assertTrue(king.hasMoved());

    }

    @Test
    @DisplayName("Black king  hasn't moved from starting square")
    public void testBlackKingHasStayedOnThsStartingSquare() {
        King king = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 3));

        assertFalse(king.hasMoved());
    }


    @Test
    @DisplayName(" Black King has moved from the starting square")
    public void testBlackKingHasMoved() {
        King king = new King(ColorForChessPieces.BLACK, new IndexPosition(5, 2));

        assertTrue(king.hasMoved());

    }


}