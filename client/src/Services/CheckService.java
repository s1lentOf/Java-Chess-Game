package Services;

import ChessBoard.BoardUI;
import ChessPieces.*;
import Constants.ColorForChessPieces;

import java.util.ArrayList;
import java.util.Arrays;

public class CheckService {
    private final Piece[][] piecesOnTheBoard;
    public CheckService(Piece[][] piecesOnTheBoard) {
        this.piecesOnTheBoard = piecesOnTheBoard;
    }
    // toggle state on the check form a king
    public boolean isInCheck(Piece[][] board,ColorForChessPieces color) {
        King king = findKing(board, color);
        return king.isInCheck(board, this);
    }
    // this method checks if square is attacked by any of the enemy pieces
    public boolean isSquareAttacked(IndexPosition square,ColorForChessPieces color,Piece[][] board) {
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
    public King findKing(Piece[][] board, ColorForChessPieces color) {
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
    public ArrayList<Piece> findAttackers(Piece[][] board, ColorForChessPieces color) {
        King king = findKing(board, color);
        ArrayList<Piece> attackers = new ArrayList<>();
        ColorForChessPieces enemyColor = color == ColorForChessPieces.WHITE ? ColorForChessPieces.BLACK : ColorForChessPieces.WHITE;
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
    public boolean canCaptureAttacker(ColorForChessPieces color) {
        ArrayList<Piece> attackers = findAttackers(piecesOnTheBoard, color);
        if (attackers.isEmpty()) {
            return false;
        }
        // if more then one piece attacks king capture cannot avoids checks -> king has to move
        if (attackers.size() > 1) {
            return false;
        }

        Piece attacker = attackers.get(0);
        IndexPosition attackerPos = attacker.getPosition();

        // check if any non-king allied piece can capture the attacker
        for (int row = 0; row < piecesOnTheBoard.length; row++) {
            for (int col = 0; col < piecesOnTheBoard[row].length; col++) {
                Piece piece = piecesOnTheBoard[row][col];
                if (piece != null && piece.getColor().equals(color) && !(piece instanceof King)) {
                    IndexPosition[] moves = piece.getPossibleMoves(piecesOnTheBoard);
                    ArrayList<IndexPosition> moveList = new ArrayList<>(Arrays.asList(moves));
                    ArrayList<IndexPosition> filtered = filterMovesForCheck(piece,moveList,color);
                    if (filtered.contains(attackerPos)) {
                        return true;
                    }
                }
            }
        }

        // if only the king can reach the attacker, make sure the attacker is not defended — otherwise the king would still be in check after the capture
        King king = findKing(piecesOnTheBoard, color);
        IndexPosition[] kingMoves = king.getPossibleMoves(piecesOnTheBoard);
        ArrayList<IndexPosition> kingMoveList = new ArrayList<>(Arrays.asList(kingMoves));
        if (kingMoveList.contains(attackerPos)) {
            ColorForChessPieces enemyColor = color == ColorForChessPieces.WHITE
                    ? ColorForChessPieces.BLACK : ColorForChessPieces.WHITE;
            // simulate the king capturing the attacker
            Piece[][] tempBoard = copyBoard();
            tempBoard[king.getPosition().getRow()][king.getPosition().getCol()] = null;
            tempBoard[attackerPos.getRow()][attackerPos.getCol()] = king;
            // if no enemy piece still attacks that square, the king can legally capture
            return !isSquareAttacked(attackerPos, enemyColor, tempBoard);
        }

        return false;
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

    // filters out moves that would leave the king in check (handles pins and must-escape-check)
    public ArrayList<IndexPosition> filterMovesForCheck(Piece piece, ArrayList<IndexPosition> moves,ColorForChessPieces color) {
        ArrayList<IndexPosition> legalMoves = new ArrayList<>();

        for (IndexPosition move : moves) {
            Piece[][] tempBoard = copyBoard();
            tempBoard[piece.getPosition().getRow()][piece.getPosition().getCol()] = null;
            tempBoard[move.getRow()][move.getCol()] = piece;

            // en passant: remove the captured pawn which is not on the target square
            if (piece instanceof Pawn
                    && Math.abs(move.getCol() - piece.getPosition().getCol()) == 1
                    && piecesOnTheBoard[move.getRow()][move.getCol()] == null) {
                int capturedRow = piece.getPosition().getRow();
                tempBoard[capturedRow][move.getCol()] = null;
            }

            if (!isInCheck(tempBoard,color)) {
                legalMoves.add(move);
            }
        }
        return legalMoves;
    }

    public Piece[][] copyBoard() {
        Piece[][] copy = new Piece[piecesOnTheBoard.length][piecesOnTheBoard[0].length];
        for (int row = 0; row < piecesOnTheBoard.length; row++) {
            System.arraycopy(piecesOnTheBoard[row], 0, copy[row], 0, piecesOnTheBoard[row].length);
        }
        return copy;
    }

    // this method checks if any of the allied pieces cna block the check
    public boolean canBlockCheck(ColorForChessPieces color) {
        ArrayList<Piece> attackers = findAttackers(piecesOnTheBoard,color);
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
        King king = findKing(piecesOnTheBoard,color);
        ArrayList<IndexPosition> positions = getSquaresBetween(king.getPosition(), attacker.getPosition());
        for (IndexPosition position : positions) {
            for (int row = 0; row < piecesOnTheBoard.length; row++) {
                for (int col = 0; col < piecesOnTheBoard[row].length; col++) {
                    Piece piece = piecesOnTheBoard[row][col];
                    if (piece != null && piece.getColor().equals(color) && !(piece instanceof King)) {
                        IndexPosition[] moves = piece.getPossibleMoves(piecesOnTheBoard);
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
