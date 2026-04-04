package Services;

import ChessBoard.Board;
import ChessPieces.*;
import Constants.ColorForChessPieces;
import org.junit.jupiter.engine.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GameService {
    private CheckService checkService;
    private int moveCounter = 1;
    // a field to keep track of which side is moving next
    private ColorForChessPieces currentColorToMove;
    private Board board;

    public GameService(Board board) {
        this.board = board;
    }

    public Piece getSelected() {
        return board.getSelected();
    }


    /*  a method which checks if the move is possible for the piece
       by taking the array of all possible moves and checking if the move that the user wants to do is in that array
    */
    public boolean isMovePossible(IndexPosition[] possibleMoves, IndexPosition nextMove) {
        for (IndexPosition move : possibleMoves) {
            if (move.getRow() == nextMove.getRow() && move.getCol() == nextMove.getCol()) {
                return true;
            }
        }
        return false;
    }

    public void handleSquareClick(IndexPosition nextMove) {
        Piece targetPiece = board.getPiecesOnTheBoard()[nextMove.getRow()][nextMove.getCol()];

        if(moveCounter%2 != 0){
            currentColorToMove = ColorForChessPieces.WHITE;
        }
        else{
            currentColorToMove = ColorForChessPieces.BLACK;
        }

        if (board.getSelected() == null) {
            if (targetPiece != null && targetPiece.getColor() == currentColorToMove) {
                board.setSelected(targetPiece);
                System.out.println("Selected new piece");
            }
            else{
                System.out.println("It is not your turn");
            }
            return;
        }

        if (targetPiece != null) {
            if (!board.getSelected().isEnemy(targetPiece)) {
                System.out.println("Reselected piece");
                board.setSelected(targetPiece);
            } else if (isMovePossible(board.getSelected().getPossibleMoves(board.getPiecesOnTheBoard()), nextMove)) {
                System.out.println("Captured piece");
                capturePiece(nextMove);
                moveCounter++;
            }
        } else if (isMovePossible(board.getSelected().getPossibleMoves(board.getPiecesOnTheBoard()), nextMove)) {
            System.out.println("Just moved piece");
            moveSelectedPiece(nextMove);
            moveCounter++;
        }

        System.out.println(Arrays.deepToString(board.getPiecesOnTheBoard()));
    }

    // helper method for moving a piece
    public void moveSelectedPiece(IndexPosition nextMove) {
        Piece selected = board.getSelected();
        IndexPosition oldPos = selected.getPosition();

        board.getPiecesOnTheBoard()[oldPos.getRow()][oldPos.getCol()] = null;

        board.getSelected().setPosition(new IndexPosition(nextMove.getRow(), nextMove.getCol()));

        board.getPiecesOnTheBoard()[nextMove.getRow()][nextMove.getCol()] = board.getSelected();

        board.setSelected(null);
    }

    //helper method for capturing the piece
    public void capturePiece(IndexPosition nextMove) {
        moveSelectedPiece(nextMove);
    }

    // a method which will select a piece for the detectMouseClickPosition() method
    public void selectPiece(int row, int col) {
        Piece piece = board.getPiecesOnTheBoard()[row][col];
        if (piece != null) {
            board.setSelected(piece);
        }
    }







}
