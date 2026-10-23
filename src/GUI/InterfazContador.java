package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import BLL.Contador;

public class InterfazContador extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Contador contador;

	public InterfazContador(Contador contador) {
		this.contador = contador;

		setTitle("BiancaTex - Contador");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsuario = new JLabel("Usuario: " + contador.getNombre());
		lblUsuario.setBounds(20, 15, 300, 20);
		contentPane.add(lblUsuario);

		JLabel lblTitulo = new JLabel("Menú Contador");
		lblTitulo.setBounds(170, 50, 150, 20);
		contentPane.add(lblTitulo);

		JButton btnVerTickets = new JButton("Ver tickets");
		btnVerTickets.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InterfazTickets tickets = new InterfazTickets();
				tickets.setVisible(true);
			}
		});
		btnVerTickets.setBounds(145, 100, 150, 30);
		contentPane.add(btnVerTickets);

		JButton btnCerrarSesion = new JButton("Cerrar sesión");
		btnCerrarSesion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				InterfazLogin login = new InterfazLogin();
				login.setVisible(true);
				dispose();
			}
		});
		btnCerrarSesion.setBounds(145, 150, 150, 30);
		contentPane.add(btnCerrarSesion);
	}
}