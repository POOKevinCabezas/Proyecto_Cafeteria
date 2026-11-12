package sigocafeteria;

public class Comanda {
    public static final String PENDIENTE = "Pendiente";
    public static final String EN_PREPARACION = "En preparación";
    public static final String LISTO = "Listo";
    public static final String ENTREGADO = "Entregado";
    public static final String CANCELADO = "Cancelado";

    private final String idComanda;
    private Venta ventaAsociada;
    private String estado;
    private String horaCreacion;

    public Comanda(String idComanda, Venta ventaAsociada) {
        this.idComanda = idComanda;
        this.ventaAsociada = ventaAsociada;
        this.estado = PENDIENTE;
        java.time.LocalTime ahora = java.time.LocalTime.now();
        this.horaCreacion = String.format("%02d:%02d:%02d",
                ahora.getHour(), ahora.getMinute(), ahora.getSecond());
    }

    // Regla de negocio: los estados solo avanzan en orden
    public boolean avanzarEstado() {
        if (estado.equals(PENDIENTE)) {
            this.estado = EN_PREPARACION;
            System.out.println("[COMANDA " + idComanda + "] El pedido pasó a: EN PREPARACIÓN.");
            return true;
        } else if (estado.equals(EN_PREPARACION)) {
            this.estado = LISTO;
            System.out.println("[COMANDA " + idComanda + "] El pedido está LISTO para servir.");
            return true;
        } else if (estado.equals(LISTO)) {
            this.estado = ENTREGADO;
            System.out.println("[COMANDA " + idComanda + "] Pedido entregado al cliente.");
            return true;
        }
        System.out.println("[FALLA CAPTURADA] La comanda está en estado " + estado.toUpperCase()
                + " y ya no puede avanzar.");
        return false;
    }

    public boolean cancelarComanda() {
        if (estado.equals(ENTREGADO) || estado.equals(CANCELADO)) {
            System.out.println("[FALLA CAPTURADA] No se puede cancelar una comanda en estado "
                    + estado.toUpperCase() + ".");
            return false;
        }
        this.estado = CANCELADO;
        System.out.println("[COMANDA " + idComanda + "] El pedido ha sido cancelado.");
        return true;
    }

    public String getIdComanda() { return idComanda; }
    public Venta getVentaAsociada() { return ventaAsociada; }
    public String getEstado() { return estado; }
    public String getHoraCreacion() { return horaCreacion; }

    @Override
    public String toString() {
        String texto = "\n--- PANTALLA DE COMANDAS (COCINA) ---\n"
                + "ID Comanda: " + idComanda + " | Hora: " + horaCreacion
                + " | Estado: [" + estado.toUpperCase() + "]\n";
        if (ventaAsociada == null) {
            return texto + "Sin venta asociada.\n----------------------------------------";
        }
        texto += "Venta Asociada: " + ventaAsociada.getCodigoVenta() + "\n"
                + "Detalle de los productos y notas:\n";
        for (DetalleVenta d : ventaAsociada.getDetalles()) {
            texto += " • " + d.getCantidad() + "x " + d.getProducto().getNombre()
                    + " ---> Nota: \"" + d.getNotaEspecial() + "\"\n";
        }
        return texto + "----------------------------------------";
    }
}