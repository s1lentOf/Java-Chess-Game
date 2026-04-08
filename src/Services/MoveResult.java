package Services;

public class MoveResult {
    private Boolean isCheckmate;
    private Boolean isDraw;

    public Boolean getCheckmate() {
        return isCheckmate;
    }

    public void setCheckmate() {
        isCheckmate = true;
    }

    public Boolean getDraw() {
        return isDraw;
    }

    public void setDraw() {
        isDraw = true;
    }
}
