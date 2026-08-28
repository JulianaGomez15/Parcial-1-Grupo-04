package TP03;

import TP03.Menu;

public class main {
	public static void main(String[] args) {
		ListaTarea tareas = new ListaTarea();
		Menu menu = new Menu(tareas);
		menu.run();
	}
}