package engine.board;

import engine.piece.Piece;

public abstract class Tile {
    private final int index;

    public static Tile createTile(final Piece piece, final int index) {
        return piece == null ? new EmptyTile(index) : new OccupiedTile(piece, index);
    }

    private Tile(final int index) { this.index = index; }
    public final int getIndex() { return this.index; }

    public abstract boolean tileNotOccupied();
    public abstract Piece getPiece();

    private static final class OccupiedTile extends Tile {
        private final Piece piece;
        private OccupiedTile(final Piece piece, final int index) {
            super(index);
            this.piece = piece;
        }
        @Override
        public boolean tileNotOccupied() { return false; }
        @Override
        public Piece getPiece() { return this.piece; }
        @Override
        public String toString() { return this.piece.toString(); }
    }

    private static final class EmptyTile extends Tile {
        private EmptyTile(final int index) { super(index); }
        @Override
        public boolean tileNotOccupied() { return true; }
        @Override
        public Piece getPiece() { return null; }
        @Override
        public String toString() { return "-"; }
    }
}