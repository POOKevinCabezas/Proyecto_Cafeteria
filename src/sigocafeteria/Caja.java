package sigocafeteria;

public class Caja {
    private double fondoInicial;
    private double ingresosEfectivo;
    private double retirosEfectivo;
    private boolean turnoAbierto;

    public Caja(double fondoInicial) {
        if (fondoInicial >= 0) {
            this.fondoInicial = fondoInicial;
        } else {
            this.fondoInicial = 0.0;
            System.out.println("[FALLA CAPTURADA] El fondo inicial no puede ser negativo. Se asigna 0.0.");
        }
        this.ingresosEfectivo = 0.0;
        this.retirosEfectivo = 0.0;
        this.turnoAbierto = true;
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }

    public boolean registrarIngreso(double monto) {
        if (!turnoAbierto) {
            System.out.println("[FALLA CAPTURADA] No se pueden registrar ingresos. El turno de caja está cerrado.");
            return false;
        }
        if (monto > 0) {
            this.ingresosEfectivo = redondear(ingresosEfectivo + monto);
            return true;
        }
        System.out.println("[FALLA CAPTURADA] El monto de ingreso debe ser mayor a 0.");
        return false;
    }

    public boolean registrarRetiro(double monto) {
        if (!turnoAbierto) {
            System.out.println("[FALLA CAPTURADA] No se pueden registrar retiros. El turno está cerrado.");
            return false;
        }
        if (monto <= 0) {
            System.out.println("[FALLA CAPTURADA] El monto de retiro debe ser mayor a 0.");
            return false;
        }
        if (monto > calcularEfectivoEsperado()) {
            System.out.println("[FALLA CAPTURADA] El retiro ($" + String.format("%.2f", monto)
                    + ") es mayor al efectivo disponible ($"
                    + String.format("%.2f", calcularEfectivoEsperado()) + ").");
            return false;
        }
        this.retirosEfectivo = redondear(retirosEfectivo + monto);
        return true;
    }

    public double calcularEfectivoEsperado() {
        return redondear(fondoInicial + ingresosEfectivo - retirosEfectivo);
    }

    // Regla de negocio: cierre de turno (esperado vs. contado)
    public boolean cerrarTurno(double efectivoContadoFisico) {
        if (!turnoAbierto) {
            System.out.println("[FALLA CAPTURADA] El turno ya se encuentra cerrado.");
            return false;
        }
        if (efectivoContadoFisico < 0) {
            System.out.println("[FALLA CAPTURADA] El efectivo contado no puede ser negativo.");
            return false;
        }

        double efectivoEsperado = calcularEfectivoEsperado();
        double diferencia = redondear(efectivoContadoFisico - efectivoEsperado);

        System.out.println("\n========== CIERRE DE TURNO - CAJA ==========");
        System.out.println("Fondo Inicial: $" + String.format("%.2f", fondoInicial));
        System.out.println("Ingresos Totales (Efectivo): +$" + String.format("%.2f", ingresosEfectivo));
        System.out.println("Retiros Totales: -$" + String.format("%.2f", retirosEfectivo));
        System.out.println("---------------------------------------------");
        System.out.println("Efectivo Esperado en Caja: $" + String.format("%.2f", efectivoEsperado));
        System.out.println("Efectivo Real Contado (Físico): $" + String.format("%.2f", efectivoContadoFisico));
        System.out.println("---------------------------------------------");

        if (diferencia == 0) {
            System.out.println("Estado de Caja: CUADRE EXACTO. Sin diferencias.");
        } else if (diferencia > 0) {
            System.out.println("SOBRANTE EN CAJA: excedente de +$" + String.format("%.2f", diferencia));
        } else {
            System.out.println("FALTANTE EN CAJA: déficit de -$" + String.format("%.2f", Math.abs(diferencia)));
        }

        this.turnoAbierto = false;
        System.out.println("Turno cerrado exitosamente.\n=============================================");
        return true;
    }

    public double getFondoInicial() { return fondoInicial; }
    public double getIngresosEfectivo() { return ingresosEfectivo; }
    public double getRetirosEfectivo() { return retirosEfectivo; }
    public boolean isTurnoAbierto() { return turnoAbierto; }
}