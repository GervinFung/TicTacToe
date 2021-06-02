package gui.menu;

import gui.TicTacToe;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.util.List;

public final class UILookAndFeel extends JMenu {

    public UILookAndFeel(final TicTacToe ticTacToe) {
        super("Layout Design");
        this.createLayoutButton(ticTacToe);
    }

    private void createLayoutButton(final TicTacToe ticTacToe) {
        final List<UIManager.LookAndFeelInfo> looks = List.of(UIManager.getInstalledLookAndFeels());
        for (final UIManager.LookAndFeelInfo look : looks) {
            final JMenuItem menuItem = new JMenuItem(look.getName());
            menuItem.addActionListener(e -> {
                try {
                    UIManager.setLookAndFeel(look.getClassName());
                    SwingUtilities.updateComponentTreeUI(ticTacToe.getJFrame());
                } catch (final ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ignored) {}
            });
            this.add(menuItem);
        }
    }
}