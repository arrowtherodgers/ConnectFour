package src;

/**
 *  OBSERVER PATTERN: GameSubject manages a list of observers and notifies them when the game status changes
 *  The GameObserver interface is implemented by different concrete observers
 */

import src.ConnectFour.GameStatus;

public interface IGameObserver {
    void update(GameStatus status);
}
