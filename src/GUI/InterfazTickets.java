package GUI;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.LinkedList;

import BLL.Venta;

public class InterfazTickets extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tablaTickets;
	private DefaultTableModel model;
	private JTextArea txtContenido;
	private JLabel lblMensaje;

	public InterfazTickets() {
		setTitle("BiancaTex - Tickets");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 700, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("Tickets de venta");
		lblTitulo.setBounds(20, 15, 200, 20);
		contentPane.add(lblTitulo);

		// tabla con los nombres de los tickets
		model = new DefaultTableModel(new String[]{"Ticket"}, 0);
		tablaTickets = new JTable(model);
		JScrollPane scrollTabla = new JScrollPane(tablaTickets);
		scrollTabla.setBounds(20, 45, 140, 330);
		contentPane.add(scrollTabla);

		// espacio  donde se muestra el contenido del ticket
		txtContenido = new JTextArea();
		txtContenido.setEditable(false);
		txtContenido.setFont(new Font("Monospaced", Font.PLAIN, 12));
		txtContenido.setMargin(new Insets(10, 20, 10, 10));
		JScrollPane scrollContenido = new JScrollPane(txtContenido);
		scrollContenido.setBounds(175, 45, 485, 330);
		contentPane.add(scrollContenido);

		lblMensaje = new JLabel("");
		lblMensaje.setForeground(Color.RED);
		lblMensaje.setBounds(20, 385, 300, 20);
		contentPane.add(lblMensaje);

		JButton btnVolver = new JButton("Volver");
		btnVolver.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}});
		
		btnVolver.setBounds(560, 385, 100, 25);
		contentPane.add(btnVolver);

		// cuando se selecciona una fila, se muestra el ticket
		tablaTickets.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				int row = tablaTickets.getSelectedRow();
				if (row != -1) {
					String nombre = (String) model.getValueAt(row, 0);
					txtContenido.setText(Venta.leerTicket(nombre));
				}}});

		cargarTickets();}

	private void cargarTickets() {
		model.setRowCount(0); 
		LinkedList<String> tickets = Venta.obtenerTickets();

		if (tickets.isEmpty()) {
			lblMensaje.setText("No hay tickets generados.");}

		for (String nombre : tickets) {
			model.addRow(new Object[]{nombre});}}
}