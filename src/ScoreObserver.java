package src;

import javax.swing.JLabel;

import src.ConnectFour.GameStatus;

public class ScoreObserver implements IGameObserver{
    private JLabel scoreLabel;
    private int redPoints;
    private int yellowPoints;

    public ScoreObserver(JLabel scoreLabel) {
        this.scoreLabel = scoreLabel;
        this.redPoints = 0;
        this.yellowPoints = 0;
    }

    public void update(GameStatus status) {
        switch (status) {
            case RED_WON:
                redPoints++;
                break;
            case YELLOW_WON:
                yellowPoints++;
                break;
            default:
                break;
        }
        scoreLabel.setText(String.format("Red: %d | Yellow: %d", redPoints, yellowPoints));
    }
}
