package ChessBoard;

import javax.swing.*;
import java.awt.*;

public class StartScreen {

    private final JFrame window;

    public StartScreen(JFrame window) {
        this.window = window;
    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(20, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 60));
        return panel;
    }
}
