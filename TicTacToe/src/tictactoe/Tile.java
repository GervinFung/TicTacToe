package tictactoe;

public final class Tile {
    // store the shape on the tile
    private Shape shapeOnTile;
    protected boolean tileNotOccupied() {
        return this.shapeOnTile == null;
    }
    protected Shape shapeOnTile() {
        return this.shapeOnTile;
    }
    protected void setShapeOnTile(final Shape shape) {
        this.shapeOnTile = shape;
    }
}
