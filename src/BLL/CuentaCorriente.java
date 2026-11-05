package BLL;
import java.time.LocalDate;

import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import DAO.CuentaCorrienteDAO;
import repository.Validaciones;

public class CuentaCorriente {




	// cargar saldo inicial (solo si la cuenta está vacía)
	public static void cargarSaldoInicial(Usuario usuario) {
	    if (CuentaCorrienteDAO.existenMovimientos()) {
	        JOptionPane.showMessageDialog(null, "Ya hay un saldo  cargado. El saldo inicial solo se carga una vez.");
	        return;  }

	    Double saldoInicial = Validaciones.validarDouble("Ingrese el saldo inicial de la cuenta:");
	    if (saldoInicial == null) return;

	    int confirmacion = JOptionPane.showConfirmDialog(null,
	            "¿Confirmar saldo inicial de $" + String.format("%.2f", saldoInicial) + "?",
	            "Saldo inicial", JOptionPane.YES_NO_OPTION);
	    if (confirmacion != JOptionPane.YES_OPTION) return;

	    boolean ok = CuentaCorrienteDAO.registrarMovimiento(LocalDate.now(), saldoInicial, "Saldo inicial",
	            usuario.getIdUsuario(), null, null);
	    if (ok) {
	        JOptionPane.showMessageDialog(null, "Saldo inicial cargado correctamente");
	    } else {
	        JOptionPane.showMessageDialog(null, "Error al cargar el saldo inicial");
	    }}


	// ver movimientos, filtros y el saldo actual
	public static void verCuentaCorriente() {
	    String[] filtros = {"Todos", "Ventas", "Compras", "Ingresos", "Egresos"};
	    String filtro = "Todos";
	    boolean seguir = true;

	    while (seguir) {
	        String lista = CuentaCorrienteDAO.obtenerListaMovimientos(filtro);

	        if (lista.isEmpty()) {
	            if (filtro.equals("Todos")) {
	                JOptionPane.showMessageDialog(null, "No hay movimientos cargados");
	                return; }
	            lista = "No hay movimientos de este tipo."; }

	        double saldo = CuentaCorrienteDAO.obtenerSaldo();
	        lista += "\n\nSALDO ACTUAL: $" + String.format("%.2f", saldo);

	        JComboBox<String> combo = new JComboBox<>(filtros);
	        combo.setSelectedItem(filtro);

	        // cambio de filtro abre y cierra 
	        combo.addActionListener(e -> SwingUtilities.getWindowAncestor(combo).dispose());

	        Object[] mensaje = {"Filtrar por:", combo, "\n" + lista};

	        JOptionPane.showMessageDialog(null, mensaje, "Cuenta corriente - " + filtro, JOptionPane.PLAIN_MESSAGE);

	        String elegido = (String) combo.getSelectedItem();
	        if (elegido.equals(filtro)) {
	            seguir = false;   
	        } else {
	            filtro = elegido;} }
	                                                     }
	
	
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

	    boolean ok = CuentaCorrienteDAO.registrarMovimiento(LocalDate.now(), monto,
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
	    boolean ok = CuentaCorrienteDAO.registrarMovimiento(LocalDate.now(), -monto,
	            "Egreso: " + descripcion.trim(), usuario.getIdUsuario(), null, null);
	    if (ok) {
	        JOptionPane.showMessageDialog(null, "Egreso registrado correctamente");
	    } else {
	        JOptionPane.showMessageDialog(null, "Error al registrar el egreso");
	    }}


	
	// eliminar un ingreso o egreso manual
	public static void eliminarMovimientoManual() {
	    String lista = CuentaCorrienteDAO.obtenerListaManuales();
	    if (lista.isEmpty()) {
	        JOptionPane.showMessageDialog(null, "No hay ingresos ni egresos manuales cargados");
	        return;  }

	    Integer idMovimiento = Validaciones.validarEntero(lista + "\nIngrese el número del movimiento a eliminar:");
	    if (idMovimiento == null) return;

	    int confirmacion = JOptionPane.showConfirmDialog(null,
	            "¿Seguro que querés eliminar el movimiento #" + idMovimiento + "?",
	            "Eliminar movimiento", JOptionPane.YES_NO_OPTION);
	    if (confirmacion != JOptionPane.YES_OPTION) return;

	    boolean eliminado = CuentaCorrienteDAO.eliminarMovimientoManual(idMovimiento);
	    if (eliminado) {
	        JOptionPane.showMessageDialog(null, "Movimiento eliminado correctamente");
	    } else {
	        JOptionPane.showMessageDialog(null, "No existe un ingreso/egreso manual con ese número");
	    }}

	
	
	


}
