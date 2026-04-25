package Test;

import ChessPieces.Bishop;
import ChessPieces.IndexPosition;
import ChessPieces.Pawn;
import ChessPieces.Piece;
import Constants.ColorForChessPieces;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class TestBishop {

    private Set<String> toSet(IndexPosition[] moves) {
        Set<String> set = new HashSet<>();
        for (IndexPosition pos : moves) {
            set.add(pos.getRow() + "," + pos.getCol());
        }
        return set;
    }

    @Test
    @DisplayName("Bishop in the centre of the board can move anywhere")
    public void testBishopInTheCentreOfTheBoardCanMoveAnywhere() {
        Piece[][] board = new Piece[8][8];
        Bishop bishop = new Bishop(ColorForChessPieces.WHITE,  new IndexPosition(3,3));

        IndexPosition[] moves = bishop.getPossibleMoves(board);

        assertEquals(13,moves.length);

    }


    @Test
    @DisplayName("Bishop can capture the enemy pieces")
    public void testBishopCaptureEnemyPieces(){
        Piece[][] board = new Piece[8][8];
        Bishop bishop = new Bishop(ColorForChessPieces.WHITE,  new IndexPosition(3,3));
        Pawn pawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(4,4));

        board[3][3] = bishop;
        board[4][4] = pawn;

        IndexPosition[] moves = bishop.getPossibleMoves(board);
        Set<String> movesSet = toSet(moves);

        assertTrue(movesSet.contains("4,4"));

    }

    @Test
    @DisplayName("Bishop does not pierces through enemy pieces")
    public void testBishopDoesNotPiercesTroughEnemyPieces(){
        Piece[][] board = new Piece[8][8];
        Bishop bishop = new Bishop(ColorForChessPieces.WHITE,  new IndexPosition(3,3));
        Pawn pawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(4,4));

        board[3][3] = bishop;
        board[4][4] = pawn;

        IndexPosition[] moves = bishop.getPossibleMoves(board);

        assertEquals(10,moves.length);

    }

    @Test
    @DisplayName("Bishop cannot capture the allied pieces")
    public void testBishopCannotCaptureAlliedPieces(){
        Piece[][] board = new Piece[8][8];
        Bishop bishop = new Bishop(ColorForChessPieces.WHITE,  new IndexPosition(3,3));
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(4,4));

        board[3][3] = bishop;
        board[4][4] = pawn;

        IndexPosition[] moves = bishop.getPossibleMoves(board);
        Set<String> movesSet = toSet(moves);

        assertFalse(movesSet.contains("4,4"));

    }


    @Test
    @DisplayName("Bishop does not pierces through allied pieces")
    public void testBishopDoesNotPiercesTroughAlliedPieces(){
        Piece[][] board = new Piece[8][8];
        Bishop bishop = new Bishop(ColorForChessPieces.WHITE,  new IndexPosition(3,3));
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(4,4));

        board[3][3] = bishop;
        board[4][4] = pawn;

        IndexPosition[] moves = bishop.getPossibleMoves(board);

        assertEquals(9,moves.length);

    }


    @Test
    @DisplayName("Bishop in the corner of the board has 8 moves")
    public void testBishopInTheCornerOfTheBoardHas8Moves(){
        Piece[][] board = new Piece[8][8];
        Bishop bishop = new Bishop(ColorForChessPieces.WHITE,  new IndexPosition(0,0));

        IndexPosition[] moves = bishop.getPossibleMoves(board);

        assertEquals(7,moves.length);
    }




}
