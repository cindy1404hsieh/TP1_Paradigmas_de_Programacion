package org.example.vista;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Casilla extends StackPane {
    int x,y;

    Casilla(int x, int y, Color color) {
        super();

        this.x = x;
        this.y = y;
        super.setPrefSize(Grilla.LADO_CASILLA, Grilla.LADO_CASILLA);
        super.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void agregarEntidad(Entidad entidad) {
        super.getChildren().add(entidad);

    }

}
