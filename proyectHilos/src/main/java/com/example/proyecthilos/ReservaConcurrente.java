package com.example.proyecthilos;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ReservaConcurrente extends Application {
    private static final int TOTAL_ENTRADAS = 20;
    private static final int INTENTOS_POR_AGENTE = 10;
    private static final int TOTAL_AGENTES = 3;

    private int entradasDisponibles = TOTAL_ENTRADAS;
    private int reservasConfirmadas = 0;
    private int solicitudesRechazadas = 0;

    private Label lblDisponibles;
    private Label lblConfirmadas;
    private Label lblRechazadas;
    private Label lblEstado;
    private Button btnIniciar;
    private TextArea txtRegistro;

    private final ExecutorService ejecutor = Executors.newFixedThreadPool(TOTAL_AGENTES);
    private final AtomicInteger agentesTerminados = new AtomicInteger(0);
    private boolean simulacionActiva = false;

    @Override
    public void start(Stage escenario) {
        Label titulo = new Label("Sistema de Reserva de Entradas");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        lblDisponibles = new Label();
        lblConfirmadas = new Label();
        lblRechazadas = new Label();

        GridPane panelDatos = new GridPane();
        panelDatos.setHgap(15);
        panelDatos.setVgap(15);

        panelDatos.add(new Label("Entradas disponibles:"), 0, 0);
        panelDatos.add(lblDisponibles, 1, 0);

        panelDatos.add(new Label("Reservas confirmadas:"), 0, 1);
        panelDatos.add(lblConfirmadas, 1, 1);

        panelDatos.add(new Label("Solicitudes rechazadas:"), 0, 2);
        panelDatos.add(lblRechazadas, 1, 2);

        btnIniciar = new Button("Iniciar simulación");
        btnIniciar.setOnAction(event -> iniciarSimulacion());
        lblEstado = new Label("Estado: listo para iniciar.");

        txtRegistro = new TextArea();
        txtRegistro.setEditable(false);
        txtRegistro.setWrapText(true);
        txtRegistro.setPromptText("Aquí aparecerán los resultados...");

        VBox contenedor = new VBox(
                15,
                titulo,
                panelDatos,
                btnIniciar,
                lblEstado,
                new Label("Registro de actividad:"),
                txtRegistro
        );

        contenedor.setPadding(new Insets(20));
        contenedor.setAlignment(Pos.CENTER_LEFT);

        actualizarDatosVisuales();

        Scene escena = new Scene(contenedor, 550, 430);
        escenario.setTitle("Reserva Concurrente");
        escenario.setScene(escena);
        escenario.setMinWidth(500);
        escenario.setMinHeight(400);
        escenario.show();
    }

    private void iniciarSimulacion() {
        if (simulacionActiva) {
            return;
        }

        simulacionActiva = true;
        btnIniciar.setDisable(true);
        lblEstado.setText("Estado: simulación en ejecución...");
        txtRegistro.clear();

        synchronized (this) {
            entradasDisponibles = TOTAL_ENTRADAS;
            reservasConfirmadas = 0;
            solicitudesRechazadas = 0;
        }

        agentesTerminados.set(0);
        actualizarDatosVisuales();
        txtRegistro.appendText("Comienza la simulación con 20 entradas.\n");

        for (int i = 1; i <= TOTAL_AGENTES; i++) {
            Task<Void> tarea = getTarea(i);
            ejecutor.submit(tarea);
        }
    }

    private Task<Void> getTarea(int i) {
        final int numeroAgente = i;
        Task<Void> tarea = new Task<>() {

            @Override
            protected Void call() throws Exception {
                for (int intento = 1; intento <= INTENTOS_POR_AGENTE; intento++) {
                    Thread.sleep(100);
                    reservar(numeroAgente, intento);
                }
                return null;
            }
        };

        tarea.setOnSucceeded(event -> finalizarAgente());

        tarea.setOnFailed(event -> {
            Throwable error = tarea.getException();
            txtRegistro.appendText(
                    "Error en el agente " + numeroAgente
                            + ": " + error + "\n"
            );
            finalizarAgente();
        });
        return tarea;
    }

    private synchronized void reservar(int agente, int intento) {
        String resultado;
        if (entradasDisponibles > 0) {
            entradasDisponibles--;

            reservasConfirmadas++;

            resultado = "Agente " + agente
                    + ", intento " + intento
                    + ": RESERVA CONFIRMADA.";

        } else {
            solicitudesRechazadas++;
            resultado = "Agente " + agente
                    + ", intento " + intento
                    + ": SOLICITUD RECHAZADA (sin entradas).";
        }

        final int disponibles = entradasDisponibles;
        final int confirmadas = reservasConfirmadas;
        final int rechazadas = solicitudesRechazadas;
        final String mensaje = resultado;

        Platform.runLater(() -> {
            lblDisponibles.setText(String.valueOf(disponibles));
            lblConfirmadas.setText(String.valueOf(confirmadas));
            lblRechazadas.setText(String.valueOf(rechazadas));

            txtRegistro.appendText(mensaje + "\n");
        });
    }

    private void actualizarDatosVisuales() {
        synchronized (this) {
            lblDisponibles.setText(
                    String.valueOf(entradasDisponibles)
            );
            lblConfirmadas.setText(
                    String.valueOf(reservasConfirmadas)
            );
            lblRechazadas.setText(
                    String.valueOf(solicitudesRechazadas)
            );
        }
    }

    private void finalizarAgente() {
        int terminados = agentesTerminados.incrementAndGet();

        if (terminados == TOTAL_AGENTES) {
            simulacionActiva = false;
            btnIniciar.setDisable(false);
            lblEstado.setText("Estado: simulación terminada.");

            synchronized (this) {
                txtRegistro.appendText("\n--- RESUMEN FINAL ---\n");
                txtRegistro.appendText(
                        "Reservas confirmadas: "
                                + reservasConfirmadas + "\n");
                txtRegistro.appendText(
                        "Solicitudes rechazadas: "
                                + solicitudesRechazadas + "\n");
                txtRegistro.appendText(
                        "Entradas disponibles: "
                                + entradasDisponibles + "\n");

                boolean inventarioCorrecto =
                        entradasDisponibles >= 0
                                && entradasDisponibles
                                + reservasConfirmadas == TOTAL_ENTRADAS;

                txtRegistro.appendText(
                        "Inventario correcto: "
                                + inventarioCorrecto + "\n");

                boolean resultadosCorrectos =
                        reservasConfirmadas == 20
                                && solicitudesRechazadas == 10
                                && entradasDisponibles == 0;

                txtRegistro.appendText(
                        "Prueba de aceptación: "
                                + (resultadosCorrectos ? "APROBADA" : "REVISAR")
                                + "\n"
                );
            }
        }
    }

    @Override
    public void stop() {
        ejecutor.shutdownNow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
