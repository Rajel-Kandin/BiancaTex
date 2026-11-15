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

import BLL.Proveedor;
import DAO.ProveedorDAO;

public class InterfazListaProveedores extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaProveedores;
	private DefaultTableModel model;
	private JTextField inpBuscar;
	private JLabel lblMensaje;

	public InterfazListaProveedores(JFrame anterior) {
		setTitle("BiancaTex - Lista de proveedores");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 900, 480);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Si cierra, vuelve a gestion de proveedores
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				anterior.setVisible(true);
			}
		});

		JLabel lblTitulo = new JLabel("Proveedores");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setBounds(20, 10, 300, 30);
		contentPane.add(lblTitulo);

		// busqueda 
		JLabel lblBuscar = new JLabel("Nombre o CUIT:");
		lblBuscar.setBounds(20, 50, 100, 25);
		contentPane.add(lblBuscar);

		inpBuscar = new JTextField();
		inpBuscar.addActionListener(e -> cargarProveedores());
		inpBuscar.setBounds(120, 50, 200, 25);
		contentPane.add(inpBuscar);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(e -> cargarProveedores());
		btnBuscar.setBounds(330, 50, 100, 25);
		contentPane.add(btnBuscar);

		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(e -> {
			inpBuscar.setText("");
			cargarProveedores();
		});
		btnLimpiar.setBounds(440, 50, 100, 25);
		contentPane.add(btnLimpiar);
		
		//crear
		JButton btnCrear = new JButton("Crear proveedor");
		btnCrear.addActionListener(e -> {
			Proveedor.crearProveedor();
			cargarProveedores();
		});
		btnCrear.setBounds(715, 50, 150, 25);
		contentPane.add(btnCrear);

		// tabla
		model = new DefaultTableModel(new String[]{"CUIT", "Nombre", "Dirección", "Teléfono", "", ""}, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;	}};

		tablaProveedores = new JTable(model);
		tablaProveedores.setRowHeight(40);
		tablaProveedores.setShowHorizontalLines(true);

		// botones 
		tablaProveedores.getColumnModel().getColumn(4).setCellRenderer((t, v, s, f, r, c) -> new JButton("Editar"));
		tablaProveedores.getColumnModel().getColumn(5).setCellRenderer((t, v, s, f, r, c) -> new JButton("Eliminar"));

		// clic
		tablaProveedores.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = tablaProveedores.rowAtPoint(e.getPoint());
				int columna = tablaProveedores.columnAtPoint(e.getPoint());
				if (fila < 0) return;

				String cuit = model.getValueAt(fila, 0).toString();

				if (columna == 4) {
					Proveedor.editarProveedor(cuit);
					cargarProveedores();
				} else if (columna == 5) {
					Proveedor.eliminarProveedor(cuit);
					cargarProveedores();
				}
			}
		});

		JScrollPane scroll = new JScrollPane(tablaProveedores);
		scroll.setBounds(20, 90, 845, 290);
		contentPane.add(scroll);

		lblMensaje = new JLabel("");
		lblMensaje.setBounds(20, 390, 400, 20);
		contentPane.add(lblMensaje);

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(e -> dispose());
		btnVolver.setBounds(765, 390, 100, 30);
		contentPane.add(btnVolver);

		cargarProveedores();}

	// carga la tabla
	private void cargarProveedores() {
		model.setRowCount(0);
		String busqueda = inpBuscar.getText().trim().toLowerCase();
		LinkedList<Proveedor> proveedores = ProveedorDAO.obtenerProveedores();
		int cantidad = 0;

		for (Proveedor p : proveedores) {
			if (busqueda.isEmpty()
					|| p.getNombre().toLowerCase().contains(busqueda)
					|| p.getCuit().contains(busqueda)) {
				model.addRow(new Object[]{p.getCuit(), p.getNombre(), p.getDireccion(),
						p.getTelefono(), "Editar", "Eliminar"});
				cantidad++;
			}
		}

		if (cantidad == 0) {
			lblMensaje.setText("No se encontraron proveedores.");
		} else {
			lblMensaje.setText("Total: " + cantidad + " proveedores");
		}}
}