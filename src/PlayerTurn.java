package src;

/**
 * TEMPLATE PATTERN: Abstract PlayerTurn provides template alogorithm for individual player turns. 
 * RedPlayerTurn and YellowPlayer turn extend the base class with specific implementations
 * Could be used to make a ComputerPlayer
*/

public abstract class PlayerTurn {
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
