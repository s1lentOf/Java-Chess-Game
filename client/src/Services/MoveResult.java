package Services;

import Constants.ColorForChessPieces;

public class MoveResult {
    private Boolean isCheckmate = false;
    private Boolean isDraw = false;
    private ColorForChessPieces colorToWin;
    private String drawReason;

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

    public String getDrawReason() {
        return drawReason;
    }

    public void setDraw(String reason) {
        isDraw = true;
        drawReason = reason;
    }

    public void reset(){
        isCheckmate = false;
        isDraw = false;
        colorToWin = null;
        drawReason = null;
    }
}
