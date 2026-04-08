package Test;


import org.junit.jupiter.api.*;
import Game.*;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private static Game game;
    private static JFrame window;

    @BeforeAll
    public static void setup() {
        game = new Game();
        window = game.getWindow();
    }

    @Test
    @DisplayName("Window title should be correct")
    public void testWindowTitle() {
        assertEquals("Chess Board", window.getTitle());
    }

    @Test
    @DisplayName("Window size should be 600x600")
    public void testWindowSize() {
        assertEquals(600, window.getWidth());
        assertEquals(600, window.getHeight());
    }

    @Test
    @DisplayName("Window should not be resizable")
    public void testWindowResizable() {
        assertFalse(window.isResizable());
    }

    @Test
    @DisplayName("Default close operation should be EXIT_ON_CLOSE")
    public void testWindowCloseOperation() {
        assertEquals(JFrame.EXIT_ON_CLOSE, window.getDefaultCloseOperation());
    }

}