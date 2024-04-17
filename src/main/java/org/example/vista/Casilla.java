package org.example.vista;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Casilla extends StackPane {
    Casilla(Color color) {
        super();

        super.setPrefSize(Grilla.LADO_CASILLA, Grilla.LADO_CASILLA);
        super.setMaxSize(Grilla.LADO_CASILLA, Grilla.LADO_CASILLA);
        super.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void agregarEntidad(String tipoEntidad) {
        super.getChildren().add(new Entidad(tipoEntidad));
    }

    public void eliminarEntidad() {
        super.getChildren().clear();
    }

}
