package org.example.vista;

import javafx.event.Event;
import javafx.event.EventType;

public class SafeTeleportEvent extends Event {
    public static final EventType<SafeTeleportEvent> SAFE_TELEPORT = new EventType<>("SAFE_TELEPORT");

    public SafeTeleportEvent() {
        super(SAFE_TELEPORT);
    }
}
