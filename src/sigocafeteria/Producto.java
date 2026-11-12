package sigocafeteria;

public class Producto {
    public static final double IVA = 0.15;
    private static final int STOCK_MINIMO_DEFECTO = 5;
    private static int totalProductosRegistrados = 0;

    private final String codigoUnico;
    private String nombre = "Sin nombre";
    private String categoria = "Sin categoría";
    private double precio = 0.01;
    private int stock = 0;
    private int stockMinimo = STOCK_MINIMO_DEFECTO;
    private boolean disponible;

    public Producto(String codigoUnico, String nombre, String categoria,
                    double precio, int stock, int stockMinimo) {
        boolean ok = true;
        if (codigoUnico != null && !codigoUnico.trim().equals("")) {
            this.codigoUnico = codigoUnico.trim();
        } else {
            this.codigoUnico = "SIN-CODIGO";
            System.out.println("[FALLA CAPTURADA] El código único no puede estar vacío.");
            ok = false;
        }
        ok = setNombre(nombre) && ok;
        ok = setCategoria(categoria) && ok;
        ok = setPrecio(precio) && ok;
        ok = setStock(stock) && ok;
        ok = setStockMinimo(stockMinimo) && ok;
        this.disponible = true;
        if (ok) {
            totalProductosRegistrados++;   // solo cuenta productos 100% válidos
        }
    }

    public Producto(String codigoUnico, String nombre, String categoria, double precio) {
        this(codigoUnico, nombre, categoria, precio, 0, STOCK_MINIMO_DEFECTO);
    }

    // Getters
    public String getCodigoUnico() { return codigoUnico; }
    public String getNombre() { return nombre; }
    public String getCategoria() { return categoria; }
    public double getPrecio() { return precio; }
    public int getStock() { return stock; }
    public int getStockMinimo() { return stockMinimo; }
    public boolean isDisponible() { return disponible; }
    public static int getTotalProductosRegistrados() { return totalProductosRegistrados; }

    // Setters con reglas de negocio
    public boolean setNombre(String nombre) {
        if (nombre != null && !nombre.trim().equals("")) {
            this.nombre = nombre.trim();
            return true;
        }
        System.out.println("[FALLA CAPTURADA] El nombre no puede estar vacío.");
        return false;
    }

    public boolean setCategoria(String categoria) {
        if (categoria != null && !categoria.trim().equals("")) {
            this.categoria = categoria.trim();
            return true;
        }
        System.out.println("[FALLA CAPTURADA] La categoría no puede estar vacía.");
        return false;
    }

    public boolean setPrecio(double precio) {
        if (precio > 0) {
            this.precio = precio;
            return true;
        }
        System.out.println("[FALLA CAPTURADA] El precio de '" + nombre + "' debe ser mayor a 0.");
        return false;
    }

    public boolean setStock(int stock) {
        if (stock >= 0) {
            this.stock = stock;
            return true;
        }
        System.out.println("[FALLA CAPTURADA] El stock no puede ser negativo.");
        return false;
    }

    public boolean setStockMinimo(int stockMinimo) {
        if (stockMinimo >= 0) {
            this.stockMinimo = stockMinimo;
            return true;
        }
        System.out.println("[FALLA CAPTURADA] El stock mínimo no puede ser negativo.");
        return false;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    // Lógica de negocio
    public boolean vender(int cantidad) {
        if (!disponible) {
            System.out.println("[FALLA CAPTURADA] El producto '" + nombre + "' no está disponible.");
            return false;
        }
        if (cantidad > 0 && cantidad <= stock) {
            stock -= cantidad;
            if (isStockBajo()) {
                System.out.println("ALERTA: '" + nombre + "' quedó en stock crítico (" + stock + " unidades).");
            }
            return true;
        }
        System.out.println("[FALLA CAPTURADA] Stock insuficiente o cantidad inválida para vender: " + nombre);
        return false;
    }

    public boolean reponer(int cantidad) {
        if (cantidad > 0) {
            stock += cantidad;
            return true;
        }
        System.out.println("[FALLA CAPTURADA] La cantidad a reponer debe ser mayor a 0.");
        return false;
    }

    public boolean isStockBajo() {
        return stock <= stockMinimo;
    }

    public double calcularSubtotal(int cantidad) {
        if (cantidad > 0) {
            return precio * cantidad;
        }
        System.out.println("[FALLA CAPTURADA] La cantidad debe ser mayor a 0.");
        return 0;
    }

    public double calcularPrecioConIva() {
        return precio * (1 + IVA);
    }

    @Override
    public String toString() {
        String estadoTexto = disponible ? "Activo" : "Inactivo";
        return "[" + categoria + "] " + nombre + " (ID: " + codigoUnico + ") - Precio: $"
                + String.format("%.2f", precio) + " | Stock: " + stock + " [" + estadoTexto + "]";
    }
}