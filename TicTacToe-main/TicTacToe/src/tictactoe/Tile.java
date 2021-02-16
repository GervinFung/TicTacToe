package tictactoe;

public final class Tile implements Cloneable{
    // store the shape on the tile
    private final Shape shapeOnTile;
    private final int x, y;
    public Tile(final Shape shape, final int x, final int y) {
        this.shapeOnTile = shape;
        this.x = x;
        this.y = y;
    }
    public Tile() {
        this.shapeOnTile = null;
        this.x = this.y = -1;
    }
    protected boolean tileNotOccupied() {
        return this.shapeOnTile == null;
    }
    protected Shape shapeOnTile() {
        return this.shapeOnTile;
    }

    protected int getX() {
        return this.x;
    }
    protected int getY() {
        return this.y;
    }

    @Override
    public Object clone(){
        try {
            return super.clone();
        } catch (final CloneNotSupportedException e) {
            return null;
        }
    }
}
