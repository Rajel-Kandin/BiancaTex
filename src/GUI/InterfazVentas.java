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
import BLL.Usuario;
import BLL.Venta;

public class InterfazVentas extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public InterfazVentas(Usuario usuario, JFrame anterior) {

		setTitle("BiancaTex - Ventas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 380);
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

		JLabel lblUsuario = new JLabel("Usuario: " + usuario.getNombre());
		lblUsuario.setBounds(20, 15, 300, 20);
		contentPane.add(lblUsuario);

		JLabel lblTitulo = new JLabel("Sección Ventas");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(0, 45, 434, 30);
		contentPane.add(lblTitulo);

		JButton btnRegistrar = new JButton("Registrar venta");
		btnRegistrar.addActionListener(e -> Venta.registrarVenta(usuario));
		btnRegistrar.setBounds(127, 95, 180, 32);
		contentPane.add(btnRegistrar);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(e -> Venta.buscarVenta());
		btnBuscar.setBounds(127, 137, 180, 32);
		contentPane.add(btnBuscar);

		JButton btnVerVentas = new JButton("Ver ventas");
		btnVerVentas.addActionListener(e -> Venta.verVentas());
		btnVerVentas.setBounds(127, 179, 180, 32);
		contentPane.add(btnVerVentas);

		// limita al admin eliminar 
		if (usuario instanceof Admin) {
			JButton btnEliminar = new JButton("Eliminar");
			btnEliminar.addActionListener(e -> Venta.eliminarVenta());
			btnEliminar.setBounds(127, 221, 180, 32);
			contentPane.add(btnEliminar);
		}

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(e -> dispose());
		btnVolver.setBounds(127, 283, 180, 32);
		contentPane.add(btnVolver);
	}
}