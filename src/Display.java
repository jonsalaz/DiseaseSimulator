/** Jonathan Salazar, Cyrus McCormick
 * Display: Initialize visualization of disease simulation & event listeners for simulation
 * resets */

import entities.Agent;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Objects;

public class Display {

    public Stage primaryStage;
    private AgentManager manager;
    private VBox vBox;
    private Scene root;
    private AnchorPane simArea;

    public Display(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(1);
        });

    }

    /** Called for e/a iteration of simulation loop (e/a day),
     * Provided list of agents, dimension properties, reference to agent manager.
     * Populate simulation area element with agent dots scaled depending on total number
     * of agents */
    public void updateDisplay(ArrayList<Agent> agents, int width, int height, AgentManager manager){
        this.simArea.getChildren().clear();

        for (Agent agent: agents) {
            Circle agentDot = agent.toDisplay();

            if(Objects.equals(manager.getFormation(), "random")) {
                agentDot.setCenterX(agent.getCoord()[0]);
                agentDot.setCenterY(agent.getCoord()[1]);
            }
            else {
                int gridHeight = manager.getGridHeight();
                int gridWidth = manager.getGridWidth();

                agentDot.setCenterX((agent.getCoord()[0] * ((float) height / (float) gridHeight)));
                agentDot.setCenterY((agent.getCoord()[1] * ((float)  width / (float) gridWidth)));
            }
            // Scales agent display size dependent on the minimum between width and height of the simulation area.
            agentDot.setRadius(Math.min(width, height)/Math.sqrt(agents.size())/2);

            simArea.getChildren().add(agentDot);
        }


        primaryStage.setScene(root);
        primaryStage.show();
    }

    /** Set AgentManager, used during initial simulation before restarts */
    public void setManager(AgentManager manager) {
        this.manager = manager;
        initializeDisplay();
    }

    /** Initialize display objects which are not dependent on retrieving agents  */
    private void initializeDisplay() {
        Button button = new Button("Restart");

        primaryStage.setWidth(manager.getWidth()+100);
        primaryStage.setHeight(manager.getHeight()+100);

        this.simArea = new AnchorPane();
        simArea.setStyle("-fx-border-width: 1px 1px 1px 1px");
        simArea.setPrefSize(manager.getWidth(), manager.getHeight());
        simArea.setMaxSize(manager.getWidth(), manager.getHeight());

        this.vBox = new VBox(simArea, button);
        vBox.setPrefSize(manager.getWidth()+100, manager.getHeight()+100);
        vBox.setMaxSize(manager.getWidth()+100, manager.getHeight()+100);
        vBox.setAlignment(Pos.CENTER);

        button.setStyle("-fx-border-radius: 500px; -fx-background-color: black;" +
                " -fx-text-fill: white; -fx-padding: 20px; -fx-border-insets: 5px; " +
                "-fx-background-insets: 10px; -fx-font-weight: bold");

        button.setOnMousePressed(event -> {
            new Thread(() -> {
                new AgentManager(manager.getFileName(), manager.getDisplay());
            }).start();
            manager.shutdown();
        });
        this.root = new Scene(vBox);
        primaryStage.setScene(root);
    }
}
