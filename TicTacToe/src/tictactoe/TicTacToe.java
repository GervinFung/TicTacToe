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

import tictactoe.Shape.Shapes;

import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Color;
import java.io.IOException;
import java.util.Objects;

public final class TicTacToe implements ActionListener{

    private static final int DIMENSION = Toolkit.getDefaultToolkit().getScreenSize().height - 50;

    private Board board;
    private TileButton[] tileButtons;
    private boolean againstAI, aiFirst, firstPlayer;
    private int divisor;
    private final JFrame jf;

    private final JSpinner gridLevel;
    private final JMenuItem friend, AI;
    private final JMenu opponent, grid;

    private TicTacToe() {
        this.board = new Board(3);
        this.jf = new JFrame("Tic Tac Toe");
        this.divisor = 0;

        //initialise menu
        this.grid = new JMenu("Grid");
        this.opponent = new JMenu("Opponent");

        //initialise menu item
        this.friend = new JMenuItem("Friend");
        this.AI = new JMenuItem("AI");

        //add action listener to menu item
        this.AI.addActionListener(this);
        this.friend.addActionListener(this);

        //grid level button
        this.gridLevel = new JSpinner(new SpinnerNumberModel(3, 3, Integer.MAX_VALUE, 1));

        final Button button = new Button("Ok");
        button.addActionListener((event)-> {
            this.removeButton();
            this.changeButtonAvailability(this.tileButtons, true);
            invokeGame((Integer)this.gridLevel.getValue());
        });

        this.opponent.add(this.AI);
        this.opponent.add(this.friend);
        this.grid.add(this.gridLevel);
        this.grid.add(button);
        this.jf.setJMenuBar(addMenuOptionIntoMenu());
    }

    private static void invokeGame(final int size) {
        SwingUtilities.invokeLater(() -> getSingletonInstance().startGame(size));
    }

    //singleton
    public static TicTacToe getSingletonInstance() { return SingleTon.INSTANCE; }

    private static final class SingleTon { private static final TicTacToe INSTANCE = new TicTacToe();}

    public static void main(final String[] args) { invokeGame(3); }

    //instantiate the buttons and add into JFrame
    private void initialiseButton(final JFrame jf) {
        for (int i = 0; i < this.tileButtons.length; i++) {
            this.tileButtons[i] = new TileButton(i);
            this.tileButtons[i].setBackground(Color.WHITE);
            jf.add(this.tileButtons[i]);
        }
    }

    //for restarting new game
    private void removeButton() {
        for (final TileButton button : this.tileButtons) {
            this.jf.remove(button);
        }
    }

    //add all menu into menu bar
    private JMenuBar addMenuOptionIntoMenu() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(Color.WHITE);
        menuBar.add(this.opponent);
        menuBar.add(this.grid);
        return menuBar;
    }

    protected void startGame(final int size) {

        this.board = new Board(size);
        this.tileButtons = new TileButton[size * size];
        this.jf.setSize(DIMENSION, DIMENSION);
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setLayout(new GridLayout(size, size, 0, 0));
        this.divisor = size;
        this.initialiseButton(this.jf);
        this.jf.setLocationRelativeTo(null);
        this.jf.setBackground(Color.black);
        this.jf.setResizable(false);
        this.jf.setVisible(true);

        if (this.aiFirst) {
            this.AImove(this.tileButtons[2]);
        }
    }

    private void changeButtonAvailability(final TileButton[] buttons, final boolean enable) {
        for (final TileButton jButton : buttons) {
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
            final int playAsX = JOptionPane.showConfirmDialog(null, "Play as X?");
            if (playAsX == 1 || playAsX == 0) {
                return playAsX;
            }
        }
    }

    @Override
    public void actionPerformed(final ActionEvent event) {
        if (event.getSource().equals(this.friend)) {
            this.againstAI = false;
            JOptionPane.showMessageDialog(this.jf, "Select Grid To Start Game. You can only start playing by selecting the grid");
            changeButtonAvailability(this.tileButtons, false);
        }
        if (event.getSource().equals(this.AI)) {
            this.againstAI = true;
            final int playAsX = askPlayRole();
            if (playAsX == 1) {
                this.aiFirst = true;
            }
            else if (playAsX == 0) {
                this.aiFirst = false;
            }
            JOptionPane.showMessageDialog(this.jf, "Select Grid To Start Game. You can only start playing by selecting the grid");
            changeButtonAvailability(this.tileButtons, false);
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

    private void displayShape(final TileButton[] button, final Tile tile, final ImageIcon aiIcon) {
        final int tilesNumber = (int)Math.sqrt(button.length);
        final int x = tile.getX(), y = tile.getY();
        for (int i = 0; i < button.length; i++) {
            final int X = i % tilesNumber;
            final int Y = i / tilesNumber;
            if (X == x && Y == y) {
                tileButtons[i].setIcon(aiIcon);
                return ;
            }
        }
    }

    private void AImove(final TileButton button) {
        final AI ai = new AI((int)Math.sqrt(this.tileButtons.length));

        //clone board so it wont affect the real board
        final Tile chosenTile = ai.bestMove((Board)this.board.clone());
        final int chosenY = chosenTile.getY(), chosenX = chosenTile.getX();

        this.board.createShape(chosenTile.shapeOnTile(), chosenX, chosenY);

        final ImageIcon aiIcon = getResizedImageIcon(button, "shape_image/O.png");
        displayShape(this.tileButtons, chosenTile, aiIcon);
        if (isGameOver(Shapes.O, board)) {
            removeButton();
            invokeGame(this.board.getGrid());
        }
    }
    
    private final class TileButton extends JButton {
        private TileButton(final int i) {
            this.addActionListener(actionEvent -> {

                final int x = i % divisor;
                final int y = i / divisor;

                if (board.getTileOn(x, y).tileNotOccupied()) {

                    if (!againstAI) {

                        final String imagePath = (firstPlayer) ? "shape_image/X.png" : "shape_image/O.png";
                        final Shapes shape = (firstPlayer) ? Shapes.X : Shapes.O;
                        final int value = (firstPlayer) ? -1 : 1;

                        board.createShape(new Shape(shape, value), x, y);

                        this.setIcon(getResizedImageIcon(this, imagePath));
                        final boolean gameOver = isGameOver(shape, board);
                        if (gameOver) {
                            removeButton();
                            invokeGame(board.getGrid());
                        }
                        firstPlayer = !firstPlayer;
                    }
                    if (againstAI) {

                        board.createShape(new Shape(Shapes.X, -1), x, y);

                        this.setIcon(getResizedImageIcon(this, "shape_image/X.png"));
                        final boolean gameOver = isGameOver(Shapes.X, board);
                        if (gameOver) {
                            removeButton();
                            invokeGame(board.getGrid());
                        } else {
                            SwingUtilities.invokeLater(() -> AImove(this));
                        }
                    }
                }
            });
        }
    }
}