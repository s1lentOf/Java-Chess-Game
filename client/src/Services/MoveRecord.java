package Services;

import ChessPieces.IndexPosition;

public class MoveRecord {
    private String pieceName;
    private IndexPosition movedFrom;
    private IndexPosition movedTo;
    private Character promotionPiece;

    public MoveRecord(String pieceName, IndexPosition movedFrom, IndexPosition movedTo, Character promotionPiece) {
        this.pieceName = pieceName;
        this.movedFrom = movedFrom;
        this.movedTo = movedTo;
        this.promotionPiece = promotionPiece;
    }
    public String getPieceName() {
        return pieceName;
    }
    public IndexPosition getMovedFrom() {
        return movedFrom;
    }
    public IndexPosition getMovedTo() {
        return movedTo;
    }
    public void setPieceName(String pieceName) {
        this.pieceName = pieceName;
    }
    public void setMovedFrom(IndexPosition movedFrom) {
        this.movedFrom = movedFrom;
    }
    public void setMovedTo(IndexPosition movedTo) {
        this.movedTo = movedTo;
    }
    public Character getPromotionPiece() {return promotionPiece;}



}
