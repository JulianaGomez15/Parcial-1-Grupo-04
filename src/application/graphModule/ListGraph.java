package application.graphModule;

import application.dictionaryModule.SimpleLinkedDictionary;
import application.listModule.SimpleArrayList;
import application.listModule.SimpleList;

public class ListGraph<T> implements Graph<T> {

    private SimpleLinkedDictionary<T, SimpleArrayList<Edge<T>>> adjacencyList;

    public ListGraph(){
        adjacencyList = new SimpleLinkedDictionary<T, SimpleArrayList<Edge<T>>>();
    }

    @Override
    public SimpleList<T> vertices() {
        return adjacencyList.keys();
    }

    @Override
    public boolean addVertex(T vertex) {
        if(containsVertex(vertex)) return false;

        adjacencyList.put(vertex, new SimpleArrayList<Edge<T>>());
        return true;
    }

    @Override
    public boolean removeVertex(T vertex) {
        if (containsVertex(vertex)) return false;

        // Cacheamos la lista de vertices
        SimpleList<T> vertices = vertices();

        // Removemos todos los edges que apunten a vertex. De esta manera evitamos referencias obsoletas
        for (int i = 0; i < vertices.size(); i++) removeEdge(vertices.get(i), vertex);

        // Finalmente, removemos el vertex en sí
        adjacencyList.remove(vertex);
        return true;
    }

    @Override
    public boolean addEdge(T from, T to, int weight) {
        // addVertex solo agrega el vertice si no existe. Nos aseguramos que existan from y to
        addVertex(from);
        addVertex(to);

        // Buscamos el edge entre from y to, si no existe guarda null
        Edge<T> edge = getEdge(from, to);

        // Si no existe el edge, lo creamos
        if (edge == null){
            // Accedemos a la lista de edges de from. Le agregamos un nuevo Edge con el to y el weight
            adjacencyList.get(from).add(new Edge<T>(to, weight));
            return true;
        }
        // Si existe, pero tiene otro weight, lo actualizamos
        else if (edge.weight != weight) {
            edge.weight = weight;
            return true;
        }

        // Si ya existe y tienen el mismo weight, nada que hacer
        return false;
    }

    @Override
    public boolean removeEdge(T from, T to) {
        // Buscamos el edge a remover
        Edge<T> edge = getEdge(from, to);

        // Si no estaba, nada que hacer
        if (edge == null) return false;

        // Lo removemos de la lista de Edges de from
        adjacencyList.get(from).remove(edge);
        return true;
    }

    @Override
    public SimpleList<Edge<T>> getNeighbors(T vertex) {
        return adjacencyList.get(vertex);
    }

    @Override
    public boolean containsVertex(T vertex) {
        return adjacencyList.containsKey(vertex);
    }

    @Override
    public boolean containsEdge(T from, T to) {
        return getEdge(from, to) != null;
    }

    @Override
    public int getWeight(T from, T to) {
        Edge<T> edge = getEdge(from, to);
        if (edge != null)
            return edge.weight;

        // Si no esta, devuelvo -1
        return -1;
    }

    private Edge<T> getEdge(T from, T to){
        // Si no esta el origen, no hay edge
        if(!containsVertex(from)) return null;

        // Guardamos la lista de todos los edges
        SimpleList<Edge<T>> neighbors = getNeighbors(from);

        // Recorremos la lista buscando un edge que lleve a TO
        for (int i = 0; i<neighbors.size(); i++){
            if (neighbors.get(i).destination.equals(to))
                return neighbors.get(i);
        }
        // Si lllegamos hasta aca, no hay edge de from a to
        return null;
    }
}
