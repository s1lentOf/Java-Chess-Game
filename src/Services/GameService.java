package Services;

import ChessBoard.Board;
import ChessPieces.*;
import Constants.ColorForChessPieces;

import java.util.ArrayList;
import java.util.Arrays;


import static Constants.ColorForChessPieces.*;

public class GameService {
    private CheckService checkService;
    private ArrayList<MoveRecord> moveStorage;
    private int moveCounter = 1;
    // a field to keep track of which side is moving next
    private ColorForChessPieces currentColorToMove;
    private Board board;

    public GameService(Board board) {
        this.board = board;
        checkService = new CheckService();
        moveStorage = new ArrayList<>();
    }
    /*  a method which checks if the move is possible for the piece
       by taking the array of all possible moves and checking if the move that the user wants to do is in that array
    */
    public boolean isMovePossible(ArrayList<IndexPosition> possibleMoves, IndexPosition nextMove) {
        for (IndexPosition move : possibleMoves) {
            if (move.getRow() == nextMove.getRow() && move.getCol() == nextMove.getCol()) {
                return true;
            }
        }
        return false;
    }

    public void handleSquareClick(IndexPosition nextMove) {
        Piece targetPiece = board.getPiecesOnTheBoard()[nextMove.getRow()][nextMove.getCol()];

        if(moveCounter%2 != 0){
            currentColorToMove = ColorForChessPieces.WHITE;
        }
        else{
            currentColorToMove = ColorForChessPieces.BLACK;
        }

        if (board.getSelected() == null) {
            if (targetPiece != null && targetPiece.getColor() == currentColorToMove) {
                board.setSelected(targetPiece);
                System.out.println("Selected new piece");
            }
            else{
                System.out.println("It is not your turn");
            }
            return;
        }

        if (targetPiece != null) {
            if (!board.getSelected().isEnemy(targetPiece)) {
                System.out.println("Reselected piece");
                board.setSelected(targetPiece);
            } else if( isMovePossible(getLegalMoves(board.getSelected(), board.getPiecesOnTheBoard()),nextMove)) {
                System.out.println("Captured piece");
                capturePiece(nextMove);
                moveCounter++;
            }
        } else if (isMovePossible(getLegalMoves(board.getSelected(), board.getPiecesOnTheBoard()),nextMove)) {
            System.out.println("Just moved piece");
            moveSelectedPiece(nextMove);
            moveCounter++;
        }

        System.out.println(Arrays.deepToString(board.getPiecesOnTheBoard()));
    }

    // helper method for moving a piece
    public void moveSelectedPiece(IndexPosition nextMove) {
        Piece selected = board.getSelected();
        IndexPosition oldPos = selected.getPosition();

        // detect castling
        if (selected instanceof King
                && Math.abs(nextMove.getCol() - oldPos.getCol()) == 2) {
            executeCastle(nextMove);
            return;
        }

        board.getPiecesOnTheBoard()[oldPos.getRow()][oldPos.getCol()] = null;

        selected.setPosition(new IndexPosition(nextMove.getRow(), nextMove.getCol()));

        board.getPiecesOnTheBoard()[nextMove.getRow()][nextMove.getCol()] = selected;

        moveStorage.add(new MoveRecord(selected.getClass().getSimpleName(), oldPos, nextMove));

        board.setSelected(null);
    }

    //helper method for capturing the piece
    public void capturePiece(IndexPosition nextMove) {
        moveSelectedPiece(nextMove);
    }

    // a method which will select a piece for the detectMouseClickPosition() method
    public void selectPiece(int row, int col) {
        Piece piece = board.getPiecesOnTheBoard()[row][col];
        if (piece != null) {
            board.setSelected(piece);
        }
    }



  
  
  
    public ArrayList<IndexPosition> getLegalMoves(Piece piece, Piece[][] board) {
        if(piece instanceof King) {
            return getKingLegalMoves(piece.getColor(), board);
        }

        if(piece instanceof Pawn) {
            // yet to be implemented when enpassant move is done
        }
        IndexPosition[] rawMoves = piece.getPossibleMoves(board);
        ArrayList<IndexPosition> moves = new ArrayList<>(Arrays.asList(rawMoves));
        return checkService.filterMovesForCheck(piece, moves, board);
    }

