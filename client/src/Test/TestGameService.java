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

        // copy testBoard into gameService's internal board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                gameService.setPieceAt(row, col, testBoard[row][col]);
            }
        }

        gameService.setLastDoubleStepPawn(blackPawn);
        gameService.setCurrentColorToMove(ColorForChessPieces.WHITE);

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

        // copy testBoard into gameService's internal board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                gameService.setPieceAt(row, col, testBoard[row][col]);
            }
        }

        gameService.setLastDoubleStepPawn(whitePawn);
        gameService.setCurrentColorToMove(ColorForChessPieces.BLACK);

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

        // copy testBoard into gameService's internal board
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                gameService.setPieceAt(row, col, testBoard[row][col]);
            }
        }

        gameService.setLastDoubleStepPawn(blackPawn);
        gameService.setCurrentColorToMove(ColorForChessPieces.WHITE);

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

    // ---------------------------------------------------------------
// updateMoveResult tests
// ---------------------------------------------------------------

    @Test
    @DisplayName("updateMoveResult sets checkmate with correct winner")
    void testUpdateMoveResultCheckmate() {
        // back rank mate — white king boxed in by pawns, black rook delivers checkmate
        King whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 7));
        Rook blackRook = new Rook(ColorForChessPieces.BLACK, new IndexPosition(0, 0));
        Pawn wp1 = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 5));
        Pawn wp2 = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 6));
        Pawn wp3 = new Pawn(ColorForChessPieces.WHITE, new IndexPosition(1, 7));

        gameService.setPieceAt(0, 7, whiteKing);
        gameService.setPieceAt(0, 0, blackRook);
        gameService.setPieceAt(1, 5, wp1);
        gameService.setPieceAt(1, 6, wp2);
        gameService.setPieceAt(1, 7, wp3);

        gameService.setCurrentColorToMove(ColorForChessPieces.WHITE);
        gameService.updateMoveResult();

        assertTrue(gameService.getMoveResult().getCheckmate());
        assertEquals(ColorForChessPieces.BLACK, gameService.getMoveResult().getColorToWin());
        assertFalse(gameService.getMoveResult().getDraw());
    }

    @Test
    @DisplayName("updateMoveResult sets stalemate draw")
    void testUpdateMoveResultStalemate() {
        King blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 0));
        Queen whiteQueen = new Queen(ColorForChessPieces.WHITE, new IndexPosition(5, 1));
        King whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(5, 3));

        gameService.setPieceAt(7, 0, blackKing);
        gameService.setPieceAt(5, 1, whiteQueen);
        gameService.setPieceAt(5, 3, whiteKing);

        gameService.setCurrentColorToMove(ColorForChessPieces.BLACK);
        gameService.updateMoveResult();

        assertTrue(gameService.getMoveResult().getDraw());
        assertEquals("Stalemate", gameService.getMoveResult().getDrawReason());
        assertFalse(gameService.getMoveResult().getCheckmate());
    }

    @Test
    @DisplayName("updateMoveResult sets insufficient material draw")
    void testUpdateMoveResultInsufficientMaterial() {
        King whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        King blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 4));

        gameService.setPieceAt(0, 4, whiteKing);
        gameService.setPieceAt(7, 4, blackKing);

        gameService.setCurrentColorToMove(ColorForChessPieces.WHITE);
        gameService.updateMoveResult();

        assertTrue(gameService.getMoveResult().getDraw());
        assertEquals("Insufficient material", gameService.getMoveResult().getDrawReason());
    }

    @Test
    @DisplayName("updateMoveResult resets previous result before evaluating")
    void testUpdateMoveResultResetsPreviousState() {
        // manually set a stale checkmate result
        gameService.getMoveResult().setCheckmate(ColorForChessPieces.WHITE);

        // set up a normal position with no checkmate or draw
        King whiteKing = new King(ColorForChessPieces.WHITE, new IndexPosition(0, 4));
        King blackKing = new King(ColorForChessPieces.BLACK, new IndexPosition(7, 4));
        Queen whiteQueen = new Queen(ColorForChessPieces.WHITE, new IndexPosition(3, 3));

        gameService.setPieceAt(0, 4, whiteKing);
        gameService.setPieceAt(7, 4, blackKing);
        gameService.setPieceAt(3, 3, whiteQueen);

        gameService.setCurrentColorToMove(ColorForChessPieces.WHITE);
        gameService.updateMoveResult();

        // stale checkmate should be cleared
        assertFalse(gameService.getMoveResult().getCheckmate());
        assertFalse(gameService.getMoveResult().getDraw());
    }

}
