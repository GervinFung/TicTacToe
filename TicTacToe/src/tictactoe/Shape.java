package tictactoe;

public final class Shape {

    private char shape;

    protected char getShape() {
        return this.shape;
    }
    public Shape(final char shape) {
        this.shape = shape;
    }
}