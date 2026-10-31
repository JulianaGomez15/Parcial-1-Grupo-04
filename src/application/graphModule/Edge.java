package application.graphModule;

public class Edge<T>
{
    public int weight;
    public T destination;

    public Edge(T destination, int weight) {
        this.weight = weight;
        this.destination = destination;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object other) {
        // Si los objetos no son de la misma clase, false
        if(other.getClass() != this.getClass()) return false;

        // Casteamos a Edge para acceder a las variables
        Edge<T> otherEdge = (Edge<T>) other;

        // Devolvemos si ambas variables son iguales o no
        return otherEdge.destination.equals(destination) && otherEdge.weight == weight;
    }
}
