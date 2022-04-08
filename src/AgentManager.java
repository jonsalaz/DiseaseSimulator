import entities.Agent;
import entities.Position;
import entities.Status;


import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AgentManager {

    /** Initialized with default values, overridden by specified config */
    private ArrayList<Agent> agents;
    private int distance = 20;
    private int incubationLen = 5;
    private int sickTime = 10;
    private int recovRate = 95;
    private int numAgents = 100;
    private String agentLoc = "grid";
    private int width = 200, height = 200;
    private int gridR, gridC;
    private int initSick = 1;
    private int initImm = 5;

    public AgentManager(String configFile) {
        readConfig(configFile);
        buildSim();
        printSim();
    }

    private void readConfig(String configFile) {

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("resources/" + configFile));

            String line = br.readLine();
            while (line != null) {

                String[] lineArr = line.split(" ");
                String label = lineArr[0];
                switch(label) {
                    case("dimensions"):
                        width = Integer.parseInt(lineArr[1]);
                        height = Integer.parseInt(lineArr[2]);
                        break;
                    case("exposuredistance"):
                        distance = Integer.parseInt(lineArr[1]);
                        break;
                    case("incubation"):
                        incubationLen = Integer.parseInt(lineArr[1]);
                        break;
                    case("sickness"):
                        sickTime = Integer.parseInt(lineArr[1]);
                        break;
                    case("recover"):
                        recovRate = Integer.parseInt(lineArr[1]);
                        break;
                    case("grid"):
                        agentLoc = lineArr[0];
                        gridR = Integer.parseInt(lineArr[1]);
                        gridC = Integer.parseInt(lineArr[2]);
                        numAgents = gridR * gridC;
                        break;
                    case("random"):
                        agentLoc = lineArr[0];
                        numAgents = Integer.parseInt(lineArr[1]);
                        break;
                    case("randomGrid"):
                        agentLoc = lineArr[0];
                        gridR = Integer.parseInt(lineArr[1]);
                        gridC = Integer.parseInt(lineArr[2]);
                        numAgents = Integer.parseInt(lineArr[3]);
                        break;
                    case("initialsick"):
                        initSick = Integer.parseInt(lineArr[1]);
                        break;
                    case("immune"):
                        initImm = Integer.parseInt(lineArr[1]);
                        break;
                    default: break;
                }

                line = br.readLine();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void buildSim() {
        agents = new ArrayList<>();

        //Initialize agents with locations based on simulation type.
        switch(agentLoc) {
            case("grid"):
                for (int r = 0; r <= gridR*distance; r+=distance) {
                    for (int c = 0; c <= gridC*distance; c+=distance) {
                        Agent agent = new Agent(r, c);
                        agents.add(agent);
                    }
                }
                break;
            case("randomGrid"):
                //TODO: Randomize arrangement of agents, accounting for if an agent has already been placed in the
                // randomly selected location.
                break;
            case("random"):
                //TODO: account for the rare chance where two agents are placed in the same location.
                Random r = new Random();
                for (int i = 0; i < numAgents; i++) {
                    int x = r.nextInt(width);
                    int y = r.nextInt(height);
                    Agent agent = new Agent(x, y);
                    agents.add(agent);
                }
                break;
        }

        // Shuffle ArrayList of agents to ensure that the location of states is randomized.
        Collections.shuffle(agents);

        int sick = 0;
        int immune = 0;

        // Assign agent status.
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            if (sick < initSick) {
                agent.setStatus(Status.SICK);
                sick++;
            }
            else if (immune < initImm) {
                agent.setStatus(Status.IMMUNE);
                immune++;
            }
            else {
                agent.setStatus(Status.VULNERABLE);
            }
        }
    }

    private void simLoop() {}

    private List<Status> findNeighborStatus(Agent agent){
        return null;
    }

    private void printSim() {
        for (int i = 0; i < agents.size(); i++) {
            System.out.println(agents.get(i));
        }
    }

}
