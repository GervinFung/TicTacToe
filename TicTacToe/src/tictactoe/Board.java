package tictactoe;

import tictactoe.Shape.Shapes;

public final class Board implements Cloneable {
    private final int grid;
    private final Tile[][] tiles;

    public Board(final int grid) {
        this.grid = grid;
        this.tiles = new Tile[this.grid][this.grid];
        createBoard();
    }

    protected int getGrid() {
        return this.grid;
    }

    protected Tile getTileOn(final int x, final int y) {
        return this.tiles[y][x];
    }
    protected void createShape(final Shape shape, final int x, final int y) {
        this.tiles[y][x] = new Tile(shape, x, y);
    }
    protected void createBoard() {
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[0].length; j++) {
                this.tiles[i][j] = new Tile();
            }
        }
    }

    //check for horizontal win
    private boolean findHorizontalWin(final Shapes shape) {
        for (final Tile[] tile : this.tiles) {
            int numberOfTilesOccupied = 0;
            for (int k = 1; k < this.tiles[0].length; k++) {
                if (!tile[k - 1].tileNotOccupied() && !tile[k].tileNotOccupied()) {
                    if (tile[k - 1].shapeOnTile().getShape() == tile[k].shapeOnTile().getShape()
                            && tile[k].shapeOnTile().getShape() == shape) {
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
        for (int i = 0; i < this.tiles.length; i++) {
            int numberOfTilesOccupied = 0;
            for (int k = 1; k < this.tiles[0].length; k++) {
                if (!this.tiles[k - 1][i].tileNotOccupied() && !this.tiles[k][i].tileNotOccupied()) {
                    if (this.tiles[k - 1][i].shapeOnTile().getShape() == this.tiles[k][i].shapeOnTile().getShape()
                    && this.tiles[k][i].shapeOnTile().getShape() == shape) {
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
        for (int k = 1; k < this.tiles.length; k++) {
            if (!this.tiles[k - 1][k - 1].tileNotOccupied() && !this.tiles[k][k].tileNotOccupied()) {
                if (this.tiles[k - 1][k - 1].shapeOnTile().getShape() == this.tiles[k][k].shapeOnTile().getShape()
                && this.tiles[k][k].shapeOnTile().getShape() == shape) {
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
        int maxSize = this.tiles[0].length - 1;
        for (int k = 0; k < this.tiles.length - 1; k++) {
            if (!this.tiles[k][maxSize].tileNotOccupied() && !this.tiles[k + 1][maxSize - 1].tileNotOccupied()) {
                if (this.tiles[k][maxSize].shapeOnTile().getShape() == this.tiles[k + 1][maxSize - 1].shapeOnTile().getShape()
                && this.tiles[k + 1][maxSize - 1].shapeOnTile().getShape() == shape) {
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
        for (final Tile[] tiles : this.tiles) {
            for (final Tile tile : tiles) {
                if (tile.tileNotOccupied()) {
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
