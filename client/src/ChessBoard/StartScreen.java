package ChessBoard;

import Constants.ColorForChessPieces;
import Game.Game;
import javax.swing.*;
import java.awt.*;

public class StartScreen {

    private final JFrame window;
    private final Game game;

    public StartScreen(JFrame window,Game game) {
        this.window = window;
        this.game = game;
    }

    public void show() {
        JPanel mainPanel = createMainPanel();

        // title
        JLabel titleLabel = new JLabel("Chess");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 52));
        titleLabel.setForeground(new Color(212, 175, 55));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Choose a game mode");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(180, 180, 180));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // buttons
        JButton oneVsOneButton = createButton("1 vs 1");
        JButton vsBotButton = createButton("Play vs Bot");

        oneVsOneButton.addActionListener(e -> startOneVsOne());
        vsBotButton.addActionListener(e -> showBotOptions());

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(subtitleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 50)));
        mainPanel.add(oneVsOneButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(vsBotButton);
        mainPanel.add(Box.createVerticalGlue());

        setWindowContent(mainPanel);
    }

    private void showBotOptions() {
        JPanel mainPanel = createMainPanel();

        JLabel titleLabel = new JLabel("Play vs Bot");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setForeground(new Color(212, 175, 55));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // colour selection
        JLabel colorLabel = new JLabel("Choose your colour");
        colorLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        colorLabel.setForeground(new Color(180, 180, 180));
        colorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel colorPanel = new JPanel();
        colorPanel.setBackground(new Color(20, 20, 20));
        colorPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 0));

        JToggleButton whiteButton = createToggleButton("White");
        JToggleButton blackButton = createToggleButton("Black");

        ButtonGroup colorGroup = new ButtonGroup();
        colorGroup.add(whiteButton);
        colorGroup.add(blackButton);
        whiteButton.setSelected(true); // default to white

        colorPanel.add(whiteButton);
        colorPanel.add(blackButton);

        // difficulty slider
        JLabel difficultyLabel = new JLabel("Difficulty");
        difficultyLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        difficultyLabel.setForeground(new Color(180, 180, 180));
        difficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JSlider difficultySlider = new JSlider(1, 20, 10);
        difficultySlider.setBackground(new Color(20, 20, 20));
        difficultySlider.setForeground(new Color(212, 175, 55));
        difficultySlider.setMajorTickSpacing(5);
        difficultySlider.setMinorTickSpacing(1);
        difficultySlider.setPaintTicks(true);
        difficultySlider.setPaintLabels(true);
        difficultySlider.setMaximumSize(new Dimension(350, 60));
        difficultySlider.setAlignmentX(Component.CENTER_ALIGNMENT);

        // style slider labels to gold
        difficultySlider.setForeground(new Color(212, 175, 55));

        // live difficulty label
        JLabel difficultyValueLabel = new JLabel("Level: " + difficultySlider.getValue());
        difficultyValueLabel.setFont(new Font("Arial", Font.BOLD, 16));
        difficultyValueLabel.setForeground(new Color(212, 175, 55));
        difficultyValueLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        difficultySlider.addChangeListener(e ->
                difficultyValueLabel.setText("Level: " + difficultySlider.getValue())
        );

        // buttons
        JButton startButton = createButton("Start Game");
        JButton backButton = createSecondaryButton("Back");

        startButton.addActionListener(e -> {
            boolean playAsWhite = whiteButton.isSelected();
            int difficulty = difficultySlider.getValue();
            startVsBot(playAsWhite, difficulty);
        });

        backButton.addActionListener(e -> show());

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(colorLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(colorPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(difficultyLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(difficultyValueLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        mainPanel.add(difficultySlider);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(startButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(backButton);
        mainPanel.add(Box.createVerticalGlue());

        setWindowContent(mainPanel);
    }

    private void startOneVsOne() {
        game.startOneVsOne(); // ← delegate to Game
    }

    private void startVsBot(boolean playAsWhite, int difficulty) {
        ColorForChessPieces color = playAsWhite ? ColorForChessPieces.WHITE : ColorForChessPieces.BLACK;
        game.startVsBot(color, difficulty); // ← delegate to Game
    }

    //helpers
    private JPanel createMainPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(20, 20, 20));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 60, 0, 60));
        return panel;
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(212, 175, 55));
        button.setForeground(new Color(20, 20, 20));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(250, 50));
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

    private void setWindowContent(JPanel panel) {
        window.getContentPane().removeAll();
        window.setLayout(new BorderLayout());
        window.add(panel, BorderLayout.CENTER);
        window.revalidate();
        window.repaint();
    }
}
