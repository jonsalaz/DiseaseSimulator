import entities.Agent;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.Objects;

public class Display {

    public Stage primaryStage;

    public Display(Stage primaryStage) {
        this.primaryStage = primaryStage;

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(1);
            }
        });

    }

    public void updateDisplay(ArrayList<Agent> agents, int width, int height, AgentManager manager){
        primaryStage.setWidth(width+100);
        primaryStage.setHeight(height+100);

        Rectangle rectangle = new Rectangle(width, height, Color.WHITE);
        rectangle.setStroke(Color.BLACK);
        rectangle.setStrokeWidth(1);
        rectangle.setX(25);
        rectangle.setY(25);

        AnchorPane anchorPane = new AnchorPane(rectangle);
        anchorPane.setPrefSize(width+100, height+100);

        for (Agent agent: agents) {
            Circle agentDot = agent.toDisplay();

            if(Objects.equals(manager.getFormation(), "random")) {
                agentDot.setCenterX(rectangle.getX() + agent.getCoord()[0]);
                agentDot.setCenterY(rectangle.getY() + agent.getCoord()[1]);
            }
            else {
                int gridHeight = manager.getGridHeight();
                int gridWidth = manager.getGridWidth();

                agentDot.setCenterX(rectangle.getX()
                        + (agent.getCoord()[0] * ((float)  width / (float) gridWidth)) + rectangle.getStrokeWidth());
                agentDot.setCenterY(rectangle.getY()
                        + (agent.getCoord()[1] * ((float) height / (float) gridHeight)) + rectangle.getStrokeWidth());
            }
            // Scales agent display size dependent on the minimum between width and height of the simulation area.
            agentDot.setRadius(Math.min(width, height)/Math.sqrt(agents.size())/2);

            anchorPane.getChildren().add(agentDot);
        }


        Scene root = new Scene(anchorPane);

        primaryStage.setScene(root);
        primaryStage.show();
    }


}
