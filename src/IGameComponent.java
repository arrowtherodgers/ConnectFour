package src;
 /**
     * COMPOSITE PATTERN: Individual cells and lines are treated uniformly by implementing GameComponent Interface
     * checkWin() method returns false for leaves and for the composite checks if all children have the same value
     * Cell value set to ClientProperty of corresponding JButton
     */
public interface IGameComponent {
    boolean checkWin();
}
