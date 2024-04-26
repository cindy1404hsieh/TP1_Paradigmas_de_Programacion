package org.example.vista;

import javafx.event.Event;
import javafx.event.EventType;
import org.example.modelo.Coordenada;
import org.example.modelo.Tablero;

public class ActionSafeTeleport implements Action{
    private final Coordenada coordenada;

    ActionSafeTeleport(Coordenada coordenada) {
        this.coordenada = coordenada;
    }

    @Override
    public void apply(Tablero tablero) {
        tablero.getJugador().teletransportarseSeguro(tablero, coordenada);
        tablero.moverRobots();
    }
}

class SafeTeleportEvent extends Event {
    public static final EventType<SafeTeleportEvent> SAFE_TELEPORT = new EventType<>("SAFE_TELEPORT");
    public SafeTeleportEvent() {
        super(SAFE_TELEPORT);
    }
}

class CeldaSeleccionadaEvent extends Event {
    public static final EventType<CeldaSeleccionadaEvent> CELDA_SELECCIONADA = new EventType<>("CELDA_SELECCIONADA");
    public CeldaSeleccionadaEvent() {
        super(CELDA_SELECCIONADA);
    }

}

