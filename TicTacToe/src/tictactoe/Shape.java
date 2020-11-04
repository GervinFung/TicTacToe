package tictactoe;

public final class Shape {
    // x and y represent the tiles X and O occupied
    private int x, y;
    // shape determines whether X or O occupied the tile
    private char shape;
    protected int getX() {
        return this.x;
    }
    protected int getY() {
        return this.y;
    }
    protected char getShape() {
        return this.shape;
    }
    public Shape(final int x, final int y, final char shape) {
        this.x = x;
        this.y = y;
        this.shape = shape;
    }
}