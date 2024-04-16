package org.example.vista;

import javafx.geometry.Insets;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

public class Casilla extends StackPane {
    int x,y;
    private boolean ocupada;

    Casilla(int x, int y, Color color) {
        super();

        this.x = x;
        this.y = y;
        super.setPrefSize(Grilla.LADO_CASILLA, Grilla.LADO_CASILLA);
        super.setMaxSize(Grilla.LADO_CASILLA, Grilla.LADO_CASILLA);
        super.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
    }

    public boolean estaOcupada() {
        return ocupada;
    }

    public void agregarEntidad(String tipoEntidad) {
        super.getChildren().add(new Entidad(x, y, tipoEntidad));
        ocupada = true;
    }

}
