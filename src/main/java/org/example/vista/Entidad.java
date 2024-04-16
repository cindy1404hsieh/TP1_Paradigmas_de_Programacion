package org.example.vista;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

public class Entidad extends ImageView {
    private int posX;
    private int posY;
    private boolean ocupada;

    Entidad(int x, int y, String tipoEntidad) {
        this.posX = x;
        this.posY = y;
        File f = new File("/home/tomas/IdeaProjects/tp1_algo3/src/main/java/org/example/vista/imagen/" + tipoEntidad +".png");
        super.setImage(new Image(f.toURI().toString()));
        super.setFitHeight(Grilla.LADO_CASILLA);
        super.setFitWidth(Grilla.LADO_CASILLA);
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public <T extends Event> void agregarEventListener  (EventType<T> eventType, EventHandler<? super T> eventHandler) {
        super.addEventHandler(eventType, eventHandler);
    }
}
