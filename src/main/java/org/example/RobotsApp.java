package org.example;

import javafx.application.Application;
import javafx.stage.Stage;
import org.example.vista.Vista;

public class RobotsApp extends Application {

    @Override
    public void start(Stage stage) {
        Vista ignored = new Vista(stage);
    }

    public static void main(String[] args) {
        launch();
    }
}
