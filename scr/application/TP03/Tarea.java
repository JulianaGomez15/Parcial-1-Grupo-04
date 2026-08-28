package TP03;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Tarea {

    private static final DateTimeFormatter FORMATO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    protected String titulo;
    protected LocalDateTime fechaCreacion;
    protected LocalDateTime fechaCompletada;
    protected boolean completada = false;
    protected LocalDateTime fechaLimite;

    public Tarea(String tit, LocalDateTime fCreacion, LocalDateTime fCompletada, LocalDateTime fLimite) {
        if (tit == null || tit.trim().isEmpty()) {
            throw new IllegalArgumentException("El titulo no puede ser nulo ni vacio.");
        }
        if (fCreacion == null) {
            throw new IllegalArgumentException("La fecha de creacion no puede ser nula.");
        }
        if (fLimite != null && !fLimite.isAfter(fCreacion)) {
            throw new IllegalArgumentException("La fecha limite debe ser posterior a la fecha de creacion.");
        }

        titulo = tit.trim();
        fechaCreacion = fCreacion;
        fechaCompletada = fCompletada;
        fechaLimite = fLimite;
        completada = fCompletada != null;
    }

    public void marcarComoRealizada(LocalDateTime fechaActual) {
        if (fechaActual == null) {
            throw new IllegalArgumentException("La fecha de completado no puede ser nula.");
        }
        this.completada = true;
        this.fechaCompletada = fechaActual;
    }

    public void marcarComoPendiente() {
        this.completada = false;
        this.fechaCompletada = null;
    }

    public boolean estaRealizada() {
        return completada;
    }

    public boolean estaAtrasada(LocalDateTime fechaActual) {
        if (fechaActual == null || fechaLimite == null || completada) {
            return false;
        }
        return fechaActual.isAfter(fechaLimite);
    }

    public String getTitulo() {
        return titulo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaCompletada() {
        return fechaCompletada;
    }

    public LocalDateTime getFechaLimite() {
        return fechaLimite;
    }

    public String toString() {
        String estado = completada ? "COMPLETADO" : "PENDIENTE";
        String texto = titulo + " [" + estado + "]";
        if (fechaLimite != null) {
            texto += " | limite: " + fechaLimite.format(FORMATO);
        }
        if (completada && fechaCompletada != null) {
            texto += " | completada: " + fechaCompletada.format(FORMATO);
        }
        return texto;
    }

}
