package sigocafeteria;

public class PruebaProducto {
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE CONTROL DE CAFETERÍA - PRUEBAS ===\n");

       
        System.out.println("--- [PRODUCTO] Pruebas exitosas ---");
        Producto prod1 = new Producto("P001", "Café Americano", "Bebidas", 2.50, 10, 3);
        Producto prod2 = new Producto("P002", "Pastel de Chocolate", "Postres", 3.00);
        System.out.println(prod1);
        System.out.println(prod2);
        System.out.println("Total de productos registrados: " + Producto.getTotalProductosRegistrados());
        prod1.setPrecio(2.80);
        prod1.vender(8);       
        prod1.reponer(15);

        System.out.println("\n--- [PRODUCTO] Fallas capturadas ---");
        prod1.setPrecio(-2.00);   
        prod1.setStock(-5);      
        prod1.vender(100);        
        prod1.setNombre("");    

      
        System.out.println("\n--- [VENTA] Prueba exitosa (carrito, descuento y vuelto) ---");
        Producto cafe = new Producto("P100", "Café Cappuccino", "Bebidas", 3.50, 20, 2);
        Producto pastel = new Producto("P101", "Cheesecake", "Postres", 4.00, 10, 2);

        Venta venta1 = new Venta("V001");
        venta1.agregarDetalle(new DetalleVenta(cafe, 2, "Con leche de almendras"));
        venta1.agregarDetalle(new DetalleVenta(pastel, 1, "Para llevar"));
        venta1.aplicarDescuento(0.10);
        venta1.procesarPago("Efectivo", 20.00);
        venta1.mostrarTicket();

        System.out.println("\n--- [VENTA] Fallas capturadas ---");
        Venta venta2 = new Venta("V002");
        venta2.agregarDetalle(new DetalleVenta(cafe, 500));   
        venta2.procesarPago("Efectivo", 10.00);               
        venta2.agregarDetalle(new DetalleVenta(cafe, 1));
        venta2.aplicarDescuento(1.50);                       
        venta2.procesarPago("Efectivo", 1.00);              
        venta2.procesarPago("Bitcoin", 20.00);             
        venta1.procesarPago("Tarjeta", 0);                   
        new DetalleVenta(cafe, -3);                    
        
      
        System.out.println("\n--- [CAJA] Prueba exitosa ---");
        Caja cajaTurno = new Caja(50.00);
        cajaTurno.registrarIngreso(venta1.calcularTotal());
        cajaTurno.registrarRetiro(5.00);
        cajaTurno.cerrarTurno(cajaTurno.calcularEfectivoEsperado());  

        System.out.println("\n--- [CAJA] Fallas capturadas ---");
        Caja cajaFallas = new Caja(-10.00);     
        cajaFallas.registrarRetiro(500.00);    
        cajaFallas.registrarIngreso(0);       
        cajaFallas.cerrarTurno(-5.00);          

        System.out.println("\n--- [CAJA] Cierre con faltante ---");
        Caja cajaFaltante = new Caja(50.00);
        cajaFaltante.cerrarTurno(40.00);       

   
        System.out.println("\n--- [COMANDA] Prueba exitosa ---");
        if (venta1.isPagada()) {
            Comanda comanda1 = new Comanda(venta1.getCodigoVenta(), venta1);
            System.out.println(comanda1);
            comanda1.avanzarEstado();    
            comanda1.avanzarEstado();    
            comanda1.avanzarEstado();    

            System.out.println("\n--- [COMANDA] Fallas capturadas ---");
            comanda1.avanzarEstado();    
            comanda1.cancelarComanda();  
            
            System.out.println("\n--- [COMANDA] Falla: venta sin pagar ---");
            Venta venta3 = new Venta("V003");               // venta nueva, nunca se paga
            Comanda comanda2 = new Comanda("C2", venta3);
            comanda2.avanzarEstado();                     
        }
    }
}
