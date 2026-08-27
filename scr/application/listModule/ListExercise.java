package application.listModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import application.Exercise;

public class ListExercise extends Exercise {
	private List<String> list = new ArrayList<String>();
	private boolean firstTime = true;
	
	public ListExercise(Scanner scanner) {
		super(scanner);
	}

	@Override
	protected void exerciseLogic() {
		switch(currentPhase)
		{
			case 0:
				menuLogic();
				break;
			case 1:
				addLogic();
				break;
			case 2:
				removeIndexLogic();
				break;
			case 3:
				removeRefLogic();
				break;
			case 4:
				clearLogic();
				break;
		}
	}

	private void menuLogic()
	{
		if(firstTime)
		{
			System.out.println("\nBienvenido al ListExercise!");
			firstTime = false; 
		}
		else
		{
			printStatus();
			printList();
		}
		
		System.out.println("\nSeleccione una opcion: "
				+ "\nadd: Agregar un elemento."
				+ "\nremove index: Remover por indice."
				+ "\nremove ref: Remover por referencia."
				+ "\nclear: Limpiar la lista."
				+ "\nmm: Volver al menu");
		
		String userInput = scanner.nextLine().toLowerCase();
		
		switch(userInput)
		{
			case "add":
				currentPhase = 1;
				break;
			case "remove index":
				currentPhase = 2;
				break;
			case "remove ref":
				currentPhase = 3;
				break;
			case "clear":
				currentPhase = 4;
				break;
			case "mm":
				running = false;
				break;
			default:
				System.out.println("\nRespuesta invalida.");
				break;
		}
	}
	
	private void addLogic()
	{
		System.out.println("\nIngrese el valor a agregar:");
		
		String value = scanner.nextLine();
		list.add(value);
		
		repeatOperationCheck("\nAgregar otro elemento? s/n");
	}
	
	private void removeIndexLogic()
	{
		// Si la lista esta vacia, no podemos remover nada
		if(list.isEmpty())
		{
			System.out.println("\nLa lista esta vacia, no se puede remover.");
			currentPhase = 0;
			return;
		}
		
		System.out.println("\nIngrese el indice a remover:");
				
		while(!scanner.hasNextInt()){
			System.out.println("\nRespuesta invalida, ingrese un numero.");
			scanner.nextLine();
		}
		
		int index = scanner.nextInt();
		scanner.nextLine(); // Limpiar el buffer

		if(index < 0 || index >= list.size()){
			System.out.println("\nIndice invalido: menor a 0 o mayor a la cantidad de elementos.");
			return;
		}
		String element = list.remove(index);
		System.out.println("\n" + element + " removido.");

		
		repeatOperationCheck("\nRemover otro elemento? s/n");
	}
	
	private void removeRefLogic()
	{
		// Si la lista esta vacia, no podemos remover nada
		if(list.isEmpty())
		{
			System.out.println("\nLa lista esta vacia, no se puede remover.");
			currentPhase = 0;
			return;
		}
				
		System.out.println("\nIngrese el elemento a remover:");
				
		String value = scanner.nextLine();
				
		if(list.remove(value))
			System.out.println("\n" + value + " removido.");
		else
			System.out.println("\n" + value + " no existe en la lista.");
				
		repeatOperationCheck("\nRemover otro elemento? s/n");
	}
	
	private void clearLogic()
	{
		if(list.isEmpty())
			System.out.println("\nLa lista esta vacia, no se puede vaciar.");
		else
		{
			list.clear();
			System.out.println("\nLista vaciada exitosamente.");
		}

		currentPhase = 0;
	}
	
	private void printList()
	{
		String message = "";
		
		for(int i = 0; i < list.size(); i++)
		{
			message += list.get(i);
			if(i != list.size()-1)
				message += ", ";
		}
		
		System.out.println(message);
	}
	
	private void printStatus()
	{
		if(list.isEmpty())
			System.out.println("\nLa lista esta vacia.");
		else
			System.out.println("\nTamaño de la lista: " + list.size());		
	}
}
