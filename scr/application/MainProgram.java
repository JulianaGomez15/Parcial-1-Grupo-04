package application;

import java.util.Scanner;

import application.TP03.TareaExercise;
import application.listModule.ListExercise;

public class MainProgram {
	private boolean running = true;
	private Exercise exercise;

	public static void main(String[] args) {
		new MainProgram().run();
	}
	
	private void run() 
	{
		Scanner scanner = new Scanner(System.in);
		while(running)
		{
			// Le pedimos al usuario que elija un ejercicio
			selectExercise(scanner);
			
			// Si hay uno elegido, lo corremos
			if(exercise != null)
				exercise.run();
			
		}
		scanner.close();
	}
	
	private void selectExercise(Scanner scanner) 
	{
		System.out.println("Selecciona un ejercicio:"
				+ "\n0: TestExercise"
				+ "\n1: ListExercise"
				+ "\n2: TP03 - Lista de tareas"
				+ "\n3: Salir");
		
		// Guardamos lo que escribe el usuario
		String userInput = scanner.nextLine();
		
		// Preguntamos por las distintas opciones
		switch(userInput)
		{
			case "0":
				exercise = new TestExercise(scanner);
				break;
			case "1":
				exercise = new ListExercise(scanner);
				break;
			case "2":
				exercise = new TareaExercise(scanner);
				break;
			case "3":
				running = false;
				break;
			default:
				System.out.println("\nRespuesta invalida");
				break;
		}
	}

}
