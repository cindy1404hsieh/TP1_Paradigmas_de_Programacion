package org.example.vista;

import javafx.animation.AnimationTimer;
import javafx.event.Event;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.example.modelo.Coordenada;
import org.example.modelo.Direccion;
import org.example.modelo.Tablero;

import java.awt.*;
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
    private Label nivelLabel;
    private final Label safeTeleportRestante;
    private final Tablero tablero;
    private final Grilla grilla;
    private final Stage stage;
    private final Coordenada maxSize;

    public Vista(Stage stage) {

        this.stage = stage;
        this.tablero = new Tablero(new Coordenada(10, 10), 1);
        this.grilla = new Grilla(tablero);
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

        // Debo esperar a que se muestre la pantalla para obtener altura de los panes.
        maxSize = getMaxSize(layoutSuperior, botones, layoutInfo);

        stage.setOnHidden(e -> {
            pantallaFinDeJuego.close();
            System.exit(0);
        });
    }

    private Coordenada getMaxSize(HBox layoutSuperior, HBox botones, HBox layoutInfo) {
        int screenWidth = (int)(Screen.getPrimary().getVisualBounds().getWidth() / Grilla.LADO_CASILLA);
        int screenHeigth = (int)((Screen.getPrimary().getVisualBounds().getHeight() - layoutSuperior.getHeight() - botones.getHeight() - layoutInfo.getHeight()) / Grilla.LADO_CASILLA);

        // Debido a que la division no es exacta, le resto una casilla por las dudas.
        return new Coordenada(screenHeigth - 1, screenWidth);
    }

    /**
     * Inicializa el loop del juego.
     */
    private void setMainLoop(Scene scene) {
        HashSet<KeyCode> teclaPresionada = new HashSet<>();
        scene.setOnKeyPressed(keyEvent -> {
            teclaPresionada.add(keyEvent.getCode());
        });

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
                if (!tablero.jugadorSigueVivo()) {
                    grilla.fireEvent(new JugadorMurioEvent());
                }
                if (tablero.getRobots().isEmpty()) {
                    tablero.siguienteNivel();
                    grilla.getChildren().clear();
                    grilla.dibujarGrilla();
                    grilla.dibujarEntidades();
                    siguienteNivelAlert();

                }
            }
        }.start();
    }

    private void siguienteNivelAlert() {
        Alert siguienteNivelAlert = new Alert(Alert.AlertType.CONFIRMATION);
        siguienteNivelAlert.setTitle("Felicidades!");
        siguienteNivelAlert.setHeaderText("Pasaste al siguiente nivel");
        siguienteNivelAlert.setContentText(":)");
        siguienteNivelAlert.show();
    }

    /**
     * Crea y carga una pantalla de fin de juego, con la opcion de reiniciar la partida.
     */
    private Stage obtenerPantallaFinDeJuego(Scene scene) {
        Label mensaje = new Label("    Perdiste el juego :(    ");
        Button intentarDeNuevo = new Button("    Intentar de nuevo?    ");
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
        Label maxTamanioLabel = new Label(String.format("Advertencia: tamaños superiores a %d X %d pueden generar errores", maxSize.getFila(), maxSize.getColumna()));
        HBox root = new HBox(new Label("Filas"), filasTextfield, new Label("Columnas"), columnasTextfield);
        Button okButton = new Button("Aceptar");
        Stage menuStage = new Stage();

        okButton.setOnAction(ignored -> {
            int columnas = Integer.parseInt(columnasTextfield.getText());
            int filas = Integer.parseInt(filasTextfield.getText());
            cambiarTamanioHandler(filas, columnas, menuStage);
            this.stage.sizeToScene();
        });

        menuStage.setScene(new Scene(new VBox(root, maxTamanioLabel, okButton)));
        menuStage.setTitle("Ajustar tamaño");
        menuStage.initModality(Modality.WINDOW_MODAL);
        menuStage.initOwner(((Node) event.getSource()).getScene().getWindow());
        menuStage.setResizable(false);
        menuStage.show();
    }

    private void cambiarTamanioHandler(int filas, int columnas, Stage menuStage) {
        Coordenada nuevaDimension = new Coordenada(filas, columnas);
        if (filas > maxSize.getFila() || columnas > maxSize.getColumna()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Tamaño superior a lo recomendado");
            alert.show();
            return;
        }
        tablero.cambiarTamanio(nuevaDimension);
        grilla.getChildren().clear();
        grilla.dibujarGrilla();
        grilla.dibujarEntidades();
        menuStage.close();
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
        nivelLabel = new Label();
        HBox layoutLabels = new HBox(nivelLabel);
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

