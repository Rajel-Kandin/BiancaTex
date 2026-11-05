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
import BLL.Compra;

public class InterfazCompras extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public InterfazCompras(Admin admin, JFrame anterior) {

		setTitle("BiancaTex - Compras");
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

		JLabel lblUsuario = new JLabel("Usuario: " + admin.getNombre());
		lblUsuario.setBounds(20, 15, 300, 20);
		contentPane.add(lblUsuario);

		JLabel lblTitulo = new JLabel("Sección Compras");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(0, 45, 434, 30);
		contentPane.add(lblTitulo);

		JButton btnRegistrar = new JButton("Registrar compra");
		btnRegistrar.addActionListener(e -> Compra.registrarCompra(admin));
		btnRegistrar.setBounds(127, 95, 180, 32);
		contentPane.add(btnRegistrar);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(e -> Compra.buscarCompra());
		btnBuscar.setBounds(127, 137, 180, 32);
		contentPane.add(btnBuscar);

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(e -> Compra.eliminarCompra());
		btnEliminar.setBounds(127, 179, 180, 32);
		contentPane.add(btnEliminar);

		JButton btnVerCompras = new JButton("Ver compras");
		btnVerCompras.addActionListener(e -> Compra.verCompras());
		btnVerCompras.setBounds(127, 221, 180, 32);
		contentPane.add(btnVerCompras);

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(e -> dispose());
		btnVolver.setBounds(127, 283, 180, 32);
		contentPane.add(btnVolver);
	}
}