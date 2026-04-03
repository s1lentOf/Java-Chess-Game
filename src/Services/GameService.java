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
        return false;
    }

    public boolean isSquareAttacked(IndexPosition square, ColorForChessPieces color, Piece[][] board){

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


}
