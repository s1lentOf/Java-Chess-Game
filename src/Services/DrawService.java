package Services;

import ChessPieces.*;
import Constants.ColorForChessPieces;

import java.util.ArrayList;

public class DrawService {

    private final CheckService checkService;
    private final GameService gameService;

    public DrawService(CheckService checkService, GameService gameService) {
        this.checkService = checkService;
        this.gameService = gameService;
    }

    public boolean isStalemate(ColorForChessPieces colorToMove, Piece[][] board) {
        if (checkService.isInCheck(colorToMove, board)){
            return false; // In check, not stalemate
        }

        for (int row = 0; row < board.length; row++){
            for (int col = 0; col < board[row].length; col++){
                Piece piece = board[row][col];
                if (piece != null && piece.getColor().equals(colorToMove)){
                    ArrayList<IndexPosition> moves = gameService.getLegalMoves(piece, board);
                    if (!moves.isEmpty()){
                        return false; //at least one legal move was found
                    }
                }
            }
        }

        return true; //stalemate
    }
}
