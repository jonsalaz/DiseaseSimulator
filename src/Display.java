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

    public void updateDisplay(ArrayList<Agent> agents, int width, int height){
        Rectangle rectangle = new Rectangle(width, height, Color.WHITE);
        rectangle.setX(width/4);
        rectangle.setY(height/4);
        rectangle.setStroke(Color.BLACK);

        AnchorPane anchorPane = new AnchorPane(rectangle);
        anchorPane.setPrefSize(width+100, height+100);

        for (Agent agent: agents) {
            Circle agentDot = agent.toDisplay();

            agentDot.setCenterX(rectangle.getX() + agent.getCoord()[0]);
            agentDot.setCenterY(rectangle.getY() + agent.getCoord()[1]);

            anchorPane.getChildren().add(agentDot);
        }


        Scene root = new Scene(anchorPane);

        primaryStage.setScene(root);
        primaryStage.show();
    }


}
