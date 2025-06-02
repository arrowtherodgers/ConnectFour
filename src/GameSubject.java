package src;

import java.util.ArrayList;
import java.util.List;

import src.ConnectFour.GameStatus;

public class GameSubject {
    private List<IGameObserver> observers = new ArrayList<IGameObserver>();

    public void addObserver(IGameObserver observer) {
        observers.add(observer);
    }

    public void notifyObservers(GameStatus status) {
        for (IGameObserver o : observers) {
            o.update(status);
        }
    }
}
