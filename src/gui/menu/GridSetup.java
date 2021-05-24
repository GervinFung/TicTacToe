package gui.menu;

import gui.TicTacToe;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

public class GridSetup extends JDialog {

    private final JSpinner gridSizeSpinner;

    public GridSetup(final TicTacToe ticTacToe) {
        final JPanel myPanel = new JPanel(new GridLayout(0, 1));

        this.getContentPane().add(myPanel);

        this.gridSizeSpinner = addLabeledSpinner(myPanel, new SpinnerNumberModel(TicTacToe.DEFAULT_GRID, TicTacToe.DEFAULT_GRID, 10, 1));

        final JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.lightGray);
        final JButton okButton = new JButton("OK");
        okButton.setBackground(Color.WHITE);

        okButton.addActionListener(e -> {
            ticTacToe.start();
            this.dispose();
        });

        cancelButton.addActionListener(e -> this.dispose());

        myPanel.add(okButton);
        myPanel.add(cancelButton);

        this.setLocationRelativeTo(ticTacToe.getJFrame());
        this.pack();
        this.setVisible(false);
        this.setResizable(false);
    }

    public void promptUser() {
        this.setVisible(true);
        this.repaint();
    }

    private static JSpinner addLabeledSpinner(final Container c, final SpinnerModel model) {
        final JLabel l = new JLabel("Select Grid Level");
        c.add(l);
        final JSpinner spinner = new JSpinner(model);
        l.setLabelFor(spinner);
        c.add(spinner);
        return spinner;
    }

    public int getGrid() { return (Integer) this.gridSizeSpinner.getValue(); }
}