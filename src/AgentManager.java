/** Jonathan Salazar, Cyrus McCormick
 * AgentMananger: Responsible for reading simulation configuration properties,
 * building, and executing disease simulations through managing agent threads. */

import entities.Agent;
import entities.Status;
import javafx.application.Platform;

import java.io.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AgentManager {

    /** Initialized with default values, overridden by specified config */
    private ArrayList<Agent> agents;
    private HashMap<Agent, List<Agent>> agentNeighbors;
    /** Holds the last status held by agent, if status changed during sim
     * (i.e. agent dies), we update the log */
    private HashMap<Agent, Status> loggedAgentStatus;
    private File file;
    private int daysPassed = 0;
    private int distance = 20;
    private int incubationLen = 5;
    private int sickTime = 10;
    private int recovRate = 95;
    private int numAgents = 100;
    private String agentLoc = "random";
    private int width = 200, height = 200;
    private int gridR, gridC;
    private int initSick = 1;
    private int initImm = 5;

    private Display display;
    private Boolean shutdown;
    private String configFile;

    /** Constructor receives file configuration name from CLA */
    public AgentManager(String configFile, Display display) {
        this.configFile = configFile;
        this.shutdown = false;
        readConfig(configFile);
        this.display = display;
        Platform.runLater(() -> display.setManager(this));
        buildSim();
        simLoop();
    }

    /** Given config file name, read lines of config file & initialize
     *  simulation parameters */
    private void readConfig(String configFile) {

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader(configFile));

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

    /** Construct simulation agents & agent neighbors using specified
     *  simulation parameters. Check for initial agent location cases,
     * & assign agent positions accordingly. Assign initial immune/sick */
    private void buildSim() {
        agents = new ArrayList<>();
        initLog();

        //Initialize agents with locations based on simulation type.
        switch (agentLoc) {
            case ("grid"):
                for (int r = 0; r < gridR * distance; r += distance) {
                    for (int c = 0; c < gridC * distance; c += distance) {
                        Agent agent = new Agent(r, c, incubationLen, sickTime, recovRate);
                        agents.add(agent);
                    }
                }

                numAgents = agents.size();
                break;
            case ("randomGrid"):
                Random r = new Random();
                Set<List<Integer>> placedAgentCoords = new HashSet<>();
                int numPlaced = 0;

                while (numPlaced < numAgents) {
                    int x = r.nextInt(gridR) * distance;
                    int y = r.nextInt(gridR) * distance;
                    numPlaced = getNumPlaced(placedAgentCoords, numPlaced, x, y);
                }
                break;
            case ("random"):
                r = new Random();
                placedAgentCoords = new HashSet<>();
                numPlaced = 0;

                while (numPlaced < numAgents) {
                    int x = r.nextInt(width);
                    int y = r.nextInt(height);
                    numPlaced = getNumPlaced(placedAgentCoords, numPlaced, x, y);
                }
                break;
            default:
                break;
        }

        // Shuffle ArrayList of agents to ensure that the location of states is randomized.
        Collections.shuffle(agents);

        int sick = 0;
        int immune = 0;

        // Assign agent status.
        for (Agent agent : agents) {
            if (sick < initSick) {
                agent.setStatus(Status.SICK);
                sick++;
            } else if (immune < initImm) {
                agent.setStatus(Status.IMMUNE);
                immune++;
            } else {
                agent.setStatus(Status.VULNERABLE);
            }
        }

        // Populate neighbors list for e/a agent
        findAgentNeighbors();
    }

    /** Find neighbors within exposure distance for e/a agent */
    private void findAgentNeighbors() {
        if (numAgents <= 0) return;
        agentNeighbors = new HashMap<>();

        for (Agent agent : agents) {
            List<Agent> neighbors = new ArrayList<>();
            for (Agent posNeighbor : agents) {
                if (agent.equals(posNeighbor)) continue;
                if (agent.distanceTo(posNeighbor) > distance) continue;
                neighbors.add(posNeighbor);
            }
            agentNeighbors.put(agent, neighbors);
        }
    }

    /** Main simulation logic, continue running simulation while
     * reachable vulnerable agents exist. Retrieve e/a agent and update
     * neighbor statuses, add to thread pool, update log and display.*/
    private void simLoop() {
        int deadOrImmune = initImm;

        while(deadOrImmune < numAgents && !shutdown) {
            ExecutorService executorService = Executors.newFixedThreadPool(numAgents);

            int counter = 0;
            for(Agent a : agents) {
                if(a.getStatus() == Status.DEAD || a.getStatus() == Status.IMMUNE) {
                    counter++;
                }
                a.setNeighborStatuses(findNeighborsStatus(a));
                executorService.submit(a);
                logAgent(a);
            }
            daysPassed++;
            deadOrImmune = counter;

            executorService.shutdown();
            try {
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Runs the updateDisplay function on the javafx application thread rather than the current thread of
            // the simulation.
            Platform.runLater(() -> display.updateDisplay(agents, width, height, this));
        }
    }

    /** Provided agent, check whether agent status has been updated since last
     * simulation iteration, if so, update agent's status in log. */
    private void logAgent(Agent a) {

        if (!loggedAgentStatus.containsKey(a)) {
            loggedAgentStatus.put(a, a.getStatus());
        }
        else if (a.getStatus() != loggedAgentStatus.get(a)) {
            loggedAgentStatus.put(a, a.getStatus());

            try {
                BufferedWriter bw =
                        new BufferedWriter(new FileWriter(file, true));
                bw.write("Agent at ["
                        + a.getCoord()[0]
                        + ", " + a.getCoord()[1] + "] became "
                        + a.getStatus() + " on day " + daysPassed);
                bw.newLine();
                bw.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /** Create text file to house agent logging information. */
    private void initLog() {
        loggedAgentStatus = new HashMap<>();
        file = new File("log.txt");
        try {
            file.createNewFile();
            // Overwrite existing log file if exists
            FileWriter fw = new FileWriter(file, false);
            fw.write("");
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Utility function for placement of random/randomGrid agents -
     * Try to place agent randomly by checking generated coordinate against
     * already placed agent locations. If successful, return updated num agents placed*/
    private int getNumPlaced(Set<List<Integer>> placedAgentCoords, int numPlaced, int x, int y) {
        List<Integer> agentCoord = new LinkedList<>(Arrays.asList(x, y));
        if (placedAgentCoords.contains(agentCoord)) return numPlaced;

        Agent agent = new Agent(x, y, incubationLen, sickTime, recovRate);
        agents.add(agent);
        placedAgentCoords.add(agentCoord);
        numPlaced++;
        return numPlaced;
    }

    /** Provided agent, retrieve list of other agents within exposure distance */
    private List<Status> findNeighborsStatus(Agent agent){
        List<Status> neighborsStatus = new ArrayList<>();
        List<Agent> neighbors = agentNeighbors.get(agent);

        for (Agent a : neighbors) {
            neighborsStatus.add(a.getStatus());
        }
        return neighborsStatus;
    }


    /** Print simulation information, testing function */
    private void printSim() {
        System.out.println(agentNeighbors.size() + " agents");
        for (Map.Entry<Agent, List<Agent>> entry : agentNeighbors.entrySet()) {
            System.out.println(entry.getKey().toString() +
                    " Num neighbors: " + entry.getValue().size());
        }
    }

    /** Retrieve initial agent location case */
    public String getFormation() {
        return agentLoc;
    }

    /** Retrieve height of grid in case of grid/randomGrid
     *  initial agent location case */
    public int getGridHeight(){
        return this.gridR * this.distance;
    }

    /** Retrieve width of grid in case of grid/randomGrid
     *  initial agent location case */
    public int getGridWidth(){
        return this.gridC*this.distance;
    }

    /** Retrieve name of configuration file */
    public String getFileName() {
        return configFile;
    }

    /** Retrieve config dimension height */
    public int getHeight() { return this.height; }

    /** Retrieve config dimension width */
    public int getWidth() { return this.width; }

    /** Retrieve display instance */
    public Display getDisplay() {
        return display;
    }

    /** Update simulation shutdown flag */
    public void shutdown() {
        this.shutdown = true;
    }
}
