package application;

import java.util.Scanner;

public abstract class Exercise {
	// protected hace que solo esta clase y sus hijas...
	// ...puedan ver la variable o funcion
	protected boolean running = true;
	protected int currentPhase = 0;
	protected Scanner scanner;
	
	// Constructor: funcion especial que se llama...
	// ...cuando hacemos "new" (instanciamos la clase)
	// Sirven para inicializar variables cuando creamos la instancia
	// En este caso, no sabemos cual es el scanner de antemano
	// Entonces lo creamos en MainProgram y se lo pasamos al Ex.
	public Exercise(Scanner scanner) {
		// Aca hay dos variables que se llaman igual
		// La del parametro y la de la clase
		// Con "this" aclaramos que es la de esta clase
		// Guardamos el scanner del parametro en la variable...
		// ...de esta clase
		this.scanner = scanner;
	}
	
	// Esta funcion se va a llamar desde MainProgram
	// Simplemente ejecuta la logica del ejercicio
	// Y cuando nosotros lo cerremos, corta el bucle
	// A partir de ahi vuelve al menu principal
	public void run(){
		while(running) exerciseLogic();
	}
	
	// Esta funcion va a tener la logica de cada ejercicio
	// Es abstracta porque cada ejercicio va a hacer algo distinto
	// Pero todos van a tener esta funcion para su logica
	protected abstract void exerciseLogic();
	
	protected void repeatOperationCheck(String promptMessage)
	{
		boolean validInput = false;
		
		while(!validInput)
		{
			System.out.println(promptMessage);
			
			// Guardamos lo que escribio el usuario
			// Pasado a minuscula
			String userInput = scanner.nextLine().toLowerCase();
			switch(userInput)
			{
				case "s":
					validInput = true;
					break;
				case "n":
					currentPhase = 0;
					validInput = true;
					break;
				default:
					System.out.println("\nRespuesta Invalida.");
					break;
			}
		}
	}
}
