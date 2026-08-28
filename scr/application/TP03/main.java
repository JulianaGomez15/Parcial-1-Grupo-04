package TP03;

import java.time.LocalDateTime;
import java.util.Scanner;

public class main {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		ListaTarea tareas = new ListaTarea();
		RelojSimulado reloj = new RelojSimulado(LocalDateTime.now());
		ValidadorEntrada validador = new ValidadorEntrada(scanner);
		Menu menu = new Menu(tareas, reloj, validador);
		menu.run();
		scanner.close();
	}
}
