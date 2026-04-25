package Services;

import ChessPieces.IndexPosition;
import Constants.ColorForChessPieces;

public class EngineMove {
    private final IndexPosition from;
    private final IndexPosition to;
    private final Character promotion;
    public EngineMove(IndexPosition from, IndexPosition to, Character promotion) {
        this.from = from;
        this.to = to;
        this.promotion = promotion;
    }
    public IndexPosition getFrom() {
        return from;
    }
    public IndexPosition getTo() {
        return to;
    }
    public Character getPromotion() {
        return promotion;
    }
}

