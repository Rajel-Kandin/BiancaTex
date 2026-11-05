package GUI;

import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import BLL.Admin;
import BLL.Producto;

public class InterfazAdmin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Admin admin;

	public InterfazAdmin(Admin admin) {
		this.admin = admin;

		setTitle("BiancaTex - Administrador");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 520, 410);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsuario = new JLabel("Usuario: " + admin.getNombre());
		lblUsuario.setBounds(20, 15, 300, 20);
		contentPane.add(lblUsuario);

		JLabel lblTitulo = new JLabel("Menú Administrador");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(0, 45, 504, 30);
		contentPane.add(lblTitulo);

		
		JButton btnUsuarios = new JButton("Gestionar usuarios");
		btnUsuarios.addActionListener(e -> {
			new InterfazUsuarios(admin, this).setVisible(true);
			setVisible(false);
		});
		btnUsuarios.setBounds(40, 95, 200, 32);
		contentPane.add(btnUsuarios);

		JButton btnProductos = new JButton("Gestionar productos");
		btnProductos.addActionListener(e -> {
			new InterfazProductos(admin, this).setVisible(true);
			setVisible(false);
		});
		btnProductos.setBounds(264, 95, 200, 32);
		contentPane.add(btnProductos);

		// fila 2
		JButton btnProveedores = new JButton("Gestionar proveedores");
		btnProveedores.addActionListener(e -> {
			new InterfazProveedores(admin, this).setVisible(true);
			setVisible(false);
		});
		btnProveedores.setBounds(40, 137, 200, 32);
		contentPane.add(btnProveedores);

		JButton btnLocales = new JButton("Gestionar locales");
		btnLocales.addActionListener(e -> {
			new InterfazLocales(admin, this).setVisible(true);
			setVisible(false);
		});
		btnLocales.setBounds(264, 137, 200, 32);
		contentPane.add(btnLocales);

	
		JButton btnCompras = new JButton("Sección compras");
		btnCompras.addActionListener(e -> {
			new InterfazCompras(admin, this).setVisible(true);
			setVisible(false);
		});
		btnCompras.setBounds(40, 179, 200, 32);
		contentPane.add(btnCompras);

		JButton btnVentas = new JButton("Sección ventas");
		btnVentas.addActionListener(e -> {
			new InterfazVentas(admin, this).setVisible(true);
			setVisible(false);
		});
		btnVentas.setBounds(264, 179, 200, 32);
		contentPane.add(btnVentas);

		
		JButton btnCuentaCorriente = new JButton("Cuenta corriente");
		btnCuentaCorriente.addActionListener(e -> {
			new InterfazCuentaCorriente(admin, this).setVisible(true);
			setVisible(false);
		});
		btnCuentaCorriente.setBounds(40, 221, 200, 32);
		contentPane.add(btnCuentaCorriente);

		JButton btnBajoStock = new JButton("Bajo stock");
		btnBajoStock.addActionListener(e -> Producto.verBajoStock());
		btnBajoStock.setBounds(264, 221, 200, 32);
		contentPane.add(btnBajoStock);

		
		JButton btnTickets = new JButton("Ver tickets");
		btnTickets.addActionListener(e -> new InterfazTickets().setVisible(true));
		btnTickets.setBounds(152, 263, 200, 32);
		contentPane.add(btnTickets);

		JButton btnCerrarSesion = new JButton("Cerrar sesión");
		btnCerrarSesion.addActionListener(e -> {
			InterfazLogin login = new InterfazLogin();
			login.setVisible(true);
			dispose();
		});
		btnCerrarSesion.setBounds(152, 320, 200, 32);
		contentPane.add(btnCerrarSesion);
	}
}