package src;

public class Cell implements IGameComponent {
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
