package GUI;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import BLL.CategoriasProd;
import BLL.Producto;
import DAO.ProductoDAO;

public class InterfazListaProductos extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaProductos;
	private DefaultTableModel model;
	private JComboBox<String> comboCategoria;
	private JTextField inpBuscar;
	private JLabel lblMensaje;

	public InterfazListaProductos(JFrame anterior) {
		setTitle("BiancaTex - Lista de productos");
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

		JLabel lblTitulo = new JLabel("Productos");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setBounds(20, 10, 300, 30);
		contentPane.add(lblTitulo);

		// filtro por categoria
		JLabel lblCategoria = new JLabel("Categoría:");
		lblCategoria.setBounds(20, 50, 70, 25);
		contentPane.add(lblCategoria);

		comboCategoria = new JComboBox<String>();
		comboCategoria.addItem("Todas");
		for (CategoriasProd c : CategoriasProd.values()) {
			comboCategoria.addItem(c.name());
		}
		comboCategoria.addActionListener(e -> cargarProductos());
		comboCategoria.setBounds(90, 50, 150, 25);
		contentPane.add(comboCategoria);

		// busqueda por nombre
		JLabel lblBuscar = new JLabel("Nombre:");
		lblBuscar.setBounds(270, 50, 60, 25);
		contentPane.add(lblBuscar);

		inpBuscar = new JTextField();
		inpBuscar.addActionListener(e -> cargarProductos()); 
		inpBuscar.setBounds(330, 50, 200, 25);
		contentPane.add(inpBuscar);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(e -> cargarProductos());
		btnBuscar.setBounds(540, 50, 90, 25);
		contentPane.add(btnBuscar);

		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(e -> {
			inpBuscar.setText("");
			comboCategoria.setSelectedIndex(0);
		});
		btnLimpiar.setBounds(640, 50, 90, 25);
		contentPane.add(btnLimpiar);

		JButton btnCrear = new JButton("Crear producto");
		btnCrear.addActionListener(e -> {
			Producto.crearProducto();
			cargarProductos();
		});
		btnCrear.setBounds(740, 50, 125, 25);
		contentPane.add(btnCrear);

		// tabla
		model = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio", "Stock", "Categoría", "Talle", "", ""}, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;	}};

		tablaProductos = new JTable(model);
		tablaProductos.setRowHeight(40);
		tablaProductos.setShowHorizontalLines(true);

		// botones 
		tablaProductos.getColumnModel().getColumn(6).setCellRenderer((t, v, s, f, r, c) -> new JButton("Editar"));
		tablaProductos.getColumnModel().getColumn(7).setCellRenderer((t, v, s, f, r, c) -> new JButton("Eliminar"));

		// clic 
		tablaProductos.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int fila = tablaProductos.rowAtPoint(e.getPoint());
				int columna = tablaProductos.columnAtPoint(e.getPoint());
				if (fila < 0) return;

				String id = model.getValueAt(fila, 0).toString();

				if (columna == 6) {
					Producto.editarProducto(id);  
					cargarProductos();
				} else if (columna == 7) {
					Producto.eliminarProducto(id);
					cargarProductos();
				}
			}
		});

		JScrollPane scroll = new JScrollPane(tablaProductos);
		scroll.setBounds(20, 90, 845, 290);
		contentPane.add(scroll);

		lblMensaje = new JLabel("");
		lblMensaje.setBounds(20, 390, 400, 20);
		contentPane.add(lblMensaje);

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(e -> dispose());
		btnVolver.setBounds(765, 390, 100, 30);
		contentPane.add(btnVolver);

		cargarProductos();}
	

	// carga la tabla 
	private void cargarProductos() {
		model.setRowCount(0);
		String categoria = (String) comboCategoria.getSelectedItem();
		String busqueda = inpBuscar.getText().trim();
		LinkedList<Producto> productos = ProductoDAO.obtenerProductosFiltrados(categoria, busqueda);

		for (Producto p : productos) {
			model.addRow(new Object[]{p.getIdProducto(), p.getNombre(), "$" + p.getPrecio(),
					p.getStockActual(), p.getCategoria(), p.getTalle(), "Editar", "Eliminar"});
		}

		if (productos.isEmpty()) {
			lblMensaje.setText("No se encontraron productos.");
		} else {
			lblMensaje.setText("Total: " + productos.size() + " productos");
		}}
}