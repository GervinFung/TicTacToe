package engine.player.ai;

import engine.board.Board;
import engine.board.Tile;
import engine.piece.League;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Evaluator {

    public static final class StandardEvaluator extends Evaluator {
        @Override
        public int evaluate(final Board board, final int depth, final int searchDepth) {
            if (depth == 0) {
                return evaluateBoard(board, board.getCrossPlayer().getLeague(), board.getNoughtPlayer().getLeague(), board.getGrid());
            }
            return evaluateBoard(board, board.getCrossPlayer().getLeague(), board.getNoughtPlayer().getLeague(), board.getGrid()) * (searchDepth - depth);
        }
    }

    public abstract int evaluate(final Board board, final int depth, final int searchDepth);

    private static int evaluateBoard(final Board board, final League myLeague, final League enemyLeague, final int grid) {
        final int verticalScore = calculateStraightRowScore(getAllVerticalRow(board.getTileList(), board.getSize(), board.getGrid()), myLeague, enemyLeague, grid);
        final int horizontalScore = calculateStraightRowScore(getAllHorizontalRow(board.getTileList(), board.getSize(), board.getGrid()), myLeague, enemyLeague, grid);
        final int positiveSlopeScore = calculateEachRowScore(getAllDiagonalRow(board.getTileList(), board.getSize(), board.getGrid(), true), myLeague, enemyLeague, grid);
        final int negativeSlopeScore = calculateEachRowScore(getAllDiagonalRow(board.getTileList(), board.getSize(), board.getGrid(), false), myLeague, enemyLeague, grid);
        return verticalScore + horizontalScore + positiveSlopeScore + negativeSlopeScore;
    }

    private static int computeScore(final int enemyTile, final int myTile, final int emptyTile, final int grid) {

        if (grid % 2 == 0) {
            if (myTile == grid / 2 && emptyTile == grid / 2) { return 10000; }
            if (enemyTile == grid / 2 && emptyTile == grid / 2) { return -10000; }
        } else {
            if (myTile == grid / 2 && emptyTile == grid / 2 + 1) { return 150; }
            if (myTile == grid / 2 + 1 && emptyTile == grid / 2) { return 100; }
            if (enemyTile == grid / 2 + 1 && emptyTile == grid / 2) { return -100; }
            if (enemyTile == grid / 2 && emptyTile == grid / 2 + 1) { return -150; }
        }

        if (myTile == grid - 1 && enemyTile == 1) { return -100; }
        if (myTile == grid - 1 && emptyTile == 1) { return 100000; }
        if (myTile == grid) { return 20000000; }

        if (enemyTile == grid) { return -20000000; }
        if (enemyTile == grid - 1 && emptyTile == 1) { return -10000000; }
        if (enemyTile == grid - 1 && myTile == 1) { return 100; }

        if (enemyTile > myTile) { return -100; }
        if (myTile > enemyTile) { return 100; }

        return 0;
    }

    private static int calculateStraightRowScore(final List<List<Tile>> straight, final League myLeague, final League enemyLeague, final int grid) {
        return straight.stream().mapToInt(tileList -> calculateEachRowScore(tileList, myLeague, enemyLeague, grid)).sum();
    }

    private static int calculateEachRowScore(final List<Tile> tileList, final League myLeague, final League enemyLeague, final int grid) {
        int enemyTile = 0, myTile = 0, emptyTile = 0;
        for (final Tile tile : tileList) {
            if (tile.tileNotOccupied()) {
                emptyTile++;
            } else {
                if (tile.getPiece().getLeague() == myLeague) {
                    myTile++;
                } else if (tile.getPiece().getLeague() == enemyLeague) {
                    enemyTile++;
                }
            }
        }
        return computeScore(enemyTile, myTile, emptyTile, grid);
    }

    private static List<List<Tile>> getAllHorizontalRow (final List<Tile> tileList, final int size, final int grid) {
        final List<List<Tile>> tilesList = new ArrayList<>();
        List<Tile> tiles = new ArrayList<>();

        for (int i = 1; i <= size; i++) {
            tiles.add(tileList.get(i - 1));
            if (i % grid == 0) {
                tilesList.add(tiles);
                tiles = new ArrayList<>();
            }
        }

        return Collections.unmodifiableList(tilesList);
    }

    private static List<List<Tile>> getAllVerticalRow (final List<Tile> tileList, final int size, final int grid) {
        final List<List<Tile>> tilesList = new ArrayList<>();
        List<Tile> tiles = new ArrayList<>();

        final int limit = size - 1;
        int begin = 0, max = grid * (grid - 1) + begin;

        for (int i = begin; i <= max; i += grid) {
            tiles.add(tileList.get(i));
            if (i == max) {
                tilesList.add(tiles);
                tiles = new ArrayList<>();
                if (max < limit) {
                    begin++;
                    i = begin - grid;
                    max = grid * (grid - 1) + begin;
                }
            }
        }

        return Collections.unmodifiableList(tilesList);
    }

    private static List<Tile> getAllDiagonalRow (final List<Tile> tileList, final int size, final int grid, final boolean positiveSlope) {
        final List<Tile> tiles = new ArrayList<>();

        final int begin = positiveSlope ? grid - 1 : 0;
        final int max = positiveSlope ? size - 1 : size;
        final int increment = positiveSlope ? grid - 1 : grid + 1;

        for (int i = begin; i < max; i += increment) { tiles.add(tileList.get(i)); }

        return Collections.unmodifiableList(tiles);
    }
}