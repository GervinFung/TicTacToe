package tictactoe;

import tictactoe.Shape.Shapes;

public final class Board implements Cloneable {
    private int grid;
    private Tile[][] tiles;

    protected void setGrid(final int grid) {
        this.grid = grid;
    }
    protected int getGrid() {
        return this.grid;
    }

    protected Tile[][] getTiles() {
        return tiles;
    }
    protected Tile getTileOn(final int x, final int y) {
        return tiles[y][x];
    }
    protected void createBoard() {
        tiles = new Tile[this.grid][this.grid];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                tiles[i][j] = new Tile();
            }
        }
    }

    //check for horizontal win
    private boolean findHorizontalWin(final Shapes shape) {
        for (int i = 0; i < tiles.length; i++) {
            int numberOfTilesOccupied = 0;
            for (int k = 1; k < tiles[0].length; k++) {
                if (!tiles[i][k - 1].tileNotOccupied() && !tiles[i][k].tileNotOccupied()) {
                    if (tiles[i][k - 1].shapeOnTile().getShape() == tiles[i][k].shapeOnTile().getShape()
                    && tiles[i][k].shapeOnTile().getShape() == shape) {
                        numberOfTilesOccupied++;
                    }
                    if (numberOfTilesOccupied == this.grid - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //check for vertical win
    private boolean findVerticalWin(final Shapes shape){
        for (int i = 0; i < tiles.length; i++) {
            int numberOfTilesOccupied = 0;
            for (int k = 1; k < tiles[0].length; k++) {
                if (!tiles[k - 1][i].tileNotOccupied() && !tiles[k][i].tileNotOccupied()) {
                    if (tiles[k - 1][i].shapeOnTile().getShape() == tiles[k][i].shapeOnTile().getShape()
                    && tiles[k][i].shapeOnTile().getShape() == shape) {
                        numberOfTilesOccupied++;
                    }
                    if (numberOfTilesOccupied == this.grid - 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //check for top left to bottom right
    private boolean findTopLeftBottomRight(final Shapes shape) {
        int numberOfTilesOccupied = 0;
        for (int k = 1; k < tiles.length; k++) {
            if (!tiles[k - 1][k - 1].tileNotOccupied() && !tiles[k][k].tileNotOccupied()) {
                if (tiles[k - 1][k - 1].shapeOnTile().getShape() == tiles[k][k].shapeOnTile().getShape()
                && tiles[k][k].shapeOnTile().getShape() == shape) {
                    numberOfTilesOccupied++;
                } else {
                    return false;
                }
                if (numberOfTilesOccupied == this.grid - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    //check from top right to bottom left
    private boolean findTopRightBottomLeft(final Shapes shape) {
        int numberOfTilesOccupied = 0;
        int maxSize = tiles[0].length - 1;
        for (int k = 0; k < tiles.length - 1; k++) {
            if (!tiles[k][maxSize].tileNotOccupied() && !tiles[k + 1][maxSize - 1].tileNotOccupied()) {
                if (tiles[k][maxSize].shapeOnTile().getShape() == tiles[k + 1][maxSize - 1].shapeOnTile().getShape()
                && tiles[k + 1][maxSize - 1].shapeOnTile().getShape() == shape) {
                    numberOfTilesOccupied++;
                    maxSize--;
                } else {
                    return false;
                }
                if (numberOfTilesOccupied == this.grid - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    protected boolean isWin(final Shapes shape) {
        return (findTopLeftBottomRight(shape) || findTopRightBottomLeft(shape) || findHorizontalWin(shape) || findVerticalWin(shape));
    }

    protected boolean isDraw() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[0].length; j++) {
                if (tiles[i][j].tileNotOccupied()) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public Object clone(){
        try {
            return super.clone();
        } catch (final CloneNotSupportedException e) {
            return null;
        }
    }
}
