package Services;

import ChessPieces.*;
import Constants.ColorForChessPieces;
import org.junit.jupiter.engine.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GameService {

    public GameService() {

    }

    public boolean isInCheck(ColorForChessPieces color, Piece[][] board) {
        King king = findKing(color, board);
        return king.isInCheck(board,this);
    }

    public boolean isSquareAttacked(IndexPosition square, ColorForChessPieces color, Piece[][] board) {
        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < board[row].length; col++){
                Piece piece = board[row][col];
                if(piece!=null && piece.getColor().equals(color)){
                    IndexPosition[] pos = piece.getPossibleMoves(board);
                    ArrayList<IndexPosition> temp = new ArrayList<IndexPosition>(Arrays.asList(pos));
                    if(temp.contains(square)){
                        return true;
                    }

                }
            }
        }
        return false;
    }


    public King findKing( ColorForChessPieces color, Piece[][] board) {
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

    public ArrayList<Piece> findAttackers(ColorForChessPieces kingColor, Piece[][] board) {
        King king = findKing(kingColor, board);
        ArrayList<Piece> attackers = new ArrayList<>();
        ColorForChessPieces enemyColor = kingColor == ColorForChessPieces.WHITE ? ColorForChessPieces.BLACK : ColorForChessPieces.WHITE;
        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < board[row].length; col++) {
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


    public boolean canCaptureAttacker(ColorForChessPieces kingColor, Piece[][] board) {
        ArrayList<Piece> attackers = findAttackers(kingColor, board);
        if(attackers.isEmpty()){
            return false;
        }
        if(attackers.size() > 1 ){
            return false;
        }

        Piece attacker = attackers.get(0);
        if(!isSquareAttacked(attacker.getPosition(), kingColor, board)){
            return false;
        }

        return true;
    }

    public ArrayList<IndexPosition> getSquaresBetween(IndexPosition from, IndexPosition to){
        ArrayList<IndexPosition> squares = new ArrayList<>();

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




}
