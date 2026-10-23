package GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import BLL.Empleado;

public class InterfazEmpleado extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private Empleado empleado;

	public InterfazEmpleado(Empleado empleado) {
		this.empleado = empleado;

		setTitle("BiancaTex - Empleado");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
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