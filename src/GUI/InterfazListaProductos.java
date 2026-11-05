package GUI;

import java.awt.Font;
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

	public InterfazListaProductos() {
		setTitle("BiancaTex - Lista de productos");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 800, 480);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

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
		inpBuscar.addActionListener(e -> cargarProductos()); // Enter también busca
		inpBuscar.setBounds(330, 50, 200, 25);
		contentPane.add(inpBuscar);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(e -> cargarProductos());
		btnBuscar.setBounds(540, 50, 100, 25);
		contentPane.add(btnBuscar);

		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(e -> {
			inpBuscar.setText("");
			comboCategoria.setSelectedIndex(0);
		});
		btnLimpiar.setBounds(650, 50, 100, 25);
		contentPane.add(btnLimpiar);

		// tabla 
		model = new DefaultTableModel(new String[]{"ID", "Nombre", "Precio", "Stock", "Categoría", "Talle"}, 0) {
			private static final long serialVersionUID = 1L;
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;	}};
				
		tablaProductos = new JTable(model);
		JScrollPane scroll = new JScrollPane(tablaProductos);
		scroll.setBounds(20, 90, 745, 290);
		contentPane.add(scroll);

		lblMensaje = new JLabel("");
		lblMensaje.setBounds(20, 390, 400, 20);
		contentPane.add(lblMensaje);

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(e -> dispose());
		btnVolver.setBounds(665, 390, 100, 30);
		contentPane.add(btnVolver);

		cargarProductos();}

	// carga la tabla segun la categoria y el nombre elegidos
	private void cargarProductos() {
		model.setRowCount(0);
		String categoria = (String) comboCategoria.getSelectedItem();
		String busqueda = inpBuscar.getText().trim();

		LinkedList<Producto> productos = ProductoDAO.obtenerProductosFiltrados(categoria, busqueda);

		for (Producto p : productos) {
			model.addRow(new Object[]{p.getIdProducto(), p.getNombre(), "$" + p.getPrecio(),
					p.getStockActual(), p.getCategoria(), p.getTalle()});
		}

		if (productos.isEmpty()) {
			lblMensaje.setText("No se encontraron productos.");
		} else {
			lblMensaje.setText("Total: " + productos.size() + " productos");
		}}
	
	
	
}