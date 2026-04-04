package Services;

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


    public boolean isCheckmate(ColorForChessPieces color,Piece[][] board) {
        return  false;
    }




}
