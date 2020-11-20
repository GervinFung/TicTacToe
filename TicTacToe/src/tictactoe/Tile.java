package tictactoe;

public final class Tile implements Cloneable{
    // store the shape on the tile
    private Shape shapeOnTile;
    private int x, y;
    protected boolean tileNotOccupied() {
        return this.shapeOnTile == null;
    }
    protected Shape shapeOnTile() {
        return this.shapeOnTile;
    }
    protected void setShapeOnTile(final Shape shape) {
        this.shapeOnTile = shape;
    }
    protected int getX() {
        return this.x;
    }
    protected int getY() {
        return this.y;
    }
    protected void setCoordinate(final int x, final int y) {
        this.x = x;
        this.y = y;
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
