package Services;

import ChessBoard.BoardUI;
import ChessPieces.*;
import Constants.ColorForChessPieces;
import Constants.Sound;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;


import static Constants.ColorForChessPieces.*;

public class GameService {
    private CheckService checkService;
    private ArrayList<String> moveStorage;
    private int moveCounter = 1;
    // a field to keep track of which side is moving next
    private ColorForChessPieces currentColorToMove = WHITE;
    private Pawn lastDoubleStepPawn;
    private BoardUI boardUI;
    // Stores elements that implement the Piece abstract class.
    private Piece[][] piecesOnTheBoard = new Piece[8][8]; // Store
    private DrawService drawService;
    private MoveResult moveResult = new MoveResult();
    private SoundService soundService;

    public GameService(BoardUI boardUI) {
        this.boardUI = boardUI;
        checkService = new CheckService(piecesOnTheBoard);
        moveStorage = new ArrayList<>();
        drawService = new DrawService(this.checkService, this);
        soundService = new SoundService();

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

    public CheckService getCheckService() {
        return checkService;
    }

    public void setCheckService(CheckService checkService) {
        this.checkService = checkService;
    }

    public int getMoveCounter() {
        return moveCounter;
    }

    public BoardUI getBoardUI() {
        return boardUI;
    }

    public void setBoardUI(BoardUI boardUI) {
        this.boardUI = boardUI;
    }

    public ColorForChessPieces getCurrentColorToMove() {
        return currentColorToMove;
    }

    public void setCurrentColorToMove(ColorForChessPieces currentColorToMove) {
        this.currentColorToMove = currentColorToMove;
    }

    public Piece getPieceAt(int row, int col) {
        return piecesOnTheBoard[row][col];
    }

    public Piece[][] getPiecesOnTheBoard() {
        return this.piecesOnTheBoard;
    }

    public MoveResult getMoveResult() {
        return moveResult;
    }

    public void setPieceAt(int row, int col, Piece piece) {
        piecesOnTheBoard[row][col] = piece;
    }

    public ArrayList<String> getMoveStorage() {
        return moveStorage;
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
        if (boardUI.isPromoting()) {
            handlePromotionClick(nextMove);
            return;
        }
        Piece targetSquare = getPiecesOnTheBoard()[nextMove.getRow()][nextMove.getCol()];
        if (targetSquare == null) {
            handleEmptySquareClick(nextMove);
        } else {
            handleOccupiedSquareClick(targetSquare, nextMove);
        }
        System.out.println(Arrays.deepToString(getPiecesOnTheBoard()));
    }


    private void handleEmptySquareClick(IndexPosition nextMove) {

        // checks for moving a piece to a new square( no capture)
        if (isMovePossible(getLegalMoves(boardUI.getSelected(), getPiecesOnTheBoard()), nextMove)) {
            Piece selected = boardUI.getSelected();

            // a pawn moving diagonally to an empty square is an en passant capture
            boolean isEnPassant = selected instanceof Pawn
                    && selected.getPosition().getCol() != nextMove.getCol();

            updateLastDoubleStepPawn(selected, nextMove);
            handleEnPassant(boardUI.getSelected(), nextMove);

            System.out.println("Just moved piece");
            moveSelectedPiece(nextMove);
            if (boardUI.isPromoting()) return;
            moveCounter++;
            changeColorToMove();
            playPostMoveSound(isEnPassant);
            drawService.recordBoardState();
            drawService.updateHalfMoveClock(selected, null);
            updateMoveResult();
        }
    }

    private void handleOccupiedSquareClick(Piece targetPiece, IndexPosition nextMove) {
        // an addition to set the selected to null anytime the wrong colored piece is clicked
        if (targetPiece != null && targetPiece.getColor() != currentColorToMove && boardUI.getSelected() == null) {
            boardUI.setSelected(null);
        }
        if (boardUI.getSelected() == null) {
            if (targetPiece.getColor() == currentColorToMove) {
                boardUI.setSelected(targetPiece);
                System.out.println("Selected new piece");
            } else {
                System.out.println("It is not your turn");
            }
            return;
        }
        // checks for capturing a piece
        if (targetPiece != null) {
            if (targetPiece.getColor() == currentColorToMove) {
                System.out.println("Reselected piece");
                boardUI.setSelected(targetPiece);
            } else if (isMovePossible(getLegalMoves(boardUI.getSelected(), getPiecesOnTheBoard()), nextMove)) {
                System.out.println("Captured piece");
                Piece selected = boardUI.getSelected();
                capturePiece(nextMove);
                if (boardUI.isPromoting()) return;
                moveCounter++;
                changeColorToMove();
                playPostMoveSound(true);
                drawService.recordBoardState();
                drawService.updateHalfMoveClock(selected, targetPiece);
                updateMoveResult();
            }
        }
    }

    // a method for handling click which results in promotion of the pawn
    private void handlePromotionClick(IndexPosition nextMove) {
        int clickedCol = nextMove.getCol();
        int clickedRow = nextMove.getRow();

        if (clickedCol != boardUI.getPromotionCol()) return;

        int index = (boardUI.getPromotionRow() == 0) ? clickedRow : 7 - clickedRow;

        if (index >= 0 && index < 4) {
            Piece selected = boardUI.getPromotionOptions()[index];
            boardUI.finishPromotion(selected);
        }
    }

    // check overrides move/capture — play the right sound after the move has been applied and the turn flipped
    private void playPostMoveSound(boolean wasCapture) {
        if (checkService.isInCheck(piecesOnTheBoard, currentColorToMove)) {
            soundService.play(Sound.CHECK);
        } else if (wasCapture) {
            soundService.play(Sound.CAPTURE);
        } else {
            soundService.play(Sound.MOVE);
        }
    }

    // helper method for the change of the color to move
    private void changeColorToMove() {
        if (moveCounter % 2 != 0) {
            currentColorToMove = ColorForChessPieces.WHITE;
        } else {
            currentColorToMove = ColorForChessPieces.BLACK;
        }

        System.out.println(Arrays.deepToString(getPiecesOnTheBoard()));
    }

    private void updateLastDoubleStepPawn(Piece selected, IndexPosition nextMove) {
        if (selected instanceof Pawn pawn) {
            int oldRow = pawn.getPosition().getRow();
            int newRow = nextMove.getRow();

            if (Math.abs(oldRow - newRow) == 2) {
                setLastDoubleStepPawn(pawn);
                return;
            }
        }
        setLastDoubleStepPawn(null);
    }

    public void handleEnPassant(Piece selected, IndexPosition nextMove) {
        if (!(selected instanceof Pawn))
            return;

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
            executeCastle(selected, nextMove);
            boardUI.setSelected(null);
            return;
        }

        if (selected instanceof Pawn) {
            int row = selected.getColor() == WHITE ? 7 : 0;
            if (nextMove.getRow() == row) {
                boardUI.startPromotion((Pawn) selected, nextMove);
                return;
            }

        }
        getPiecesOnTheBoard()[oldPos.getRow()][oldPos.getCol()] = null;

        selected.setPosition(new IndexPosition(nextMove.getRow(), nextMove.getCol()));

        getPiecesOnTheBoard()[nextMove.getRow()][nextMove.getCol()] = selected;

        moveStorage.add(addToMoveRecord(new MoveRecord(selected.getClass().getSimpleName(), oldPos, nextMove, null)));

        boardUI.setSelected(null);
    }

