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
import BLL.Local;

public class InterfazLocales extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public InterfazLocales(Admin admin, JFrame anterior) {

		setTitle("BiancaTex - Locales");
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

		JLabel lblTitulo = new JLabel("Gestión de Locales");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(0, 45, 434, 30);
		contentPane.add(lblTitulo);

		JButton btnCrear = new JButton("Crear");
		btnCrear.addActionListener(e -> Local.crearLocal());
		btnCrear.setBounds(127, 95, 180, 32);
		contentPane.add(btnCrear);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(e -> Local.buscarLocal());
		btnBuscar.setBounds(127, 137, 180, 32);
		contentPane.add(btnBuscar);

		JButton btnEditar = new JButton("Editar");
		btnEditar.addActionListener(e -> Local.editarLocal());
		btnEditar.setBounds(127, 179, 180, 32);
		contentPane.add(btnEditar);

		JButton btnEliminar = new JButton("Eliminar");
		btnEliminar.addActionListener(e -> Local.eliminarLocal());
		btnEliminar.setBounds(127, 221, 180, 32);
		contentPane.add(btnEliminar);

		JButton btnVer = new JButton("Ver locales");
		btnVer.addActionListener(e -> Local.verLocales());
		btnVer.setBounds(127, 263, 180, 32);
		contentPane.add(btnVer);

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(e -> dispose());
		btnVolver.setBounds(127, 320, 180, 32);
		contentPane.add(btnVolver);
	}
}