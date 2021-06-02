package gui.button;

import engine.move.Move;
import gui.TicTacToe;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.util.Objects;

public final class TileButton extends JButton {

    public TileButton(final TicTacToe ticTacToe, final int index) {
        super.setBackground(Color.WHITE);
        this.addActionListener(e -> {
            if (ticTacToe.isGameOver()) {
                JOptionPane.showMessageDialog(ticTacToe.getJFrame(), "Game is over. Please start a new one");
                return;
            }
            final boolean AI = ticTacToe.isAIStopped() || !ticTacToe.isAiThinking();
            if (ticTacToe.getBoard().getTileList().get(index).tileNotOccupied() && AI) {

                final String imagePath = (ticTacToe.getBoard().getCurrentPlayer().getLeague().isCross()) ? "shape_image/X.png" : "shape_image/O.png";
                this.setIcon(getResizedImageIcon(this, imagePath));

                final Move moveFound = ticTacToe.getBoard().getCurrentPlayer().getLegalMoves().stream().filter(move -> move.getIndex() == index).findFirst().orElseThrow();
                ticTacToe.updateBoard(ticTacToe.getBoard().getCurrentPlayer().makeMove(moveFound));

                SwingUtilities.invokeLater(() -> {
                    if (ticTacToe.getGameSetup().isAIPlayer(ticTacToe.getBoard().getCurrentPlayer())) {
                        ticTacToe.moveMadeUpdate();
                    } else {
                        ticTacToe.displayEndGameMessage();
                    }
                });
            }
        });
    }

    private static ImageIcon resizeIcon(final ImageIcon icon, final int resizedWidth, final int resizedHeight) {
        final Image resizedImage = icon.getImage().getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    //get the resized image
    public static ImageIcon getResizedImageIcon(final TileButton button, final String path) {
        try {
            return resizeIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(button.getClass().getClassLoader().getResource(path)))), button.getWidth(), button.getHeight());
        } catch (final IOException | NullPointerException e) { e.printStackTrace(); }
        return null;
    }
}