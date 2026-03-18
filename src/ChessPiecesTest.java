import org.junit.jupiter.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.awt.image.BufferedImage;

import static org.junit.jupiter.api.Assertions.*;

public class ChessPiecesTest {
    @ParameterizedTest
    @EnumSource(Constants.Piece.class)
    void get_returnsNotNullImageForAllPieces(Constants.Piece piece){
        BufferedImage img = ChessPieces.get(piece);
        assertNotNull(img,"Expected a valid image for " + piece + " but got null. " +
                "Make sure the Base64 string is filled in for this piece.");
    }

    @ParameterizedTest
    @EnumSource(Constants.Piece.class)
    void get_returnedImageHasPositiveDimensions(Constants.Piece piece){
        BufferedImage img = ChessPieces.get(piece);
        assertTrue(img.getWidth() > 0, piece + ": image width should be > 0");
        assertTrue(img.getHeight() > 0, piece + ": image height should be > 0");
    }

    @ParameterizedTest
    @EnumSource(Constants.Piece.class)
    void get_calledTwiceReturnsSameDimensions(Constants.Piece piece){
        BufferedImage first = ChessPieces.get(piece);
        BufferedImage second = ChessPieces.get(piece);
        assertNotNull(first);
        assertNotNull(second);
        assertEquals(first.getWidth(),  second.getWidth(),  piece + ": width mismatch between calls");
        assertEquals(first.getHeight(), second.getHeight(), piece + ": height mismatch between calls");
    }
}
