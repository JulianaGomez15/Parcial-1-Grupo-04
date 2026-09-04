package application.TP03.simuladorTiempo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Reloj simulado: el "ahora" de TP03 no es la hora de la maquina.
// Asi la demo de atrasadas es reproducible y se puede avanzar de a 1 hora.
public class SimuladorTiempo {

	public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	private LocalDateTime tiempoActual;

	public SimuladorTiempo(LocalDateTime tiempoInicial) {
		if (tiempoInicial == null) {
			throw new IllegalArgumentException("El tiempo inicial no puede ser null.");
		}
		this.tiempoActual = tiempoInicial;
	}

	public void avanzarUnaHora() {
		tiempoActual = tiempoActual.plusHours(1);
	}

	public LocalDateTime getTiempoActual() {
		return tiempoActual;
	}

	public String getTiempoFormateado() {
		return tiempoActual.format(FORMATO_FECHA);
	}
}
