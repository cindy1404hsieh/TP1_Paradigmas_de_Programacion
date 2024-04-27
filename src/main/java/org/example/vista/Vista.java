package org.example.vista;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
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
            KeyCode.S, new ActionMover(Direccion.ABAJO),
            KeyCode.Q, new ActionMover(Direccion.ARRIBA_IZQUIERDA),
            KeyCode.E, new ActionMover(Direccion.ARRIBA_DERECHA),
            KeyCode.Z, new ActionMover(Direccion.ABAJO_IZQUIERDA),
            KeyCode.C, new ActionMover(Direccion.ABAJO_DERECHA)
    );
    private Label scoreLabel;
    private Label nivelLabel;
    private final Label safeTeleportRestante;
    private Tablero tablero;
    private Grilla grilla;


    public Vista(Stage stage){
        tablero = new Tablero(new Coordenada(10, 10), 1);
        grilla = new Grilla(tablero);
        HBox layoutSuperior = obtenerLayoutSuperior();
        HBox botones = obtenerBotones();

        safeTeleportRestante = new Label();
        HBox layoutInfo = new HBox(safeTeleportRestante);
        layoutInfo.setAlignment(Pos.CENTER);
        layoutInfo.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        VBox root = new VBox(layoutSuperior, grilla, botones, layoutInfo);
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.SILVER, CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(root);
        Stage pantallaFinDeJuego = obtenerPantallaFinDeJuego(scene);

        grilla.addEventHandler(JugadorMurioEvent.JUGADOR_MURIO, e -> {
            if (!pantallaFinDeJuego.isShowing()) pantallaFinDeJuego.show();
        });

        setMainLoop(scene);
        stage.setTitle("Robots");
        stage.setScene(scene);
        stage.show();

        stage.setOnHidden(e -> {
            pantallaFinDeJuego.close();
            System.exit(0);
        });
    }

    /**
     * Inicializa el loop del juego.
     */
    private void setMainLoop(Scene scene) {
        HashSet<KeyCode> teclaPresionada = new HashSet<>();
        scene.setOnKeyPressed(keyEvent -> {teclaPresionada.add(keyEvent.getCode());});

        new AnimationTimer() {
            @Override
            public void handle(long ignored) {
                nivelLabel.setText("Nivel: " + tablero.getNivelActual());
                safeTeleportRestante.setText("Teletransportaciones Restantes: " + tablero.getJugador().getTeletransportacionesDisponibles());

                for (KeyCode keyCode : teclaPresionada) {
                    Action action = controles.get(keyCode);
                    if (action != null) grilla.aplicarAccion(action);
                    teclaPresionada.remove(keyCode);
                }
                /*if (!tablero.jugadorSigueVivo()) {
                    grilla.fireEvent(new JugadorMurioEvent());
                }*/
                if (tablero.getRobots().isEmpty()) tablero.siguienteNivel();
            }
        }.start();
    }

    /**
     * Crea y carga una pantalla de fin de juego, con la opcion de reiniciar la partida.
     */
    private Stage obtenerPantallaFinDeJuego(Scene scene) {
        Label mensaje = new Label("Perdiste el juego :(");
        Button intentarDeNuevo = new Button("Intentar de nuevo?");
        Stage finDelJuegoStage = new Stage();

        intentarDeNuevo.setOnAction(e -> {
            grilla.fireEvent(new ReiniciarJuegoEvent());
            tablero.getJugador().setTeletransportacionesDisponibles(1);
            finDelJuegoStage.close();
        });
        finDelJuegoStage.setOnCloseRequest(e -> {
            System.exit(0);
        });
        finDelJuegoStage.setScene(new Scene(new VBox(mensaje, intentarDeNuevo)));
        finDelJuegoStage.setTitle("Fin del Juego");
        finDelJuegoStage.initModality(Modality.WINDOW_MODAL);
        finDelJuegoStage.initOwner(scene.getWindow());
        finDelJuegoStage.setResizable(false);
        return finDelJuegoStage;
    }

    /**
     * Crea y carga una pantalla aparte para configurar el tamaño de la grilla.
     */
    public void cargarPantallaMenu(Event event) {
        TextField columnasTextfield = new TextField("10");
        TextField filasTextfield = new TextField("10");
        HBox root = new HBox(new Label("Columnas"), columnasTextfield, new Label("filas"), filasTextfield);
        Button okButton = new Button("Aceptar");
        Stage menuStage = new Stage();

        okButton.setOnAction(ignored -> {
            int columnas = Integer.parseInt(columnasTextfield.getText());
            int filas = Integer.parseInt(filasTextfield.getText());
            tablero.cambiarTamanio(new Coordenada(filas, columnas));
            grilla.getChildren().clear();
            grilla.dibujarGrilla();
            grilla.dibujarEntidades();
            menuStage.close();
        });

        menuStage.setScene(new Scene(new VBox(root, okButton)));
        menuStage.setTitle("Ajustar tamaño");
        menuStage.initModality(Modality.WINDOW_MODAL);
        menuStage.initOwner(((Node)event.getSource()).getScene().getWindow());
        menuStage.setResizable(false);
        menuStage.show();
    }

    /**
     * Devuelve la hilera de botones con sus funcionalidades inicializadas.
     */
    private HBox obtenerBotones() {
        Button safeTeleportButton = new Button("Teleport Safely");
        HBox.setHgrow(safeTeleportButton, Priority.ALWAYS);
        safeTeleportButton.setMaxWidth(Double.MAX_VALUE);
        safeTeleportButton.setOnAction(ignored -> {
            grilla.fireEvent(new SafeTeleportEvent());
        });

        Button randomTeleportButton = new Button("Teleport Randomly");
        HBox.setHgrow(randomTeleportButton, Priority.ALWAYS);
        randomTeleportButton.setMaxWidth(Double.MAX_VALUE);
        randomTeleportButton.setOnAction(ignored -> {
            grilla.aplicarAccion(new ActionTeleport());
        });

        Button waitButton = new Button("Wait for robots");
        HBox.setHgrow(waitButton, Priority.ALWAYS);
        waitButton.setMaxWidth(Double.MAX_VALUE);
        waitButton.setOnAction(ignored -> {
            grilla.update();
        });

        return new HBox(safeTeleportButton, randomTeleportButton, waitButton);
    }

    /**
     * crea y devuelve la parte superior de la interfaz grafica.
     */
    private HBox obtenerLayoutSuperior() {
        scoreLabel = new Label();
        nivelLabel = new Label();
        HBox layoutLabels = new HBox(scoreLabel, nivelLabel);
        HBox.setHgrow(layoutLabels, Priority.ALWAYS);
        layoutLabels.setMaxWidth(Double.MAX_VALUE);
        layoutLabels.setAlignment(Pos.CENTER);

        Button menuButton = new Button("MENU");
        menuButton.setOnAction(this::cargarPantallaMenu);

        HBox layoutSuperior = new HBox(layoutLabels, menuButton);
        layoutSuperior.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        return layoutSuperior;
    }

}

