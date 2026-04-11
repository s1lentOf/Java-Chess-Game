package Services;

import Constants.ColorForChessPieces;

public class MoveResult {
    private Boolean isCheckmate;
    private Boolean isDraw;
    private ColorForChessPieces colorToWin;

    public ColorForChessPieces getColorToWin() {
        return colorToWin;
    }

    public Boolean getCheckmate() {
        return isCheckmate;
    }

    public void setCheckmate(ColorForChessPieces colorToWin) {
        this.colorToWin = colorToWin;
        isCheckmate = true;
    }

    public Boolean getDraw() {
        return isDraw;
    }

    public void setDraw() {
        isDraw = true;
    }
}
