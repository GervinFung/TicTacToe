package tictactoe;

public final class Shape {

    private final Shapes shape;
    private final int score;
    public enum Shapes{
        X,O
    }

    protected Shapes getShape() {
        return this.shape;
    }
    protected int getScore() {
        return this.score;
    }
    public Shape(final Shapes shape, final int score) {
        this.shape = shape;
        this.score = score;
    }
}