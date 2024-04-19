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


    public Vista(Stage stage){
        Label scoreLabel = new Label();
        Label nivelLabel = new Label();
        HBox menu = new HBox(new Label("score: "), scoreLabel, new Label("nivel: "), nivelLabel);
        menu.setAlignment(Pos.CENTER);

        Tablero tablero = new Tablero(new Coordenada(10, 10), 1);
        Grilla grilla = new Grilla(tablero);
        HBox botones = obtenerBotones(tablero, grilla);
        VBox root = new VBox(grilla, botones);
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        Scene scene = new Scene(root);
        HashSet<KeyCode> teclaPresionada = new HashSet<>();
        scene.setOnKeyPressed(keyEvent -> {teclaPresionada.add(keyEvent.getCode());});
        new AnimationTimer() {
            @Override
            public void handle(long ignored) {
                for (KeyCode keyCode : teclaPresionada) {
                    Action action = controles.get(keyCode);
                    if (action != null) {
                        grilla.update(action);
                    }
                    teclaPresionada.remove(keyCode);
                }
            }
        }.start();
        stage.setTitle("Robots");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    private HBox obtenerBotones(Tablero tablero, Grilla g) {
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

    private void mostrarMenu() {
        Stage stage = new Stage();
        VBox root = new VBox(new Label("Hola"));
        Scene scene = new Scene(root, 20, 20);
        stage.setScene(scene);
        stage.show();
    }

}

