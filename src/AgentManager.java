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
    private Position[][] diseaseArr;
    private int distance = 20;
    private int incubationLen = 5;
    private int sickTime = 10;
    private int recovRate = 95;
    private int numAgents = 100;
    private String agentLoc = "grid";
    private int gridR = 200, gridC = 200;
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
                    case("dim"):
                        int r = Integer.parseInt(lineArr[1]),
                                c = Integer.parseInt(lineArr[2]);
                        diseaseArr = new Position[r][c];
                        break;
                    case("dist"):
                        distance = Integer.parseInt(lineArr[1]);
                        break;
                    case("incLen"):
                        incubationLen = Integer.parseInt(lineArr[1]);
                        break;
                    case("sickTime"):
                        sickTime = Integer.parseInt(lineArr[1]);
                        break;
                    case("recovRate"):
                        recovRate = Integer.parseInt(lineArr[1]);
                        break;
                    case("numAgents"):
                        numAgents = Integer.parseInt(lineArr[1]);
                        break;
                    case("initLoc"):
                        agentLoc = lineArr[1];
                        gridR = Integer.parseInt(lineArr[2]);
                        gridC = Integer.parseInt(lineArr[3]);
                        break;
                    case("initSick"):
                        initSick = Integer.parseInt(lineArr[1]);
                        break;
                    case("initImm"):
                        initImm = Integer.parseInt(lineArr[1]);
                        break;
                    default: continue;
                }

                line = br.readLine();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

    private void buildSim() {

        diseaseArr = new Position[gridR][gridC];
        List<Position> agentPositions = new ArrayList<>();
        int sick = 0;
        int immune = 0;

        // create initial agents
        for (int i = 0; i < numAgents; i++) {

            Agent agent = new Agent();
            Position agentPos = new Position();

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

            agentPos.setAgent(agent);
            agentPositions.add(agentPos);
        }

        // Shuffle so initial sick/imm are placed randomly in sim
        Collections.shuffle(agentPositions);

        // fill diseaseArr, if agent location is grid add agent positions
        int currDist = 0;
        for (int r = 0; r < gridR; r++) {
            for (int c = 0; c < gridC; c++) {

                // place distanced agent if location type is grid &
                // agent positions not empty
                if (agentLoc.equals("grid") && agentPositions.size() > 0 &&
                        (currDist == distance || (r == 0 && c == 0))) {
                    diseaseArr[r][c] = agentPositions.remove(0);
                    currDist = 0;
                }

                // if random/randomGrid or out of agent positions
                else {
                    diseaseArr[r][c] = new Position();
                    currDist++;
                }
            }
        }

        // if random/randomGrid agent location, place agent pos randomly
        if (!agentLoc.equals("grid")) {
            Random r = new Random();
            while (!agentPositions.isEmpty()) {
                int ranR = r.nextInt(gridR);
                int ranC = r.nextInt(gridC);
                Position pos = diseaseArr[r.nextInt(gridR)][r.nextInt(gridC)];

                if (pos.getAgent() == null) {
                    diseaseArr[ranR][ranC] = agentPositions.remove(0);
                }

            }
        }

    }

    private void simLoop() {}

    private List<Status> findNeighborStatus(Agent agent){
        return null;
    }

    private void printSim() {
        for (int r = 0; r < gridR; r++) {
            for (int c = 0; c < gridC; c++) {
                System.out.print(diseaseArr[r][c]);
            }
            System.out.println();
        }
    }

}
