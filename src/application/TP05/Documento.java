package application.TP05;

public class Documento {
    private String nombre;
    private int paginas;
    private boolean color;

    public Documento(String nombre, int paginas, boolean color) {
        this.nombre = nombre.trim();
        this.paginas = paginas;
        this.color = color;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPaginas() {
        return paginas;
    }

    public boolean esColor() {
        return color;
    }

    @Override
    public String toString() {
        String textoPaginas = paginas == 1 ? "1 página" : paginas + " páginas";
        String tipo = color ? "Color" : "Blanco y negro";
        return nombre + " | " + textoPaginas + " | " + tipo;
    }
}
