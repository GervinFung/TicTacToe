package tictactoe;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import java.awt.Image;
import java.awt.GridLayout;

import javax.swing.JOptionPane;

import tictactoe.Shape.Shapes;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Color;

public final class TicTacToe implements ActionListener{

    private Board board;
    private JButton[] buttons;
    private boolean againstAI, aiFirst;
    private double divisor;
    private final JFrame jf;

    private final JMenuItem[] grids;
    private final JMenuItem friend, AI;
    private final JMenu opponent, grid;


    public TicTacToe() {
        this.board = new Board(3);
        this.jf = new JFrame("Tic Tac Toe");
        this.divisor = 0;
        //initialise menu
        this.opponent = new JMenu("Opponent");
        this.grid = new JMenu("Grids");
        //initialise menu item
        this.friend = new JMenuItem("Friend");
        this.AI = new JMenuItem("AI");
        //add action listener to menu item
        this.AI.addActionListener(this);
        this.friend.addActionListener(this);
        this.grids = new JMenuItem[4];
    }

    public static void main(final String[] args) {
        final TicTacToe ticTacToe = new TicTacToe();
        ticTacToe.startGame(3);
    }

    //instantite the buttons and add into JFrame
    private void initialiseButton(final JFrame jf) {
        for (int i = 0; i < this.buttons.length; i++) {
            this.buttons[i] = new JButton();
            this.buttons[i].setBackground(Color.WHITE);
            jf.add(this.buttons[i]);
        }
    }

    //initialise the grid menu and add into JMenu
    private void initialiseGridMenu(final JMenuItem[] grids) {
        for (int i = 0; i < grids.length; i++) {
            grids[i] = new JMenuItem((i + 3) + " x " + (i + 3));
            grids[i].addActionListener(this);
            grids[i].setName(Integer.toString(i + 3));
            this.grid.add(grids[i]);
        }
    }
    //remove all the buttons added to JFrame
    //remove all grids previouslt added into grid
    //for restarting new game
    private void removeButton() {
        for (int i = 0; i < this.buttons.length; i++) {
            this.jf.remove(this.buttons[i]);
        }
        this.grid.removeAll();
    }

    //add all menu into menu bar
    private void addMenuOptionIntoMenu(final JMenuBar menuBar) {
        menuBar.setVisible(true);
        menuBar.setBackground(Color.WHITE);
        this.opponent.add(this.AI);
        this.opponent.add(this.friend);
        menuBar.add(this.opponent);
        menuBar.add(this.grid);
    }

    protected void startGame(final int size) {
        final JMenuBar menuBar = new JMenuBar();
        addMenuOptionIntoMenu(menuBar);
        initialiseGridMenu(this.grids);

        this.jf.setJMenuBar(menuBar);

        this.board = new Board(size);
        this.buttons = new JButton[size * size];
        this.board.createBoard();
        this.jf.setSize(600, 600);
        this.jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.jf.setLayout(new GridLayout(size, size, 0, 0));
        this.divisor = 600 / size;
        initialiseButton(this.jf);
        this.jf.setLocationRelativeTo(null);
        this.jf.setBackground(Color.black);
        this.jf.setResizable(false);
        this.jf.setVisible(true);

        if (this.aiFirst) {
            this.AImove(this.buttons[2]);
        }
        final MouseAction ma = new MouseAction(this.divisor, this.againstAI, this.board);
        this.addMouseActionListener(ma);
    }

