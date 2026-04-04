package Services;

import ChessPieces.*;
import Constants.ColorForChessPieces;

import java.awt.*;
import java.util.ArrayList;


import static Constants.ColorForChessPieces.BLACK;
import static Constants.ColorForChessPieces.WHITE;

public class GameService {
    private CheckService checkService;
    public GameService() {
        checkService = new CheckService();
    }
    // this method filters king moves removing checked squares
    public ArrayList<IndexPosition> getKingLegalMoves(ColorForChessPieces color, Piece[][] board) {
        King king = checkService.findKing(color, board);
        IndexPosition[] rawMoves = king.getPossibleMoves(board);
        ColorForChessPieces enemyColor = color == WHITE ? BLACK : WHITE;
        ArrayList<IndexPosition> legalMoves = new ArrayList<>();
        // simulate a move to a square and check if king is inder check
        for (IndexPosition move : rawMoves) {
            Piece[][] tempBoard = copyBoard(board);
            tempBoard[king.getPosition().getRow()][king.getPosition().getCol()] = null;
            tempBoard[move.getRow()][move.getCol()] = king;

            if (!checkService.isSquareAttacked(move, enemyColor, tempBoard)) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }
    // this method creates a copy of current boar state for move simulation to avoid straight manipulation on the main board
    private Piece[][] copyBoard(Piece[][] board) {
        Piece[][] copy = new Piece[board.length][board[0].length];
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                copy[row][col] = board[row][col];
            }
        }
        return copy;
    }


    public boolean isCheckmate(ColorForChessPieces color,Piece[][] board) {
        return false;
    }




}
