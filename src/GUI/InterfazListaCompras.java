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

import DAO.CompraDAO;

public class InterfazListaCompras extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaCompras;
	private DefaultTableModel modelo;

	public InterfazListaCompras(JFrame anterior) {

		setTitle("BiancaTex - Lista de compras");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 650, 420);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Si cierra, vuelve a la sección compras
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				anterior.setVisible(true);
			}
		});

		JLabel lblTitulo = new JLabel("Lista de compras");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(0, 15, 634, 30);
		contentPane.add(lblTitulo);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(20, 60, 594, 260);
		contentPane.add(scrollPane);

		modelo = new DefaultTableModel(
			new Object[][] {},
			new String[] {"N° compra", "Fecha", "CUIT proveedor", "Total", "Registrada por"}
		) {
			private static final long serialVersionUID = 1L;

			@Override
			public boolean isCellEditable(int fila, int columna) {
				return false;
			}
		};

		tablaCompras = new JTable(modelo);
		tablaCompras.getTableHeader().setReorderingAllowed(false);
		scrollPane.setViewportView(tablaCompras);

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(e -> dispose());
		btnVolver.setBounds(234, 335, 180, 32);
		contentPane.add(btnVolver);

		CompraDAO.cargarTablaCompras(modelo);
	}
}