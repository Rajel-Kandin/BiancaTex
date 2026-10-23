package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import BLL.Admin;
import BLL.Contador;
import BLL.Empleado;
import BLL.Usuario;
import DDL.ControllerUsuario;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class InterfazLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField inpEmail;
	private JPasswordField inpPassword;
	private JLabel lblMensaje;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InterfazLogin frame = new InterfazLogin();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public InterfazLogin() {
		setTitle("BiancaTex - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("BIANCATEX - LOGIN");
		lblTitulo.setBounds(160, 15, 150, 20);
		contentPane.add(lblTitulo);

		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(110, 50, 100, 15);
		contentPane.add(lblEmail);

		inpEmail = new JTextField();
		inpEmail.setBounds(110, 70, 220, 22);
		contentPane.add(inpEmail);
		inpEmail.setColumns(10);

		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setBounds(110, 105, 100, 15);
		contentPane.add(lblPassword);

		inpPassword = new JPasswordField();
		inpPassword.setBounds(110, 125, 220, 22);
		contentPane.add(inpPassword);

		lblMensaje = new JLabel("");
		lblMensaje.setForeground(Color.RED);
		lblMensaje.setBounds(110, 155, 250, 15);
		contentPane.add(lblMensaje);

		JButton btnLogin = new JButton("Ingresar");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String email = inpEmail.getText().trim().toLowerCase();
				String password = new String(inpPassword.getPassword());

				if (email.isEmpty() || password.trim().isEmpty()) {
					lblMensaje.setText("Completá email y contraseña.");
					return;}

				if (!email.matches("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$")) {
					lblMensaje.setText("Ingresá un email válido.");
					return;}

				Usuario logueado = ControllerUsuario.login(email, password);

				if (logueado == null) {
					lblMensaje.setText("Email o contraseña incorrectos.");
					inpPassword.setText("");
				} else if (!logueado.getActivo()) {
					lblMensaje.setText("Tu usuario está desactivado.");
					inpPassword.setText("");
				} else {
					if (logueado instanceof Admin) {
						InterfazAdmin menu = new InterfazAdmin((Admin) logueado);
						menu.setVisible(true);
					} else if (logueado instanceof Empleado) {
						InterfazEmpleado menu = new InterfazEmpleado((Empleado) logueado);
						menu.setVisible(true);
					} else {
						InterfazContador menu = new InterfazContador((Contador) logueado);
						menu.setVisible(true);}

					dispose();}}
		});

		btnLogin.setBounds(160, 185, 110, 25);
		contentPane.add(btnLogin);}
}