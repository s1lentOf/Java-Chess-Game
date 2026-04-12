package Test;

import ChessBoard.Board;
import ChessPieces.*;
import Constants.ColorForChessPieces;
import Services.CheckService;
import Services.DrawService;
import Services.GameService;
import org.junit.jupiter.api.*;

import java.lang.reflect.Field;

import static Constants.ColorForChessPieces.*;
import static org.junit.jupiter.api.Assertions.*;

public class TestDrawService {

    private DrawService drawService;
    private GameService gameService;
    private Board board;
    private Piece[][] pieces;

    @BeforeEach
    void setUp() throws Exception {
        board = new Board();
        pieces = new Piece[8][8];
        setBoard(board, pieces);
        gameService = new GameService(board);
        drawService = new DrawService(new CheckService(), gameService);
        setCurrentColor(gameService, WHITE);
    }

    private void setBoard(Board board, Piece[][] pieces) throws Exception {
        Field f = Board.class.getDeclaredField("piecesOnTheBoard");
        f.setAccessible(true);
        f.set(board, pieces);
    }

    private void setCurrentColor(GameService gs, ColorForChessPieces color) throws Exception {
        Field f = GameService.class.getDeclaredField("currentColorToMove");
        f.setAccessible(true);
        f.set(gs, color);
    }

    // Stalemate

    @Test
    @DisplayName("Stalemate: player not in check but has no legal moves")
    void testStalemate() {
        //Black king on a8 (7,0), white queen on b6 (5,1)
        //Black king has no legal moves and is not in check = stalemate

        King blackKing = new King(BLACK, new IndexPosition(7, 0));
        Queen whiteQueen = new Queen(WHITE, new IndexPosition(5, 1));
        King whiteKing = new King(WHITE, new IndexPosition(5, 3));

        pieces[7][0] = blackKing;
        pieces[5][1] = whiteQueen;
        pieces[5][3] = whiteKing;

        assertTrue(drawService.isStalemate(BLACK, pieces));
    }

    @Test
    @DisplayName("Stalemate: player in check is not stalemate")
    void testNotStalemateWhenInCheck() {

        // Black king boxed in and in check = checkmate, not stalemate

        King blackKing = new King(BLACK, new IndexPosition(7, 0));
        Rook whiteRook1 = new Rook(WHITE, new IndexPosition(7, 7));
        Rook whiteRook2 = new Rook(WHITE, new IndexPosition(6, 7));

        pieces[7][0] = blackKing;
        pieces[7][7] = whiteRook1;
        pieces[6][7] = whiteRook2;

        assertFalse(drawService.isStalemate(BLACK, pieces));
    }

    @Test
    @DisplayName("Stalemate: player has legal moves is not stalemate")
    void testNotStalemateWhenHasMoves() {
        King whiteKing = new King(WHITE, new IndexPosition(0, 4));
        pieces[0][4] = whiteKing;

        assertFalse(drawService.isStalemate(WHITE, pieces));
    }

    //Insufficient Material

    @Test
    @DisplayName("Insufficient material: King vs King")
    void testKingVsKing(){
        King whiteKing = new King(WHITE, new IndexPosition(0,4));
        King blackKing = new King(BLACK, new IndexPosition(7,4));

        pieces[0][4] = whiteKing;
        pieces[7][4] = blackKing;

        assertTrue(drawService.isInsufficientMaterial(pieces));
    }

    @Test
    @DisplayName("Insufficient material: King + Bishop vs King")
    void testKingBishopVsKing(){
        King whiteKing = new King(WHITE, new IndexPosition(0,4));
        Bishop whiteBishop = new Bishop(WHITE, new IndexPosition(0,3));
        King blackKing = new King(BLACK, new IndexPosition(7,4));

        pieces[0][4] = whiteKing;
        pieces[0][3] = whiteBishop;
        pieces[7][4] = blackKing;

        assertTrue(drawService.isInsufficientMaterial(pieces));
    }

    @Test
    @DisplayName("Insufficient material: King + Knight vs King")
    void testKingKnightVsKing(){
        King whiteKing = new King(WHITE, new IndexPosition(0,4));
        Knight whiteKnight = new Knight(WHITE, new IndexPosition(0,3));
        King blackKing = new King(BLACK, new IndexPosition(7,4));

        pieces[0][4] = whiteKing;
        pieces[0][3] = whiteKnight;
        pieces[7][4] = blackKing;

        assertTrue(drawService.isInsufficientMaterial(pieces));
    }