    public void updateMoveResult() {
        moveResult.reset();

        if (isCheckmate()) {
            ColorForChessPieces winner = currentColorToMove == WHITE ? BLACK : WHITE;
            moveResult.setCheckmate(winner);

            System.out.println("Checkmate is set");

            return;
        }

        if (drawService.isStalemate()) {
            moveResult.setDraw("Stalemate");
        } else if (drawService.isInsufficientMaterial()) {
            moveResult.setDraw("Insufficient material");
        } else if (drawService.isFiftyMoveRule()) {
            moveResult.setDraw("50-Move Rule");
        } else if (drawService.isThreefoldRepetition()) {
            moveResult.setDraw("Threefold repetition");
        }
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
        if (piece instanceof King) {
            return getKingLegalMoves();
        }

        if (piece instanceof Pawn pawn) {
            IndexPosition[] rawMoves = pawn.getPossibleMoves(board, getLastDoubleStepPawn());
            ArrayList<IndexPosition> moves = new ArrayList<>(Arrays.asList(rawMoves));
            return checkService.filterMovesForCheck(piece, moves, getCurrentColorToMove());
        }

        IndexPosition[] rawMoves = piece.getPossibleMoves(board);
        ArrayList<IndexPosition> moves = new ArrayList<>(Arrays.asList(rawMoves));
        return checkService.filterMovesForCheck(piece, moves, currentColorToMove);
    }

