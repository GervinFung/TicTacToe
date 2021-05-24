package gui.menu;

import gui.TicTacToe;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public final class GameMenu extends JMenu {

    public GameMenu(final TicTacToe ticTacToe) {
        super("Game Option");
        this.add(this.createNewGame(ticTacToe));
        this.add(this.exitGame(ticTacToe));
        this.add(this.gameSetup(ticTacToe));
    }

    private JMenuItem createNewGame(final TicTacToe ticTacToe) {
        final JMenuItem menuItem = new JMenuItem("New Game");
        menuItem.addActionListener(e -> ticTacToe.getGridSetup().promptUser());
        return menuItem;
    }

    private JMenuItem exitGame(final TicTacToe ticTacToe) {
        final JMenuItem menuItem = new JMenuItem("Exit Game");
        menuItem.addActionListener(e -> ticTacToe.exit());
        return menuItem;
    }

    private JMenuItem gameSetup(final TicTacToe ticTacToe) {
        final JMenuItem menuItem = new JMenuItem("Game Setup");
        menuItem.addActionListener(e-> ticTacToe.getGameSetup().promptUser());
        return menuItem;
    }
}