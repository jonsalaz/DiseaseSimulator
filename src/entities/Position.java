package entities;


// essentially a "tile", or a "box"
public class Position {

    private Agent agent;

    @Override
    public String toString() {
        return "Box{" +
                "agent=" + agent +
                '}';
    }
}
