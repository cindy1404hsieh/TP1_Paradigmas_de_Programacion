package org.example.vista;

import javafx.scene.image.ImageView;

public class Entidad extends ImageView {
    private int x;
    private int y;

    Entidad(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
