import javafx.application.Application;
import javafx.stage.Stage;

public class DiseaseSimApplication extends Application {
    public static void main(String[] args) {

        AgentManager manager = new AgentManager(args[0]);

    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        //new Display();
    }
}