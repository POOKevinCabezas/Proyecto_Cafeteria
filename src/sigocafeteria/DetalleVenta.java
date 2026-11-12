package sigocafeteria;

public class DetalleVenta {
    private Producto producto;
    private int cantidad = 1;                  // valor seguro por defecto
    private String notaEspecial = "Ninguna";

    public DetalleVenta(Producto producto, int cantidad, String notaEspecial) {
        this.producto = producto;
        setCantidad(cantidad);
        setNotaEspecial(notaEspecial);
    }

    public DetalleVenta(Producto producto, int cantidad) {
        this(producto, cantidad, "Ninguna");
    }

    public Producto getProducto() { return producto; }
    public int getCantidad() { return cantidad; }
    public String getNotaEspecial() { return notaEspecial; }

    public boolean setCantidad(int cantidad) {
        if (cantidad > 0) {
            this.cantidad = cantidad;
            return true;
        }
        System.out.println("[FALLA CAPTURADA] La cantidad debe ser mayor a 0.");
        return false;   // conserva el valor anterior
    }

    public void setNotaEspecial(String notaEspecial) {
        if (notaEspecial == null || notaEspecial.trim().equals("")) {
            this.notaEspecial = "Ninguna";
        } else {
            this.notaEspecial = notaEspecial.trim();
        }
    }

    public double calcularSubtotalDetalle() {
        return producto.getPrecio() * cantidad;
    }

    @Override
    public String toString() {
        return cantidad + "x " + producto.getNombre()
                + " (Unitario: $" + String.format("%.2f", producto.getPrecio()) + ")"
                + " | Subtotal: $" + String.format("%.2f", calcularSubtotalDetalle())
                + " [Nota: " + notaEspecial + "]";
    }
}