    @Test
    @DisplayName("Insufficient material: King + Bishop vs King + Bishop same color squares")
    void testKingBishopVsKingBishopSameColor(){
        King whiteKing = new King(WHITE, new IndexPosition(0,4));
        Bishop whiteBishop = new Bishop(WHITE, new IndexPosition(0,2));
        King blackKing = new King(BLACK, new IndexPosition(7,4));
        Bishop blackBishop = new Bishop(BLACK, new IndexPosition(2,2));

        pieces[0][4] = whiteKing;
        pieces[0][2] = whiteBishop;
        pieces[7][4] = blackKing;
        pieces[2][2] = blackBishop;

        assertTrue(drawService.isInsufficientMaterial(pieces));
    }

    @Test
    @DisplayName("Insufficient material: King + Bishop vs King + Bishop different color squares")
    void testKingBishopVsKingBishopDifferentColor(){
        King whiteKing = new King(WHITE, new IndexPosition(0,4));
        Bishop whiteBishop = new Bishop(WHITE, new IndexPosition(0,2));
        King blackKing = new King(BLACK, new IndexPosition(7,4));
        Bishop blackBishop = new Bishop(BLACK, new IndexPosition(2,3));

        pieces[0][4] = whiteKing;
        pieces[0][2] = whiteBishop;
        pieces[7][4] = blackKing;
        pieces[2][3] = blackBishop;

        assertFalse(drawService.isInsufficientMaterial(pieces));
    }

    @Test
    @DisplayName("Insufficient material: King + Rook vs King")
    void testKingRookVsKing(){
        King whiteKing = new King(WHITE, new IndexPosition(0,4));
        Rook whiteRook = new Rook(WHITE, new IndexPosition(0,0));
        King blackKing = new King(BLACK, new IndexPosition(7,4));

        pieces[0][4] = whiteKing;
        pieces[0][0] = whiteRook;
        pieces[7][4] = blackKing;

        assertFalse(drawService.isInsufficientMaterial(pieces));
    }

    @Test
    @DisplayName("Insufficient material: King + Queen vs King")
    void testKingQueenVsKing(){
        King whiteKing = new King(WHITE, new IndexPosition(0,4));
        Queen whiteQueen = new Queen(WHITE, new IndexPosition(3,3));
        King blackKing = new King(BLACK, new IndexPosition(7,4));

        pieces[0][4] = whiteKing;
        pieces[3][3] = whiteQueen;
        pieces[7][4] = blackKing;

        assertFalse(drawService.isInsufficientMaterial(pieces));
    }

    //50 move rule
    @Test
    @DisplayName("50 move rule: not triggered before 100 half moves")
    void testFiftyMoveRuleNotTriggered(){
        King whiteKing = new King(WHITE, new IndexPosition(0,4));
        Rook whiteRook = new Rook(WHITE, new IndexPosition(3,3));

        for (int i = 0; i < 99; i++) {
            drawService.updateHalfMoveClock(whiteRook, null);
        }

        assertFalse(drawService.isFiftyMoveRule());
    }

    @Test
    @DisplayName("50 move rule: triggered after 100 half moves with no pawn move or capture")
    void testFiftyMoveRuleTriggered(){
        Rook whiteRook = new Rook(WHITE, new IndexPosition(3,3));

        for (int i = 0; i < 100; i++) {
            drawService.updateHalfMoveClock(whiteRook, null);
        }

        assertTrue(drawService.isFiftyMoveRule());
    }

    @Test
    @DisplayName("50-move rule: resets on pawn move")
    void testFiftyMoveRuleResetsOnPawnMove() {
        Rook whiteRook = new Rook(WHITE, new IndexPosition(3, 3));
        Pawn whitePawn = new Pawn(WHITE, new IndexPosition(4, 4));

        for (int i = 0; i < 99; i++) {
            drawService.updateHalfMoveClock(whiteRook, null);
        }
        // pawn move resets the clock
        drawService.updateHalfMoveClock(whitePawn, null);

        assertFalse(drawService.isFiftyMoveRule());
    }

    @Test
    @DisplayName("50-move rule: resets on capture")
    void testFiftyMoveRuleResetsOnCapture() {
        Rook whiteRook = new Rook(WHITE, new IndexPosition(3, 3));
        Rook blackRook = new Rook(BLACK, new IndexPosition(3, 4));

        for (int i = 0; i < 99; i++) {
            drawService.updateHalfMoveClock(whiteRook, null);
        }
        // capture resets the clock
        drawService.updateHalfMoveClock(whiteRook, blackRook);

        assertFalse(drawService.isFiftyMoveRule());
    }

}