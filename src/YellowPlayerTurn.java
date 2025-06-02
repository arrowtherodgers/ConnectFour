package src;

import javax.swing.JButton;

public class YellowPlayerTurn extends PlayerTurn {
    YellowPlayerTurn(ConnectFour game) {
        super(game);
    }

    protected boolean dropDisc() {
        for (int row = ConnectFour.ROWS - 1; row >= 0; row--) {
            JButton cell = game.getCell(row, selectedColumn);
            if (cell.getClientProperty("state").equals(ConnectFour.EMPTY)) {
                cell.putClientProperty("state", ConnectFour.YELLOW);
                cell.setIcon(game.getYellowIcon());
                return true;
            }
        }
        return false;
    }

    protected void switchTurns() {
        game.setRedTurn(true);
    }
}
