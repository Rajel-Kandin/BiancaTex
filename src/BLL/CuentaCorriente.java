package BLL;
import java.time.LocalDate;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import DDL.ControllerCuentaCorriente;
import repository.Validaciones;

public class CuentaCorriente {

	private Double saldo;
	private static LinkedList<Movimiento> movimientos = new   LinkedList<Movimiento>();


	public CuentaCorriente(Double saldo) {
		super();
		this.saldo = saldo;
	}


	public Double getSaldo() {
		return saldo;
	}
	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
	public static LinkedList<Movimiento> getMovimientos() {
		return movimientos;
	}
	public static void setMovimientos(LinkedList<Movimiento> movimientos) {
		CuentaCorriente.movimientos = movimientos;
	}


	@Override
	public String toString() {
		return "CuentaCorriente [saldo=" + saldo + "]";
	}


	public LinkedList<Venta> filtrarVentas() {
	    return null;
	}

	public LinkedList<Compra> filtrarCompras() {
	    return null;
	}

	public LinkedList<Ingreso> filtrarIngresos() {
	    return null;
	}

	public LinkedList<Egreso> filtrarEgresos() {
	    return null;
	}


	// cargar saldo inicial (solo si la cuenta está vacía)
	public static void cargarSaldoInicial(Usuario usuario) {
	    if (ControllerCuentaCorriente.existenMovimientos()) {
	        JOptionPane.showMessageDialog(null, "Ya hay movimientos cargados. El saldo inicial solo se carga al principio.");
	        return;  }

	    Double saldoInicial = Validaciones.validarDouble("Ingrese el saldo inicial de la cuenta:");
	    if (saldoInicial == null) return;

	    int confirmacion = JOptionPane.showConfirmDialog(null,
	            "¿Confirmar saldo inicial de $" + String.format("%.2f", saldoInicial) + "?",
	            "Saldo inicial", JOptionPane.YES_NO_OPTION);
	    if (confirmacion != JOptionPane.YES_OPTION) return;

	    boolean ok = ControllerCuentaCorriente.registrarMovimiento(LocalDate.now(), saldoInicial, "Saldo inicial",
	            usuario.getIdUsuario(), null, null);
	    if (ok) {
	        JOptionPane.showMessageDialog(null, "Saldo inicial cargado correctamente");
	    } else {
	        JOptionPane.showMessageDialog(null, "Error al cargar el saldo inicial");
	    }}
	


	// ver todos los movimientos y el saldo actual
	public static void verCuentaCorriente() {
	    String lista = ControllerCuentaCorriente.obtenerListaMovimientos();
	    if (lista.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "No hay movimientos cargados");
	        return;
	    }
	    double saldo = ControllerCuentaCorriente.obtenerSaldo();
	    lista += "\nSALDO ACTUAL: $" + String.format("%.2f", saldo);
	    JOptionPane.showMessageDialog(null, lista, "Cuenta corriente", JOptionPane.PLAIN_MESSAGE);}
	

	
	// crear ingreso manual (suma al saldo)
	public static void crearIngreso(Usuario usuario) {
	    Double monto = Validaciones.validarDouble("Monto del ingreso:");
	    if (monto == null) return;

	    String descripcion;
	    do {
	        descripcion = JOptionPane.showInputDialog("Descripción del ingreso:");
	        if (descripcion == null) return;
	        if (descripcion.trim().isEmpty()) {
	            JOptionPane.showMessageDialog(null, "La descripción no puede estar vacía.");
	            descripcion = null;
	        }
	    } while (descripcion == null);

	    int confirmacion = JOptionPane.showConfirmDialog(null,
	            "¿Confirmar ingreso de $" + String.format("%.2f", monto) + "?\n" + descripcion,
	            "Crear ingreso", JOptionPane.YES_NO_OPTION);
	    if (confirmacion != JOptionPane.YES_OPTION) return;

	    boolean ok = ControllerCuentaCorriente.registrarMovimiento(LocalDate.now(), monto,
	            "Ingreso: " + descripcion.trim(), usuario.getIdUsuario(), null, null);
	    if (ok) {
	        JOptionPane.showMessageDialog(null, "Ingreso registrado correctamente");
	    } else {
	        JOptionPane.showMessageDialog(null, "Error al registrar el ingreso");
	    }}
	


	// crear egreso manual (resta al saldo)
	public static void crearEgreso(Usuario usuario) {
	    Double monto = Validaciones.validarDouble("Monto del egreso:");
	    if (monto == null) return;

	    String descripcion;
	    do {
	        descripcion = JOptionPane.showInputDialog("Descripción del egreso:");
	        if (descripcion == null) return;
	        if (descripcion.trim().isEmpty()) {
	            JOptionPane.showMessageDialog(null, "La descripción no puede estar vacía.");
	            descripcion = null;
	        }
	    } while (descripcion == null);

	    int confirmacion = JOptionPane.showConfirmDialog(null,
	            "¿Confirmar egreso de $" + String.format("%.2f", monto) + "?\n" + descripcion,
	            "Crear egreso", JOptionPane.YES_NO_OPTION);
	    if (confirmacion != JOptionPane.YES_OPTION) return;

	    
	    // monto negativo porque sale plata
	    boolean ok = ControllerCuentaCorriente.registrarMovimiento(LocalDate.now(), -monto,
	            "Egreso: " + descripcion.trim(), usuario.getIdUsuario(), null, null);
	    if (ok) {
	        JOptionPane.showMessageDialog(null, "Egreso registrado correctamente");
	    } else {
	        JOptionPane.showMessageDialog(null, "Error al registrar el egreso");
	    }}


	
	// eliminar un ingreso o egreso manual
	public static void eliminarMovimientoManual() {
	    String lista = ControllerCuentaCorriente.obtenerListaManuales();
	    if (lista.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "No hay ingresos ni egresos manuales cargados");
	        return;  }

	    Integer idMovimiento = Validaciones.validarEntero(lista + "\nIngrese el número del movimiento a eliminar:");
	    if (idMovimiento == null) return;

	    int confirmacion = JOptionPane.showConfirmDialog(null,
	            "¿Seguro que querés eliminar el movimiento #" + idMovimiento + "?",
	            "Eliminar movimiento", JOptionPane.YES_NO_OPTION);
	    if (confirmacion != JOptionPane.YES_OPTION) return;

	    boolean eliminado = ControllerCuentaCorriente.eliminarMovimientoManual(idMovimiento);
	    if (eliminado) {
	        JOptionPane.showMessageDialog(null, "Movimiento eliminado correctamente");
	    } else {
	        JOptionPane.showMessageDialog(null, "No existe un ingreso/egreso manual con ese número");
	    }}



}
