package org.example.vista;

import javafx.event.Event;
import javafx.event.EventType;

public class ReiniciarJuegoEvent extends Event {
    public static final EventType<ReiniciarJuegoEvent> RESET = new EventType<>("RESET");

    ReiniciarJuegoEvent() {
        super(RESET);
    }
}
