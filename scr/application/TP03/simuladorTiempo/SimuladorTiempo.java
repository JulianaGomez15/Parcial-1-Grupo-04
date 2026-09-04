package application.TP03.simuladorTiempo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Representa un reloj simulado cuyo tiempo podemos controlar manualmente.
// En lugar de usar LocalDateTime.now() (tiempo real), 
// usamos este objeto para tener control total sobre el "ahora".
public class SimuladorTiempo {

	// Formato compartido para mostrar fechas en toda la simulacion
	public static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

	// El "ahora" dentro de la simulacion
	private LocalDateTime tiempoActual;

	public SimuladorTiempo(LocalDateTime tiempoInicial) {
		this.tiempoActual = tiempoInicial;
	}

	// Avanza el reloj simulado exactamente 1 hora hacia adelante
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
