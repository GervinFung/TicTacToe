package tictactoe;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.Button;
import java.awt.Image;
import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import tictactoe.Shape.Shapes;

import java.awt.Toolkit;
import java.awt.Color;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public final class TicTacToe {

    private static final int DIMENSION = Toolkit.getDefaultToolkit().getScreenSize().height - 50;

    private enum PLAYER {
        FIRST {
            @Override
            PLAYER getOpponent() { return SECOND; }
            @Override
            boolean isFirstPlayer() { return true; }
        }, SECOND {
            @Override
            PLAYER getOpponent() { return FIRST; }
            @Override
            boolean isFirstPlayer() { return false; }
        };
        abstract PLAYER getOpponent();
        abstract boolean isFirstPlayer();
    }

    private Board board;
    private final List<TileButton> tileButtons;
    private boolean againstAI, aiFirst, aiThinking;
    private PLAYER currentPlayer;
    private final JFrame jf;
    private final PropertyChangeSupport propertyChangeSupport;

    private TicTacToe() {
        this.aiThinking = false;
        this.currentPlayer = PLAYER.FIRST;
        this.board = new Board(3);

        this.jf = new JFrame("Tic Tac Toe");
        this.tileButtons = new ArrayList<>();

        this.jf.setJMenuBar(addMenuOptionIntoMenu());
        final PropertyChangeListener gameSetupPropertyChangeListener = propertyChangeEvent -> {
            if (againstAI) {
                this.aiThinking = true;
                SwingUtilities.invokeLater(() -> new AIThinkTank().execute());
            }
        };

        this.propertyChangeSupport = new PropertyChangeSupport(gameSetupPropertyChangeListener);
        this.propertyChangeSupport.addPropertyChangeListener(gameSetupPropertyChangeListener);
    }

    private final class AIThinkTank extends SwingWorker<Tile, Void> {
        @Override
        public Tile doInBackground() {
            //clone board so it wont affect the real board
             return new AI().bestMove(new Board(TicTacToe.this.board));
        }

        @Override
        public void done() {
            try {
                final Tile chosenTile = this.get();
                TicTacToe.this.board.createShape(chosenTile.shapeOnTile(), chosenTile.getIndex());

                final TileButton button = TicTacToe.this.tileButtons.get(chosenTile.getIndex());

                final ImageIcon aiIcon = getResizedImageIcon(button, "shape_image/O.png");
                button.setIcon(aiIcon);

                if (isGameOver(Shapes.O, board)) {
                    removeButton();
                    invokeGame(TicTacToe.this.board.getGrid());
                }

                TicTacToe.this.aiThinking = false;

            } catch (final InterruptedException | ExecutionException ignored) {}
        }
    }


    private static void invokeGame(final int size) { SwingUtilities.invokeLater(() -> getSingletonInstance().startGame(size)); }

    private void fireAI() { this.propertyChangeSupport.firePropertyChange(null, null, null); }

    //singleton
    public static TicTacToe getSingletonInstance() { return SingleTon.INSTANCE; }

    private static final class SingleTon { private static final TicTacToe INSTANCE = new TicTacToe();}

    public static void main(final String[] args) { invokeGame(3); }

    //instantiate the buttons and add into JFrame
    private void initialiseButton() {
        for (int i = 0; i < this.board.getSize(); i++) {
            this.tileButtons.add(new TileButton(i));
            this.jf.add(this.tileButtons.get(i));
        }
    }

    //for restarting new game
    private void removeButton() { this.tileButtons.forEach(this.jf::remove); }

    //add all menu into menu bar
    private JMenuBar addMenuOptionIntoMenu() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.add(this.chooseOpponent());
        menuBar.add(this.chooseGrid());
        return menuBar;
    }

    private JMenu chooseGrid() {
        final JMenu grid = new JMenu("Grid");

        final JSpinner gridLevel = new JSpinner(new SpinnerNumberModel(3, 3, Integer.MAX_VALUE, 1));
        final Button button = new Button("Ok");
        button.addActionListener((event)-> {
            this.removeButton();
            this.changeButtonAvailability(true);
            invokeGame((Integer)gridLevel.getValue());
        });

        grid.add(gridLevel);
        grid.add(button);

        return grid;
    }

    private JMenu chooseOpponent() {
        final JMenu opponent = new JMenu("Opponent");

        final JMenuItem human = new JMenuItem("Human");
        human.addActionListener(e -> {
            this.againstAI = false;
            JOptionPane.showMessageDialog(this.jf, "Select Grid To Start Game. You can only start playing by selecting the grid");
            changeButtonAvailability(false);
            this.aiThinking = false;
        });

        final JMenuItem ai = new JMenuItem("AI");
        ai.addActionListener(e -> {
            this.againstAI = true;
            this.aiFirst = askPlayRole() == 1;
            JOptionPane.showMessageDialog(this.jf, "Select Grid To Start Game. You can only start playing by selecting the grid");
            changeButtonAvailability(false);
        });

        opponent.add(ai);
        opponent.add(human);

        return opponent;
    }

    protected void startGame(final int size) {

        this.board = new Board(size);
        this.tileButtons.clear();
        this.jf.setSize(DIMENSION, DIMENSION);
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setLayout(new GridLayout(size, size, 0, 0));
        this.initialiseButton();
        this.jf.setLocationRelativeTo(null);
        this.jf.setBackground(Color.black);
        this.jf.setResizable(false);
        this.jf.setVisible(true);

        this.currentPlayer = PLAYER.FIRST;

        if (aiFirst) { this.fireAI(); }
    }

    private void changeButtonAvailability(final boolean enable) {
        for (final TileButton jButton : this.tileButtons) {
            if (enable) {
                jButton.setBackground(Color.white);
            } else {
                jButton.setBackground(Color.lightGray);
            }
            jButton.setEnabled(enable);
        }
    }

    private int askPlayRole() {
        while (true) {
            final int playAsFirst = JOptionPane.showConfirmDialog(null, "Play as First Player?");
            if (playAsFirst == 1 || playAsFirst == 0) {
                return playAsFirst;
            }
        }
    }

    //resize image according to number and size of grids
    private ImageIcon resizeIcon(final ImageIcon icon, final int resizedWidth, final int resizedHeight) {
        final Image resizedImage = icon.getImage().getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    //get the resized image
    private ImageIcon getResizedImageIcon(final TileButton button, final String path) {
        try {
            return resizeIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getClassLoader().getResource(path)))), button.getWidth(), button.getHeight());
        } catch (final IOException | NullPointerException e) { e.printStackTrace(); }
        return null;
    }

    private boolean isGameOver(final Shapes shape, final Board board) {
        if (board.isWin(shape)) {
            final String message = shape.toString() + " has won the game";
            JOptionPane.showMessageDialog(null, message);
            return true;

        } else if (board.isDraw()) {
            final String message = "Game drawn";
            JOptionPane.showMessageDialog(null, message);
            return true;
        }
        return false;
    }

    private final class TileButton extends JButton {
        private TileButton(final int i) {
            this.setBackground(Color.WHITE);
            this.addActionListener(actionEvent -> {

                if (board.getTileOn(i).tileNotOccupied() && !aiThinking) {

                    if (!againstAI) {

                        final String imagePath = (currentPlayer.isFirstPlayer()) ? "shape_image/X.png" : "shape_image/O.png";
                        final Shapes shape = (currentPlayer.isFirstPlayer()) ? Shapes.X : Shapes.O;

                        board.createShape(new Shape(shape), i);

                        this.setIcon(getResizedImageIcon(this, imagePath));
                        final boolean gameOver = isGameOver(shape, board);
                        if (gameOver) {
                            removeButton();
                            invokeGame(board.getGrid());
                        }
                        currentPlayer = currentPlayer.getOpponent();

                    }
                    if (againstAI) {

                        board.createShape(new Shape(Shapes.X), i);

                        this.setIcon(getResizedImageIcon(this, "shape_image/X.png"));

                        final boolean gameOver = isGameOver(Shapes.X, board);
                        if (gameOver) {
                            removeButton();
                            invokeGame(board.getGrid());
                        } else {
                            fireAI();
                        }
                    }
                }
            });
        }
    }
}