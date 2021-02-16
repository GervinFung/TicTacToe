package tictactoe;

import tictactoe.Shape.Shapes;

import java.util.Objects;

public final class AI {

    private final int tilesNumber;
    private Tile chosenTile;

    public AI(final int tilesNumber) {
        this.tilesNumber = tilesNumber;
    }
    private void setTile(final Tile chosenTile) {
        this.chosenTile = chosenTile;
    }

    protected Tile getChosenTile() {
        return this.chosenTile;
    }

    private int max(final Board board, final int depth, final Shapes shape, int alpha, final int beta) {
        if (board.isWin(shape)) {
            if (shape == Shapes.X) {
                return -10 + depth;
            }
        }
        else if (board.isDraw()) {
            if (shape == Shapes.X) {
                return depth;
            }
        }
        //cpu can search until the end if grid is 3
        if (depth == 0 && this.tilesNumber > 3) {
            return evaluationBoard(board);
        }
        final int grid = this.tilesNumber;
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                if (board.getTileOn(i, j).tileNotOccupied()) {
                    //make move
                    board.createShape(new Shape(Shapes.O, 1), i, j);
                    //calculate score
                    final int score = min((Board) Objects.requireNonNull(board.clone()), depth - 1, Shapes.O, alpha, beta);
                    //undo move
                    board.createShape(null, i, j);
                    bestScore = Math.max(score, bestScore);
                    alpha = Math.max(alpha, bestScore);
                    if (beta <= alpha) {
                        return alpha;
                    }
                }
            }
        }
        return bestScore;
    }

    private int min (final Board board, final int depth, final Shapes shape, final int alpha, int beta) {
        if (board.isWin(shape)) {
            if (shape == Shapes.O) {
                return 10 - depth;
            }
        }
        else if (board.isDraw()) {
            if (shape == Shapes.O) {
                return -depth;
            }
        }
        //cpu can search until the end if grid is 3
        if (depth == 0 && this.tilesNumber > 3) {
            return evaluationBoard(board);
        }
        final int grid = this.tilesNumber;
        int bestScore = Integer.MAX_VALUE;
        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                if (board.getTileOn(i, j).tileNotOccupied()) {
                    //make move
                    board.createShape(new Shape(Shapes.X, -1), i, j);

                    //calculate score
                    final int score = max((Board) Objects.requireNonNull(board.clone()), depth - 1, Shapes.X, alpha, beta);
                    //undo move
                    board.createShape(null, i, j);
                    bestScore = Math.min(score, bestScore);
                    beta = Math.min(beta, bestScore);
                    if (beta <= alpha) {
                        return beta;
                    }
                }
            }
        }
        return bestScore;
    }

    protected void bestMove(final Board board) {
        final int grid = this.tilesNumber;
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                if (board.getTileOn(i, j).tileNotOccupied()) {

                    //make move
                    board.createShape(new Shape(Shapes.O, 1), i, j);

                    final Tile tempTile = (Tile)board.getTileOn(i, j).clone();
                    //calculate score
                    final int score = min((Board) Objects.requireNonNull(board.clone()), 4, Shapes.O, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    //undo move
                    board.createShape(null, i, j);
                    if (score > bestScore) {
                        bestScore = score;
                        this.setTile(tempTile);
                    }
                }
            }
        }
    }

    private int evaluationBoard(final Board board) {
        int totalScore = 0;
        for (int i = 0; i < this.tilesNumber; i++) {
            for (int j = 0; j < this.tilesNumber; j++) {
                final Tile tile = board.getTileOn(i, j);
                if (tile != null) {
                    if (!tile.tileNotOccupied()) {
                        totalScore += tile.shapeOnTile().getScore();
                    }
                }
            }
        }
        return totalScore;
    }
}