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
    public GameService() {
        checkService = new CheckService();
    }

    public IndexPosition[] getLegalMoves(Piece piece, Piece[][] board) {
        return new  IndexPosition[0];
    }


    public boolean isCheckmate(ColorForChessPieces color,Piece[][] board) {
        return false;
    }




}
