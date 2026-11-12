package sigocafeteria;

import java.util.ArrayList;

public class Venta {
    private final String codigoVenta;
    private ArrayList<DetalleVenta> detalles;
    private double porcentajeDescuento;
    private String metodoPago;
    private double montoPagado;
    private double vuelto;
    private boolean pagada;

    public Venta(String codigoVenta) {
        this.codigoVenta = codigoVenta;
        this.detalles = new ArrayList<>();
        this.porcentajeDescuento = 0.0;
        this.metodoPago = "Pendiente";
        this.montoPagado = 0.0;
        this.vuelto = 0.0;
        this.pagada = false;
    }

    // Redondea a 2 decimales
    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    // Solo agrega el ítem si la venta del producto fue posible
    public boolean agregarDetalle(DetalleVenta detalle) {
        if (pagada) {
            System.out.println("[FALLA CAPTURADA] No se puede modificar una venta ya pagada.");
            return false;
        }
        if (detalle == null || detalle.getProducto() == null) {
            System.out.println("[FALLA CAPTURADA] El detalle o el producto no es válido.");
            return false;
        }
        if (detalle.getProducto().vender(detalle.getCantidad())) {
            detalles.add(detalle);
            return true;
        }
        return false;   // Producto.vender ya mostró el motivo
    }

    public double calcularSubtotal() {
        double subtotal = 0;
        for (DetalleVenta detalle : detalles) {
            subtotal += detalle.calcularSubtotalDetalle();
        }
        return redondear(subtotal);
    }

    public double calcularDescuento() {
        return redondear(calcularSubtotal() * porcentajeDescuento);
    }

    public double calcularIVA() {
        return redondear((calcularSubtotal() - calcularDescuento()) * Producto.IVA);
    }

    public double calcularTotal() {
        return redondear(calcularSubtotal() - calcularDescuento() + calcularIVA());
    }

    // Regla de negocio: descuento entre 0% y 50%
    public boolean aplicarDescuento(double porcentaje) {
        if (pagada) {
            System.out.println("[FALLA CAPTURADA] No se puede cambiar el descuento de una venta pagada.");
            return false;
        }
        if (porcentaje >= 0 && porcentaje <= 0.50) {
            this.porcentajeDescuento = porcentaje;
            return true;
        }
        System.out.println("[FALLA CAPTURADA] El descuento debe estar entre 0% y 50%.");
        return false;
    }

    // Regla de negocio: pago y vuelto
    public boolean procesarPago(String metodo, double montoEntregado) {
        if (pagada) {
            System.out.println("[FALLA CAPTURADA] Esta venta ya fue pagada.");
            return false;
        }
        if (detalles.isEmpty()) {
            System.out.println("[FALLA CAPTURADA] No se puede pagar una venta sin productos.");
            return false;
        }
        if (metodo == null) {
            System.out.println("[FALLA CAPTURADA] Método de pago no válido.");
            return false;
        }

        double totalPagar = calcularTotal();

        if (metodo.equalsIgnoreCase("Efectivo")) {
            if (montoEntregado >= totalPagar) {
                this.metodoPago = "Efectivo";
                this.montoPagado = montoEntregado;
                this.vuelto = redondear(montoEntregado - totalPagar);
                this.pagada = true;
                return true;
            }
            System.out.println("[FALLA CAPTURADA] Monto insuficiente. Falta dinero para cubrir el total.");
            return false;
        } else if (metodo.equalsIgnoreCase("Tarjeta") || metodo.equalsIgnoreCase("Transferencia")) {
            this.metodoPago = metodo;
            this.montoPagado = totalPagar;
            this.vuelto = 0.0;
            this.pagada = true;
            return true;
        }
        System.out.println("[FALLA CAPTURADA] Método de pago no válido.");
        return false;
    }

    // Getters
    public String getCodigoVenta() { return codigoVenta; }
    public ArrayList<DetalleVenta> getDetalles() { return new ArrayList<>(detalles); }  // copia
    public double getPorcentajeDescuento() { return porcentajeDescuento; }
    public String getMetodoPago() { return metodoPago; }
    public double getMontoPagado() { return montoPagado; }
    public double getVuelto() { return vuelto; }
    public boolean isPagada() { return pagada; }

    public void mostrarTicket() {
        System.out.println("\n========== TICKET DE VENTA ==========");
        System.out.println("Código Venta: " + codigoVenta);
        System.out.println("-------------------------------------");
        for (DetalleVenta d : detalles) {
            System.out.println(d.toString());
        }
        System.out.println("-------------------------------------");
        System.out.println("Subtotal: $" + String.format("%.2f", calcularSubtotal()));
        System.out.println("Descuento (" + (porcentajeDescuento * 100) + "%): -$" + String.format("%.2f", calcularDescuento()));
        System.out.println("IVA (15%): +$" + String.format("%.2f", calcularIVA()));
        System.out.println("TOTAL A PAGAR: $" + String.format("%.2f", calcularTotal()));
        System.out.println("Método de Pago: " + metodoPago);
        if (metodoPago.equalsIgnoreCase("Efectivo")) {
            System.out.println("Efectivo Entregado: $" + String.format("%.2f", montoPagado));
            System.out.println("Vuelto calculado: $" + String.format("%.2f", vuelto));
        }
        System.out.println("Estado: " + (pagada ? "PAGADO EXITOSAMENTE" : "PENDIENTE"));
        System.out.println("=====================================\n");
    }
}