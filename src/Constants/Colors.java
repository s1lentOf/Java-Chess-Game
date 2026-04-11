package Constants;

import java.awt.*;

// Stores different colors in rgb format
public enum Colors {
    BROWN (240, 217, 181),
    WHITE (181, 136, 99),
    GRAY  (80, 80, 80),;

    private final Color color;

    private Colors(int r, int g, int b) {
        this.color = new Color(r, g, b);
    }

    public Color getColor() { return color; }
}
