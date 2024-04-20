package org.example.vista;

import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.modelo.Coordenada;
import org.example.modelo.Direccion;
import org.example.modelo.Tablero;

import java.util.*;

public class Vista {
    final Map<KeyCode, Action> controles = Map.of(
            KeyCode.A, new ActionMover(Direccion.IZQUIERDA),
            KeyCode.D, new ActionMover(Direccion.DERECHA),
            KeyCode.W, new ActionMover(Direccion.ARRIBA),
            KeyCode.S, new ActionMover(Direccion.ABAJO)
    );
//    private final Label scoreLabel;
    private final Label nivelLabel;
    private final Label safeTeleportRestante;


    public Vista(Stage stage){
        Label scoreLabel = new Label();
        nivelLabel = new Label();
        HBox layoutSuperior = new HBox(scoreLabel, nivelLabel);
        layoutSuperior.setAlignment(Pos.CENTER);
        layoutSuperior.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        Tablero tablero = new Tablero(new Coordenada(10, 10), 1);
        Grilla grilla = new Grilla(tablero);

        HBox botones = obtenerBotones(grilla);

        safeTeleportRestante = new Label();
        HBox layoutInfo = new HBox(safeTeleportRestante);
        layoutInfo.setAlignment(Pos.CENTER);
        layoutInfo.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        VBox root = new VBox(layoutSuperior, grilla, botones, layoutInfo);
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(root);
        setMainLoop(scene, grilla, tablero);
        stage.setTitle("Robots");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private void setMainLoop(Scene scene, Grilla grilla, Tablero tablero) {
        HashSet<KeyCode> teclaPresionada = new HashSet<>();
        scene.setOnKeyPressed(keyEvent -> {teclaPresionada.add(keyEvent.getCode());});
        new AnimationTimer() {
            @Override
            public void handle(long ignored) {
                if (tablero.jugadorSigueVivo()) {
                    nivelLabel.setText("nivel: " + tablero.getNivelActual());
                    safeTeleportRestante.setText("teletransportes restantes: " + tablero.getJugador().getTeletransportacionesDisponibles());
                    for (KeyCode keyCode : teclaPresionada) {
                        Action action = controles.get(keyCode);
                        if (action != null) {
                            grilla.update(action);
                        }
                        teclaPresionada.remove(keyCode);
                    }
                } else {
                    System.out.println("Perdiste");
                }
            }
        }.start();
    }

    private HBox obtenerBotones(Grilla g) {
        Button safeTeleportButton = new Button("Teleport Safely");
        HBox.setHgrow(safeTeleportButton, Priority.ALWAYS);
        safeTeleportButton.setMaxWidth(Double.MAX_VALUE);
        safeTeleportButton.setOnAction(ignored -> {
            g.fireEvent(new SafeTeleportEvent());
        });

        Button randomTeleportButton = new Button("Teleport Randomly");
        HBox.setHgrow(randomTeleportButton, Priority.ALWAYS);
        randomTeleportButton.setMaxWidth(Double.MAX_VALUE);
        randomTeleportButton.setOnAction(ignored -> {
            g.update(new ActionTeleport());
        });

        Button waitButton = new Button("Wait for robots");
        HBox.setHgrow(waitButton, Priority.ALWAYS);
        waitButton.setMaxWidth(Double.MAX_VALUE);
        waitButton.setOnAction(ignored -> {
            g.update(new ActionEsperar());
        });

        return new HBox(safeTeleportButton, randomTeleportButton, waitButton);
    }

}

