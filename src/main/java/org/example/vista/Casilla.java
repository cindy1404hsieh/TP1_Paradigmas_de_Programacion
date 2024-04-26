package org.example.vista;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Casilla extends StackPane {
    private final Color color;

    Casilla(Color color) {
        super();
        this.color = color;
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

    public void resaltar() {
        super.setBackground(new Background(new BackgroundFill(Color.STEELBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public void desResaltar() {
        super.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }
}
