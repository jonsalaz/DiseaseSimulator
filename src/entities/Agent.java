/** Jonathan Salazar, Cyrus McCormick
 * Agent: Entity which represents disease vector for simulation.
 * Implements runnable, tasked with iterating neighbor statuses
 * and deciding whether own status should be updated,
 * and when status is updated, begin infection/sickness timer if applicable
 * */

package entities;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.List;
import java.util.Random;

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

    /** When thread task is executed, update agent status if necessary, if agent currently
     * infected update incubation period, if currently sick update remaining sickness duration,
     * if sickness ends, decide whether agent has survived or died. */
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

    /** Retrieve agent's current status */
    public Status getStatus() {
        return status;
    }

    /** Update agent's current status */
    public void setStatus(Status status) {
        this.status = status;
    }

    /** Update agent's neighbor statuses list */
    public void setNeighborStatuses(List<Status> neighborStatuses) {
        this.neighborStatuses = neighborStatuses;
    }

    /** Retrieve agent's position coordinate */
    public Integer[] getCoord() {
        return coord;
    }

    /** Provided agent, check distance to this agent */
    public double distanceTo(Agent a) {
        return Math.sqrt(Math.pow((this.x - a.x), 2)
                + Math.pow((this.y - a.y), 2));
    }

    /** Initialize agent dot for display w/ color
     * appropriate to agent's current status */
    public Circle toDisplay() {
        Circle circle = new Circle();
        circle.setRadius(1);
        switch (this.status) {
            case IMMUNE:
                circle.setFill(Color.GREEN);
                break;
            case SICK:
                circle.setFill(Color.YELLOW);
                break;
            case DEAD:
                circle.setFill(Color.RED);
                break;
            case VULNERABLE:
                circle.setFill(Color.GREY);
                break;
            default:
                break;
        }
        return circle;
    }

    @Override
    public String toString() {
        return "Agent{" +
                "status=" + status + ", " +
                "coordinates=" + "(" + this.x + ", " + this.y + ")" +
                '}';
    }

}
