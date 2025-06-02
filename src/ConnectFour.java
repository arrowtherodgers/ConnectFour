package src;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectFour extends JPanel {

    public static enum GameStatus {RED_WON, YELLOW_WON, DRAW, RED_TURN, YELLOW_TURN};

    public static final Font CONTROL_FONT = new Font("SansSerif", Font.BOLD, 16);

    public static final int ROWS = 6;
    public static final int COLS = 7;
    public static final int CONNECT = 4;

    // Values to be used for button text within the board.
    public static String RED = "R";
    public static String YELLOW = "Y";
    public static String EMPTY = " ";

    private Icon redDiscIcon;
    private Icon yellowDiscIcon;

    // A 2D array of JButton objects, representing the Connect Four grid.
    private JButton[][] _board;

    private RedPlayerTurn redPlayer;
    private YellowPlayerTurn yellowPlayer;
    private GameStatus status;

    private JLabel statusLabel;
    private JLabel scoreLabel;
    private JButton newGameButton;

    private GameSubject subject = new GameSubject();

    public ConnectFour() {
        redPlayer = new RedPlayerTurn(this);
        yellowPlayer = new YellowPlayerTurn(this);
        status = GameStatus.RED_TURN;

        // Build the GUI.
        setLayout(new BorderLayout());
        buildGUI();

        subject.addObserver(new StatusObserver(statusLabel));
        subject.addObserver(new ScoreObserver(scoreLabel));
        subject.addObserver(new ConsoleObserver());
    }

    private void buildGUI() {
        // Initialize the board.
        _board = new JButton[ROWS][COLS];
        JPanel boardPanel = new JPanel(new GridLayout(ROWS, COLS));
        boardPanel.setPreferredSize(new Dimension(COLS * 100, ROWS * 100));
        boardPanel.setBackground(Color.WHITE);

        try {
            redDiscIcon = new ImageIcon(getClass().getResource("./resources/red_disc.png"));
        } catch (Exception e) {
            System.err.println("Red Icon not loaded");
        }
        
        try {
            yellowDiscIcon = new ImageIcon(getClass().getResource("./resources/yellow_disc.png"));
        } catch (Exception e) {
            System.err.println("Yellow Icon not loaded");
        }

        for(int row = 0; row < ROWS; row++) {
            for(int col = 0; col < COLS; col++) {
                JButton btn = new JButton();
                btn.setBackground(Color.LIGHT_GRAY);
                //btn.setForeground(new Color(0, 0, 0, 0));
                btn.putClientProperty("state", EMPTY);
                btn.setIcon(null);
                final int selectedColumn = col;
                btn.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        handleCellClick(selectedColumn);
                    }
                });
                _board[row][col] = btn;
                boardPanel.add(btn);
            }
        }
        
        //Bottom-Left text for displaying game status
        statusLabel = new JLabel("Game start!");
        statusLabel.setPreferredSize(new Dimension(COLS * 40, 30));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); 
        statusLabel.setFont(CONTROL_FONT);
        statusLabel.setForeground(Color.DARK_GRAY);

        //Bottom-Center text for tracking score
        scoreLabel = new JLabel("Red: 0 | Yellow: 0");
        scoreLabel.setPreferredSize(new Dimension(COLS * 40, 30));
        scoreLabel.setHorizontalAlignment(SwingConstants.LEFT);
        scoreLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); 
        scoreLabel.setFont(CONTROL_FONT);
        scoreLabel.setForeground(Color.DARK_GRAY);

        //Bottom-Right button to reset the game state
        newGameButton = new JButton("New Game");
        newGameButton.setPreferredSize(new Dimension(140, 30));
        newGameButton.setMargin(new Insets(10, 20, 10, 20));
        newGameButton.setFont(CONTROL_FONT);
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        JPanel controlPanel = new JPanel(new BorderLayout());
        JPanel _left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        _left.add(statusLabel);
        JPanel _center = new JPanel(new FlowLayout(FlowLayout.CENTER));
        _center.add(scoreLabel);
        JPanel _right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        _right.add(newGameButton);

        controlPanel.add(_left, BorderLayout.WEST);
        controlPanel.add(_center, BorderLayout.CENTER);
        controlPanel.add(_right, BorderLayout.EAST);

        add(boardPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    public JButton getCell(int row, int col) {
        return _board[row][col];
    }

    public void setRedTurn(boolean value) {
        status = value ? GameStatus.RED_TURN : GameStatus.YELLOW_TURN;
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    public Icon getRedIcon() {
        return redDiscIcon;
    }

    public Icon getYellowIcon() {
        return yellowDiscIcon;
    }
    
    private void handleCellClick(int col) {
        if (status == GameStatus.RED_TURN) {
            redPlayer.takeTurn(col);
        } else if (status == GameStatus.YELLOW_TURN) {
            yellowPlayer.takeTurn(col);
        }
    }

    //Game state is reset by clearing board and setting turn to Red
    private void resetGame() {
        for(int row = 0; row < ROWS; row++) {
            for(int col = 0; col < COLS; col++) {
                JButton btn = _board[row][col];
                btn.putClientProperty("state", EMPTY);
                btn.setIcon(null);
            }
        }
        status = GameStatus.RED_TURN;
        subject.notifyObservers(status);
    }

    public void updateStatus() {
        status = getGameStatus();
        subject.notifyObservers(status);
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JFrame frame = new JFrame("Connect Four");
                ConnectFour gamePanel = new ConnectFour();

                frame.setLayout(new BorderLayout());
                frame.add(gamePanel, BorderLayout.CENTER);
                frame.pack();
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setResizable(false);
                frame.setVisible(true);
            }
        });
    }

    // Helper method to determine the status of the Connect Four game.
    private GameStatus getGameStatus() {
        List<LineComposite> lines = new ArrayList<>();

        // Horizontal check
        for(int row = 0; row < ROWS; row++) {
            for(int col = 0; col <= COLS - 4; col++) {
                lines.add(new LineComposite(Arrays.asList(
                    new Cell(_board[row][col].getClientProperty("state")),
                    new Cell(_board[row][col+1].getClientProperty("state")),
                    new Cell(_board[row][col+2].getClientProperty("state")),
                    new Cell(_board[row][col+3].getClientProperty("state"))
                )));    
            }
        }

        // Vertical check
        for(int col = 0; col < COLS; col++) {
            for(int row = 0; row <= ROWS - 4; row++) {
                lines.add(new LineComposite(Arrays.asList(
                    new Cell(_board[row][col].getClientProperty("state")),
                    new Cell(_board[row+1][col].getClientProperty("state")),
                    new Cell(_board[row+2][col].getClientProperty("state")),
                    new Cell(_board[row+3][col].getClientProperty("state"))
                )));  
            }
        }

        // Diagonal check (top-left to bottom-right)
        for(int row = 0; row <= ROWS - 4; row++) {
            for(int col = 0; col <= COLS - 4; col++) {
                    lines.add(new LineComposite(Arrays.asList(
                    new Cell(_board[row][col].getClientProperty("state")),
                    new Cell(_board[row + 1][col + 1].getClientProperty("state")),
                    new Cell(_board[row + 2][col + 2].getClientProperty("state")),
                    new Cell(_board[row + 3][col + 3].getClientProperty("state"))
                )));
            }
        }

        // Diagonal check (bottom-left to top-right)
        for(int row = 3; row < ROWS; row++) {
            for(int col = 0; col <= COLS - 4; col++) {
                    lines.add(new LineComposite(Arrays.asList(
                    new Cell(_board[row][col].getClientProperty("state")),
                    new Cell(_board[row - 1][col + 1].getClientProperty("state")),
                    new Cell(_board[row - 2][col + 2].getClientProperty("state")),
                    new Cell(_board[row - 3][col + 3].getClientProperty("state"))
                )));
            }
        }

        //Check for a winning line
        for (LineComposite line : lines) {
            if (line.checkWin()) {
                Object winner = line.getFirstValue();
                return winner.equals(RED) ? GameStatus.RED_WON : GameStatus.YELLOW_WON;
            }
        }

        //Check if board is full
        for (int col = 0; col < COLS; col++) {
            if (_board[0][col].getClientProperty("state").equals(EMPTY)) {
                return status;
            }
        }

        return GameStatus.DRAW;
    }
}
