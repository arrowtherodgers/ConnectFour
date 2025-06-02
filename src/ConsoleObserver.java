package src;

import src.ConnectFour.GameStatus;

public class ConsoleObserver implements IGameObserver {
    public void update(GameStatus status) {
        System.out.println("Game status updated to " + status);
    }
}
