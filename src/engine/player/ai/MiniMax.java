package engine.player.ai;

import engine.board.Board;
import engine.move.Move;
import engine.player.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class MiniMax {

    private final Evaluator evaluator;
    private final int thread, depth;

    public MiniMax(final int depth) {
        this.thread = Runtime.getRuntime().availableProcessors();
        if (this.thread > 4) {
            this.depth = depth + 1;
        } else {
            this.depth = depth;
        }
        this.evaluator = new Evaluator.StandardEvaluator();
    }

    public Move execute(final Board board) {
        final Player currentPlayer = board.getCurrentPlayer();
        final AtomicInteger highestSeenValue = new AtomicInteger(Integer.MIN_VALUE);
        final AtomicInteger lowestSeenValue = new AtomicInteger(Integer.MAX_VALUE);
        final AtomicInteger currentValue = new AtomicInteger(0);

        final AtomicReference<Move> bestMove = new AtomicReference<>(Move.NullMove.NULL_MOVE);

        final ExecutorService executorService = Executors.newFixedThreadPool(this.thread);

        for (final Move move : currentPlayer.getLegalMoves()) {
            final Board latestBoard = currentPlayer.makeMove(move);
            if (latestBoard.isWin() || latestBoard.isDraw()) {
                return move;
            }
            executorService.execute(() -> {
                    final int currentVal = currentPlayer.getLeague().isCross() ?
                            min(latestBoard, this.depth - 1, highestSeenValue.get(), lowestSeenValue.get()) :
                            max(latestBoard, this.depth - 1, highestSeenValue.get(), lowestSeenValue.get());

                    currentValue.set(currentVal);
                    if (currentPlayer.getLeague().isCross() && currentValue.get() > highestSeenValue.get()) {
                        highestSeenValue.set(currentValue.get());
                        bestMove.set(move);
                    }
                    else if (currentPlayer.getLeague().isNought() && currentValue.get() < lowestSeenValue.get()) {
                        lowestSeenValue.set(currentValue.get());
                        bestMove.set(move);
                    }
                }
            );
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
        return bestMove.get();
    }

    private int max(final Board board, final int depth, final int highest, final int lowest) {
        if (board.isWin()) {
            return depth - 10;
        }
        if (board.isDraw()) {
            return depth;
        }
        if (board.getGrid() > 3 && depth == 0) {
            return evaluator.evaluate(board, depth, this.depth);
        }
        int currentHighest = highest;
        final Player currentPlayer = board.getCurrentPlayer();
        for (final Move move : currentPlayer.getLegalMoves()) {
            final Board latestBoard = currentPlayer.makeMove(move);
            if (board.getGrid() > 3 && (latestBoard.isWin() || latestBoard.isDraw())) { return evaluator.evaluate(board, depth, this.depth); }
            currentHighest = Math.max(currentHighest, min(latestBoard, depth - 1, currentHighest, lowest));
            if (currentHighest >= lowest) {
                return lowest;
            }
        }
        return currentHighest;
    }

    private int min(final Board board, final int depth, final int highest, final int lowest) {
        if (board.isWin()) {
            return 10 - depth;
        }
        if (board.isDraw()) {
            return -depth;
        }
        if (board.getGrid() > 3 && depth == 0) {
            return evaluator.evaluate(board, depth, this.depth);
        }
        int currentLowest = lowest;
        final Player currentPlayer = board.getCurrentPlayer();
        for (final Move move : currentPlayer.getLegalMoves()) {
            final Board latestBoard = currentPlayer.makeMove(move);
            if (board.getGrid() > 3 && (latestBoard.isWin() || latestBoard.isDraw())) { return evaluator.evaluate(board, depth, this.depth); }
            currentLowest = Math.min(currentLowest, max(latestBoard, depth - 1, highest, currentLowest));
            if (currentLowest <= highest) {
                return highest;
            }
        }
        return currentLowest;
    }
}