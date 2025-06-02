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
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ConnectFour extends JPanel {

    private enum GameStatus {RED_WON, YELLOW_WON, DRAW, RED_TURN, YELLOW_TURN};

    private static final Font CONTROL_FONT = new Font("SansSerif", Font.BOLD, 16);

    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final int CONNECT = 4;

    // Values to be used for button text within the board.
    private static String RED = "R";
    private static String YELLOW = "Y";
    private static String EMPTY = " ";

    private Icon redDiscIcon;
    private Icon yellowDiscIcon;

    // A 2D array of JButton objects, representing the Connect Four grid.
    private JButton[][] _board;

    private RedPlayerTurn redPlayer;
    private YellowPlayerTurn yellowPlayer;
    private GameStatus status;

    private JLabel statusLabel;
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
        statusLabel.setPreferredSize(new Dimension(COLS * 100, 30));
        statusLabel.setHorizontalAlignment(SwingConstants.LEFT);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); 
        statusLabel.setFont(CONTROL_FONT);
        statusLabel.setForeground(Color.DARK_GRAY);

        //Bottom-Right button to reset the game state
        newGameButton = new JButton("New Game");
        newGameButton.setPreferredSize(new Dimension(140, 40));
        newGameButton.setMargin(new Insets(10, 20, 10, 20));
        newGameButton.setFont(CONTROL_FONT);
        newGameButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                resetGame();
            }
        });

        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.add(statusLabel, BorderLayout.CENTER);
        controlPanel.add(newGameButton, BorderLayout.EAST);

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

    /**
     * COMPOSITE PATTERN: Individual cells and lines are treated uniformly by implementing GameComponent Interface
     * checkWin() method returns false for leaves and for the composite checks if all children have the same value
     * Cell value set to ClientProperty of corresponding JButton
     */
    interface GameComponent {
        boolean checkWin();
    }

    class Cell implements GameComponent {
        private Object value;

        public Cell(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }

        public boolean checkWin() {
            return false;
        }
    }

    class LineComposite implements GameComponent {
        private List<Cell> components;

        public LineComposite(List<Cell> components) {
            if (components.size() != CONNECT) { throw new IllegalArgumentException("Invalid Line Size"); }

            this.components = components;
        }

        public boolean checkWin() {
            Object first = components.get(0).getValue();
            if (first.equals(EMPTY)) {
                return false;
            }

            for (Cell c : components) {
                if (!c.getValue().equals(first)) {
                    return false;
                }
            }

            return true;
        }

        public Object getFirstValue() {
            return components.get(0).getValue();
        }
    }

    /**
     * TEMPLATE PATTERN: Abstract PlayerTurn provides template alogorithm for individual player turns. 
     * RedPlayerTurn and YellowPlayer turn extend the base class with specific implementations
     * Could be used to make a ComputerPlayer
     */

    abstract class PlayerTurn {
        protected int selectedColumn;
        protected ConnectFour game;

        public PlayerTurn(ConnectFour game) {
            this.game = game;
        }

        public final void takeTurn(int col) {
            this.selectedColumn = col;

            if (!dropDisc()) {
                return;
            }

            switchTurns();
            game.updateStatus();
            
        }

        protected abstract boolean dropDisc();
        protected abstract void switchTurns();
    }

    class RedPlayerTurn extends PlayerTurn {
        RedPlayerTurn(ConnectFour game) {
            super(game);
        }

        protected boolean dropDisc() {
            for (int row = ConnectFour.ROWS - 1; row >= 0; row--) {
                JButton cell = game.getCell(row, selectedColumn);
                if (cell.getClientProperty("state").equals(EMPTY)) {
                    cell.putClientProperty("state", RED);
                    cell.setIcon(redDiscIcon);
                    return true;
                }
            }
            return false;
        }

        protected void switchTurns() {
            game.setRedTurn(false);
        }
    }

    class YellowPlayerTurn extends PlayerTurn {
        YellowPlayerTurn(ConnectFour game) {
            super(game);
        }

        protected boolean dropDisc() {
            for (int row = ConnectFour.ROWS - 1; row >= 0; row--) {
                JButton cell = game.getCell(row, selectedColumn);
                if (cell.getClientProperty("state").equals(EMPTY)) {
                    cell.putClientProperty("state", YELLOW);
                    cell.setIcon(yellowDiscIcon);
                    return true;
                }
            }
            return false;
        }

        protected void switchTurns() {
            game.setRedTurn(true);
        }
    }

    /**
     *  OBSERVER PATTERN: GameSubject manages a list of observers and notifies them when the game status changes
     *  The GameObserver interface is implemented by different concrete observers
     */
    interface GameObserver {
        void update(GameStatus status);
    }

    class GameSubject {
        private List<GameObserver> observers = new ArrayList<GameObserver>();

        public void addObserver(GameObserver observer) {
            observers.add(observer);
        }

        public void notifyObservers(GameStatus status) {
            for (GameObserver o : observers) {
                o.update(status);
            }
        }
    }

    class StatusObserver implements GameObserver {
        private JLabel statusLabel;

        public StatusObserver(JLabel label) {
            this.statusLabel = label;
        }

        public void update(GameStatus status) {
            switch(status) {
                case RED_WON:
                    statusLabel.setText("Red Wins!");
                    break;
                case YELLOW_WON:
                    statusLabel.setText("Yellow Wins!");
                    break;
                case DRAW:
                    statusLabel.setText("Everyone is a winner!");
                    break;
                case RED_TURN:
                    statusLabel.setText("Red\'s Turn.");
                    break;
                case YELLOW_TURN:
                    statusLabel.setText("Yellow\'s Turn.");
                    break;
                default:
                    statusLabel.setText("Invalid Status!");
                    break;
            }
        }
    }

    class ConsoleObserver implements GameObserver {
        public void update(GameStatus status) {
            System.out.println("Game status updated to " + status);
        }
    }
}
