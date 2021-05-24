package engine.player.ai;

import engine.board.Board;
import engine.move.Move;
import engine.player.Player;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public final class MiniMax {

    private final static Evaluator EVALUATOR = new Evaluator();
    private final int thread, depth;

    public MiniMax(final int depth) {
        this.thread = Runtime.getRuntime().availableProcessors();
        if (this.thread > 4) {
            this.depth = depth + 1;
        } else {
            this.depth = depth;
        }
    }

    public Move execute(final Board board) {
        final Player currentPlayer = board.getCurrentPlayer();
        final AtomicInteger highestSeenValue = new AtomicInteger(Integer.MIN_VALUE);
        final AtomicInteger lowestSeenValue = new AtomicInteger(Integer.MAX_VALUE);
        final AtomicInteger currentValue = new AtomicInteger(0);

        final AtomicBoolean isEndGame = new AtomicBoolean(false);

        final AtomicReference<Move> bestMove = new AtomicReference<>(Move.NullMove.NULL_MOVE);

        final ExecutorService executorService = Executors.newFixedThreadPool(this.thread);

        for (final Move move : currentPlayer.getLegalMoves()) {
            final Board latestBoard = currentPlayer.makeMove(move);
            if (isEndGame.get()) {
                break;
            }
            executorService.execute(() -> {
                    final int currentVal = currentPlayer.getLeague().isCross() ?
                            min(latestBoard, this.depth - 1, highestSeenValue.get(), lowestSeenValue.get()) :
                            max(latestBoard, this.depth - 1, highestSeenValue.get(), lowestSeenValue.get());

                    currentValue.set(currentVal);
                    if (currentPlayer.getLeague().isCross() && currentValue.get() > highestSeenValue.get()) {
                        highestSeenValue.set(currentValue.get());
                        bestMove.set(move);
                        if(latestBoard.isWin(latestBoard.getNoughtPlayer().getLeague())) {
                            isEndGame.set(true);
                        }
                    }
                    else if (currentPlayer.getLeague().isNought() && currentValue.get() < lowestSeenValue.get()) {
                        lowestSeenValue.set(currentValue.get());
                        bestMove.set(move);
                        if(latestBoard.isWin(latestBoard.getCrossPlayer().getLeague())) {
                            isEndGame.set(true);
                        }
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
        else if (board.isDraw()) {
            return depth;
        }
        if (depth == 0 && board.getGrid() > 3) {
            return EVALUATOR.evaluate(board, depth);
        }
        int currentHighest = highest;
        final Player currentPlayer = board.getCurrentPlayer();
        for (final Move move : currentPlayer.getLegalMoves()) {
            final Board latestBoard = currentPlayer.makeMove(move);
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
        else if (board.isDraw()) {
            return -depth;
        }
        if (depth == 0 && board.getGrid() > 3) {
            return EVALUATOR.evaluate(board, depth);
        }
        int currentLowest = lowest;
        final Player currentPlayer = board.getCurrentPlayer();
        for (final Move move : currentPlayer.getLegalMoves()) {
            final Board latestBoard = currentPlayer.makeMove(move);
            currentLowest = Math.min(currentLowest, max(latestBoard, depth - 1, highest, currentLowest));
            if (currentLowest <= highest) {
                return highest;
            }
        }
        return currentLowest;
    }
}