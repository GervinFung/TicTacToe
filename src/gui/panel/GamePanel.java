package gui.panel;

import gui.TicTacToe;
import gui.button.TileButton;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class GamePanel extends JPanel {

    private final List<TileButton> tileButtons;

    public GamePanel(final TicTacToe ticTacToe, final int size, final int grid) {
        super(new GridLayout(grid, grid, 0, 0));
        this.tileButtons = this.createTileButtons(ticTacToe, size);
        this.drawPanel();
        this.validate();
    }

    private void drawPanel() {
        this.tileButtons.forEach(this::add);
    }

    public void setTileImage(final int index, final String path) {
        final TileButton button = this.tileButtons.get(index);
        final ImageIcon icon = TileButton.getResizedImageIcon(button, path);
        button.setIcon(icon);
    }

    private List<TileButton> createTileButtons(final TicTacToe ticTacToe, final int size) {
        final List<TileButton> tileButtons = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            tileButtons.add(new TileButton(ticTacToe, i));
        }
        return Collections.unmodifiableList(tileButtons);
    }
}