package Test;

import ChessBoard.Board;
import ChessPieces.IndexPosition;
import ChessPieces.Knight;
import ChessPieces.Pawn;
import ChessPieces.Piece;
import Constants.ColorForChessPieces;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class TestKnight {

    // helper to convert moves array to a set of "row,col" strings for easy comparison
    private Set<String> toSet(IndexPosition[] moves) {
        Set<String> set = new HashSet<>();
        for (IndexPosition pos : moves) {
            set.add(pos.getRow() + "," + pos.getCol());
        }
        return set;
    }

    @Test
    @DisplayName("Knight in the centre of board can move anywhere ")
    public void testKnightInCentreOfBoardCanMoveAnywhere() {
        Piece[][] board = new Piece[8][8];
        Knight knight = new Knight(ColorForChessPieces.WHITE, new IndexPosition(3, 3));

        IndexPosition[] moves = knight.getPossibleMoves(board);

        assertEquals(8, moves.length);
    }
    @Test
    @DisplayName("Knight can capture enemy pieces")
    public void testKnightCanCaptureEnemyPieces(){
        Piece[][] board = new Piece[8][8];
        Knight knight = new Knight(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Pawn pawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(5, 4));

        board[3][3] = knight;
        board[5][4] = pawn;

        IndexPosition[] moves = knight.getPossibleMoves(board);
        Set<String> movesSet = toSet(moves);
        assertTrue(movesSet.contains("5,4"));

    }


    @Test
    @DisplayName("Knight cannot capture allied pieces")
    public void testKnightCannotCaptureAlliedPieces(){
        Piece[][] board = new Piece[8][8];
        Knight knight = new Knight(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(5, 4));

        board[3][3] = knight;
        board[5][4] = pawn;

        IndexPosition[] moves = knight.getPossibleMoves(board);
        Set<String> movesSet = toSet(moves);
        assertFalse(movesSet.contains("5,4"));

    }

    @Test
    @DisplayName("Knight in the corner of board can move only to 2 squares ")
    public void testKnightInCornerOfBoardCanMoveTo2Squares() {
        Piece[][] board = new Piece[8][8];
        Knight knight = new Knight(ColorForChessPieces.WHITE, new IndexPosition(0, 0));

        IndexPosition[] moves = knight.getPossibleMoves(board);

        assertEquals(2, moves.length);
    }








}

