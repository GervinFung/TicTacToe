package engine.board;

import engine.piece.Piece;
import engine.piece.League;
import engine.player.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static engine.player.Player.*;

public final class Board {

    private final List<Tile> tileList;
    private final CrossPlayer crossPlayer;
    private final NoughtPlayer noughtPlayer;
    private final Player currentPlayer;
    private final int grid;
    private final int size;

    private Board(final BoardBuilder builder) {
        this.tileList = builder.tileList;
        this.grid = builder.grid;
        this.size = builder.size;
        this.crossPlayer = new CrossPlayer(this);
        this.noughtPlayer = new NoughtPlayer(this);
        this.currentPlayer = builder.league.nextPlayer(this.crossPlayer, this.noughtPlayer);
    }

    public CrossPlayer getCrossPlayer() { return this.crossPlayer; }
    public NoughtPlayer getNoughtPlayer() { return this.noughtPlayer; }
    public List<Tile> getTileList() { return Collections.unmodifiableList(this.tileList); }
    public Player getCurrentPlayer() { return this.currentPlayer; }
    public int getGrid() { return this.grid; }
    public int getSize() { return this.size; }

    public static Board createStandardBoard(final int grid) {
        final BoardBuilder builder = new BoardBuilder(League.Cross, grid);
        for (int i = 0; i < builder.size; i++) {
            builder.addTile(Tile.createTile(null, i));
        }
        return builder.build();
    }

    private boolean findHorizontalWin(final League league) {

        int numberOfTilesOccupied = 0;

        for (int i = 0; i < this.size; i++) {

            final Tile tile = this.tileList.get(i);

            numberOfTilesOccupied = i % this.grid == 0 ? 0 : numberOfTilesOccupied;

            if (tile.tileNotOccupied()) { continue; }

            numberOfTilesOccupied = tile.getPiece().getLeague() == league ? numberOfTilesOccupied + 1 : 0;

            if (numberOfTilesOccupied == this.grid) { return true; }
        }

        return false;
    }

    //check for vertical win
    private boolean findVerticalWin(final League league) {

        final int limit = this.size - 1;

        int numberOfTilesOccupied = 0, begin = 0, max = this.grid * (this.grid - 1) + begin;

        for (int i = begin; i <= max; i += this.grid) {

            final Tile tile = this.tileList.get(i);

            if (!tile.tileNotOccupied()) {
                numberOfTilesOccupied = tile.getPiece().getLeague() == league ? numberOfTilesOccupied + 1 : 0;

                if (numberOfTilesOccupied == this.grid) { return true; }
            }

            if (i == max && max < limit) {
                begin++;
                i = begin - this.grid;
                max = this.grid * (this.grid - 1) + begin;
                numberOfTilesOccupied = 0;
            }
        }

        return false;
    }

    private boolean findDiagonalWin(final League league, final boolean findPositiveSlope) {

        int numberOfTilesOccupied = 0;

        final int begin = findPositiveSlope ? this.grid - 1 : 0;
        final int max = findPositiveSlope ? this.size - 1 : this.size;
        final int increment = findPositiveSlope ? this.grid - 1 : this.grid + 1;

        for (int i = begin; i < max; i += increment) {

            final Tile tile = this.tileList.get(i);

            if (tile.tileNotOccupied()) { return false; }

            numberOfTilesOccupied = tile.getPiece().getLeague() == league ? numberOfTilesOccupied + 1 : 0;
        }

        return numberOfTilesOccupied == this.grid;
    }

    public boolean isWin(final League league) {
        return (findDiagonalWin(league, true) || findDiagonalWin(league, false) || findHorizontalWin(league) || findVerticalWin(league));
    }

    public boolean isWin() { return this.isWin(this.currentPlayer.getOpponent().getLeague()); }

    public boolean isDraw() { return this.tileList.stream().noneMatch(Tile::tileNotOccupied); }

    @Override
    public String toString() {
        final StringBuilder stringBuilder = new StringBuilder();
        for (final Tile tile : this.tileList) {
            if (tile.getIndex() > 0 && tile.getIndex() % this.grid == 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(tile).append("(").append(tile.getIndex()).append(")").append("\t");
        }
        return stringBuilder.toString();
    }

    public static final class BoardBuilder {

        private final List<Tile> tileList;
        private final League league;
        private final int grid, size;

        public BoardBuilder(final League league, final int grid) {
            this.tileList = new ArrayList<>(grid * grid);
            this.league = league;
            this.grid = grid;
            this.size = grid * grid;
        }

        public BoardBuilder addTile(final Tile tile) {
            this.tileList.add(tile);
            return this;
        }

        public BoardBuilder addPiece(final Piece piece) {
            this.tileList.set(piece.getIndex(), Tile.createTile(piece, piece.getIndex()));
            return this;
        }

        public Board build() { return new Board(this); }
    }
}