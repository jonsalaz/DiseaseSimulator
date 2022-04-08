package entities;


import java.util.concurrent.BlockingQueue;

// Each agent should be responsible for finding its neighbors
public class Agent implements Runnable {

    private Status status;

    private BlockingQueue<Status> neighborStatuses;

    public Agent() {
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

    @Override
    public String toString() {
        return "Agent{" +
                "status=" + status +
                '}';
    }
}
