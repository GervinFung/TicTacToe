package gui;

import engine.board.Board;
import engine.move.Move;
import engine.player.ai.MiniMax;
import gui.menu.GameMenu;
import gui.menu.GameSetup;
import gui.menu.GridSetup;
import gui.panel.GamePanel;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicReference;

public final class TicTacToe {

    public static final int DEFAULT_GRID = 3;

    private static final int DIMENSION = Toolkit.getDefaultToolkit().getScreenSize().height - 50;

    private Board board;
    private boolean aiThinking, gameOver, stopAI;
    private GamePanel gamePanel;

    private final JFrame jFrame;
    private final GameSetup gameSetup;
    private final GridSetup gridSetup;
    private final PropertyChangeSupport gameSetupPropertyChangeSupport;

    public static TicTacToe getSingletonInstance() { return SingleTon.INSTANCE; }
    private static final class SingleTon {
        private static final TicTacToe INSTANCE = generateInstance();

        private static TicTacToe generateInstance() {
            final AtomicReference<TicTacToe> tictactoe = new AtomicReference<>(null);
            SwingUtilities.invokeLater(() -> tictactoe.set(new TicTacToe()));
            return tictactoe.get();
        }
    }
    public enum PlayerType {HUMAN, COMPUTER}

    private TicTacToe() {
        this.aiThinking = this.gameOver = false;
        this.stopAI = true;
        this.board = Board.createStandardBoard(DEFAULT_GRID);
        this.gamePanel = new GamePanel(this, this.board.getSize(), this.board.getGrid());
        this.gameSetup = new GameSetup(this);
        this.gridSetup = new GridSetup(this);
        this.jFrame = this.createFrame();

        final PropertyChangeListener gameSetupPropertyChangeListener = propertyChangeEvent -> {
            if (this.getGameSetup().isAIPlayer(this.board.getCurrentPlayer()) && !this.board.isWin() && !this.board.isDraw() && !this.aiThinking) {
                new AIThinkTank(this).execute();
            }
            this.displayEndGameMessage();
        };

        this.gameSetupPropertyChangeSupport = new PropertyChangeSupport(gameSetupPropertyChangeListener);
        this.gameSetupPropertyChangeSupport.addPropertyChangeListener(gameSetupPropertyChangeListener);
    }

    private JFrame createFrame() {
        final JFrame jFrame = new JFrame("Tic Tac Toe");
        jFrame.setBackground(Color.black);
        jFrame.setJMenuBar(this.createMenuBar());
        jFrame.setVisible(true);
        jFrame.setSize(DIMENSION, DIMENSION);
        jFrame.setLocationRelativeTo(null);
        jFrame.add(this.gamePanel, BorderLayout.CENTER);
        jFrame.setResizable(false);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                if (askExit(TicTacToe.this.jFrame)) {
                    TicTacToe.this.jFrame.dispose();
                    System.exit(0);
                    TicTacToe.this.jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                } else {
                    TicTacToe.this.jFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                }
            }
        });
        return jFrame;
    }

    private JMenuBar createMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.add(new GameMenu(this));
        return menuBar;
    }

    public void start() {
        final int option = JOptionPane.showConfirmDialog(this.jFrame, "Confirmation to start a new game?");
        if (option == JOptionPane.YES_OPTION) {
            this.aiThinking = this.gameOver = false;
            this.stopAI = true;
            this.board = Board.createStandardBoard(this.gridSetup.getGrid());
            this.jFrame.remove(this.gamePanel);
            this.gamePanel = new GamePanel(this, this.board.getSize(), this.board.getGrid());
            this.jFrame.add(this.gamePanel, BorderLayout.CENTER);
            this.jFrame.repaint();
            this.jFrame.revalidate();
            this.moveMadeUpdate();
        }
    }

    private static boolean askExit(final JFrame jFrame) {
        return JOptionPane.showConfirmDialog(jFrame, "Confirmation to exit game?") == JOptionPane.YES_OPTION;
    }

    public void exit() {
        if (askExit(this.jFrame)) {
            this.jFrame.dispose();
            System.exit(0);
        }
    }

    public void displayEndGameMessage() {
        if (this.board.isWin()) {
            this.gameOver = true;
            final String message = (this.board.getCurrentPlayer().getOpponent().getLeague().isCross() ? "X" : "O") + " has won the game";
            JOptionPane.showMessageDialog(this.jFrame, message);
            this.start();
        } else if (this.board.isDraw()) {
            this.gameOver = true;
            final String message = "Game drawn";
            JOptionPane.showMessageDialog(this.jFrame, message);
            this.start();
        }
    }

    //setter
    public void setAiThinking(final boolean aiThinking) { this.aiThinking = aiThinking; }
    public void setStopAI(final boolean stopAI) { this.stopAI = stopAI; }
    public void updateBoard(final Board board) { this.board = board; }

    //getter
    public boolean isAiThinking() { return this.aiThinking; }
    public boolean isGameOver() { return this.gameOver; }
    public boolean isAIStopped() { return this.stopAI; }
    public JFrame getJFrame() { return this.jFrame; }
    public Board getBoard() { return this.board; }
    public GamePanel getGamePanel() { return this.gamePanel; }
    public GameSetup getGameSetup() { return this.gameSetup; }
    public GridSetup getGridSetup() { return this.gridSetup; }

    public void moveMadeUpdate() { this.gameSetupPropertyChangeSupport.firePropertyChange("moveMadeUpdate", null, null); }

    private final static class AIThinkTank extends SwingWorker<Move, Void> {

        private final TicTacToe ticTacToe;

        private AIThinkTank(final TicTacToe ticTacToe) {
            this.ticTacToe = ticTacToe;
            this.ticTacToe.setAiThinking(true);
            this.ticTacToe.setStopAI(false);
        }

        @Override
        public Move doInBackground() { return new MiniMax(this.ticTacToe.getGameSetup().getSearchDepth()).execute(this.ticTacToe.getBoard()); }

        @Override
        public void done() {
            try {
                if (!this.ticTacToe.isAIStopped()) {
                    final String path = (this.ticTacToe.getBoard().getCurrentPlayer().getLeague().isCross()) ? "shape_image/X.png" : "shape_image/O.png";
                    final Move bestMove = this.get();
                    this.ticTacToe.updateBoard(bestMove.execute());
                    this.ticTacToe.getGamePanel().setTileImage(bestMove.getIndex(), path);
                }
                this.ticTacToe.setAiThinking(false);
                this.ticTacToe.setStopAI(true);
                this.ticTacToe.moveMadeUpdate();
            } catch (final InterruptedException | ExecutionException ignored) {}
        }
    }
}