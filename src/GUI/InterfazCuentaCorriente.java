package GUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import BLL.Admin;
import BLL.CuentaCorriente;
import DAO.CuentaCorrienteDAO;

public class InterfazCuentaCorriente extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaMovimientos;
	private DefaultTableModel model;
	private JComboBox<String> comboFiltro;
	private JLabel lblSaldo;
	private JLabel lblMensaje;

	public InterfazCuentaCorriente(Admin admin, JFrame anterior) {
		setTitle("BiancaTex - Cuenta corriente");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 520);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Si cierra, vuelve al menu
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				anterior.setVisible(true);
			}
		});

		JLabel lblTitulo = new JLabel("Cuenta corriente");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setBounds(20, 10, 400, 30);
		contentPane.add(lblTitulo);

		// saldo actual
		JLabel lblSaldoTexto = new JLabel("Saldo actual:");
		lblSaldoTexto.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		lblSaldoTexto.setBounds(580, 10, 100, 30);
		contentPane.add(lblSaldoTexto);

		lblSaldo = new JLabel("");
		lblSaldo.setFont(new Font("Segoe UI", Font.BOLD, 22));
		lblSaldo.setBounds(680, 8, 185, 34);
		contentPane.add(lblSaldo);

		// filtro
		JLabel lblFiltro = new JLabel("Mostrar:");
		lblFiltro.setBounds(20, 55, 60, 25);
		contentPane.add(lblFiltro);

		comboFiltro = new JComboBox<String>();
		comboFiltro.addItem("Todos");
		comboFiltro.addItem("Ventas");
		comboFiltro.addItem("Compras");
		comboFiltro.addItem("Ingresos");
		comboFiltro.addItem("Egresos");
		comboFiltro.addActionListener(e -> cargarMovimientos());
		comboFiltro.setBounds(80, 55, 150, 25);
		contentPane.add(comboFiltro);

		// botones 
		JButton btnSaldoInicial = new JButton("Saldo inicial");
		btnSaldoInicial.addActionListener(e -> {
			CuentaCorriente.cargarSaldoInicial(admin);
			cargarMovimientos();
		});
		btnSaldoInicial.setBounds(280, 55, 150, 25);
		contentPane.add(btnSaldoInicial);

		JButton btnIngreso = new JButton("Crear ingreso");
		btnIngreso.addActionListener(e -> {
			CuentaCorriente.crearIngreso(admin);
			cargarMovimientos();
		});
		btnIngreso.setBounds(440, 55, 120, 25);
		contentPane.add(btnIngreso);

		JButton btnEgreso = new JButton("Crear egreso");
		btnEgreso.addActionListener(e -> {
			CuentaCorriente.crearEgreso(admin);
			cargarMovimientos();
		});
		btnEgreso.setBounds(570, 55, 120, 25);
		contentPane.add(btnEgreso);

		JButton btnEliminar = new JButton("Eliminar ingreso/egreso");
		btnEliminar.addActionListener(e -> {
			CuentaCorriente.eliminarMovimientoManual();
			cargarMovimientos();
		});
		btnEliminar.setBounds(700, 55, 165, 25);
		contentPane.add(btnEliminar);

		// tabla
		model = new DefaultTableModel(new String[]{"N°", "Fecha", "Tipo", "Descripción", "Monto", "Usuario"}, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;	}};

		tablaMovimientos = new JTable(model);
		tablaMovimientos.setRowHeight(30);
		tablaMovimientos.setShowHorizontalLines(true);
		tablaMovimientos.getTableHeader().setReorderingAllowed(false);

		// ancho columna
		tablaMovimientos.getColumnModel().getColumn(0).setPreferredWidth(40);
		tablaMovimientos.getColumnModel().getColumn(1).setPreferredWidth(140);
		tablaMovimientos.getColumnModel().getColumn(2).setPreferredWidth(100);
		tablaMovimientos.getColumnModel().getColumn(3).setPreferredWidth(270);
		tablaMovimientos.getColumnModel().getColumn(4).setPreferredWidth(120);
		tablaMovimientos.getColumnModel().getColumn(5).setPreferredWidth(140);

		// colores de los montos
		tablaMovimientos.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
			private static final long serialVersionUID = 1L;
			@Override
			public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) {
				super.getTableCellRendererComponent(t, v, s, f, r, c);
				setFont(getFont().deriveFont(Font.BOLD));
				if (v.toString().startsWith("-")) {
					setForeground(new Color(200, 40, 40));
				} else {
					setForeground(new Color(30, 140, 60));
				}
				return this;
			}
		});

		JScrollPane scroll = new JScrollPane(tablaMovimientos);
		scroll.setBounds(20, 95, 845, 320);
		contentPane.add(scroll);

		lblMensaje = new JLabel("");
		lblMensaje.setBounds(20, 430, 400, 20);
		contentPane.add(lblMensaje);

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(e -> dispose());
		btnVolver.setBounds(765, 430, 100, 30);
		contentPane.add(btnVolver);

		cargarMovimientos();}

	// carga la tabla 
	private void cargarMovimientos() {
		String filtro = (String) comboFiltro.getSelectedItem();
		CuentaCorrienteDAO.cargarTablaMovimientos(model, filtro);

		double saldo = CuentaCorrienteDAO.obtenerSaldo();
		lblSaldo.setText("$" + String.format("%.2f", saldo));
		if (saldo >= 0) {
			lblSaldo.setForeground(new Color(30, 140, 60));
		} else {
			lblSaldo.setForeground(new Color(200, 40, 40));
		}

		if (model.getRowCount() == 0) {
			lblMensaje.setText("No hay movimientos para mostrar.");
		} else {
			lblMensaje.setText("Total: " + model.getRowCount() + " movimientos");
		}}
}