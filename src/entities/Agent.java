package entities;


import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

// Each agent should be responsible for finding its neighbors
public class Agent implements Runnable {

    private Status status;
    private int x, y;
    private Integer[] coord;
    private int incubation;
    private int survivalChance;
    private int sicknessTime;

    private List<Status> neighborStatuses;

    public Agent(int x, int y, int incubation, int sicknessTime, int survivalChance) {
        this.sicknessTime = sicknessTime;
        this.survivalChance = survivalChance;
        this.incubation = incubation;
        this.x = x;
        this.y = y;
        coord = new Integer[]{x, y};
    }

    @Override
    public void run() {
        if(this.status == Status.DEAD || this.status == Status.IMMUNE) {
            return;
        }
        else if(this.status == Status.SICK) {
            if(sicknessTime == 0) {
                Random r = new Random();
                int prob = r.nextInt(100);

                if(prob <= survivalChance) {
                    this.status = Status.IMMUNE;
                }
                else {
                    this.status = Status.DEAD;
                }
            }
            sicknessTime--;
        }
        else if(incubation == 0) {
            this.status = Status.SICK;
        }
        else if (this.status == Status.VULNERABLE) {
            for(Status neighborStatus: neighborStatuses) {
                if(neighborStatus == neighborStatus.SICK) {
                    incubation--;
                    break;
                }
            }
        }

    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setNeighborStatuses(List<Status> neighborStatuses) {
        this.neighborStatuses = neighborStatuses;
    }

    public Integer[] getCoord() {
        return coord;
    }

    public double distanceTo(Agent a) {
        return Math.sqrt(Math.pow((this.x - a.x), 2)
                + Math.pow((this.y - a.y), 2));
    }

    @Override
    public String toString() {
        return "Agent{" +
                "status=" + status + ", " +
                "coordinates=" + "(" + this.x + ", " + this.y + ")" +
                '}';
    }

}
