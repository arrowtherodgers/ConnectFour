package src;

import javax.swing.JButton;

public class RedPlayerTurn extends PlayerTurn {
    RedPlayerTurn(ConnectFour game) {
        super(game);
    }

    protected boolean dropDisc() {
        for (int row = ConnectFour.ROWS - 1; row >= 0; row--) {
            JButton cell = game.getCell(row, selectedColumn);
            if (cell.getClientProperty("state").equals(ConnectFour.EMPTY)) {
                cell.putClientProperty("state", ConnectFour.RED);
                cell.setIcon(game.getRedIcon());
                return true;
            }
        }
        return false;
    }

    protected void switchTurns() {
        game.setRedTurn(false);
    }
}