    // this method filters king moves removing checked squares
    public ArrayList<IndexPosition> getKingLegalMoves() {
        King king = checkService.findKing(piecesOnTheBoard, currentColorToMove);
        IndexPosition[] rawMoves = king.getPossibleMoves(piecesOnTheBoard);
        ColorForChessPieces enemyColor = currentColorToMove == WHITE ? BLACK : WHITE;
        ArrayList<IndexPosition> legalMoves = new ArrayList<>();
        // simulate a move to a square and check if king is inder check
        for (IndexPosition move : rawMoves) {
            Piece[][] tempBoard = checkService.copyBoard();
            tempBoard[king.getPosition().getRow()][king.getPosition().getCol()] = null;
            tempBoard[move.getRow()][move.getCol()] = king;

            if (!checkService.isSquareAttacked(move, enemyColor, tempBoard)) {
                legalMoves.add(move);
            }
        }
        if (canCastleKingSide()) {
            legalMoves.add(new IndexPosition(king.getPosition().getRow(), king.getPosition().getCol() + 2));
        }

        if (canCastleQueenSide()) {
            legalMoves.add(new IndexPosition(king.getPosition().getRow(), king.getPosition().getCol() - 2));
        }
        return legalMoves;
    }


    // this method defines the rules of king side castling
    public boolean canCastleKingSide() {
        ColorForChessPieces color = currentColorToMove;
        // set row based on color of the castle desired pieces
        ColorForChessPieces enemyColor = color == WHITE ? BLACK : WHITE;
        int row = color == WHITE ? 0 : 7;
        // if rook or king has moved castle is not allowed
        if (hasPiecedMoved(new IndexPosition(row, 4)) || hasPiecedMoved(new IndexPosition(row, 7))) {
            return false;
        }
        // castle is only possible is rook on the required field exists
        if (piecesOnTheBoard[row][7] == null) {
            return false;
        }
        // check if squares between king and rook are empty
        for (int i = 5; i <= 6; i++) {
            if (piecesOnTheBoard[row][i] != null) {
                return false;
            }
        }
        // king cannot castle while in check or through/into attacked squares
        for (int i = 4; i <= 6; i++) {
            if (checkService.isSquareAttacked(new IndexPosition(row, i), enemyColor, piecesOnTheBoard)) {
                return false;
            }
        }

        return true;
    }

