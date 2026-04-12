package Services;

import ChessPieces.*;
import Constants.ColorForChessPieces;

import java.util.ArrayList;

public class DrawService {

    private final CheckService checkService;
    private final GameService gameService;

    // for 50-move rule: counts half-moves since last pawn move or capture
    private int halfMoveClock = 0;

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

    public boolean isInsufficientMaterial(Piece[][] board){
        ArrayList<Piece> whitePieces = new ArrayList<>();
        ArrayList<Piece> blackPieces = new ArrayList<>();

        for (int row = 0; row < board.length; row++){
            for (int col = 0; col < board[row].length; col++){
                Piece piece = board[row][col];
                if (piece == null) continue;
                if (piece.getColor() == ColorForChessPieces.WHITE){
                    whitePieces.add(piece);
                }else {
                    blackPieces.add(piece);
                }
            }
        }

        //King vs King
        if (whitePieces.size() == 1 && blackPieces.size() == 1){
            return true;
        }

        // King + Bishop vs King or King + Knight vs King
        if (whitePieces.size() == 1 && blackPieces.size() == 2){
            return hasOnlyKingAndMinor(blackPieces);
        }

        if (blackPieces.size() == 1 && whitePieces.size() == 2){
            return hasOnlyKingAndMinor(whitePieces);
        }

        // King + Bishop vs King + Bishop (same colored squares)
        if (whitePieces.size() == 2 && blackPieces.size() == 2){
            return bothSidesOnlyKingAndBishopSameColor(whitePieces, blackPieces, board);
        }

        return false;
    }

    public boolean hasOnlyKingAndMinor(ArrayList<Piece> pieces){
        int bishops = 0, knights = 0;
        
        for(Piece piece : pieces){
            if (piece instanceof Bishop) bishops++;
            else if (piece instanceof Knight) knights++;
            else if (!(piece instanceof King)) return false;
        }

        return (bishops == 1 && knights == 0) || (knights == 1 && bishops == 0);
    }

    public boolean bothSidesOnlyKingAndBishopSameColor(ArrayList<Piece> whitePieces, ArrayList<Piece> blackPieces, Piece[][] board){
        Bishop whiteBishop = null, blackBishop = null;

        for (Piece p : whitePieces){
            if (p instanceof Bishop) whiteBishop = (Bishop) p;
            else if (!(p instanceof King)) return false;
        }

        for (Piece p : blackPieces){
            if (p instanceof Bishop) blackBishop = (Bishop) p;
            else if(!(p instanceof King)) return false;
        }

        if (whiteBishop == null || blackBishop == null) return false;

        // bishop on same color squares
        int wSquareColor = (whiteBishop.getPosition().getRow() + whiteBishop.getPosition().getCol()) % 2;
        int bSquareColor = (blackBishop.getPosition().getRow() + blackBishop.getPosition().getCol()) % 2;
        return wSquareColor == bSquareColor;
    }

    //50 move rule
    //50 full moves (100 half-moves) with no pawn move or capture
    public void updateHalfMoveClock(Piece movedPiece, Piece capturedPiece){
        if (movedPiece instanceof Pawn || capturedPiece != null) {
            halfMoveClock = 0; // reset on pawn move or capture
        } else {
            halfMoveClock++;
        }
    }

    public boolean isFiftyMoveRule(){
        return halfMoveClock >= 100;
    }

    //Threefold Repetition
    public void recordBoardState(){

    }

    public boolean isThreefoldRepetition(){
        return false;
    }

    public String serializeBoard(){
        return "";
    }
}
