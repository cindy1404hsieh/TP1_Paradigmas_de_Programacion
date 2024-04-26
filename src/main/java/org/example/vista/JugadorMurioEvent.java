package org.example.vista;

import javafx.event.Event;
import javafx.event.EventType;

public class JugadorMurioEvent extends Event {
    public static final EventType<JugadorMurioEvent> JUGADOR_MURIO = new EventType<>("JUGADOR_MURIO");

    JugadorMurioEvent() {
        super(JUGADOR_MURIO);
    }
}