    private void changeButtonAvailability(final JButton[] buttons, final boolean enable) {
        for (final JButton jButton : buttons) {
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
        for (int i = 0; i < grids.length; i++) {
            if (event.getSource().equals(grids[i])) {
                final int size = Integer.parseInt(grids[i].getName());
                removeButton();
                startGame(size);
                changeButtonAvailability(this.buttons, true);
            }
        }
        if (event.getSource().equals(this.friend)) {
            this.againstAI = false;
            JOptionPane.showMessageDialog(null, "Select Grid To Start Game. You can only start playing by selecting the grid");
            changeButtonAvailability(this.buttons, false);
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
            JOptionPane.showMessageDialog(null, "Select Grid To Start Game. You can only start playing by selecting the grid");
            changeButtonAvailability(this.buttons, false);
        }
    }

    private void addMouseActionListener(final MouseAction mouseAction) {
        for (int i = 0; i < this.buttons.length; i++) {
            this.buttons[i].addActionListener(mouseAction);
        }
    }

    //resize image according to number and size of grids
    private ImageIcon resizeIcon(final ImageIcon icon, final int resizedWidth, final int resizedHeight) {
        final Image img = icon.getImage();
        final Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    //get the resized image
    private ImageIcon getResizedImageIcon(final JButton button, final String path) {
        final File file = new File(path);
        final String imagePath = file.getAbsolutePath();
        return resizeIcon(new ImageIcon(imagePath), button.getWidth(), button.getHeight());
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

    private void displayShape(final JButton[] button, final Tile tile, final ImageIcon aiIcon) {
        final int tilesNumber = (int)Math.sqrt(button.length);
        final int x = tile.getX(), y = tile.getY();
        for (int i = 0; i < button.length; i++) {
            final int X = i % tilesNumber;
            final int Y = i / tilesNumber;
            if (X == x && Y == y) {
                buttons[i].setIcon(aiIcon);
                return ;
            }
        }
    }

    private void AImove(final JButton button) {
        final AI ai = new AI((int)Math.sqrt(this.buttons.length));

        //clone board so it wont affect the real board
        ai.bestMove((Board)this.board.clone());
        final Tile chosenTile = ai.getChosenTile();
        final int chosenY = chosenTile.getY(), chosenX = chosenTile.getX();

        this.board.createShape(chosenTile.shapeOnTile(), chosenX, chosenY);

        final ImageIcon aiIcon = getResizedImageIcon(button, "shape_image/O.png");
        displayShape(this.buttons, chosenTile, aiIcon);
        if (isGameOver(Shapes.O, board)) {
            removeButton();
            startGame(this.board.getGrid());
        }
    }

    private final class MouseAction implements ActionListener {
        private boolean firstPlayer;
        private final boolean againstAI;
        private final double divisor;
        private final Board board;
        public MouseAction(final double divisor, final boolean againstAI, final Board board) {
            this.firstPlayer = true;
            this.divisor = divisor;
            this.againstAI = againstAI;
            this.board = board;
        }
        @Override
        public void actionPerformed(final ActionEvent event) {
            final JButton button = (JButton)event.getSource();
            final int x = (int)Math.round(button.getLocation().getX()/this.divisor);
            final int y = (int)Math.round(button.getLocation().getY()/this.divisor);

            if (this.board.getTileOn(x, y).tileNotOccupied()) {

                if (!this.againstAI) {

                    final String imagePath = (this.firstPlayer) ? "shape_image/X.png" : "shape_image/O.png";
                    final Shapes shape = (this.firstPlayer) ? Shapes.X : Shapes.O;
                    final int value = (this.firstPlayer) ? -1 : 1;

                    this.board.createShape(new Shape(shape, value), x, y);

                    final ImageIcon icon = getResizedImageIcon(button, imagePath);
                    button.setIcon(icon);
                    final boolean gameOver = isGameOver(shape, this.board);
                    if (gameOver) {
                        removeButton();
                        startGame(this.board.getGrid());
                    }
                    this.firstPlayer = !this.firstPlayer;
                }
                if (this.againstAI) {

                    this.board.createShape(new Shape(Shapes.X, -1), x, y);

                    final ImageIcon icon = getResizedImageIcon(button, "shape_image/X.png");
                    button.setIcon(icon);
                    final boolean gameOver = isGameOver(Shapes.X, this.board);
                    if (gameOver) {
                        removeButton();
                        startGame(this.board.getGrid());
                    } else {
                        AImove(button);
                    }
                }
            }
        }
    }
}