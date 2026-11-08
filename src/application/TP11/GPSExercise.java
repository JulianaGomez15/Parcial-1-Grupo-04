package application.TP11;

import java.util.Scanner;

import application.Exercise;
import application.dictionaryModule.SimpleDictionary;
import application.graphModule.DijkstraSolver;
import application.graphModule.Edge;
import application.graphModule.Graph;
import application.graphModule.ListGraph;
import application.listModule.SimpleArrayList;
import application.listModule.SimpleList;
import application.stackModule.SimpleLinkedStack;
import application.stackModule.SimpleStack;

public class GPSExercise extends Exercise {

    private static final String ORIGEN = "UADE Belgrano";
    private static final String DESTINO = "UADE Monserrat";

    // Usamos el Graph/ListGraph y el DijkstraSolver tal cual estan en graphModule.
    private final Graph<String> grafo = new ListGraph<>();
    private final DijkstraSolver<String> dijkstra = new DijkstraSolver<>();
    private boolean firstTime = true;

    public GPSExercise(Scanner scanner) {
        super(scanner);

        // Mapa pre-cargado con las sedes de UADE y las distancias (en km) entre ellas.
        // Las conexiones son de ida y vuelta, por eso se cargan en ambos sentidos.
        agregarConexion("UADE Belgrano", "UADE Recoleta", 6);
        agregarConexion("UADE Belgrano", "UADE Monserrat", 9);
        agregarConexion("UADE Recoleta", "UADE Monserrat", 4);
        agregarConexion("UADE Monserrat", "UADE Pinamar", 340);
        agregarConexion("UADE Recoleta", "UADE Pinamar", 345);
    }

    private void agregarConexion(String sedeA, String sedeB, int distancia) {
        grafo.addEdge(sedeA, sedeB, distancia);
        grafo.addEdge(sedeB, sedeA, distancia);
    }

    @Override
    protected void exerciseLogic() {
        menuLogic();
    }

    private void menuLogic() {
        if (firstTime) {
            System.out.println("\nBienvenido al GPS de sedes UADE!");
            mostrarGrafo();
            firstTime = false;
        }

        System.out.println("\nSeleccione una opcion:"
                + "\ncalcular: Calcular el camino mas corto de " + ORIGEN + " a " + DESTINO
                + "\nmostrar: Mostrar el grafo completo"
                + "\nmm: Volver al menu principal");

        String userInput = scanner.nextLine().trim().toLowerCase();

        switch (userInput) {
            case "calcular":
                calcularLogic();
                break;
            case "mostrar":
                mostrarGrafo();
                break;
            case "mm":
                running = false;
                break;
            default:
                System.out.println("\nRespuesta invalida.");
                break;
        }
    }

    private void calcularLogic() {
        // DijkstraSolver.dijkstraAllNodes devuelve, para cada vertice, un Edge
        // donde "destination" es el nodo anterior en el camino mas corto desde
        // ORIGEN, y "weight" es el costo total acumulado hasta ese vertice.
        SimpleDictionary<String, Edge<String>> tabla = dijkstra.dijkstraAllNodes(ORIGEN, grafo);

        if (tabla.get(DESTINO).weight == Integer.MAX_VALUE) {
            System.out.println("\nNo hay camino posible entre " + ORIGEN + " y " + DESTINO + ".");
            return;
        }

        SimpleList<String> camino = reconstruirCamino(tabla, DESTINO);

        System.out.print("\nCamino mas corto: ");
        for (int i = 0; i < camino.size(); i++) {
            System.out.print(camino.get(i));
            if (i != camino.size() - 1) System.out.print(" -> ");
        }
        System.out.println();
        System.out.println("Distancia total: " + tabla.get(DESTINO).weight + " km");
    }

    // Recorre la tabla de Dijkstra de atras para adelante (de destino a origen,
    // siguiendo el "anterior" de cada nodo) y arma el camino en el orden correcto
    // usando una pila para invertirlo.
    private SimpleList<String> reconstruirCamino(SimpleDictionary<String, Edge<String>> tabla, String destino) {
        SimpleStack<String> pila = new SimpleLinkedStack<>();

        String actual = destino;
        while (actual != null) {
            pila.push(actual);
            actual = tabla.get(actual).destination;
        }

        SimpleList<String> camino = new SimpleArrayList<>();
        while (!pila.isEmpty()) {
            camino.add(pila.pop());
        }
        return camino;
    }

    private void mostrarGrafo() {
        System.out.println("\nGrafo de sedes:");
        SimpleList<String> vertices = grafo.vertices();

        for (int i = 0; i < vertices.size(); i++) {
            String origen = vertices.get(i);
            SimpleList<Edge<String>> vecinos = grafo.getNeighbors(origen);

            for (int j = 0; j < vecinos.size(); j++) {
                Edge<String> vecino = vecinos.get(j);
                System.out.println(origen + " -> " + vecino.destination + ": " + vecino.weight + " km");
            }
        }
    }
}
