package GUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import com.formdev.flatlaf.FlatLightLaf;

import BLL.Admin;
import BLL.Contador;
import BLL.Empleado;
import BLL.Usuario;
import DAO.UsuarioDAO;

public class InterfazLogin extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField inpEmail;
	private JPasswordField inpPassword;
	private JLabel lblMensaje;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				FlatLightLaf.setup();
				UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 14));
				UIManager.put("Button.arc", 12);
				UIManager.put("Component.arc", 12);
				UIManager.put("TextComponent.arc", 12);

				InterfazLogin frame = new InterfazLogin();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public InterfazLogin() {
		setTitle("BiancaTex - Login");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 320);
		setLocationRelativeTo(null);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("BIANCATEX - LOGIN");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(0, 20, 434, 25);
		contentPane.add(lblTitulo);

		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(107, 60, 220, 20);
		contentPane.add(lblEmail);

		inpEmail = new JTextField();
		inpEmail.setBounds(107, 82, 220, 28);
		contentPane.add(inpEmail);

		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setBounds(107, 120, 220, 20);
		contentPane.add(lblPassword);

		inpPassword = new JPasswordField();
		inpPassword.setBounds(107, 142, 220, 28);
		contentPane.add(inpPassword);

		lblMensaje = new JLabel("");
		lblMensaje.setForeground(Color.RED);
		lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
		lblMensaje.setBounds(0, 180, 434, 20);
		contentPane.add(lblMensaje);

		JButton btnLogin = new JButton("Ingresar");
		btnLogin.setBounds(162, 210, 110, 32);
		btnLogin.addActionListener(e -> ingresar());
		contentPane.add(btnLogin);

		getRootPane().setDefaultButton(btnLogin); // Enter Ingresa
	}

	private void ingresar() {
		String email = inpEmail.getText().trim().toLowerCase();
		String password = new String(inpPassword.getPassword());

		if (email.isEmpty() || password.trim().isEmpty()) {
		    lblMensaje.setText("Completá email y contraseña.");
		    return;
		}

		if (!email.matches("^[\\w+-]+(\\.[\\w+-]+)*@biancatex\\.com$")) {
		    lblMensaje.setText("El email debe ser de la empresa");
		    return;
		}

		Usuario logueado = UsuarioDAO.login(email, password);

		if (logueado == null) {
			lblMensaje.setText("Email o contraseña incorrectos.");
			inpPassword.setText("");
			return;
		}

		if (!logueado.getActivo()) {
			lblMensaje.setText("Tu usuario está desactivado.");
			inpPassword.setText("");
			return;
		}

		if (logueado instanceof Admin) {
			new InterfazAdmin((Admin) logueado).setVisible(true);
		} else if (logueado instanceof Empleado) {
			new InterfazEmpleado((Empleado) logueado).setVisible(true);
		} else if (logueado instanceof Contador) {
			new InterfazContador((Contador) logueado).setVisible(true);
		}
		dispose();}
	
}