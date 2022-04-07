package entities;


// Each agent should be responsible for finding its neighbors
public class Agent implements Runnable {

    private Status status;

    @Override
    public void run() {

    }

    @Override
    public String toString() {
        return "Agent{" +
                "status=" + status +
                '}';
    }
}
