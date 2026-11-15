package GUI;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import BLL.Admin;
import BLL.Local;
import DAO.LocalDAO;

public class InterfazLocales extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaLocales;
	private DefaultTableModel model;
	private JTextField inpBuscar;
	private JLabel lblMensaje;

	public InterfazLocales(Admin admin, JFrame anterior) {
		setTitle("BiancaTex - Locales");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 480);
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

		JLabel lblTitulo = new JLabel("Locales");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setBounds(20, 10, 300, 30);
		contentPane.add(lblTitulo);

		// busqueda por direccion
		JLabel lblBuscar = new JLabel("Dirección:");
		lblBuscar.setBounds(20, 50, 70, 25);
		contentPane.add(lblBuscar);

		inpBuscar = new JTextField();
		inpBuscar.addActionListener(e -> cargarLocales());
		inpBuscar.setBounds(90, 50, 230, 25);
		contentPane.add(inpBuscar);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(e -> cargarLocales());
		btnBuscar.setBounds(330, 50, 90, 25);
		contentPane.add(btnBuscar);

		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(e -> {
			inpBuscar.setText("");
			cargarLocales();
		});
		btnLimpiar.setBounds(430, 50, 90, 25);
		contentPane.add(btnLimpiar);

		JButton btnCrear = new JButton("Crear local");
		btnCrear.addActionListener(e -> {
			Local.crearLocal();
			cargarLocales();
		});
		btnCrear.setBounds(740, 50, 125, 25);
		contentPane.add(btnCrear);

		// tabla
		model = new DefaultTableModel(new String[]{"ID", "Dirección", "", ""}, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;	}};

		tablaLocales = new JTable(model);
		tablaLocales.setRowHeight(40);
		tablaLocales.setShowHorizontalLines(true);

		// botones editar y eliminar
		tablaLocales.getColumnModel().getColumn(2).setCellRenderer((t, v, s, f, r, c) -> new JButton("Editar"));
		tablaLocales.getColumnModel().getColumn(3).setCellRenderer((t, v, s, f, r, c) -> new JButton("Eliminar"));

		// clic
		tablaLocales.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = tablaLocales.rowAtPoint(e.getPoint());
				int columna = tablaLocales.columnAtPoint(e.getPoint());
				if (fila < 0) return;

				int idLocal = Integer.parseInt(model.getValueAt(fila, 0).toString());

				if (columna == 2) {
					Local.editarLocal(idLocal);
					cargarLocales();
				} else if (columna == 3) {
					Local.eliminarLocal(idLocal);
					cargarLocales();
				}
			}
		});

		JScrollPane scroll = new JScrollPane(tablaLocales);
		scroll.setBounds(20, 90, 845, 290);
		contentPane.add(scroll);

		lblMensaje = new JLabel("");
		lblMensaje.setBounds(20, 390, 400, 20);
		contentPane.add(lblMensaje);

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(e -> dispose());
		btnVolver.setBounds(765, 390, 100, 30);
		contentPane.add(btnVolver);

		cargarLocales();}

	// carga la tabla segun lo que se busco
	private void cargarLocales() {
		model.setRowCount(0);
		String busqueda = inpBuscar.getText().trim().toLowerCase();
		LinkedList<Local> locales = LocalDAO.obtenerLocales();
		int cantidad = 0;

		for (Local l : locales) {
			if (busqueda.isEmpty() || l.getDireccion().toLowerCase().contains(busqueda)) {
				model.addRow(new Object[]{l.getIdLocal(), l.getDireccion(), "Editar", "Eliminar"});
				cantidad++;
			}
		}

		if (cantidad == 0) {
			lblMensaje.setText("No se encontraron locales.");
		} else {
			lblMensaje.setText("Total: " + cantidad + " locales");
		}}
}