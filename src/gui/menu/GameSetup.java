package gui.menu;

import engine.player.Player;
import gui.TicTacToe;
import gui.TicTacToe.PlayerType;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridLayout;

public final class GameSetup extends JDialog {

    private PlayerType crossPlayerType;
    private PlayerType noughtPlayerType;

    private final JSpinner searchDepthSpinner;

    private static final String HUMAN_TEXT = "Human", COMPUTER_TEXT = "Computer";

    public GameSetup(final TicTacToe ticTacToe) {
        final JPanel myPanel = new JPanel(new GridLayout(0, 1));

        final JRadioButton crossHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton crossComputerButton = new JRadioButton(COMPUTER_TEXT);
        final JRadioButton noughtHumanButton = new JRadioButton(HUMAN_TEXT);
        final JRadioButton noughtComputerButton = new JRadioButton(COMPUTER_TEXT);

        crossHumanButton.setActionCommand(HUMAN_TEXT);
        final ButtonGroup crossGroup = new ButtonGroup();
        crossGroup.add(crossHumanButton);
        crossGroup.add(crossComputerButton);
        crossHumanButton.setSelected(true);

        final ButtonGroup noughtGroup = new ButtonGroup();
        noughtGroup.add(noughtHumanButton);
        noughtGroup.add(noughtComputerButton);
        noughtHumanButton.setSelected(true);

        this.getContentPane().add(myPanel);
        myPanel.add(new JLabel("Cross"));
        myPanel.add(crossHumanButton);
        myPanel.add(crossComputerButton);
        myPanel.add(new JLabel("Nought"));
        myPanel.add(noughtHumanButton);
        myPanel.add(noughtComputerButton);

        this.searchDepthSpinner = addLabeledSpinner(myPanel, new SpinnerNumberModel(1, 1, 10, 1));

        final JButton cancelButton = new JButton("Cancel");
        cancelButton.setBackground(Color.lightGray);
        final JButton okButton = new JButton("OK");
        okButton.setBackground(Color.WHITE);

        okButton.addActionListener(e -> {
            if (this.isAIPlayer(ticTacToe.getBoard().getCurrentPlayer()) && !ticTacToe.isAIStopped()) {
                ticTacToe.setStopAI(true);
            }
            this.crossPlayerType = crossComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
            this.noughtPlayerType = noughtComputerButton.isSelected() ? PlayerType.COMPUTER : PlayerType.HUMAN;
            this.dispose();
            ticTacToe.moveMadeUpdate();
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

    public boolean isAIPlayer(final Player player) {
        return player.getLeague().isCross() ? this.crossPlayerType == PlayerType.COMPUTER : this.noughtPlayerType == PlayerType.COMPUTER;
    }

    private static JSpinner addLabeledSpinner(final Container c, final SpinnerModel model) {
        final JLabel l = new JLabel("Select Level");
        c.add(l);
        final JSpinner spinner = new JSpinner(model);
        l.setLabelFor(spinner);
        c.add(spinner);
        return spinner;
    }

    public int getSearchDepth() { return (Integer)this.searchDepthSpinner.getValue(); }
}