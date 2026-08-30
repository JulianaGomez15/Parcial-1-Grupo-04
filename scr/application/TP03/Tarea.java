package TP03;

import java.time.LocalDateTime;

public class Tarea {

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
        return titulo + " " + "Creada: " + this.fechaCreacion + " " + estado;
    }

}
