package io.github.haohao123coding.encodeter;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application{

    @Override
    public void start(Stage primaryStage){
        Label label = new Label("Hello, JavaFX!");

        StackPane root = new StackPane();
        root.getChildren().add(label);

        Scene scene = new Scene(root, 400, 300);

        primaryStage.setTitle("EnCodeter IDE");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
