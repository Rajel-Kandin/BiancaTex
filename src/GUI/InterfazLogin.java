package GUI;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.ImageIcon;
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
		setBounds(100, 100, 450, 400);
		setLocationRelativeTo(null);
		setResizable(false);

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// logo 
		ImageIcon icono = new ImageIcon(InterfazLogin.class.getResource("/logo.png"));
		int anchoMax = 300;
		int altoMax = 120;
		double escala = Math.min((double) anchoMax / icono.getIconWidth(),
				(double) altoMax / icono.getIconHeight());
		int ancho = (int) (icono.getIconWidth() * escala);
		int alto = (int) (icono.getIconHeight() * escala);
		Image imagen = icono.getImage().getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);

		JLabel lblLogo = new JLabel(new ImageIcon(imagen));
		lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
		lblLogo.setBounds(0, 15, 434, altoMax);
		contentPane.add(lblLogo);

		JLabel lblEmail = new JLabel("Email:");
		lblEmail.setBounds(107, 145, 220, 20);
		contentPane.add(lblEmail);

		inpEmail = new JTextField();
		inpEmail.setBounds(107, 167, 220, 28);
		contentPane.add(inpEmail);

		JLabel lblPassword = new JLabel("Contraseña:");
		lblPassword.setBounds(107, 205, 220, 20);
		contentPane.add(lblPassword);

		inpPassword = new JPasswordField();
		inpPassword.setBounds(107, 227, 220, 28);
		contentPane.add(inpPassword);

		lblMensaje = new JLabel("");
		lblMensaje.setForeground(Color.RED);
		lblMensaje.setHorizontalAlignment(SwingConstants.CENTER);
		lblMensaje.setBounds(0, 265, 434, 20);
		contentPane.add(lblMensaje);

		JButton btnLogin = new JButton("Ingresar");
		btnLogin.setBounds(162, 295, 110, 32);
		btnLogin.addActionListener(e -> ingresar());
		contentPane.add(btnLogin);

		getRootPane().setDefaultButton(btnLogin);
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