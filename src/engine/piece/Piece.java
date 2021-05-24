package engine.piece;

public abstract class Piece {
    private final League league;
    private final int index;
    private Piece(final League league, final int index) {
        this.league = league;
        this.index = index;
    }
    public final int getIndex() { return this.index; }
    public final int getScore() { return this.league.getScore(); }
    public final League getLeague() { return this.league; }
    public static Piece createPiece(final League league, final int index) { return league.isCross() ? new CrossPiece(index) : new NoughtPiece(index); }
    @Override
    public final String toString() { return this.getLeague().toString(); }
    @Override
    public final int hashCode() { return (this.league.hashCode() + this.index) * 31; }
    @Override
    public final boolean equals(final Object object) {
        if (this == object) { return true; }
        if (!(object instanceof Piece)) { return false; }
        final Piece piece = (Piece)object;
        return piece.hashCode() == this.hashCode() && piece.index == this.index && piece.getScore() == piece.getScore();
    }

    public static final class CrossPiece extends Piece { private CrossPiece(final int index) { super(League.Cross, index); }}
    public static final class NoughtPiece extends Piece { private NoughtPiece(final int index) { super(League.Nought, index); }}
}