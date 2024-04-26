package org.example.vista;

import javafx.event.Event;
import javafx.event.EventType;

public class CeldaSeleccionadaEvent extends Event {
    public static final EventType<CeldaSeleccionadaEvent> CELDA_SELECCIONADA = new EventType<>("CELDA_SELECCIONADA");
    private final int i;
    private final int j;

    public CeldaSeleccionadaEvent(int i, int j) {
        super(CELDA_SELECCIONADA);
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }
    public int getJ() {
        return j;
    }
}
