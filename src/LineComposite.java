package src;

import java.util.List;

public class LineComposite implements IGameComponent {
    private List<Cell> components;

    public LineComposite(List<Cell> components) {
        if (components.size() != ConnectFour.CONNECT) { throw new IllegalArgumentException("Invalid Line Size"); }

        this.components = components;
    }

    public boolean checkWin() {
        Object first = components.get(0).getValue();
        if (first.equals(ConnectFour.EMPTY)) {
            return false;
        }

        for (Cell c : components) {
            if (!c.getValue().equals(first)) {
                return false;
            }
        }

        return true;
    }

    public Object getFirstValue() {
        return components.get(0).getValue();
    }
}

