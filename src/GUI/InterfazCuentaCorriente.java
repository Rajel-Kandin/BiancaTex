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
import BLL.CuentaCorriente;

public class InterfazCuentaCorriente extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	public InterfazCuentaCorriente(Admin admin, JFrame anterior) {

		setTitle("BiancaTex - Cuenta corriente");
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

		JLabel lblTitulo = new JLabel("Cuenta Corriente");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(0, 45, 434, 30);
		contentPane.add(lblTitulo);

		JButton btnVer = new JButton("Ver");
		btnVer.addActionListener(e -> CuentaCorriente.verCuentaCorriente());
		btnVer.setBounds(117, 95, 200, 32);
		contentPane.add(btnVer);

		JButton btnSaldoInicial = new JButton("Cargar saldo inicial");
		btnSaldoInicial.addActionListener(e -> CuentaCorriente.cargarSaldoInicial(admin));
		btnSaldoInicial.setBounds(117, 137, 200, 32);
		contentPane.add(btnSaldoInicial);

		JButton btnIngreso = new JButton("Crear ingreso");
		btnIngreso.addActionListener(e -> CuentaCorriente.crearIngreso(admin));
		btnIngreso.setBounds(117, 179, 200, 32);
		contentPane.add(btnIngreso);

		JButton btnEgreso = new JButton("Crear egreso");
		btnEgreso.addActionListener(e -> CuentaCorriente.crearEgreso(admin));
		btnEgreso.setBounds(117, 221, 200, 32);
		contentPane.add(btnEgreso);

		JButton btnEliminar = new JButton("Eliminar ingreso/egreso");
		btnEliminar.addActionListener(e -> CuentaCorriente.eliminarMovimientoManual());
		btnEliminar.setBounds(117, 263, 200, 32);
		contentPane.add(btnEliminar);

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(e -> dispose());
		btnVolver.setBounds(117, 320, 200, 32);
		contentPane.add(btnVolver);
	}
}