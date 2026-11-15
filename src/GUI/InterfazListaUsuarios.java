package GUI;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

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
import DAO.UsuarioDAO;

public class InterfazListaUsuarios extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaUsuarios;
	private DefaultTableModel model;
	private JTextField inpBuscar;
	private JLabel lblMensaje;

	public InterfazListaUsuarios(Admin admin, JFrame anterior) {
		setTitle("BiancaTex - Lista de usuarios");
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

		JLabel lblTitulo = new JLabel("Usuarios");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setBounds(20, 10, 300, 30);
		contentPane.add(lblTitulo);

		// busqueda 
		JLabel lblBuscar = new JLabel("Nombre o DNI:");
		lblBuscar.setBounds(20, 50, 100, 25);
		contentPane.add(lblBuscar);

		inpBuscar = new JTextField();
		inpBuscar.addActionListener(e -> cargarUsuarios());
		inpBuscar.setBounds(120, 50, 200, 25);
		contentPane.add(inpBuscar);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(e -> cargarUsuarios());
		btnBuscar.setBounds(330, 50, 90, 25);
		contentPane.add(btnBuscar);

		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(e -> {
			inpBuscar.setText("");
			cargarUsuarios();
		});
		btnLimpiar.setBounds(430, 50, 90, 25);
		contentPane.add(btnLimpiar);

		JButton btnCrear = new JButton("Crear usuario");
		btnCrear.addActionListener(e -> {
			admin.crearUsuario();
			cargarUsuarios();
		});
		btnCrear.setBounds(740, 50, 125, 25);
		contentPane.add(btnCrear);

		// tabla
		model = new DefaultTableModel(new String[]{"ID", "DNI", "Nombre", "Email", "Tipo", "Estado", "", ""}, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;	}};

		tablaUsuarios = new JTable(model);
		tablaUsuarios.setRowHeight(40);
		tablaUsuarios.setShowHorizontalLines(true);

		// botones 
		tablaUsuarios.getColumnModel().getColumn(6).setCellRenderer((t, v, s, f, r, c) -> new JButton("Editar"));
		tablaUsuarios.getColumnModel().getColumn(7).setCellRenderer((t, v, s, f, r, c) -> new JButton(v.toString()));

		// clic
		tablaUsuarios.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = tablaUsuarios.rowAtPoint(e.getPoint());
				int columna = tablaUsuarios.columnAtPoint(e.getPoint());
				if (fila < 0) return;

				int idUsuario = Integer.parseInt(model.getValueAt(fila, 0).toString());

				if (columna == 6) {
					admin.editarUsuario(idUsuario);
					cargarUsuarios();
				} else if (columna == 7) {
					if (model.getValueAt(fila, 7).equals("Desactivar")) {
						admin.desactivarUsuario(idUsuario);
					} else {
						admin.activarUsuario(idUsuario);
					}
					cargarUsuarios();
				}
			}
		});

		JScrollPane scroll = new JScrollPane(tablaUsuarios);
		scroll.setBounds(20, 90, 845, 290);
		contentPane.add(scroll);

		lblMensaje = new JLabel("");
		lblMensaje.setBounds(20, 390, 400, 20);
		contentPane.add(lblMensaje);

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(e -> dispose());
		btnVolver.setBounds(765, 390, 100, 30);
		contentPane.add(btnVolver);

		cargarUsuarios();}

	// carga la tabla
	private void cargarUsuarios() {
		UsuarioDAO.cargarTablaUsuarios(model, inpBuscar.getText().trim());

		if (model.getRowCount() == 0) {
			lblMensaje.setText("No se encontraron usuarios.");
		} else {
			lblMensaje.setText("Total: " + model.getRowCount() + " usuarios");
		}}
}