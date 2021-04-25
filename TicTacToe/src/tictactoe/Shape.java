package tictactoe;

public final class Shape {

    private final Shapes shape;
    public enum Shapes{
        X {
            @Override
            int getScore() { return -1; }
            @Override
            boolean isX() { return true; }
        },O {
            @Override
            int getScore() { return 1; }
            @Override
            boolean isX() { return false; }
        };
        abstract int getScore();
        abstract boolean isX();
    }

    protected Shapes getShape() { return this.shape; }
    protected int getScore() { return this.shape.getScore(); }
    public Shape(final Shapes shape) {
        this.shape = shape;
    }
}