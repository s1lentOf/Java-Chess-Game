package Services;

import ChessPieces.*;
import Constants.ColorForChessPieces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DrawService {

    private final CheckService checkService;
    private final GameService gameService;

    private int halfMoveClock = 0;
    private final Map<String, Integer> boardStateHistory = new HashMap<>();

    public DrawService(CheckService checkService, GameService gameService) {
        this.checkService = checkService;
        this.gameService = gameService;
    }

    public boolean isStalemate() {
        ColorForChessPieces color = gameService.getCurrentColorToMove();
        Piece[][] board = gameService.getPiecesOnTheBoard();

        if (checkService.isInCheck(color, board)) {
            return false;
        }
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[row].length; col++) {
                Piece piece = board[row][col];
                if (piece != null && piece.getColor().equals(color)) {
                    if (!gameService.getLegalMoves(piece, board).isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isInsufficientMaterial() {
        Piece[][] board = gameService.getPiecesOnTheBoard();
        ArrayList<Piece> whitePieces = new ArrayList<>();
        ArrayList<Piece> blackPieces = new ArrayList<>();

        for (Piece[] row : board) {
            for (Piece piece : row) {
                if (piece == null) continue;
                if (piece.getColor() == ColorForChessPieces.WHITE) whitePieces.add(piece);
                else blackPieces.add(piece);
            }
        }

        if (whitePieces.size() == 1 && blackPieces.size() == 1) return true;

        if (whitePieces.size() == 1 && blackPieces.size() == 2) return hasOnlyKingAndMinor(blackPieces);
        if (blackPieces.size() == 1 && whitePieces.size() == 2) return hasOnlyKingAndMinor(whitePieces);

        if (whitePieces.size() == 2 && blackPieces.size() == 2) return bothSidesOnlyKingAndBishopSameColor(whitePieces, blackPieces);

        return false;
    }

    public boolean isFiftyMoveRule() {
        return halfMoveClock >= 100;
    }

    public boolean isThreefoldRepetition() {
        return boardStateHistory.values().stream().anyMatch(count -> count >= 3);
    }

    public void updateHalfMoveClock(Piece movedPiece, Piece capturedPiece) {
        if (movedPiece instanceof Pawn || capturedPiece != null) {
            halfMoveClock = 0;
        } else {
            halfMoveClock++;
        }
    }

    public void recordBoardState() {
        String state = serializeBoard(gameService.getPiecesOnTheBoard(), gameService.getCurrentColorToMove());
        boardStateHistory.merge(state, 1, Integer::sum);
    }

    // Private helpers

    private boolean hasOnlyKingAndMinor(ArrayList<Piece> pieces) {
        int bishops = 0, knights = 0;
        for (Piece piece : pieces) {
            if (piece instanceof Bishop) bishops++;
            else if (piece instanceof Knight) knights++;
            else if (!(piece instanceof King)) return false;
        }
        return (bishops == 1 && knights == 0) || (knights == 1 && bishops == 0);
    }

    private boolean bothSidesOnlyKingAndBishopSameColor(ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces) {
        Bishop whiteBishop = null, blackBishop = null;

        for (Piece p : whitePieces) {
            if (p instanceof Bishop) whiteBishop = (Bishop) p;
            else if (!(p instanceof King)) return false;
        }
        for (Piece p : blackPieces) {
            if (p instanceof Bishop) blackBishop = (Bishop) p;
            else if (!(p instanceof King)) return false;
        }

        if (whiteBishop == null || blackBishop == null) return false;

        int wSquareColor = (whiteBishop.getPosition().getRow() + whiteBishop.getPosition().getCol()) % 2;
        int bSquareColor = (blackBishop.getPosition().getRow() + blackBishop.getPosition().getCol()) % 2;
        return wSquareColor == bSquareColor;
    }

    private String serializeBoard(Piece[][] board, ColorForChessPieces colorToMove) {
        StringBuilder sb = new StringBuilder();
        for (Piece[] row : board) {
            for (Piece piece : row) {
                if (piece == null) {
                    sb.append("--");
                } else {
                    sb.append(piece.getColor() == ColorForChessPieces.WHITE ? "W" : "B");
                    sb.append(piece.getClass().getSimpleName().charAt(0));
                }
                sb.append(",");
            }
        }
        sb.append(colorToMove);
        return sb.toString();
    }
}