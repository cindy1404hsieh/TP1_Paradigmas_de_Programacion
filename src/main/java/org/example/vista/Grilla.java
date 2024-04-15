package org.example.vista;

import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Grilla extends TilePane {
    public static final int LADO_CASILLA = 15;
    private int columnas;
    private int filas;

    Grilla(int columnas, int filas) {
        super();
        this.columnas = columnas;
        this.filas = filas;
        crearGrilla();
    }

    private void crearGrilla(){
        super.setPrefColumns(columnas);
        super.setPrefRows(filas);

        boolean colorGrilla = true;
        super.setMaxSize(columnas*LADO_CASILLA, filas*LADO_CASILLA);
        for (int i = 0; i < filas; i++) {
            // si la cantidad de columnas es impar, una fila empieza y termina con el mismo color. En ese caso actualizo el valor de colorGrilla
            colorGrilla = (columnas % 2 != 0) == colorGrilla;
            for (int j = 0; j < columnas; j++) {
                Color color = (colorGrilla) ? Color.LIGHTBLUE : Color.BLUE;
                super.getChildren().add(new Casilla(i, j, color));
                colorGrilla = !colorGrilla;

            }
        }

    }

    public void actualizarTamanio(int columnas, int filas) {
        this.columnas = columnas;
        this.filas = filas;

        super.getChildren().clear();
        crearGrilla();
    }

    public int getColumnas() {
        return columnas;
    }

    public int getFilas() {
        return filas;
    }
}
