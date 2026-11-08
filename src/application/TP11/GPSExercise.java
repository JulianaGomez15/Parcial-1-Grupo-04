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
import application.setModule.SimpleArraySet;
import application.setModule.SimpleSet;

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
        // DijkstraSolver nos da la distancia real mas corta, para saber cual
        // de todos los caminos que encontremos es el ganador.
        SimpleDictionary<String, Edge<String>> tabla = dijkstra.dijkstraAllNodes(ORIGEN, grafo);

        if (tabla.get(DESTINO).weight == Integer.MAX_VALUE) {
            System.out.println("\nNo hay camino posible entre " + ORIGEN + " y " + DESTINO + ".");
            return;
        }

        int costoMasCorto = tabla.get(DESTINO).weight;

        // Buscamos TODOS los caminos simples (sin repetir sedes) entre origen y
        // destino, para poder mostrar las alternativas y comparar sus costos,
        // en vez de solo mostrar el resultado final.
        SimpleList<SimpleList<String>> caminos = new SimpleArrayList<>();
        SimpleList<Integer> costos = new SimpleArrayList<>();
        buscarCaminos(ORIGEN, 0, new SimpleArraySet<>(), new SimpleArrayList<>(), caminos, costos);

        System.out.println("\nCaminos posibles de " + ORIGEN + " a " + DESTINO + ":");
        for (int i = 0; i < caminos.size(); i++) {
            imprimirCamino(i + 1, caminos.get(i), costos.get(i), costos.get(i) == costoMasCorto);
        }
    }

    // Busqueda exhaustiva (DFS con backtracking) de todos los caminos simples
    // entre ORIGEN y DESTINO. Con solo 4 sedes esto es barato, y nos deja
    // mostrar las alternativas, no solo la respuesta final.
    private void buscarCaminos(String actual, int costoActual, SimpleSet<String> visitadas,
                                SimpleList<String> caminoActual, SimpleList<SimpleList<String>> caminos,
                                SimpleList<Integer> costos) {
        visitadas.add(actual);
        caminoActual.add(actual);

        if (actual.equals(DESTINO)) {
            caminos.add(copiarCamino(caminoActual));
            costos.add(costoActual);
        } else {
            SimpleList<Edge<String>> vecinos = grafo.getNeighbors(actual);
            for (int i = 0; i < vecinos.size(); i++) {
                Edge<String> vecino = vecinos.get(i);
                if (!visitadas.contains(vecino.destination)) {
                    buscarCaminos(vecino.destination, costoActual + vecino.weight, visitadas, caminoActual, caminos, costos);
                }
            }
        }

        // Backtracking: al volver, sacamos la sede actual para poder probar otras ramas
        visitadas.remove(actual);
        caminoActual.remove(caminoActual.size() - 1);
    }

    private SimpleList<String> copiarCamino(SimpleList<String> caminoActual) {
        SimpleList<String> copia = new SimpleArrayList<>();
        for (int i = 0; i < caminoActual.size(); i++) {
            copia.add(caminoActual.get(i));
        }
        return copia;
    }

    private void imprimirCamino(int numero, SimpleList<String> camino, int costo, boolean esElMasCorto) {
        System.out.print("Camino " + numero + ": ");
        for (int i = 0; i < camino.size(); i++) {
            System.out.print(camino.get(i));
            if (i != camino.size() - 1) System.out.print(" -> ");
        }
        System.out.print(" (costo: " + costo + " km)");
        if (esElMasCorto) System.out.print("  <-- el mas corto");
        System.out.println();
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
