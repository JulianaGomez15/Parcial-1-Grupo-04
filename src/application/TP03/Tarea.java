package application.TP03;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tarea {
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    protected String titulo;
    protected LocalDateTime fechaCreacion;
    protected LocalDateTime fechaCompletada;
    protected Boolean completada = false;
    protected LocalDateTime fechaLimite;

    public Tarea(String tit, LocalDateTime fCreacion, LocalDateTime fCompletada, LocalDateTime fLimite) {
        if (tit == null || tit.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        titulo = tit;
        fechaCreacion = fCreacion; /* fecha de ahora */
        fechaCompletada = fCompletada;
        fechaLimite = fLimite;
    }

    // ahora viene del reloj simulado, no de LocalDateTime.now()
    protected void setCompletada(LocalDateTime ahora) {
        this.completada = true;
        this.fechaCompletada = ahora;
        /* setear fecha de complitud */
    }

    public boolean estaAtrasada(LocalDateTime ahora) {
        return !completada && fechaLimite != null && ahora.isAfter(fechaLimite);
    }

    public String getTitulo() {
        return titulo;
    }

    public String toString() {
        String estado = completada ? "COMPLETADO" : "PENDIENTE";
        String texto = titulo + " " + "Creada: " + this.fechaCreacion.format(FORMATO_FECHA) + " " + estado;
        if (fechaLimite != null) {
            texto += " | Limite: " + fechaLimite.format(FORMATO_FECHA);
        }
        return texto;
    }

}
