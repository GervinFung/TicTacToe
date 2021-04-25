package tictactoe;

import tictactoe.Shape.Shapes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class AI {

    private int max(final Board board, final int depth, final int highest, final int lowest) {
        if (board.isWin(Shapes.X)) {
            return -10 + depth;
        }
        else if (board.isDraw()) {
            return depth;
        }
        //cpu can search until the end if grid is 3
        if (depth == 0 && board.getGrid() > 3) {
            return evaluationBoard(board);
        }

        int currentHighest = highest;

        for (final Tile tile : board.getTiles()) {
            if (tile.tileNotOccupied()) {
                final Board tempBoard = new Board(board);
                //make move
                tempBoard.createShape(new Shape(Shapes.O), tile.getIndex());
                currentHighest = Math.max(min(tempBoard, depth - 1, currentHighest, lowest), currentHighest);
                if (currentHighest >= lowest) {
                    return lowest;
                }
            }
        }

        return currentHighest;
    }

    private int min(final Board board, final int depth, final int highest, final int lowest) {
        if (board.isWin(Shapes.O)) {
            return 10 - depth;
        }
        else if (board.isDraw()) {
            return -depth;
        }
        //cpu can search until the end if grid is 3
        if (depth == 0 && board.getGrid() > 3) {
            return evaluationBoard(board);
        }

        int currentLowest = lowest;

        for (final Tile tile : board.getTiles()) {
            if (tile.tileNotOccupied()) {
                final Board tempBoard = new Board(board);
                //make move
                tempBoard.createShape(new Shape(Shapes.X), tile.getIndex());
                //calculate score
                currentLowest = Math.min(max(tempBoard, depth - 1, highest, currentLowest), currentLowest);
                if (currentLowest <= highest) {
                    return highest;
                }
            }
        }

        return currentLowest;
    }

    protected Tile bestMove(final Board board) {

        final AtomicInteger bestScore = new AtomicInteger(Integer.MIN_VALUE);

        final AtomicReference<Tile> chosenTile = new AtomicReference<>(null);

        final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        for (final Tile tile : board.getTiles()) {

            if (tile.tileNotOccupied()) {

                executorService.execute(() -> {
                    final Board tempBoard = new Board(board);
                    //make move
                    tempBoard.createShape(new Shape(Shapes.O), tile.getIndex());

                    final int score = min(tempBoard, 4, Integer.MIN_VALUE, Integer.MAX_VALUE);

                    if (score > bestScore.get()) {
                        bestScore.set(score);
                        chosenTile.set(tempBoard.getTileOn(tile.getIndex()));
                    }
                });
            }
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }

        return chosenTile.get();
    }

    private int evaluationBoard(final Board board) {
        int totalScore = 0;
        for (int i = 0; i < board.getSize(); i++) {
            final Tile tile = board.getTileOn(i);
            if (tile != null) {
                if (!tile.tileNotOccupied()) {
                    totalScore += tile.shapeOnTile().getScore();
                }
            }
        }
        return totalScore;
    }
}