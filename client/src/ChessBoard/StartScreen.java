package ChessBoard;

import javax.swing.*;
import java.awt.*;

public class StartScreen {
    private final JFrame window;

    public StartScreen(JFrame window) {
        this.window = window;
    }

    public void show(){

    }

    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(255, 255, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 60));
        return panel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(212, 175, 55));
        button.setForeground(new Color(239, 239, 239));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(200, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addHoverEffect(button, new Color(212, 175, 55), new Color(180, 145, 30));
        return button;
    }

    private JButton createSecondaryButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(60, 60, 60));
        button.setForeground(new Color(212, 175, 55));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 50));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        addHoverEffect(button, new Color(60, 60, 60), new Color(80, 80, 80));
        return button;
    }

    private void addHoverEffect(JButton button, Color normalColor, Color hoverColor) {
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(normalColor);
            }
        });
    }

    private JToggleButton createToggleButton(String text) {
        JToggleButton button = new JToggleButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setBackground(new Color(60, 60, 60));
        button.setForeground(new Color(212, 175, 55));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setPreferredSize(new Dimension(120, 45));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // highlight when selected
        button.addChangeListener(e -> {
            if (button.isSelected()) {
                button.setBackground(new Color(212, 175, 55));
                button.setForeground(new Color(20, 20, 20));
            } else {
                button.setBackground(new Color(60, 60, 60));
                button.setForeground(new Color(212, 175, 55));
            }
        });
        return button;
    }

    private void setWindowContent(JPanel panel) {
        window.getContentPane().removeAll();
        window.setLayout(new BorderLayout());
        window.add(panel, BorderLayout.CENTER);
        window.revalidate();
        window.repaint();
    }
}