    // this method filters king moves removing checked squares
    public ArrayList<IndexPosition> getKingLegalMoves(ColorForChessPieces color, Piece[][] board) {
        King king = checkService.findKing(color, board);
        IndexPosition[] rawMoves = king.getPossibleMoves(board);
        ColorForChessPieces enemyColor = color == WHITE ? BLACK : WHITE;
        ArrayList<IndexPosition> legalMoves = new ArrayList<>();
        // simulate a move to a square and check if king is inder check
        for (IndexPosition move : rawMoves) {
            Piece[][] tempBoard = checkService.copyBoard(board);
            tempBoard[king.getPosition().getRow()][king.getPosition().getCol()] = null;
            tempBoard[move.getRow()][move.getCol()] = king;

            if (!checkService.isSquareAttacked(move, enemyColor, tempBoard)) {
                legalMoves.add(move);
            }
        }
        if(canCastleKingSide(color, board)) {
            legalMoves.add(new IndexPosition(king.getPosition().getRow(), king.getPosition().getCol() + 2));
        }

        if (canCastleQueenSide(color, board)) {
            legalMoves.add(new IndexPosition(king.getPosition().getRow(), king.getPosition().getCol() - 2));
        }
        return legalMoves;
    }


    // this method defines the rules of king side castling
    public boolean canCastleKingSide(ColorForChessPieces color, Piece[][] board) {
        King king = checkService.findKing(color, board);
        // set row based on color of the castle desired pieces
        ColorForChessPieces enemyColor = color == WHITE ? BLACK : WHITE;
        int row = color == WHITE ? 0 : 7;
        // if rook or king has moved castle is not allowed
        if (hasPiecedMoved(new IndexPosition(row, 4)) || hasPiecedMoved(new IndexPosition(row, 7))) {
            return false;
        }
        // castle is only possible is rook on the required field exists
        if (board[row][7] == null) {
            return false;
        }
        // check if squares between king and rook are empty
        for (int i = 5; i <= 6; i++) {
            if (board[row][i] != null) {
                return false;
            }
        }
        // king cannot castle while in check or through/into attacked squares
        for (int i = 4; i <= 6; i++) {
            if (checkService.isSquareAttacked(new IndexPosition(row, i), enemyColor, board)) {
                return false;
            }
        }

        return true;
    }

    // this method defines teh rules of queen side castling
    public boolean canCastleQueenSide(ColorForChessPieces color, Piece[][] board) {

        King king = checkService.findKing(color, board);
        // set row based on color of the castle desired pieces
        ColorForChessPieces enemyColor = color == WHITE ? BLACK : WHITE;
        int row = color == WHITE ? 0 : 7;
        // if rook or king has moved castle is not allowed
        if (hasPiecedMoved(new IndexPosition(row, 4)) || hasPiecedMoved(new IndexPosition(row, 0))) {
            return false;
        }
        if (board[row][0] == null) {
            return false;
        }
        // check if squares between king and rook are empty (columns 1, 2, 3)
        for (int i = 1; i <= 3; i++) {
            if (board[row][i] != null) {
                return false;
            }
        }
        // king cannot castle while in check or through/into attacked squares (columns 2, 3, 4)
        for (int i = 2; i <= 4; i++) {
            if (checkService.isSquareAttacked(new IndexPosition(row, i), enemyColor, board)) {
                return false;
            }
        }

        return true;
    }


    // this method checks if moved was performed from a particular square
    private boolean hasPiecedMoved(IndexPosition from) {
        if (moveStorage.isEmpty()) {
            return false;
        }
        for (MoveRecord moveRecord : moveStorage) {
            if (moveRecord.getMovedFrom().equals(from)) {
                return true;
            }
        }
        return false;
    }

    public void executeCastle(IndexPosition kingTarget) {
        Piece[][] pieces = board.getPiecesOnTheBoard();
        Piece king = board.getSelected();
        IndexPosition kingFrom = king.getPosition();
        int row = kingFrom.getRow();

        boolean isKingSide = kingTarget.getCol() > kingFrom.getCol();
        int rookFromCol = isKingSide ? 7 : 0;
        int rookToCol   = isKingSide ? 5 : 3;

        Piece rook = pieces[row][rookFromCol];

        // move king
        pieces[kingFrom.getRow()][kingFrom.getCol()] = null;
        king.setPosition(kingTarget);
        pieces[kingTarget.getRow()][kingTarget.getCol()] = king;

        // move rook
        pieces[row][rookFromCol] = null;
        rook.setPosition(new IndexPosition(row, rookToCol));
        pieces[row][rookToCol] = rook;

        // record moves so hasPiecedMoved blocks future castling
        moveStorage.add(new MoveRecord("King", kingFrom, kingTarget));
        moveStorage.add(new MoveRecord("Rook", new IndexPosition(row, rookFromCol), new IndexPosition(row, rookToCol)));

        board.setSelected(null);
    }


    public boolean isCheckmate(ColorForChessPieces color, Piece[][] board) {
        return false;
    }
}


