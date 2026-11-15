package GUI;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import BLL.Empleado;
import BLL.Producto;

public class InterfazEmpleado extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public InterfazEmpleado(Empleado empleado) {

		setTitle("BiancaTex - Empleado");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 340);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsuario = new JLabel("Usuario: " + empleado.getNombre());
		lblUsuario.setBounds(20, 15, 300, 20);
		contentPane.add(lblUsuario);

		JLabel lblTitulo = new JLabel("Menú Empleado");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(0, 45, 434, 30);
		contentPane.add(lblTitulo);

		JButton btnVentas = new JButton("Sección ventas");
		btnVentas.addActionListener(e -> {
			new InterfazVentas(empleado, this).setVisible(true);
			setVisible(false);
		});
		btnVentas.setBounds(127, 95, 180, 32);
		contentPane.add(btnVentas);

		JButton btnProductos = new JButton("Gestionar productos");
		btnProductos.addActionListener(e -> {
			new InterfazListaProductos(this).setVisible(true);
			setVisible(false);
		});
		btnProductos.setBounds(127, 137, 180, 32);
		contentPane.add(btnProductos);

		JButton btnBajoStock = new JButton("Bajo stock");
		btnBajoStock.addActionListener(e -> Producto.verBajoStock());
		btnBajoStock.setBounds(127, 179, 180, 32);
		contentPane.add(btnBajoStock);

		JButton btnCerrarSesion = new JButton("Cerrar sesión");
		btnCerrarSesion.addActionListener(e -> {
			new InterfazLogin().setVisible(true);
			dispose();
		});
		btnCerrarSesion.setBounds(127, 241, 180, 32);
		contentPane.add(btnCerrarSesion);
	}
}