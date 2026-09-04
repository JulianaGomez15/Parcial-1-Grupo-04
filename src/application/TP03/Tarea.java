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
        titulo = tit;
        fechaCreacion = fCreacion; /* fecha de ahora */
        fechaCompletada = fCompletada;
        fechaLimite = fLimite;
    }

    protected void setCompletada() {
        this.completada = true;
        this.fechaCompletada = LocalDateTime.now();
        /* setear fecha de complitud */
    }

    public String getTitulo() {
        return titulo;
    }

    public String toString() {
        String estado = completada ? "COMPLETADO" : "PENDIENTE";
        return titulo + " " + "Creada: " + this.fechaCreacion.format(FORMATO_FECHA) + " " + estado;
    }

}
