package src;

import javax.swing.JLabel;

import src.ConnectFour.GameStatus;

public class StatusObserver implements IGameObserver {
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
