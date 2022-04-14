import javafx.application.Application;
import javafx.stage.Stage;

public class DiseaseSimApplication extends Application {
    private static String fileName;
    public static void main(String[] args) {
        fileName = args[0];
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Display display = new Display(primaryStage);

        new Thread(() -> {
            AgentManager manager = new AgentManager(fileName, display);
        }).start();
    }
}