    // this method defines teh rules of queen side castling
    public boolean canCastleQueenSide() {
        ColorForChessPieces color = currentColorToMove;
        // set row based on color of the castle desired pieces
        ColorForChessPieces enemyColor = color == WHITE ? BLACK : WHITE;
        int row = color == WHITE ? 0 : 7;
        // if rook or king has moved castle is not allowed
        if (hasPiecedMoved(new IndexPosition(row, 4)) || hasPiecedMoved(new IndexPosition(row, 0))) {
            return false;
        }
        if (piecesOnTheBoard[row][0] == null) {
            return false;
        }
        // check if squares between king and rook are empty (columns 1, 2, 3)
        for (int i = 1; i <= 3; i++) {
            if (piecesOnTheBoard[row][i] != null) {
                return false;
            }
        }
        // king cannot castle while in check or through/into attacked squares (columns 2, 3, 4)
        for (int i = 2; i <= 4; i++) {
            if (checkService.isSquareAttacked(new IndexPosition(row, i), enemyColor, piecesOnTheBoard)) {
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
        char expectedFile = (char) ('a' + from.getCol());
        char expectedRank = (char) ('1' + from.getRow());
        for (String move : moveStorage) {
            if (move.charAt(0) == expectedFile && move.charAt(1) == expectedRank) {
                return true;
            }
        }
        return false;
    }

    public void executeCastle(Piece king, IndexPosition kingTarget) {
        IndexPosition kingFrom = king.getPosition();
        int row = kingFrom.getRow();

        boolean isKingSide = kingTarget.getCol() > kingFrom.getCol();
        int rookFromCol = isKingSide ? 7 : 0;
        int rookToCol = isKingSide ? 5 : 3;

        Piece rook = piecesOnTheBoard[row][rookFromCol];

        // move king
        piecesOnTheBoard[kingFrom.getRow()][kingFrom.getCol()] = null;
        king.setPosition(kingTarget);
        piecesOnTheBoard[kingTarget.getRow()][kingTarget.getCol()] = king;

        // move rook
        piecesOnTheBoard[row][rookFromCol] = null;
        rook.setPosition(new IndexPosition(row, rookToCol));
        piecesOnTheBoard[row][rookToCol] = rook;

        moveStorage.add(addToMoveRecord(new MoveRecord("King", kingFrom, kingTarget, null)));
    }

    // a method for the promotion logic of the pawn( hard coded to auto promotion to the queen just for this iteration)
    public void executePromotion(IndexPosition nextMove) {
        Pawn pawn = (Pawn) boardUI.getSelected();
        Queen queen = new Queen(pawn.getColor(), nextMove);
        piecesOnTheBoard[pawn.getPosition().getRow()][pawn.getPosition().getCol()] = null;
        piecesOnTheBoard[nextMove.getRow()][nextMove.getCol()] = queen;
        moveStorage.add(addToMoveRecord(new MoveRecord("Queen", pawn.getPosition(), nextMove, null)));
    }

    public boolean isCheckmate() {
        if (!checkService.isInCheck(piecesOnTheBoard, currentColorToMove)) {
            return false; // Not in check, cannot be checkmate

        }

        ArrayList<IndexPosition> kingMoves = getKingLegalMoves();
        if (kingMoves != null && !kingMoves.isEmpty()) {
            return false; // King can escape, not checkmate
        }
        System.out.println("Checkmate " + (!checkService.canBlockCheck(currentColorToMove) && !checkService.canCaptureAttacker(currentColorToMove)));
        return !checkService.canBlockCheck(currentColorToMove) && !checkService.canCaptureAttacker(currentColorToMove);
    }

    public boolean isDraw() {
        return drawService.isStalemate()
                || drawService.isInsufficientMaterial()
                || drawService.isFiftyMoveRule()
                || drawService.isThreefoldRepetition();
    }


    public String addToMoveRecord(MoveRecord moveRecord) {

        IndexPosition from = moveRecord.getMovedFrom();
        IndexPosition to = moveRecord.getMovedTo();

        char fileMovedFrom = (char) ('a' + from.getCol());
        int rankMovedFrom = from.getRow() + 1;
        char fileMovedTo = (char) ('a' + to.getCol());
        int rankMovedTo = to.getRow() + 1;
        String uci = "" + fileMovedFrom + rankMovedFrom + fileMovedTo + rankMovedTo;
        if (moveRecord.getPromotionPiece() != null) {
            uci += moveRecord.getPromotionPiece();
        }

        return uci;
    }

    public void recordPromotion(Piece promoted, IndexPosition promotedFrom, IndexPosition promotedTo) {
        char letter = switch (promoted.getClass().getSimpleName()) {
            case "Queen" -> 'q';
            case "Rook" -> 'r';
            case "Bishop" -> 'b';
            case "Knight" -> 'n';
            default ->
                    throw new IllegalStateException("Invalid promotion piece: " + promoted.getClass().getSimpleName());
        };
        MoveRecord record = new MoveRecord(promoted.getClass().getSimpleName(), promotedFrom, promotedTo, letter);
        moveStorage.add(addToMoveRecord(record));

        moveCounter++;
        changeColorToMove();
        playPostMoveSound(false);
        drawService.recordBoardState();
        drawService.updateHalfMoveClock(promoted, null);
        updateMoveResult();
    }

    public void applyBotMove(EngineMove move) {
        IndexPosition from = move.getFrom();
        IndexPosition to = move.getTo();
        Character promotion = move.getPromotion();

        Piece moving = piecesOnTheBoard[from.getRow()][from.getCol()];

        updateLastDoubleStepPawn(moving, to);

        // detect castling: king moving 2 squares horizontally
        if (moving instanceof King && Math.abs(to.getCol() - from.getCol()) == 2) {
            executeCastle(moving, to);
            moveCounter++;
            changeColorToMove();
            playPostMoveSound(false);
            drawService.recordBoardState();
            drawService.updateHalfMoveClock(moving, null);
            updateMoveResult();
            return;
        }

        // detect en passant: pawn moves diagonally to an empty destination
        boolean isEnPassant = moving instanceof Pawn
                && from.getCol() != to.getCol()
                && piecesOnTheBoard[to.getRow()][to.getCol()] == null;

        Piece captured = piecesOnTheBoard[to.getRow()][to.getCol()];
        if (isEnPassant) {
            int capturedRow = from.getRow();
            int capturedCol = to.getCol();
            captured = piecesOnTheBoard[capturedRow][capturedCol];
            piecesOnTheBoard[capturedRow][capturedCol] = null;
        }

        piecesOnTheBoard[from.getRow()][from.getCol()] = null;
        Piece placed;
        if (promotion != null) {
            placed = createPromotedPiece(promotion, moving.getColor(), to);
        } else {
            moving.setPosition(to);
            placed = moving;
        }
        piecesOnTheBoard[to.getRow()][to.getCol()] = placed;

        String pieceName = (promotion != null)
                ? placed.getClass().getSimpleName()
                : moving.getClass().getSimpleName();
        moveStorage.add(addToMoveRecord(new MoveRecord(pieceName, from, to, promotion)));

        moveCounter++;
        changeColorToMove();
        playPostMoveSound(captured != null);

        drawService.recordBoardState();
        drawService.updateHalfMoveClock(moving, captured);
        updateMoveResult();
    }


    private Piece createPromotedPiece(char promotion, ColorForChessPieces color, IndexPosition pos) {
        return switch (Character.toLowerCase(promotion)) {
            case 'q' -> new Queen(color, pos);
            case 'r' -> new Rook(color, pos);
            case 'b' -> new Bishop(color, pos);
            case 'n' -> new Knight(color, pos);
            default -> throw new IllegalArgumentException("Invalid promotion char: " + promotion);
        };
    }

    public void resetGame(){
        //clear the board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                piecesOnTheBoard[row][col] = null;
            }
        }
        //reset all state
        moveCounter = 1;
        currentColorToMove = WHITE;
        lastDoubleStepPawn = null;
        moveStorage.clear();
        moveResult.reset();
        drawService = new DrawService(checkService, this);
        boardUI.setSelected(null);

        // set up pieces again
        setUpPiecesOnTheBoard();
    }

}



