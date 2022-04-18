/** Jonathan Salazar, Cyrus McCormick
 * DiseaseSimApplication: Main method for JavaFX application */

import javafx.application.Application;
import javafx.stage.Stage;

public class DiseaseSimApplication extends Application {
    private static String fileName;
    public static void main(String[] args) {
        fileName = args[0];
        launch(args);
    }

    /** Initialize display & agent manager  */
    @Override
    public void start(Stage primaryStage) {
        Display display = new Display(primaryStage);

        new Thread(() -> {
            new AgentManager(fileName, display);
        }).start();
    }
}