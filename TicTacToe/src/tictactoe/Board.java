package tictactoe;

import tictactoe.Shape.Shapes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Board {

    private final int grid;
    private final int size;
    private final List<Tile> tiles;

    public Board(final int grid) {
        this.grid = grid;
        this.size = grid * grid;
        this.tiles = createBoard();
    }

    public Board(final Board board) {
        this.grid = board.grid;
        this.size = board.size;
        this.tiles = board.createBoard(board.tiles);
    }

    protected int getGrid() {
        return this.grid;
    }

    protected int getSize() { return this.size; }

    protected List<Tile> getTiles() { return Collections.unmodifiableList(this.tiles); }

    protected Tile getTileOn(final int index) { return this.tiles.get(index); }

    protected void createShape(final Shape shape, final int index) { this.tiles.set(index, new Tile(shape, index)); }

    private List<Tile> createBoard() {
        final List<Tile> tiles = new ArrayList<>(this.size);
        for (int i = 0; i < this.size; i++) {
            tiles.add(new Tile(i));
        }
        return tiles;
    }

    private List<Tile> createBoard(final List<Tile> previousTiles) {
        final List<Tile> tiles = new ArrayList<>(this.size);
        for (int i = 0; i < this.size; i++) {
            tiles.add(previousTiles.get(i));
        }
        return tiles;
    }

    //check for horizontal win
    private boolean findHorizontalWin(final Shapes shape) {

        int numberOfTilesOccupied = 0;

        for (int i = 0; i < this.size; i++) {

            numberOfTilesOccupied = i % this.grid == 0 ? 0 : numberOfTilesOccupied;

            if (this.tiles.get(i).tileNotOccupied()) { continue; }

            numberOfTilesOccupied = this.tiles.get(i).shapeOnTile().getShape() == shape ? numberOfTilesOccupied + 1 : 0;

            if (numberOfTilesOccupied == this.grid) { return true; }
        }

        return false;
    }
    
    //check for vertical win
    private boolean findVerticalWin(final Shapes shape) {

        final int limit = this.size - 1;

        int numberOfTilesOccupied = 0, begin = 0, max = this.grid * (this.grid - 1) + begin;

        for (int i = begin; i <= max; i += this.grid) {

            if (!this.tiles.get(i).tileNotOccupied()) {
                numberOfTilesOccupied = this.tiles.get(i).shapeOnTile().getShape() == shape ? numberOfTilesOccupied + 1 : 0;

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
    
    private boolean findDiagonalWin(final Shapes shape, final boolean findTopRightBottomLeftWin) {

        int numberOfTilesOccupied = 0;

        final int begin = findTopRightBottomLeftWin ? this.grid - 1 : 0;
        final int max = findTopRightBottomLeftWin ? this.size - 1 : this.size;
        final int increment = findTopRightBottomLeftWin ? this.grid - 1 : this.grid + 1;

        for (int i = begin; i < max; i += increment) {

            if (this.tiles.get(i).tileNotOccupied()) { return false; }

            numberOfTilesOccupied = this.tiles.get(i).shapeOnTile().getShape() == shape ? numberOfTilesOccupied + 1 : 0;
        }

        return numberOfTilesOccupied == this.grid;
    }

    protected boolean isWin(final Shapes shape) {
        return (findDiagonalWin(shape, true) || findDiagonalWin(shape, false) || findHorizontalWin(shape) || findVerticalWin(shape));
    }

    protected boolean isDraw() {
        for (final Tile tile : this.tiles) {
            if (tile.tileNotOccupied()) {
                return false;
            }
        }
        return true;
    }
}