package Services;

import ChessPieces.*;
import Constants.ColorForChessPieces;

public class DrawService {

    private final CheckService checkService;
    private final GameService gameService;

    public DrawService(CheckService checkService, GameService gameService) {
        this.checkService = checkService;
        this.gameService = gameService;
    }

    public boolean isStalemate(ColorForChessPieces colorToMove, Piece[][] board) {
        return false;
    }
}
