package application.graphModule;


import application.dictionaryModule.SimpleArrayDictionary;
import application.dictionaryModule.SimpleDictionary;
import application.listModule.SimpleList;
import application.priorityQueueModule.SimpleLinkedPriorityQueue;
import application.priorityQueueModule.SimplePriorityQueue;
import application.setModule.SimpleArraySet;
import application.setModule.SimpleSet;
import application.stackModule.SimpleStack;

public class DijkstraSolver<T> {

    public SimpleDictionary<T, Edge<T>> dijkstraAllNodes(T origin, Graph<T> graph){

        // Inicializamos las colecciones
        SimpleDictionary<T, Edge<T>> table = new SimpleArrayDictionary<T, Edge<T>>();
        SimplePriorityQueue<T> unvisited = new SimpleLinkedPriorityQueue<T>();
        SimpleSet<T> visited = new SimpleArraySet<T>();

        // Asignamos valores iniciales y cargamos los nodos del grafo
        SimpleList<T> vertices = graph.vertices();

        // Los pasamos a la tabla, con prev = null y costo infinito
        for(int i = 0; i < vertices.size(); i++)
            table.put(vertices.get(i), new Edge<T>(null, Integer.MAX_VALUE));

        // El nodo inicial no tiene costo
        table.get(origin).weight = 0;

        // Lo marcamos como no visitado, con su costo (0)
        unvisited.enqueue(origin, table.get(origin).weight);

        while(!unvisited.isEmpty()){

            // Sacamos el primero de la queue
            T current = unvisited.dequeue();

            // Si ya lo visitamos, nos salteamos el nodo
            if(visited.contains(current)) continue;

            // Revisamos los vecinos
            SimpleList<Edge<T>> neighbors = graph.getNeighbors(current);
            for(int i = 0; i < neighbors.size(); i++){

                // El costo total al vecino es = Costo hasta current + costo del current al vecino
                int totalWeight = table.get(current).weight + neighbors.get(i).weight;

                // Encolamos al vecino para visitar, con el costo que calculamos
                unvisited.enqueue(neighbors.get(i).destination, totalWeight);

                // Si el nuevo costo calculado es menor alqu eya estaba en la tabla para el vecino
                if(totalWeight < table.get(neighbors.get(i).destination).weight)

                    // Actulizamos la tabla. El anterior vecino es el current y el costo el que calcuamos
                    table.put(neighbors.get(i).destination, new Edge<T>(current, totalWeight));
            }

            // Agrego el nodo como visitado
            visited.add(current);

        }


        return table;
    }
}
