package Test;

import ChessBoard.BoardUI;
import ChessPieces.*;
import Constants.ColorForChessPieces;
import Constants.GameMode;
import Services.GameService;
import Services.MoveRecord;
import org.junit.jupiter.api.*;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class TestGameService {
    private static GameService gameService;
    private static final BoardUI boardUI = new BoardUI(GameMode.HUMAN_VS_BOT,ColorForChessPieces.WHITE);

    @BeforeAll
    public static void setup() {
        gameService = new GameService(boardUI);
        gameService.resetGame();
    }

    @Test
    @DisplayName("Test filtered by check king moves ")
    public void testFilterByCheckKingMoves() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        Bishop bishop = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(6, 6));
        Piece[][] board = new Piece[8][8];
        board[3][3] = king;
        board[6][6] = bishop;
        ArrayList<IndexPosition> moves = gameService.getKingLegalMoves();
        assertEquals(6, moves.size());

    }


    @Test
    @DisplayName("Test that castle is possible if all requirements are met")
    public void testCastleIsPossible() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 7));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[0][7] = rook;
        assertTrue(gameService.canCastleKingSide());
    }

    @Test
    @DisplayName("Test castle is not allowed through a piece")
    public void testCastleIsNotAllowedThroughAPiece() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 7));
        Knight knight = new Knight(ColorForChessPieces.WHITE, new IndexPosition(0, 6));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[0][6] = knight;
        board[0][7] = rook;
        assertFalse(gameService.canCastleKingSide());

    }


    @Test
    @DisplayName("Castle cannot be done under check ")
    public void testCastleIsNotAllowedUnderCheck() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 7));
        Queen queen = new Queen(ColorForChessPieces.BLACK, new IndexPosition(1, 3));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[1][3] = queen;
        board[0][7] = rook;
        assertFalse(gameService.canCastleKingSide());
    }

    @Test
    @DisplayName("Castle cannot be done trough a checked field  ")
    public void testCastleIsNotAllowedTroughCheck() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 7));
        Queen queen = new Queen(ColorForChessPieces.BLACK, new IndexPosition(2, 4));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[2][4] = queen;
        board[0][7] = rook;
        assertFalse(gameService.canCastleKingSide());
    }


    @Test
    @DisplayName("Castle cannot be done on  a checked field  ")
    public void testCastleIsNotAllowedOnCheck() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 7));
        Queen queen = new Queen(ColorForChessPieces.BLACK, new IndexPosition(3, 6));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[3][6] = queen;
        board[0][7] = rook;
        assertFalse(gameService.canCastleKingSide());
    }

    @Test
    @DisplayName(" Queen side  castle is possible if all requirements are met")
    public void testQueenSideIsPossible() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[0][0] = rook;
        assertTrue(gameService.canCastleQueenSide());
    }


    @Test
    @DisplayName("queen side  castle is not allowed through a piece")
    public void testQueenSideCastleIsNotAllowedThroughAPiece() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Knight knight = new Knight(ColorForChessPieces.WHITE, new IndexPosition(0, 2));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[0][2] = knight;
        board[0][0] = rook;
        assertFalse(gameService.canCastleQueenSide());

    }


    @Test
    @DisplayName(" Queen side castle cannot be done under check ")
    public void testQueenSideCastleIsNotAllowedUnderCheck() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Queen queen = new Queen(ColorForChessPieces.BLACK, new IndexPosition(1, 3));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[1][3] = queen;
        board[0][0] = rook;
        assertFalse(gameService.canCastleQueenSide());
    }


    @Test
    @DisplayName("Queen side castle cannot be done trough a checked field  ")
    public void testQueenSideCastleIsNotAllowedTroughCheck() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Bishop bishop = new Bishop(ColorForChessPieces.BLACK, new IndexPosition(2, 4));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[2][4] = bishop;
        board[0][7] = rook;
        assertFalse(gameService.canCastleQueenSide());
    }



    @Test
    @DisplayName("Retrieve filtered legal moves of the king with one possible castle")
    public void testRetrievedLegalKingMoves() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[0][0] = rook;
        int size = gameService.getLegalMoves(king,board).size();
        assertEquals(size,6);
    }

    @Test
    @DisplayName("Retrieve filtered legal moves of the king with two possible castles")
    public void testRetrievedLegalKingMovesWithCastles() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Rook rook2 = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 7));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[0][0] = rook;
        board[0][7] = rook2;
        int size = gameService.getLegalMoves(king,board).size();
        assertEquals(size,7);
    }

    @Test
    @DisplayName("pinned piece cannot move")
    public void testPinnedPieceCannotMove() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Bishop bishop = new Bishop(ColorForChessPieces.WHITE, new IndexPosition(1, 4));
        Rook rook = new Rook(ColorForChessPieces.BLACK, new IndexPosition(7, 4));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[1][4] = bishop;
        board[0][0] = rook;
        assertEquals(gameService.getLegalMoves(bishop,board).size(),0);
    }

    @Test
    @DisplayName("Piined piece can only move on the direction of teh pin and capture enemy piece")
    public void  testPinnedMovement(){
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(1, 4));
        Rook rook2 = new Rook(ColorForChessPieces.BLACK, new IndexPosition(2, 4));
        Piece[][] board = new Piece[8][8];
        board[0][4] = king;
        board[1][4] = rook;
        board[2][4] = rook2;
        assertEquals(gameService.getLegalMoves(rook,board).size(),1);
    }


    @Test
    @DisplayName("King cannot capture piece which is defended")
    public void testKingCannotCapturePiece() {
        King king = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        Rook rook1 = new Rook(ColorForChessPieces.WHITE, new IndexPosition(0, 1));
        Rook rook2 = new Rook(ColorForChessPieces.WHITE, new IndexPosition(1, 0));
        Rook rook3 = new Rook(ColorForChessPieces.BLACK, new IndexPosition(1, 1));
        Rook rook4 = new Rook(ColorForChessPieces.BLACK, new IndexPosition(2, 1));
        Piece[][] board = new Piece[8][8];
        board[0][0] = king;
        board[0][1] = rook1;
        board[1][0] = rook2;
        board[1][1] = rook3;
        board[2][1] = rook4;
        assertEquals(gameService.getLegalMoves(king,board).size(),0);
    }

    @Test
    @DisplayName("White pawn can capture en passant via getLegalMoves")
    void testWhitePawnEnPassantLegalMove() {
        Piece[][] testBoard = new Piece[8][8];

        // Add white king so checkService doesn't break
        King whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        testBoard[0][4] = whiteKing;

        // White pawn at (4,4)
        Pawn whitePawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(4, 4));
        testBoard[4][4] = whitePawn;

        // Black pawn just moved two squares to (4,5)
        Pawn blackPawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(4, 5));
        testBoard[4][5] = blackPawn;

        gameService.setLastDoubleStepPawn(blackPawn);

        ArrayList<IndexPosition> moves = gameService.getLegalMoves(whitePawn, testBoard);

        assertTrue(moves.contains(new IndexPosition(5, 5)),
                "White pawn should be able to capture en passant at (5,5)");
    }

    @Test
    @DisplayName("Black pawn can capture en passant via getLegalMoves")
    void testBlackPawnEnPassantLegalMove() {
        Piece[][] testBoard = new Piece[8][8];

        // Add black king so checkService doesn't break
        King blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 4));
        testBoard[7][4] = blackKing;

        // Black pawn at (3,3)
        Pawn blackPawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(3, 3));
        testBoard[3][3] = blackPawn;

        // White pawn just moved two squares to (3,4)
        Pawn whitePawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(3, 4));
        testBoard[3][4] = whitePawn;

        gameService.setLastDoubleStepPawn(whitePawn);

        ArrayList<IndexPosition> moves = gameService.getLegalMoves(blackPawn, testBoard);

        assertTrue(moves.contains(new IndexPosition(2, 4)),
                "Black pawn should be able to capture en passant at (2,4)");
    }

    @Test
    @DisplayName("Pawn cannot capture en passant if not next to double-step pawn")
    void testPawnCannotEnPassantLegalMove() {
        Piece[][] testBoard = new Piece[8][8];

        // Add white king
        King whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        testBoard[0][4] = whiteKing;

        Pawn whitePawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(4, 4));
        testBoard[4][4] = whitePawn;

        Pawn blackPawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(4, 6));
        testBoard[4][6] = blackPawn;

        gameService.setLastDoubleStepPawn(blackPawn);

        ArrayList<IndexPosition> moves = gameService.getLegalMoves(whitePawn, testBoard);

        assertFalse(moves.contains(new IndexPosition(5, 6)),
                "White pawn should not be able to capture en passant if not adjacent");
    }

    @Test
    @DisplayName("En passant captures black pawn for white")
    void testWhitePawnEnPassant() {
        Piece[][] internalBoard = gameService.getPiecesOnTheBoard();

        Pawn whitePawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(4, 4));
        Pawn blackPawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(4, 5));

        internalBoard[4][4] = whitePawn;
        internalBoard[4][5] = blackPawn;

        IndexPosition nextMove = new IndexPosition(5, 5);
        gameService.handleEnPassant(whitePawn, nextMove);

        // Check the internal board, not a local array
        assertNull(internalBoard[4][5],
                "Black pawn should be removed after en passant");
    }

    @Test
    @DisplayName("En passant captures white pawn for black")
    void testBlackPawnEnPassant() {
        Piece[][] internalBoard = gameService.getPiecesOnTheBoard();

        Pawn blackPawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(3, 4));
        Pawn whitePawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(3, 3));

        internalBoard[3][4] = blackPawn;
        internalBoard[3][3] = whitePawn;

        IndexPosition nextMove = new IndexPosition(2, 3);
        gameService.handleEnPassant(blackPawn, nextMove);

        assertNull(internalBoard[3][3],
                "White pawn should be removed after en passant");
    }

    @Test
    @DisplayName("Non-pawn piece does not trigger en passant")
    void testNonPawnDoesNothing() {
        Piece[][] testBoard = new Piece[8][8];

        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(4, 4));
        testBoard[4][4] = rook;

        IndexPosition nextMove = new IndexPosition(5, 5);
        gameService.handleEnPassant(rook, nextMove);

        // rook is still on the board, nothing removed
        assertNotNull(testBoard[4][4]);
    }

    @Test
    @DisplayName("En passant does not happen if target square is not empty")
    void testEnPassantFailsIfTargetNotEmpty() {
        Piece[][] testBoard = new Piece[8][8];

        Pawn whitePawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(4, 4));
        Pawn blackPawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(4, 5));
        Pawn blockingPawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(5, 5));

        testBoard[4][4] = whitePawn;
        testBoard[4][5] = blackPawn;
        testBoard[5][5] = blockingPawn;

        IndexPosition nextMove = new IndexPosition(5, 5);
        gameService.handleEnPassant(whitePawn, nextMove);

        // black pawn should still exist because target square is occupied
        assertNotNull(testBoard[4][5]);
    }

    @Test
    @DisplayName("Move is parsed from MoveRecord to input data")
    void testMoveIsParsedFromMoveRecordToInputData() {
        MoveRecord move = new  MoveRecord("King",new IndexPosition(4, 4),new IndexPosition(5, 5),null);

        String parsedMove = gameService.addToMoveRecord(move);
        assertEquals(parsedMove,"e5f6");
    }


    @Test
    @DisplayName("handleSquareClick for an empty target")
    public void testHandleSquareClickEmptyTarget() {
        Piece[][] board = gameService.getPiecesOnTheBoard();
        King whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        King blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 7));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(3, 3));
        board[0][0] = whiteKing;
        board[7][7] = blackKing;
        board[3][3] = rook;
        boardUI.setSelected(rook);

        gameService.handleSquareClick(new IndexPosition(3, 6));

        assertNull(board[3][3], "Rook should have left its starting square");
        assertNotNull(board[3][6], "Rook should be on the new square");
        assertTrue(board[3][6] instanceof Rook);
    }

    @Test
    @DisplayName("test for illegal click")
    public void testHandleEmptySquareClickIllegalMove() {
        Piece[][] board = gameService.getPiecesOnTheBoard();
        King whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        King blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 7));
        Rook rook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(4, 4));
        board[0][0] = whiteKing;
        board[7][7] = blackKing;
        board[4][4] = rook;
        boardUI.setSelected(rook);

        gameService.handleEmptySquareClick(new IndexPosition(5, 5));

        assertNotNull(board[4][4], "Rook should not have moved on an illegal click");
        assertNull(board[5][5], "Destination should remain empty");
    }

    @Test
    @DisplayName("test en passant")
    public void testHandleEmptySquareClickEnPassant() {
        Piece[][] board = gameService.getPiecesOnTheBoard();
        King whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        King blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 7));
        Pawn whitePawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(4, 4));
        Pawn blackPawn = new Pawn(ColorForChessPieces.BLACK, new IndexPosition(4, 5));
        board[0][0] = whiteKing;
        board[7][7] = blackKing;
        board[4][4] = whitePawn;
        board[4][5] = blackPawn;

        gameService.setLastDoubleStepPawn(blackPawn);
        boardUI.setSelected(whitePawn);

        gameService.handleEmptySquareClick(new IndexPosition(5, 5));

        assertNull(board[4][4], "White pawn should have left its starting square");
        assertTrue(board[5][5] instanceof Pawn, "White pawn should be on the en passant target");
        assertNull(board[4][5], "Captured black pawn should be removed via en passant");
    }
    @Test
    @DisplayName("test click on the opponent's piece when the piece users piece is not selected")
    public void testHandleOccupiedClickOpponentNothingSelected() {
        Piece[][] board = gameService.getPiecesOnTheBoard();
        King whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        King blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 7));
        Rook blackRook = new Rook(ColorForChessPieces.BLACK, new IndexPosition(4, 4));
        board[0][0] = whiteKing;
        board[7][7] = blackKing;
        board[4][4] = blackRook;
        boardUI.setSelected(null);

        gameService.handleOccupiedSquareClick(blackRook, new IndexPosition(4, 4));

        assertNull(boardUI.getSelected(),
                "Clicking an opponent's piece with no selection should leave selection null");
    }

    @Test
    @DisplayName("click on own piece when no piece is selected")
    public void testHandleOccupiedClickOwnNothingSelected() {
        Piece[][] board = gameService.getPiecesOnTheBoard();
        King whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        King blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 7));
        Rook whiteRook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(4, 4));
        board[0][0] = whiteKing;
        board[7][7] = blackKing;
        board[4][4] = whiteRook;
        boardUI.setSelected(null);

        gameService.handleOccupiedSquareClick(whiteRook, new IndexPosition(4, 4));

        assertEquals(whiteRook, boardUI.getSelected(),
                "Clicking own piece with no selection should select it");
    }

    @Test
    @DisplayName("test click on your own piece when the piece is already selected")
    public void testHandleOccupiedClickReselectsOwnPiece() {
        Piece[][] board = gameService.getPiecesOnTheBoard();
        King whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        King blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 7));
        Rook rook1 = new Rook(ColorForChessPieces.WHITE, new IndexPosition(4, 4));
        Rook rook2 = new Rook(ColorForChessPieces.WHITE, new IndexPosition(4, 6));
        board[0][0] = whiteKing;
        board[7][7] = blackKing;
        board[4][4] = rook1;
        board[4][6] = rook2;
        boardUI.setSelected(rook1);

        gameService.handleOccupiedSquareClick(rook2, new IndexPosition(4, 6));

        assertEquals(rook2, boardUI.getSelected(),
                "Clicking another of own pieces should reselect to the new piece");
    }

    @Test
    @DisplayName("test capture opponents piece")
    public void testHandleOccupiedClickCapturesOpponent() {
        Piece[][] board = gameService.getPiecesOnTheBoard();
        King whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        King blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 7));
        Rook whiteRook = new Rook(ColorForChessPieces.WHITE, new IndexPosition(4, 4));
        Rook blackRook = new Rook(ColorForChessPieces.BLACK, new IndexPosition(4, 7));
        board[0][0] = whiteKing;
        board[7][7] = blackKing;
        board[4][4] = whiteRook;
        board[4][7] = blackRook;
        boardUI.setSelected(whiteRook);

        gameService.handleOccupiedSquareClick(blackRook, new IndexPosition(4, 7));

        assertNull(board[4][4], "Capturing rook should have left its starting square");
        assertTrue(board[4][7] instanceof Rook, "White rook should now occupy the captured square");
        assertEquals(ColorForChessPieces.WHITE, board[4][7].getColor(),
                "Piece on capture square should be white");
    }

    @Test
    @DisplayName("test promotion state of the pawn")
    public void testPawnPromotionEntersPromotingState() {
        Piece[][] board = gameService.getPiecesOnTheBoard();
        King whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        King blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 0));
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(6, 4));
        board[0][0] = whiteKing;
        board[7][0] = blackKing;
        board[6][4] = pawn;
        boardUI.setSelected(pawn);

        gameService.handleSquareClick(new IndexPosition(7, 4));

        assertTrue(boardUI.isPromoting(),
                "Pushing a pawn to the last rank should trigger promotion state");
    }

    @Test
    @DisplayName("handlePromotionClick on the correct column finishes promotion")
    public void testHandlePromotionClickCorrectColumn() {
        Piece[][] board = gameService.getPiecesOnTheBoard();
        King whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 0));
        King blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 0));
        Pawn pawn = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(6, 4));
        board[0][0] = whiteKing;
        board[7][0] = blackKing;
        board[6][4] = pawn;
        boardUI.setSelected(pawn);
        gameService.handleSquareClick(new IndexPosition(7, 4));

        gameService.handlePromotionClick(new IndexPosition(0, 4));

        assertFalse(boardUI.isPromoting(),
                "Clicking a valid promotion option should finish promotion");
    }


}
