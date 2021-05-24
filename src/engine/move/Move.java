package engine.move;

import engine.board.Board;
import engine.board.Tile;
import engine.piece.League;
import engine.piece.Piece;

import static engine.board.Board.BoardBuilder;

public class Move {

    private final Board board;
    private final Piece piece;

    public Move(final Board board, final League league, final int index) {
        this.board = board;
        this.piece = league == null ? null : Piece.createPiece(league, index);
    }

    public final Board execute() {
        final BoardBuilder builder = new BoardBuilder(this.board.getCurrentPlayer().getOpponent().getLeague() ,this.board.getGrid());
        for (final Tile tile : this.board.getTileList()) {
            builder.addTile(tile);
        }
        builder.addPiece(this.piece);
        return builder.build();
    }

    public final int getIndex() { return this.piece.getIndex(); }

    @Override
    public final int hashCode() { return this.piece.hashCode() * 31; }

    @Override
    public final String toString() { return this.piece.toString(); }

    @Override
    public final boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Move) || object instanceof NullMove) {
            return false;
        }
        final Move move = (Move)object;
        return move.piece.equals(this.piece);
    }

    public static final class NullMove extends Move {
        public final static NullMove NULL_MOVE = new NullMove();
        private NullMove() { super(null, null, -1); }
    }
}