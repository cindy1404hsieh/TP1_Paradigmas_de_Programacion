package org.example;

import org.example.modelo.*;

import java.util.List;
import java.util.Scanner;

//solo lo implemente para visualizar el flujo del juego, aca creo que es donde va la vista,
//despues se modificatodo esto
public class Main {
    public static final int FILA_INICIAL = 10;//ejemplo
    public static final int COLUMNA_INICIAL = 10;
    public static void main(String[] args) {
        iniciarJuego();
    }

    public static void iniciarJuego() {
        Coordenada coordenada = new Coordenada(FILA_INICIAL, COLUMNA_INICIAL);
        Tablero tablero = new Tablero(coordenada, 1);
        Jugador jugador = tablero.getJugador();
        List<Robot> r = tablero.getRobots();
        for (Robot ignored : r) {
            System.out.println("hola");
        }

        while (true) {
            try {
                //creo que aca Thread.sleep() es de la vista
                //Thread.sleep(1000);
                //hardcodeo
                Direccion direccion = Direccion.ARRIBA;
                jugador.mover(tablero, direccion);
                tablero.moverRobots();
                tablero.verificarColisiones();
                tablero.actualizarCeldasIncendiadas();

                jugador.teletransportarse(tablero);

                Coordenada destino = new Coordenada(5, 5);
                jugador.teletransportarseSeguro(tablero, destino);
                boolean juegoContinua = tablero.jugadorSigueVivo();

                if (!juegoContinua) {
                    //una prueba para ver la salida
                    //los prints son para visualizar lo que estoy haciendo
                    //despues se sacan
                    System.out.println("perdiste! queres reiniciar el juego? (Y/N)");
                    Scanner scanner = new Scanner(System.in);
                    String respuesta = scanner.nextLine();
                    if (respuesta.equalsIgnoreCase("Y")) {
                        iniciarJuego();
                    } else {
                        System.out.println("gracias por jugar.");
                        System.exit(0);
                    }
                }
                if (tablero.getRobots().isEmpty()) {
                    System.out.println("ganaste el nivel " + tablero.getNivelActual() + "!");
                    tablero.siguienteNivel();
                    System.out.println("avanzaste al nivel " + tablero.getNivelActual());
                }
            }catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }
}
