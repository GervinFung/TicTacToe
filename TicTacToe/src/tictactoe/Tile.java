package tictactoe;

public final class Tile {
    // store the shape on the tile
    private final Shape shapeOnTile;
    private final int index;
    public Tile(final Shape shape, final int index) {
        this.shapeOnTile = shape;
        this.index = index;
    }
    public Tile(final int index) { this(null, index); }

    protected boolean tileNotOccupied() { return this.shapeOnTile == null; }
    protected Shape shapeOnTile() { return this.shapeOnTile; }

    protected int getIndex() { return this.index; }
}