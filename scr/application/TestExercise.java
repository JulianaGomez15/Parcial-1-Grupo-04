package application;

import java.util.Scanner;

public class TestExercise extends Exercise {

	// super llama a la misma funcion del padre
	// En este caso el constrictor
	public TestExercise(Scanner scanner) {
		super(scanner);
	}

	// run es heredada, no la vemos pero esta
	
	// exerciseLogic era abstracta en Exercise
	// Ahora tenemos que completar con algo
	@Override
	protected void exerciseLogic() {
		System.out.println("Bienvenido al Test Exercise"
				+ "\n Volver al menu? s/n");
		
		// Scanner.nextLine lee lo que escribe el usuario
		String input = scanner.nextLine();
		
		// Si el usuario ingresa "s", corta la ejecucion
		// Si no, sigue preguntando
		if(input.equals("s")) running = false;
		else if(!input.equals("n"))
		{
			System.out.println("Respuesta invalida, intentelo nuevamente");
		}	
	}

}
