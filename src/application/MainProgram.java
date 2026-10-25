package application;

import java.util.Scanner;

import application.TP03.TareaExercise;
import application.TP04.AventuraExercise;
import application.TP05.ImpresionExercise;
import application.TP06.RecomendacionMusicalExercise;
import application.TP07.EmailExercise;
import application.TP08.LoginExercise;
import application.TP09.ContactosExercise;
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
		System.out.println("\n+------------------------------------------+"
				+ "\n|           AYEDD II - GRUPO 04           |"
				+ "\n+------------------------------------------+"
				+ "\n| 1: TP01 - Test Exercise                  |"
				+ "\n| 2: TP02 - List Exercise                  |"
				+ "\n| 3: TP03 - Tarea Exercise                 |"
				+ "\n| 4: TP04 - Aventura Exercise              |"
				+ "\n| 5: TP05 - Impresion Exercise             |"
				+ "\n| 6: TP06 - Recomendacion Musical Exercise |"
				+ "\n| 7: TP07 - Email Exercise                 |"
				+ "\n| 8: TP08 - Login Exercise                 |"
				+ "\n| 9: TP09 - Contactos Exercise             |"
				+ "\n+------------------------------------------+"
				+ "\n| 0: Salir                                 |"
				+ "\n+------------------------------------------+");
		
		// Guardamos lo que escribe el usuario
		String userInput = scanner.nextLine();
		
		// Preguntamos por las distintas opciones
		switch(userInput)
		{
			case "1":
				exercise = new TestExercise(scanner);
				break;
			case "2":
				exercise = new ListExercise(scanner);
				break;
			case "3":
				exercise = new TareaExercise(scanner);
				break;
			case "4":
				exercise = new AventuraExercise(scanner);
				break;
			case "5":
				exercise = new ImpresionExercise(scanner);
				break;
			case "6":
				exercise = new RecomendacionMusicalExercise(scanner);
				break;
			case "7":
				exercise = new EmailExercise(scanner);
				break;
			case "8":
				exercise = new LoginExercise(scanner);
				break;
			case "9":
				exercise = new ContactosExercise(scanner);
				break;
			case "0":
				running = false;
				break;
			default:
				System.out.println("\nRespuesta invalida");
				break;
		}
	}

}