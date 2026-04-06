package Services;

import ChessPieces.*;
import Constants.ColorForChessPieces;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;


import static Constants.ColorForChessPieces.*;

public class GameService {
    private CheckService checkService;
    private ArrayList<MoveRecord> moveStorage;

    public GameService() {
        checkService = new CheckService();
        moveStorage = new ArrayList<>();
    }



    public ArrayList<IndexPosition> getLegalMoves(Piece piece, Piece[][] board) {
        if(piece instanceof King) {
            return getKingLegalMoves(piece.getColor(), board);
        }

        if(piece instanceof Pawn) {
            // yet to be implemented when enpassant move is done
        }
        IndexPosition[] rawMoves = piece.getPossibleMoves(board);
        ArrayList<IndexPosition> moves = new ArrayList<>(Arrays.asList(rawMoves));
        return checkService.filterMovesForCheck(piece, moves, board);
    }

    // this method filters king moves removing checked squares
    public ArrayList<IndexPosition> getKingLegalMoves(ColorForChessPieces color, Piece[][] board) {
        King king = checkService.findKing(color, board);
        IndexPosition[] rawMoves = king.getPossibleMoves(board);
        ColorForChessPieces enemyColor = color == WHITE ? BLACK : WHITE;
        ArrayList<IndexPosition> legalMoves = new ArrayList<>();
        // simulate a move to a square and check if king is inder check
        for (IndexPosition move : rawMoves) {
            Piece[][] tempBoard = checkService.copyBoard(board);
            tempBoard[king.getPosition().getRow()][king.getPosition().getCol()] = null;
            tempBoard[move.getRow()][move.getCol()] = king;

            if (!checkService.isSquareAttacked(move, enemyColor, tempBoard)) {
                legalMoves.add(move);
            }
        }
        if(canCastleKingSide(color, board)) {
            legalMoves.add(new IndexPosition(king.getPosition().getRow(), king.getPosition().getCol() + 2));
        }

        if (canCastleQueenSide(color, board)) {
            legalMoves.add(new IndexPosition(king.getPosition().getRow(), king.getPosition().getCol() - 2));
        }
        return legalMoves;
    }


    // thsi method defines the rules of king side castling
    public boolean canCastleKingSide(ColorForChessPieces color, Piece[][] board) {
        King king = checkService.findKing(color, board);
        // set row based on color of the castle desired pieces
        ColorForChessPieces enemyColor = color == WHITE ? BLACK : WHITE;
        int row = color == WHITE ? 0 : 7;
        // if rook or king has moved castle is not allowed
        if (hasPiecedMoved(new IndexPosition(row, 4)) || hasPiecedMoved(new IndexPosition(row, 7))) {
            return false;
        }
        // castle is only possible is rook on the required field exists
        if (board[row][7] == null) {
            return false;
        }
        // check if squares between king and rook are empty
        for (int i = 5; i <= 6; i++) {
            if (board[row][i] != null) {
                return false;
            }
        }
        // king cannot castle while in check or through/into attacked squares
        for (int i = 4; i <= 6; i++) {
            if (checkService.isSquareAttacked(new IndexPosition(row, i), enemyColor, board)) {
                return false;
            }
        }

        return true;
    }

    // this method defines teh rules of queen side castling
    public boolean canCastleQueenSide(ColorForChessPieces color, Piece[][] board) {

        King king = checkService.findKing(color, board);
        // set row based on color of the castle desired pieces
        ColorForChessPieces enemyColor = color == WHITE ? BLACK : WHITE;
        int row = color == WHITE ? 0 : 7;
        // if rook or king has moved castle is not allowed
        if (hasPiecedMoved(new IndexPosition(row, 4)) || hasPiecedMoved(new IndexPosition(row, 0))) {
            return false;
        }
        if (board[row][0] == null) {
            return false;
        }
        // check if squares between king and rook are empty (columns 1, 2, 3)
        for (int i = 1; i <= 3; i++) {
            if (board[row][i] != null) {
                return false;
            }
        }
        // king cannot castle while in check or through/into attacked squares (columns 2, 3, 4)
        for (int i = 2; i <= 4; i++) {
            if (checkService.isSquareAttacked(new IndexPosition(row, i), enemyColor, board)) {
                return false;
            }
        }

        return true;
    }


    // this method checks if moved was performed from a particular square
    private boolean hasPiecedMoved(IndexPosition from) {
        if (moveStorage.isEmpty()) {
            return false;
        }
        for (MoveRecord moveRecord : moveStorage) {
            if (moveRecord.getMovedFrom().equals(from)) {
                return true;
            }
        }
        return false;
    }


    public boolean isCheckmate(ColorForChessPieces color, Piece[][] board) {
        return false;
    }
}


