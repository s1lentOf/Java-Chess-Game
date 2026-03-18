package Test;

import ChessPieces.*;
import Constants.ColorForChessPieces;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class TestRook {

    private Set<String> toSet(IndexPosition[] moves) {
        Set<String> set = new HashSet<>();
        for (IndexPosition pos : moves) {
            set.add(pos.getRow() + "," + pos.getCol());
        }
        return set;
    }


    @Test
    @DisplayName("Rook in the centre of the board can move anywhere")
    public void testRookInTheCentreOfTheBoardCanMoveAnywhere() {
        Piece[][] board = new Piece[8][8];
        Rook rook = new Rook(ColorForChessPieces.WHITE,  new IndexPosition(3,3));

        IndexPosition[] moves = rook.getPossibleMoves(board);

        assertEquals(14,moves.length);

    }


    @Test
    @DisplayName("Rook can capture the enemy pieces")
    public void testRookCaptureEnemyPieces(){
        Piece[][] board = new Piece[8][8];
        Rook rook = new Rook(ColorForChessPieces.WHITE,  new IndexPosition(3,3));
        Pawn pawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(3,4));

        board[3][3] = rook;
        board[3][4] = pawn;

        IndexPosition[] moves = rook.getPossibleMoves(board);
        Set<String> movesSet = toSet(moves);

        assertTrue(movesSet.contains("3,4"));

    }




    @Test
    @DisplayName("Rook does not pierces through enemy pieces")
    public void testRookDoesNotPiercesTroughEnemyPieces(){
        Piece[][] board = new Piece[8][8];
        Rook rook = new Rook(ColorForChessPieces.WHITE,  new IndexPosition(3,3));
        Pawn pawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(3,4));

        board[3][3] = rook;
        board[3][4] = pawn;

        IndexPosition[] moves = rook.getPossibleMoves(board);

        assertEquals(11,moves.length);

    }


    @Test
    @DisplayName("Rook cannot capture the allied pieces")
    public void testRookCannotCaptureAlliedPieces(){
        Piece[][] board = new Piece[8][8];
        Rook rook = new Rook(ColorForChessPieces.WHITE,  new IndexPosition(3,3));
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(3,4));

        board[3][3] = rook;
        board[3][4] = pawn;

        IndexPosition[] moves = rook.getPossibleMoves(board);
        Set<String> movesSet = toSet(moves);

        assertFalse(movesSet.contains("3,4"));

    }


    @Test
    @DisplayName("Rook does not pierces through allied pieces")
    public void testRookDoesNotPiercesTroughAlliedPieces(){
        Piece[][] board = new Piece[8][8];
        Rook rook = new Rook(ColorForChessPieces.WHITE,  new IndexPosition(3,3));
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(3,4));

        board[3][3] = rook;
        board[3][4] = pawn;

        IndexPosition[] moves = rook.getPossibleMoves(board);

        assertEquals(10,moves.length);

    }







}

