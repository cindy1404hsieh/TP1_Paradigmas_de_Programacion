package org.example.vista;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;

public class Vista {
    public static int COLUMNAS_DEFAULT = 20;
    public static int FILAS_DEFAULT = 15;

    public Vista(Stage stage){
        Label scoreLabel = new Label();
        Label nivelLabel = new Label();
        HBox menu = new HBox(new Label("score: "), scoreLabel, new Label("nivel: "), nivelLabel);
        menu.setAlignment(Pos.CENTER);
        
        Button safeTeleportButton = new Button("Teleport Safely");
        HBox.setHgrow(safeTeleportButton, Priority.ALWAYS);
        safeTeleportButton.setMaxWidth(Double.MAX_VALUE);

        Button randomTeleportButton = new Button("Teleport Randomly");
        HBox.setHgrow(randomTeleportButton, Priority.ALWAYS);
        randomTeleportButton.setMaxWidth(Double.MAX_VALUE);

        Button waitButton = new Button("Wait for robots");
        HBox.setHgrow(waitButton, Priority.ALWAYS);
        waitButton.setMaxWidth(Double.MAX_VALUE);


        
        HBox layoutBotones = new HBox(safeTeleportButton, randomTeleportButton, waitButton);
        layoutBotones.setPrefHeight(Double.MAX_VALUE);
        Grilla grilla = new Grilla(COLUMNAS_DEFAULT, FILAS_DEFAULT);
        Scene scene = new Scene(new VBox(menu, grilla, layoutBotones), grilla.getColumnas()*Grilla.LADO_CASILLA, grilla.getFilas()*Grilla.LADO_CASILLA + 40);
        stage.setTitle("Robots");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }


}
