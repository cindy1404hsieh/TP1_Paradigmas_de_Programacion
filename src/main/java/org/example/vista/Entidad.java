package org.example.vista;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Entidad extends ImageView {
    Entidad(String tipoEntidad) {
        File f = new File("src/main/java/org/example/vista/imagen/" + tipoEntidad + ".png");
        super.setImage(new Image(f.toURI().toString()));
        super.setFitHeight(Grilla.LADO_CASILLA);
        super.setFitWidth(Grilla.LADO_CASILLA);
    }

}
