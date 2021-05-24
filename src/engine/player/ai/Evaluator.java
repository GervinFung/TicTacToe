package engine.player.ai;

import engine.board.Board;
import engine.board.Tile;

public final class Evaluator {
    private final static int MAX = 100000;

    public int evaluate(final Board board, final int depth) {
        int totalScore = 0;
        for (final Tile tile : board.getTileList()) {
            if (tile != null) {
                if (!tile.tileNotOccupied()) {
                    totalScore += tile.getPiece().getScore();
                }
            }
        }
        return totalScore;
    }
}