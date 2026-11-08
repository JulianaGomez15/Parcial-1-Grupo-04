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

    private static final String ORIGEN = "Torre del Mago";
    private static final String DESTINO = "Castillo del Dragon";

    // Usamos el Graph/ListGraph y el DijkstraSolver tal cual estan en graphModule.
    private final Graph<String> grafo = new ListGraph<>();
    private final DijkstraSolver<String> dijkstra = new DijkstraSolver<>();
    private boolean firstTime = true;

    public GPSExercise(Scanner scanner) {
        super(scanner);

        // Mapa pre-cargado del reino, con el esfuerzo (costo) de cada tramo.
        // Las conexiones son de ida y vuelta, por eso se cargan en ambos sentidos.
        agregarConexion("Torre del Mago", "Bosque Encantado", 6);
        agregarConexion("Torre del Mago", "Castillo del Dragon", 9);
        agregarConexion("Bosque Encantado", "Castillo del Dragon", 4);
        agregarConexion("Castillo del Dragon", "Pantano Oscuro", 340);
        agregarConexion("Bosque Encantado", "Pantano Oscuro", 345);
    }

    private void agregarConexion(String lugarA, String lugarB, int esfuerzo) {
        grafo.addEdge(lugarA, lugarB, esfuerzo);
        grafo.addEdge(lugarB, lugarA, esfuerzo);
    }

    @Override
    protected void exerciseLogic() {
        menuLogic();
    }

    private void menuLogic() {
        if (firstTime) {
            System.out.println("\nUn mago tiene una mision: llegar desde " + ORIGEN
                    + " hasta " + DESTINO + ". Para eso cuenta con varios caminos posibles.");
            mostrarGrafoCompacto();
            firstTime = false;
        }

        System.out.println("\nSeleccione una opcion:"
                + "\ncalcular: Ver los caminos posibles y cual conviene tomar"
                + "\nmostrar: Mostrar el mapa completo, con el esfuerzo de cada tramo"
                + "\nmm: Volver al menu principal");

        String userInput = scanner.nextLine().trim().toLowerCase();

        switch (userInput) {
            case "calcular":
                calcularLogic();
                break;
            case "mostrar":
                mostrarGrafoCompleto();
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
        // DijkstraSolver nos da el esfuerzo real mas bajo, para saber cual
        // de todos los caminos que encontremos es el ganador.
        SimpleDictionary<String, Edge<String>> tabla = dijkstra.dijkstraAllNodes(ORIGEN, grafo);

        if (tabla.get(DESTINO).weight == Integer.MAX_VALUE) {
            System.out.println("\nEl mago no tiene forma de llegar de " + ORIGEN + " a " + DESTINO + ".");
            return;
        }

        int esfuerzoMasBajo = tabla.get(DESTINO).weight;

        // Buscamos TODOS los caminos simples (sin repetir lugares) entre origen y
        // destino, para poder mostrar las alternativas y comparar su esfuerzo,
        // en vez de solo mostrar el resultado final.
        SimpleList<SimpleList<String>> caminos = new SimpleArrayList<>();
        SimpleList<Integer> esfuerzos = new SimpleArrayList<>();
        buscarCaminos(ORIGEN, 0, new SimpleArraySet<>(), new SimpleArrayList<>(), caminos, esfuerzos);

        System.out.println("\nCaminos posibles para que el mago llegue de " + ORIGEN + " a " + DESTINO + ":");
        for (int i = 0; i < caminos.size(); i++) {
            imprimirCamino(i + 1, caminos.get(i), esfuerzos.get(i), esfuerzos.get(i) == esfuerzoMasBajo);
        }
    }

    // Busqueda exhaustiva (DFS con backtracking) de todos los caminos simples
    // entre ORIGEN y DESTINO. Con solo 4 lugares esto es barato, y nos deja
    // mostrar las alternativas, no solo la respuesta final.
    private void buscarCaminos(String actual, int esfuerzoActual, SimpleSet<String> visitados,
                                SimpleList<String> caminoActual, SimpleList<SimpleList<String>> caminos,
                                SimpleList<Integer> esfuerzos) {
        visitados.add(actual);
        caminoActual.add(actual);

        if (actual.equals(DESTINO)) {
            caminos.add(copiarCamino(caminoActual));
            esfuerzos.add(esfuerzoActual);
        } else {
            SimpleList<Edge<String>> vecinos = grafo.getNeighbors(actual);
            for (int i = 0; i < vecinos.size(); i++) {
                Edge<String> vecino = vecinos.get(i);
                if (!visitados.contains(vecino.destination)) {
                    buscarCaminos(vecino.destination, esfuerzoActual + vecino.weight, visitados, caminoActual, caminos, esfuerzos);
                }
            }
        }

        // Backtracking: al volver, sacamos el lugar actual para poder probar otras ramas
        visitados.remove(actual);
        caminoActual.remove(caminoActual.size() - 1);
    }

    private SimpleList<String> copiarCamino(SimpleList<String> caminoActual) {
        SimpleList<String> copia = new SimpleArrayList<>();
        for (int i = 0; i < caminoActual.size(); i++) {
            copia.add(caminoActual.get(i));
        }
        return copia;
    }

    private void imprimirCamino(int numero, SimpleList<String> camino, int esfuerzo, boolean esElMejor) {
        System.out.print("Camino " + numero + ": ");
        for (int i = 0; i < camino.size(); i++) {
            System.out.print(camino.get(i));
            if (i != camino.size() - 1) System.out.print(" -> ");
        }
        System.out.print(" (esfuerzo: " + esfuerzo + ")");
        if (esElMejor) System.out.print("  <-- el camino indicado");
        System.out.println();
    }

    // Version corta para el mensaje de bienvenida: un lugar y todos sus
    // vecinos en una sola linea, sin esfuerzo (el esfuerzo se ve al calcular,
    // o con el comando "mostrar" si se quiere el detalle completo).
    private void mostrarGrafoCompacto() {
        System.out.println("\nMapa del reino:");
        SimpleList<String> vertices = grafo.vertices();

        for (int i = 0; i < vertices.size(); i++) {
            String origen = vertices.get(i);
            SimpleList<Edge<String>> vecinos = grafo.getNeighbors(origen);

            StringBuilder linea = new StringBuilder(origen + " -> ");
            for (int j = 0; j < vecinos.size(); j++) {
                linea.append(vecinos.get(j).destination);
                if (j != vecinos.size() - 1) linea.append(", ");
            }
            System.out.println(linea);
        }
    }

    // Version completa, arista por arista con su esfuerzo (formato pedido por
    // la consigna). Se muestra con el comando "mostrar", no en la bienvenida.
    private void mostrarGrafoCompleto() {
        System.out.println("\nMapa del reino (detallado):");
        SimpleList<String> vertices = grafo.vertices();

        for (int i = 0; i < vertices.size(); i++) {
            String origen = vertices.get(i);
            SimpleList<Edge<String>> vecinos = grafo.getNeighbors(origen);

            for (int j = 0; j < vecinos.size(); j++) {
                Edge<String> vecino = vecinos.get(j);
                System.out.println(origen + " -> " + vecino.destination + ": " + vecino.weight);
            }
        }
    }
}
