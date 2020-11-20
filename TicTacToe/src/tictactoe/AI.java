package tictactoe;

import tictactoe.Shape.Shapes;

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

    private int minimax(final Board board, final int depth, final boolean maximizing, final Shapes shape, int alpha, int beta) {
        if (board.isWin(shape)) {
            if (shape == Shapes.O) {
                return 10 - depth;
            }
            else {
                return -10 + depth;
            }
        }
        else if (board.isDraw()) {
            if (shape == Shapes.O) {
                return 0 - depth;
            }
            else {
                return -0 + depth;
            }
        }
        //cpu can search until the end if grid is 3
        if (depth == 0 && this.tilesNumber > 3) {
            return evaluationBoard(board);
        }
        if (maximizing) {
            final int grid = this.tilesNumber;
            int bestScore = Integer.MIN_VALUE;
            for (int i = 0; i < grid; i++) {
                for (int j = 0; j < grid; j++) {
                    if (board.getTileOn(i, j).tileNotOccupied()) {
                        //make move
                        board.getTileOn(i, j).setShapeOnTile(new Shape(Shapes.O, 1));
                        board.getTileOn(i, j).setCoordinate(i, j);
                        //calculate score
                        final int score = minimax((Board)board.clone(), depth - 1, false, Shapes.O, alpha, beta);
                        //undo move
                        board.getTileOn(i, j).setCoordinate(-1, -1);
                        board.getTileOn(i, j).setShapeOnTile(null);
                        bestScore = Math.max(score, bestScore);
                        alpha = Math.max(alpha, bestScore);
                        if (beta <= alpha) {
                            return alpha;
                        }
                    }
                }
            }
            return bestScore;
        } else {
            final int grid = this.tilesNumber;
            int bestScore = Integer.MAX_VALUE;
            for (int i = 0; i < grid; i++) {
                for (int j = 0; j < grid; j++) {
                    if (board.getTileOn(i, j).tileNotOccupied()) {
                        //make move
                        board.getTileOn(i, j).setShapeOnTile(new Shape(Shapes.X, -1));
                        board.getTileOn(i, j).setCoordinate(i, j);

                        //calculate score
                        final int score = minimax((Board)board.clone(), depth - 1, true, Shapes.X, alpha, beta);
                        //undo move
                        board.getTileOn(i, j).setCoordinate(-1, -1);
                        board.getTileOn(i, j).setShapeOnTile(null);
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
    }

    protected void bestMove(final Board board) {
        final int grid = this.tilesNumber;
        int bestScore = Integer.MIN_VALUE;
        for (int i = 0; i < grid; i++) {
            for (int j = 0; j < grid; j++) {
                if (board.getTileOn(i, j).tileNotOccupied()) {

                    //make move
                    board.getTileOn(i, j).setShapeOnTile(new Shape(Shapes.O, 1));
                    board.getTileOn(i, j).setCoordinate(i, j);

                    final Tile tempTile = (Tile)board.getTileOn(i, j).clone();
                    //calculate score
                    final int score = minimax((Board)board.clone(), 4, false, Shapes.O, Integer.MIN_VALUE, Integer.MAX_VALUE);
                    //undo move
                    board.getTileOn(i, j).setCoordinate(-1, -1);
                    board.getTileOn(i, j).setShapeOnTile(null);
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