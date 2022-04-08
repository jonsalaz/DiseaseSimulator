package entities;


import java.util.concurrent.BlockingQueue;

// Each agent should be responsible for finding its neighbors
public class Agent implements Runnable {

    private Status status;
    private int x, y;
    private Integer[] coord;

    private BlockingQueue<Status> neighborStatuses;

    public Agent(int x, int y) {
        this.x = x;
        this.y = y;
        coord = new Integer[]{x, y};
    }

    @Override
    public void run() {

    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setNeighborStatuses(BlockingQueue<Status> neighborStatuses) {
        this.neighborStatuses = neighborStatuses;
    }

    public Integer[] getCoord() {
        return coord;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "status=" + status + ", " +
                "coordinates=" + "(" + this.x + ", " + this.y + ")" +
                '}';
    }
}
