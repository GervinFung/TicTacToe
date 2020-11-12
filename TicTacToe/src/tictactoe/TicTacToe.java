package tictactoe;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.GridLayout;
import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.io.File;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.awt.Color;

public final class TicTacToe extends JPanel{

    private static final long serialVersionUID = 1L;
    private final Board board;
    private JButton[] buttons;
    private boolean firstPlayer, inputNull;
    private double divisor;
    private final JFrame jf;

    public static void main(String[] args) {
        TicTacToe ticTacToe = new TicTacToe();
        ticTacToe.startGame();
    }

    //instantite the buttons and add into JFrame
    private void initialiseButton(final JFrame jf) {
        for (int i = 0; i < this.buttons.length; i++) {
            this.buttons[i] = new JButton();
            jf.add(this.buttons[i]);
        }
        for (int i = 0; i < this.buttons.length; i++) {
            this.buttons[i].setBackground(Color.WHITE);
        }
    }
    //remove all the buttons added to JFrame
    //for restarting new game
    private void removeButton() {
        for (int i = 0; i < this.buttons.length; i++) {
            jf.remove(this.buttons[i]);
        }
    }

    public TicTacToe() {
        this.board = new Board();
        this.jf = new JFrame();
        this.firstPlayer = true;
        this.divisor = 0;
    }

    protected void startGame() {
        setGameGrid();
        board.createBoard();
        jf.setSize(600, 600);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.setLayout(new GridLayout(this.board.getGrid(), this.board.getGrid(), 0, 0));
        this.divisor = 600 / this.board.getGrid();
        initialiseButton(jf);
        jf.setTitle("Tic Tac Toe");
        jf.setLocationRelativeTo(null);
        jf.setBackground(Color.black);
        jf.setResizable(false);
        jf.setVisible(true);
        final MouseAction ma = new MouseAction();
        addActionListener(ma);
    }

    private void addActionListener(final MouseAction mouseAction) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].addActionListener(mouseAction);
        }
    }

    //set tic tac toe grids
    private void setGameGrid() {
        int size = -1;
        final Pattern pattern = Pattern.compile("[3-9]");
        boolean match = false;
        String input = "";
        do {
            input = JOptionPane.showInputDialog("Please input an integer from 3 to 9 to set TicTacToe grid layout\nIf 3 x 3 enter 3 ", "3");
            if (input == null) {
                inputNull = true;
                break;
            }
            match = pattern.matcher(input).matches();
            if (match) {
                size = Integer.parseInt(input);
            }
        } while (!(size >= 3 && size <= 11) || !match);
        if (inputNull) {
            System.exit(0);
        }
        this.board.setGrid(size);
        this.buttons = new JButton[size * size];
    }
    //resize image according to number and size of grids
    private ImageIcon resizeIcon(final ImageIcon icon, final int resizedWidth, final int resizedHeight) {
        final Image img = icon.getImage();
        final Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight,  java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    private class MouseAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent event) {
            final JButton button = (JButton)event.getSource();
            String imagePath = (firstPlayer) ? "shape_image/X.png" : "shape_image/O.png";
            final File file = new File(imagePath);
            //for getting the absolute path of imagePath
            imagePath = file.getAbsolutePath();
            final char name = (firstPlayer) ? 'X' : 'O';
            final int x = (int)Math.round(button.getLocation().getX()/TicTacToe.this.divisor);
            final int y = (int)Math.round(button.getLocation().getY()/TicTacToe.this.divisor);
            if (board.getTileOn(x, y).tileNotOccupied()) {
                firstPlayer = !firstPlayer;
                board.getTileOn(x, y).setShapeOnTile(new Shape(name));
                final ImageIcon icon = resizeIcon(new ImageIcon(imagePath), button.getWidth(), button.getHeight());
                button.setIcon(icon);
                if (board.isWin()) {
                    final String message = name + " has won the game";
                    JOptionPane.showMessageDialog(null, message);
                    removeButton();
                    startGame();
                } else if (board.isDraw()) {
                    final String message = "Game drawn";
                    JOptionPane.showMessageDialog(null, message);
                    removeButton();
                    startGame();
                }
            }
        }
    }
}