package engine.player;

import engine.board.Board;
import engine.board.Tile;
import engine.move.Move;
import engine.piece.League;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Player {

    private final List<Move> legalMoves;
    private final Board board;

    private Player(final Board board) {
        this.board = board;
        this.legalMoves = this.generateLegalMoves();
    }

    public final Board getBoard() { return this.board; }
    public final List<Move> getLegalMoves() { return this.legalMoves; }
    private List<Move> generateLegalMoves() {
        final List<Move> legalMoves = new ArrayList<>();
        for (final Tile tile : this.board.getTileList()) {
            if (tile.tileNotOccupied()) {
                legalMoves.add(new Move(this.board, this.getLeague(), tile.getIndex()));
            }
        }
        return Collections.unmodifiableList(legalMoves);
    }

    public final Board makeMove(final Move move) {
        if (move == null) {
            throw new IllegalArgumentException("Move cannot be null");
        } else if (move.equals(Move.NullMove.NULL_MOVE)) {
            throw new IllegalArgumentException("Move cannot be null move");
        }
        if (this.legalMoves.contains(move)) {
            return move.execute();
        }
        throw new IllegalArgumentException("Move is not within legal move list");
    }

    @Override
    public final String toString() { return this.getLeague().toString(); }

    public abstract Player getOpponent();
    public abstract League getLeague();

    public static final class CrossPlayer extends Player {
        public CrossPlayer(final Board board) { super(board); }
        @Override
        public Player getOpponent() { return super.getBoard().getNoughtPlayer(); }
        @Override
        public League getLeague() { return League.Cross; }
    }

    public static final class NoughtPlayer extends Player {
        public NoughtPlayer(final Board board) { super(board); }
        @Override
        public Player getOpponent() { return super.getBoard().getCrossPlayer(); }
        @Override
        public League getLeague() { return League.Nought; }
    }
}