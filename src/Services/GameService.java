package Services;

import ChessBoard.BoardUI;
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
    private ColorForChessPieces currentColorToMove = WHITE;
    private Pawn lastDoubleStepPawn;
    private BoardUI boardUI;
    // Stores elements that implement the Piece abstract class.
    private Piece[][] piecesOnTheBoard = new Piece[8][8]; // Store

    public GameService(BoardUI boardUI) {
        this.boardUI = boardUI;
        checkService = new CheckService();
        moveStorage = new ArrayList<>();
    }
    // setter for the move counter
    public void setMoveCounter(int count) {
        this.moveCounter = count;
        if (count % 2 != 0) {
            currentColorToMove = ColorForChessPieces.WHITE;
        } else {
            currentColorToMove = ColorForChessPieces.BLACK;
        }
    }

    public Pawn getLastDoubleStepPawn() {
        return lastDoubleStepPawn;
    }
    public void setLastDoubleStepPawn(Pawn pawn) {
        this.lastDoubleStepPawn = pawn;
    }

    public ColorForChessPieces getCurrentColorToMove() {
        return this.currentColorToMove;
    }

    public Piece getPieceAt(int row, int col) {
        return piecesOnTheBoard[row][col];
    }

    public Piece[][] getPiecesOnTheBoard() { return this.piecesOnTheBoard; }

    public void setPieceAt(int row, int col, Piece piece) {
        piecesOnTheBoard[row][col] = piece;
    }

    public void setUpPiecesOnTheBoard() {

        // Pawns
        for (int i = 0; i < 8; i++) {
            Pawn whitePawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, i));
            Pawn blackPawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(6, i));

            piecesOnTheBoard[1][i] = whitePawn;
            piecesOnTheBoard[6][i] = blackPawn;
        }

        // Rooks
        piecesOnTheBoard[7][0] = new Rook(ColorForChessPieces.BLACK, new IndexPosition(7, 0));
        piecesOnTheBoard[7][7] = new Rook(ColorForChessPieces.BLACK, new IndexPosition(7, 7));

        piecesOnTheBoard[0][0] = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        piecesOnTheBoard[0][7] = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 7));

        // Knights
        piecesOnTheBoard[7][1] = new Knight(ColorForChessPieces.BLACK, new IndexPosition(7, 1));
        piecesOnTheBoard[7][6] = new Knight(ColorForChessPieces.BLACK, new IndexPosition(7, 6));

        piecesOnTheBoard[0][1] = new Knight(ColorForChessPieces.WHITE, new IndexPosition(0, 1));
        piecesOnTheBoard[0][6] = new Knight(ColorForChessPieces.WHITE, new IndexPosition(0, 6));

        // Bishops
        piecesOnTheBoard[7][2] = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(7, 2));
        piecesOnTheBoard[7][5] = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(7, 5));

        piecesOnTheBoard[0][2] = new Bishop(ColorForChessPieces.WHITE, new IndexPosition(0, 2));
        piecesOnTheBoard[0][5] = new Bishop(ColorForChessPieces.WHITE, new IndexPosition(0, 5));

        // Queens
        piecesOnTheBoard[7][3] = new Queen(ColorForChessPieces.BLACK, new IndexPosition(7, 3));
        piecesOnTheBoard[0][3] = new Queen(ColorForChessPieces.WHITE, new IndexPosition(0, 3));

        // Kings
        piecesOnTheBoard[7][4] = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 4));
        piecesOnTheBoard[0][4] = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
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

    /* what this method does:
    //  1) check if there is a piece on the square
            1.2) if yes check if any piece was selected.
            1.3) if the piece is the same color, then reassign piece.
            1.4) if not, check if the move is possible( if yes capture)
        2) if there is no piece - check if the move is possible(if yes move)
    */


    public void handleSquareClick(IndexPosition nextMove) {
        Piece targetPiece = getPiecesOnTheBoard()[nextMove.getRow()][nextMove.getCol()];

        // an addition to set the selected to null anytime the wrong colored piece is clicked
        if (targetPiece != null && targetPiece.getColor() != currentColorToMove && boardUI.getSelected() == null) {
            return;
        }

        if (boardUI.getSelected() == null) {
            if (targetPiece != null && targetPiece.getColor() == currentColorToMove) {
                boardUI.setSelected(targetPiece);
                System.out.println("Selected new piece");
            }
            else{
                System.out.println("It is not your turn");
            }
            return;
        }

        if (targetPiece != null) {
            if (targetPiece.getColor() == currentColorToMove) {
                System.out.println("Reselected piece");
                boardUI.setSelected(targetPiece);
            } else if (isMovePossible(getLegalMoves(boardUI.getSelected(), getPiecesOnTheBoard()), nextMove)) {
                System.out.println("Captured piece");
                capturePiece(nextMove);
                moveCounter++;
            }
        } else if (isMovePossible(getLegalMoves(boardUI.getSelected(), getPiecesOnTheBoard()), nextMove)) {
            Piece selected = boardUI.getSelected();

            // track double-step pawn
            if (selected instanceof Pawn) {
                int oldRow = selected.getPosition().getRow();
                int newRow = nextMove.getRow();

                if (Math.abs(oldRow - newRow) == 2) {
                    setLastDoubleStepPawn((Pawn) selected);
                } else {
                    setLastDoubleStepPawn(null);
                }
            } else {
                setLastDoubleStepPawn(null);
            }

            handleEnPassant(boardUI.getSelected(), nextMove);

            System.out.println("Just moved piece");
            moveSelectedPiece(nextMove);
            moveCounter++;
        }

        if (moveCounter % 2 != 0) {
            currentColorToMove = ColorForChessPieces.WHITE;
        }
        else{
            currentColorToMove = ColorForChessPieces.BLACK;
        }

        System.out.println(Arrays.deepToString(getPiecesOnTheBoard()));
    }

    public void handleEnPassant(Piece selected, IndexPosition nextMove){
        if (!(selected instanceof Pawn)) return;

        int fromCol = selected.getPosition().getCol();
        int toCol = nextMove.getCol();

        // if pawn moves diagonally to an empty square it's en passant
        if (Math.abs(fromCol - toCol) == 1 &&
                getPiecesOnTheBoard()[nextMove.getRow()][nextMove.getCol()] == null) {

            int dir = (selected).isWhite() ? 1 : -1;

            // remove pawn directly behind the target square
            int capturedRow = nextMove.getRow() - dir;
            int capturedCol = nextMove.getCol();

            getPiecesOnTheBoard()[capturedRow][capturedCol] = null;
        }
    }

    // helper method for moving a piece
    public void moveSelectedPiece(IndexPosition nextMove) {
        Piece selected = boardUI.getSelected();
        IndexPosition oldPos = selected.getPosition();

        // detect castling
        if (selected instanceof King
                && Math.abs(nextMove.getCol() - oldPos.getCol()) == 2) {
            executeCastle(nextMove);
            return;
        }

        if(selected instanceof Pawn){
            int row = selected.getColor() == WHITE ? 7 : 0;
            if(nextMove.getRow()==row){
                executePromotion(nextMove);
                System.out.println(getPieceAt(nextMove.getRow(), nextMove.getCol()).toString());
                return;
            }

        }
        getPiecesOnTheBoard()[oldPos.getRow()][oldPos.getCol()] = null;

        selected.setPosition(new IndexPosition(nextMove.getRow(), nextMove.getCol()));

        getPiecesOnTheBoard()[nextMove.getRow()][nextMove.getCol()] = selected;

        moveStorage.add(new MoveRecord(selected.getClass().getSimpleName(), oldPos, nextMove));

        boardUI.setSelected(null);
    }

    //helper method for capturing the piece
    public void capturePiece(IndexPosition nextMove) {
        moveSelectedPiece(nextMove);
    }

    // a method which will select a piece for the detectMouseClickPosition() method
    public void selectPiece(int row, int col) {
        Piece piece = getPiecesOnTheBoard()[row][col];
        if (piece != null) {
            boardUI.setSelected(piece);
        }
    }



  
  
  
    public ArrayList<IndexPosition> getLegalMoves(Piece piece, Piece[][] board) {
        if(piece instanceof King) {
            return getKingLegalMoves(piece.getColor(), board);
        }

        if(piece instanceof Pawn pawn) {

            IndexPosition[] rawMoves = pawn.getPossibleMoves(board);
            ArrayList<IndexPosition> moves = new ArrayList<>(Arrays.asList(rawMoves));

            int row = pawn.getPosition().getRow();
            int col = pawn.getPosition().getCol();
            int dir = pawn.isWhite() ? 1 : -1;

            Pawn enemyPawn = getLastDoubleStepPawn();

            if (enemyPawn != null) {
                int enemyRow = enemyPawn.getPosition().getRow();
                int enemyCol = enemyPawn.getPosition().getCol();

                if (enemyRow == row && Math.abs(enemyCol - col) == 1) {
                    moves.add(new IndexPosition(row + dir, enemyCol));
                }
            }

            return checkService.filterMovesForCheck(piece, moves, board);
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
        if(canCastleKingSide( board)) {
            legalMoves.add(new IndexPosition(king.getPosition().getRow(), king.getPosition().getCol() + 2));
        }

        if (canCastleQueenSide( board)) {
            legalMoves.add(new IndexPosition(king.getPosition().getRow(), king.getPosition().getCol() - 2));
        }
        return legalMoves;
    }


    // this method defines the rules of king side castling
    public boolean canCastleKingSide( Piece[][] board) {
        ColorForChessPieces color = currentColorToMove;
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
    public boolean canCastleQueenSide( Piece[][] board) {
        ColorForChessPieces color = currentColorToMove;
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
        Piece[][] pieces = getPiecesOnTheBoard();
        Piece king = boardUI.getSelected();
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

        boardUI.setSelected(null);
    }

    // a method for the promotion logic of the pawn( hard coded to auto promotion to the queen just for this iteration)
    public void executePromotion(IndexPosition nextMove){
        Piece[][] pieces = getPiecesOnTheBoard();
        Pawn pawn = (Pawn) boardUI.getSelected();
        Queen queen = new Queen(pawn.getColor(),nextMove);
        pieces[pawn.getPosition().getRow()][pawn.getPosition().getCol()] = null;
        pieces[nextMove.getRow()][nextMove.getCol()] = queen;
        moveStorage.add(new MoveRecord("Queen", pawn.getPosition(), nextMove));
    }
    public boolean isCheckmate() {
        if (!checkService.isInCheck(currentColorToMove, getPiecesOnTheBoard())) {
            return false; // Not in check, cannot be checkmate
        }

        ArrayList<IndexPosition> kingMoves = getKingLegalMoves(currentColorToMove, getPiecesOnTheBoard());
        if (kingMoves != null && !kingMoves.isEmpty()) {
            return false; // King can escape, not checkmate
        }
        return !checkService.canBlockCheck(currentColorToMove, getPiecesOnTheBoard()) && !checkService.canCaptureAttacker(currentColorToMove, getPiecesOnTheBoard());
    }


}


