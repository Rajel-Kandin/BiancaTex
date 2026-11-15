package GUI;

import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import DAO.VentaDAO;

public class InterfazListaVentas extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaVentas;
	private DefaultTableModel modelo;

	public InterfazListaVentas(JFrame anterior) {

		setTitle("BiancaTex - Lista de ventas");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 420);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Si cierra, vuelve a la sección ventas
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				anterior.setVisible(true);
			}
		});

		JLabel lblTitulo = new JLabel("Lista de ventas");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(0, 15, 684, 30);
		contentPane.add(lblTitulo);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 60, 644, 260);
		contentPane.add(scrollPane);

		modelo = new DefaultTableModel(
			new Object[][] {},
			new String[] {"N° venta", "Fecha", "Subtotal", "Descuento", "Total", "Registrada por"}
		) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int fila, int columna) {
				return false;
			}
		};

		tablaVentas = new JTable(modelo);
		tablaVentas.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(tablaVentas);

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(e -> dispose());
		btnVolver.setBounds(252, 335, 180, 32);
		contentPane.add(btnVolver);

		VentaDAO.cargarTablaVentas(modelo);
	}
}