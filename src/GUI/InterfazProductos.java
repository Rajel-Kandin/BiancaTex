package GUI;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import BLL.Admin;
import BLL.Producto;

public class InterfazProductos extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public InterfazProductos(Admin admin, JFrame anterior) {

		setTitle("BiancaTex - Productos");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 420);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Si  cierra, velve el menu
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				anterior.setVisible(true);
			}
		});

		JLabel lblUsuario = new JLabel("Usuario: " + admin.getNombre());
		lblUsuario.setBounds(20, 15, 300, 20);
		contentPane.add(lblUsuario);

		JLabel lblTitulo = new JLabel("Gestión de Productos");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(0, 45, 434, 30);
		contentPane.add(lblTitulo);

		JButton btnCrear = new JButton("Crear");
		btnCrear.addActionListener(e -> Producto.crearProducto());
		btnCrear.setBounds(127, 95, 180, 32);
		contentPane.add(btnCrear);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(e -> Producto.buscarProductos());
		btnBuscar.setBounds(127, 137, 180, 32);
		contentPane.add(btnBuscar);

		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(e -> Producto.editarProducto());
		btnEditar.setBounds(127, 179, 180, 32);
		contentPane.add(btnEditar);

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(e -> Producto.eliminarProducto());
		btnEliminar.setBounds(127, 221, 180, 32);
		contentPane.add(btnEliminar);

		JButton btnVer = new JButton("Ver productos");
		btnVer.addActionListener(e -> new InterfazListaProductos().setVisible(true));
		btnVer.setBounds(127, 263, 180, 32);
		contentPane.add(btnVer);

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(e -> dispose());
		btnVolver.setBounds(127, 320, 180, 32);
		contentPane.add(btnVolver);
	}
}