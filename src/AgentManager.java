import entities.Position;

import java.io.BufferedReader;
import java.io.FileReader;

public class AgentManager {

    private Position[][] diseaseArr;

    public AgentManager(String configFile) {

        initSim(configFile);

    }

    private void initSim(String configFile) {

        try {
            BufferedReader br = new BufferedReader(
                    new FileReader("resources/" + configFile));

            String line = br.readLine();
            while (line != null) {
                System.out.println(line);
                line = br.readLine();
            }

        } catch (Exception e){
            e.printStackTrace();
        }

    }

}
