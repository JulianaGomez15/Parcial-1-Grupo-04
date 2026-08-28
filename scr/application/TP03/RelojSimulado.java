package TP03;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RelojSimulado {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private LocalDateTime fechaHoraActual;

    public RelojSimulado(LocalDateTime fechaInicial) {
        if (fechaInicial == null) {
            throw new IllegalArgumentException("La fecha inicial del reloj no puede ser nula.");
        }
        this.fechaHoraActual = fechaInicial;
    }

    public LocalDateTime obtenerFechaHoraActual() {
        return fechaHoraActual;
    }

    public void avanzarUnaHora() {
        fechaHoraActual = fechaHoraActual.plusHours(1);
    }

    public String toString() {
        return fechaHoraActual.format(FORMATO);
    }

}
