package Services;

import ChessPieces.*;
import Constants.ColorForChessPieces;

import java.util.ArrayList;
import java.util.Arrays;

public class CheckService {
    public CheckService() {
    }
    // toggle state on the check form a king
    public boolean isInCheck(ColorForChessPieces color, Piece[][] board) {
        King king = findKing(color, board);
        return king.isInCheck(board, this);
    }
    // this method checks if square is attacked by any of the enemy pieces
    public boolean isSquareAttacked(IndexPosition square, ColorForChessPieces color, Piece[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColor().equals(color)) {
                    IndexPosition[] pos = piece.getPossibleMoves(board);
                    ArrayList<IndexPosition> temp = new ArrayList<IndexPosition>(Arrays.asList(pos));
                    if (temp.contains(square)) {
                        return true;
                    }

                }
            }
        }
        return false;
    }

    // this method finds the king of given color
    public King findKing(ColorForChessPieces color, Piece[][] board) {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Piece piece = board[row][col];
                if (piece instanceof King && piece.getColor().equals(color)) {
                    return (King) piece;
                }
            }

        }
        return null;
    }
    // this method retrieves all pieces which attack king (check the king)
    public ArrayList<Piece> findAttackers(ColorForChessPieces kingColor, Piece[][] board) {
        King king = findKing(kingColor, board);
        ArrayList<Piece> attackers = new ArrayList<>();
        ColorForChessPieces enemyColor = kingColor == ColorForChessPieces.WHITE ? ColorForChessPieces.BLACK : ColorForChessPieces.WHITE;
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColor().equals(enemyColor)) {
                    IndexPosition[] pos = piece.getPossibleMoves(board);
                    ArrayList<IndexPosition> temp = new ArrayList<IndexPosition>(Arrays.asList(pos));
                    if (temp.contains(king.getPosition())) {
                        attackers.add(piece);
                    }
                }
            }
        }
        return attackers;
    }

    // this method checks if the checking piece can eb captured
    public boolean canCaptureAttacker(ColorForChessPieces kingColor, Piece[][] board) {
        ArrayList<Piece> attackers = findAttackers(kingColor, board);
        if (attackers.isEmpty()) {
            return false;
        }
        // if more then one piece attacks king capture cannot avoids checks -> king has to move
        if (attackers.size() > 1) {
            return false;
        }

        Piece attacker = attackers.get(0);
        // check if any allied  piece can attack attacking piece
        if (!isSquareAttacked(attacker.getPosition(), kingColor, board)) {
            return false;
        }

        return true;
    }
    // this method retrieves all squares (IndexPositions) between two particular squares(it is used to retrieve squares between attacker and a king)
    public ArrayList<IndexPosition> getSquaresBetween(IndexPosition from, IndexPosition to) {
        ArrayList<IndexPosition> squares = new ArrayList<>();
        // this two lines define the direction of the movement
        int rowDir = Integer.signum(to.getRow() - from.getRow());
        int colDir = Integer.signum(to.getCol() - from.getCol());

        int currentRow = from.getRow() + rowDir;
        int currentCol = from.getCol() + colDir;

        while (currentRow != to.getRow() || currentCol != to.getCol()) {
            squares.add(new IndexPosition(currentRow, currentCol));
            currentRow += rowDir;
            currentCol += colDir;
        }

        return squares;
    }

    // this method checks if any of the allied pieces cna block the check
    public boolean canBlockCheck(ColorForChessPieces kingColor, Piece[][] board) {
        ArrayList<Piece> attackers = findAttackers(kingColor, board);
        if (attackers.isEmpty()) {
            return false;
        }
        // more than two attackers cannot be blocked by one piece
        if (attackers.size() > 1) {
            return false;
        }
        Piece attacker = attackers.get(0);
        // check by pawn and knight cannot be blocked
        if (attacker instanceof Knight || attacker instanceof Pawn) {
            return false;
        }
        // sudo check for each piece to block the check line
        King king = findKing(kingColor, board);
        ArrayList<IndexPosition> positions = getSquaresBetween(king.getPosition(), attacker.getPosition());
        for (IndexPosition position : positions) {
            for (int row = 0; row < board.length; row++) {
                for (int col = 0; col < board[row].length; col++) {
                    Piece piece = board[row][col];
                    if (piece != null && piece.getColor().equals(kingColor) && !(piece instanceof King)) {
                        IndexPosition[] moves = piece.getPossibleMoves(board);
                        ArrayList<IndexPosition> moveList = new ArrayList<>(Arrays.asList(moves));
                        if (moveList.contains(position)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
