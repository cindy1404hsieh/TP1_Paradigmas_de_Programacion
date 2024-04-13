package org.example.vista;

import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Vista {
    public static int COLUMNAS = 40;
    public static int FILAS = 30;
    private int LADO_CASILLA = 15;

    public Vista(Stage stage){
        
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
        layoutBotones.setMaxHeight(Double.MAX_VALUE);
        Scene scene = new Scene(new VBox(crearGrilla(), layoutBotones), COLUMNAS*LADO_CASILLA, FILAS*LADO_CASILLA + 40);
        stage.setTitle("Robots");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private TilePane crearGrilla(){
        TilePane grilla = new TilePane();
        grilla.setPrefColumns(COLUMNAS);
        grilla.setPrefRows(FILAS);
        boolean colorGrilla = true;
        grilla.setMaxSize(COLUMNAS*LADO_CASILLA, FILAS*LADO_CASILLA);
        for (int i = 0; i < FILAS; i++) {
            // si la cantidad de columnas es impar, una fila empieza y termina con el mismo color.
            // en ese caso actualizo el valor de colorGrilla
            colorGrilla = !(COLUMNAS % 2 == 0) == colorGrilla;
            for (int j = 0; j < COLUMNAS; j++) {
                Color color = (colorGrilla) ? Color.LIGHTBLUE : Color.BLUE;
                grilla.getChildren().add(new Rectangle(LADO_CASILLA, LADO_CASILLA, color));
                colorGrilla = !colorGrilla;

            }
        }
        return grilla;
    }
}
