package Test;

import ChessPieces.*;
import Constants.ColorForChessPieces;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestQueen {


    private Set<String> toSet(IndexPosition[] moves) {
        Set<String> set = new HashSet<>();
        for (IndexPosition pos : moves) {
            set.add(pos.getRow() + "," + pos.getCol());
        }
        return set;
    }

    @Test
    @DisplayName("Queen in the centre of the board can move anywhere")
    public void testQueenInTheCentreOfTheBoardCanMoveAnywhere() {
        Piece[][] board = new Piece[8][8];
        Queen queen = new Queen(ColorForChessPieces.WHITE,  new IndexPosition(3,3));

        IndexPosition[] moves = queen.getPossibleMoves(board);

        assertEquals(27,moves.length);

    }


    @Test
    @DisplayName("Queen can capture the enemy pieces")
    public void testQueenCaptureEnemyPieces(){
        Piece[][] board = new Piece[8][8];
        Queen queen = new Queen(ColorForChessPieces.WHITE,  new IndexPosition(3,3));
        Pawn pawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(3,4));

        board[3][3] = queen;
        board[3][4] = pawn;

        IndexPosition[] moves = queen.getPossibleMoves(board);
        Set<String> movesSet = toSet(moves);

        assertTrue(movesSet.contains("3,4"));

    }


    @Test
    @DisplayName("Queen does not pierces through enemy pieces")
    public void testQueenDoesNotPiercesTroughEnemyPieces(){
        Piece[][] board = new Piece[8][8];
        Queen queen = new Queen(ColorForChessPieces.WHITE,  new IndexPosition(3,3));
        Pawn pawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(3,4));

        board[3][3] = queen;
        board[3][4] = pawn;

        IndexPosition[] moves = queen.getPossibleMoves(board);

        assertEquals(24,moves.length);

    }



    @Test
    @DisplayName("Queen cannot capture the allied pieces")
    public void testQueenCannotCaptureAlliedPieces(){
        Piece[][] board = new Piece[8][8];
        Queen queen = new Queen(ColorForChessPieces.WHITE,  new IndexPosition(3,3));
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(3,4));

        board[3][3] = queen;
        board[3][4] = pawn;

        IndexPosition[] moves = queen.getPossibleMoves(board);
        Set<String> movesSet = toSet(moves);

        assertFalse(movesSet.contains("3,4"));

    }



    @Test
    @DisplayName("Queen does not pierces through allied pieces")
    public void testQueenDoesNotPiercesTroughAlliedPieces(){
        Piece[][] board = new Piece[8][8];
        Queen queen = new Queen(ColorForChessPieces.WHITE,  new IndexPosition(3,3));
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(3,4));

        board[3][3] = queen;
        board[3][4] = pawn;

        IndexPosition[] moves = queen.getPossibleMoves(board);

        assertEquals(23,moves.length);

    }
}
